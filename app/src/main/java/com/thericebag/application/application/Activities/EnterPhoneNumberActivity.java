package com.thericebag.application.application.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.thericebag.application.R;
import com.thericebag.application.application.Dialog.NetworkError_Dialog;
import com.thericebag.application.application.Services.ApiConstants;
import com.thericebag.application.application.Services.NetworkManager;
import com.thericebag.application.application.Services.TheRiceBagRequest;
import com.thericebag.application.application.Services.TheRiceBagResponse;
import com.thericebag.application.application.utility.AppConstants;

import java.net.URLEncoder;

public class EnterPhoneNumberActivity extends AppCompatActivity {

    private EditText editMobileNumber;

    private RelativeLayout relativeScreen;

    private TextView txtNext;

    private ImageView backIcon;

    private Context mContext;

    private String email;

    private ProgressDialog progressDialog;

    private String fromActivity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_enter_phonenumber);
        mContext = EnterPhoneNumberActivity.this;

        progressDialog = new ProgressDialog(mContext);

        if (getIntent().getExtras() != null) {

            email = getIntent().getExtras().getString(AppConstants.EMAIL);

            fromActivity = getIntent().getExtras().getString(AppConstants.FROM_ACTIVITY);

        }

        initializeViews();

        buttonOnClicks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setScreenHeight();
    }

    private void setScreenHeight() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        final int width = displayMetrics.widthPixels;
        final int height = displayMetrics.heightPixels;

        ViewTreeObserver viewTreeObserver = relativeScreen.getViewTreeObserver();
        viewTreeObserver
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);

                        layoutParams.width = width;
                        layoutParams.height = height;

                        relativeScreen.setLayoutParams(layoutParams);
                        relativeScreen.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });

    }

    private void buttonOnClicks() {
        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    sendOTP();
                } else {
                    Toast.makeText(mContext, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private boolean validate() {
        boolean flag = false;
        if (editMobileNumber.getText().toString() != null && editMobileNumber.getText().toString().length() == 10) {
            flag = true;
        }
        return flag;
    }

    private void sendOTP() {
        final String phoneNumber = editMobileNumber.getText().toString();
        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,
                ApiConstants.ApiName.VERIFY_PHONE_NUMBER + "email=" + URLEncoder.encode(email) + "&phone=" + phoneNumber, new Response.Listener<TheRiceBagResponse>() {
            @Override
            public void onResponse(TheRiceBagResponse response) {
                progressDialog.dismiss();
                if (response.isSuccess()) {
                    if (response.getResponseBody().equals("1")) {
                        Intent intent = new Intent(mContext, VerifyOTPPhoneNumber.class);
                        intent.putExtra(AppConstants.EMAIL, email);
                        intent.putExtra(AppConstants.PHONE, phoneNumber);
                        intent.putExtra(AppConstants.FROM_ACTIVITY, fromActivity);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                    }

                } else if (response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {

                    final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                    networkError_dialog.showDialog(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            networkError_dialog.dismissDialog();
                            onBackPressed();
                        }
                    });

                } else {
                    Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);

        NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);

        progressDialog.setMessage("Almost done...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void initializeViews() {
        relativeScreen = (RelativeLayout) findViewById(R.id.relativeScreen);
        editMobileNumber = (EditText) findViewById(R.id.editMobileNumber);
        txtNext = (TextView) findViewById(R.id.txtNext);
        backIcon = (ImageView) findViewById(R.id.backIcon);
    }
}
