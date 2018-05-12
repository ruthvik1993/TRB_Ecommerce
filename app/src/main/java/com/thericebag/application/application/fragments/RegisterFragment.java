package com.thericebag.application.application.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.thericebag.application.R;
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

import java.util.Arrays;

import static com.thericebag.application.application.utility.Utility.isValidEmail;

public class RegisterFragment extends Fragment {

    private static final int RC_SIGN_IN = 9001;
    ImageView imgGoogle, imgFB;
    LoginButton fblogin_button;
    CallbackManager callbackManager;
    private EditText editEmail, editPhone, editPassword, editConfirmPassword;
    private TextView txtSignUp;
    private Context mContext;
    private View convertView;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        convertView = inflater.inflate(R.layout.register_fragment, container, false);

        mContext = getContext();

        progressDialog = new ProgressDialog(mContext);

        imgFB = (ImageView) convertView.findViewById(R.id.imgFB);
        imgGoogle = (ImageView) convertView.findViewById(R.id.imgGoogle);
        fblogin_button = (LoginButton) convertView.findViewById(R.id.fblogin_button);
        editEmail = (EditText) convertView.findViewById(R.id.editEmail);
        editPhone = (EditText) convertView.findViewById(R.id.editPhone);
        editPassword = (EditText) convertView.findViewById(R.id.editPassword);
        editConfirmPassword = (EditText) convertView.findViewById(R.id.editConfirmPassword);
        txtSignUp = (TextView) convertView.findViewById(R.id.txtVerify);


        buttonOnClicks();

        return convertView;
    }

    private void buttonOnClicks() {

        imgFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbSignIn();
            }
        });

        imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity login = (LoginActivity) getActivity();
                login.signIn();
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    signUp();
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
        if (TextUtils.isEmpty(editPhone.getText().toString())) {
            flag = false;
        }
        if (editPhone.getText().toString().length() != 10) {
            Toast.makeText(mContext, "Enter valid phone number", Toast.LENGTH_SHORT).show();
        }

        if (!editPassword.getText().toString().equals(editConfirmPassword.getText().toString())) {
            flag = false;
            Toast.makeText(mContext, "Password doesn't match with confirm password", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }


    private void signUp() {
        String email = editEmail.getText().toString();
        String phone = editPhone.getText().toString();
        String password = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();

        if (password.equals(confirmPassword)) {
            password = Utility.stringToMD5(password);
            email = Utility.encodeURL(email);
            callSignUp(email, phone, password);
        }
    }

    private void callSignUp(final String email, final String phone, final String password) {

        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,
                ApiConstants.ApiName.SIGNUP + "email=" + email + "&password=" + password + "&phone=" + phone, new Response.Listener<TheRiceBagResponse>() {
            @Override
            public void onResponse(TheRiceBagResponse response) {
                progressDialog.dismiss();
                if (response.isSuccess()) {
                    LoginActivity login = (LoginActivity) getActivity();
                    LoginResponseBean responseObj = new Gson().fromJson(response.getResponseBody().toString(), LoginResponseBean.class);
                    if (responseObj.getStatus() == 1) {   //Successful
                        login.SignUpUpdateUI(true, AppConstants.SUCCESS, phone, email);
                    } else if (responseObj.getStatus() == 0) {  // Insertion in DB failed
                        Snackbar.make(convertView, AppConstants.FAILED_INSERT, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if (responseObj.getStatus() == 2) {  // Already registered with us
                        Snackbar.make(convertView, AppConstants.ALREADY_REGISTERED, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else if (responseObj.getStatus() == 3) {  // Something went wrong
                        Toast.makeText(mContext, AppConstants.ALREADY_REGISTERED, Toast.LENGTH_SHORT).show();
                    } else if (response.getErrorMessage() != null && response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {

                        final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                        networkError_dialog.showDialog(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                networkError_dialog.dismissDialog();
                                callSignUp(email, phone, password);
                            }
                        });

                    } else {
                        Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                    }
                    System.out.print(response.getResponseBody());
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
