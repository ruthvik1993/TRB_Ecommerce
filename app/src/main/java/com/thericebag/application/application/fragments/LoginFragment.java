package com.thericebag.application.application.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.thericebag.application.R;
import com.thericebag.application.application.Activities.ForgotPasswordActivity;
import com.thericebag.application.application.Activities.LoginActivity;
import com.thericebag.application.application.Dialog.NetworkError_Dialog;
import com.thericebag.application.application.Services.ApiConstants;
import com.thericebag.application.application.Services.NetworkManager;
import com.thericebag.application.application.Services.TheRiceBagRequest;
import com.thericebag.application.application.Services.TheRiceBagResponse;
import com.thericebag.application.application.beans.LoginResponseBean;
import com.thericebag.application.application.utility.AppConstants;
import com.thericebag.application.application.utility.Utility;

import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.Arrays;

import static com.thericebag.application.application.utility.Utility.isValidEmail;

public class LoginFragment extends Fragment {

    private static final int RC_SIGN_IN = 9001;
    TextView txtLogin, txtForgotPassword;
    ImageView imgGoogle, imgFB;
    EditText editEmail, editPassword;
    LoginButton fblogin_button;
    CallbackManager callbackManager;
    private Context mContext;
    private ProgressDialog progressDialog;
//    private ProgressBar rbProprogressBar;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View convertView = inflater.inflate(R.layout.login_fragment, container, false);
        mContext = getActivity();
        progressDialog = new ProgressDialog(mContext);

        FacebookSdk.sdkInitialize(getContext());
        imgFB = (ImageView) convertView.findViewById(R.id.imgFB);
        imgGoogle = (ImageView) convertView.findViewById(R.id.imgGoogle);
        txtLogin = (TextView) convertView.findViewById(R.id.txtLogin);
        fblogin_button = (LoginButton) convertView.findViewById(R.id.fblogin_button);
        editEmail = (EditText) convertView.findViewById(R.id.editEmail);
        editPassword = (EditText) convertView.findViewById(R.id.editPassword);
        txtForgotPassword = (TextView) convertView.findViewById(R.id.txtForgotPassword);

        buttonOnClicks();

        return convertView;
    }

    private void buttonOnClicks() {

        txtForgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ForgotPasswordActivity.class);
                intent.putExtra(AppConstants.FROM_ACTIVITY, ((LoginActivity) getActivity()).fromActivity);
                startActivity(intent);
            }
        });

        imgFB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fbSignIn();
            }
        });

        imgGoogle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity login = (LoginActivity) getActivity();
                login.signIn();
            }
        });

        txtLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    checkLogin();
                }
            }
        });
    }

    private boolean validate() {
        boolean flag = true;
        if (editPassword.getText().length() < 6) {
            flag = false;
            editPassword.setError(AppConstants.ERROR_PASSWORD);
        }
        if (!isValidEmail(editEmail.getText().toString())) {
            flag = false;
            editEmail.setError(AppConstants.ERROR_EMAIL);
        }
        return flag;
    }


    private void checkLogin() {
        String password = Utility.stringToMD5(editPassword.getText().toString());
        final String email = Utility.encodeURL(editEmail.getText().toString());

        progressDialog.setMessage("Loggin in...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,
                ApiConstants.ApiName.CHECKLOGIN + "email=" + email + "&password=" + password, new Response.Listener<TheRiceBagResponse>() {
            @Override
            public void onResponse(TheRiceBagResponse response) {
                progressDialog.dismiss();
                if (response.isSuccess()) {
                    LoginActivity login = (LoginActivity) getActivity();
                    LoginResponseBean loginResponse = new Gson().fromJson(response.getResponseBody(), LoginResponseBean.class);
                    if (loginResponse != null && loginResponse.getStatus() == 1) {  // Success
                        if (TextUtils.isEmpty(loginResponse.getPhone())) {
                            login.updateUI(true, AppConstants.SUCCESS, AppConstants.MISSING_PHONE, URLDecoder.decode(email));
                        } else {
                            Utility.setId(mContext, loginResponse.getId());
                            Utility.setEmail(mContext, loginResponse.getEmail());
                            login.updateUI(true, AppConstants.SUCCESS, "", loginResponse.getEmail());
                        }
                    } else if (loginResponse != null && loginResponse.getStatus() == 0) {
                        Toast.makeText(mContext, AppConstants.FAILED_WRONG_CREDENTIALS, Toast.LENGTH_SHORT).show();
                    } else if (response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {
                        final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                        networkError_dialog.showDialog(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                networkError_dialog.dismissDialog();
                                checkLogin();
                            }
                        });
                    }
                } else {
                    Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);

        NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);
    }

    private void fbSignIn() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();
        fblogin_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                System.out.println(loginResult.getAccessToken());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                try {
                                    String id = object.getString("id");
                                    String username = object.getString("name");
                                    String email = object.getString("email");
                                    LoginActivity login = (LoginActivity) getActivity();
                                    login.verifyMail(email, username, username, id, "facebook");
//                                    login.updateUI(true, AppConstants.SUCCESS);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                System.out.println("testing");// App code
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("testing");// App code
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != RC_SIGN_IN) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
