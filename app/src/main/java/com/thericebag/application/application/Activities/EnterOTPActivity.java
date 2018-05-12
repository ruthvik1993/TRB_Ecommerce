package com.thericebag.application.application.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
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
import com.google.gson.Gson;
import com.thericebag.application.R;
import com.thericebag.application.application.Dialog.NetworkError_Dialog;
import com.thericebag.application.application.Services.ApiConstants;
import com.thericebag.application.application.Services.NetworkManager;
import com.thericebag.application.application.Services.TheRiceBagRequest;
import com.thericebag.application.application.Services.TheRiceBagResponse;
import com.thericebag.application.application.beans.LoginResponseBean;
import com.thericebag.application.application.utility.AppConstants;
import com.thericebag.application.application.utility.Utility;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnterOTPActivity extends AppCompatActivity {

    private static final String INTENT_FILTER_FOR_SMS = "android.provider.Telephony.SMS_RECEIVED";
    private TextView txtSignUp;
    private ImageView backIcon;
    private RelativeLayout relativeScreen;
    private TextView txtResendOTP;
    private EditText editOTP;
    private Context mContext = EnterOTPActivity.this;
    private String codeFromMsg;
    private String phone = "";
    private String email = "";
    private long startTime = System.currentTimeMillis();
    private String OTP;
    private Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if ((System.currentTimeMillis() - startTime) / 1000 > 120) {
                try {
                    unregisterReceiver(mReceiver);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                mHandler.removeCallbacks(runnable);
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mHandler.postDelayed(runnable, 1000);
            }
        }
    };
    private String fromActivity = "";
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mHandler.removeCallbacks(runnable);
            Bundle bundle = intent.getExtras();
            Object[] messages = (Object[]) bundle.get("pdus");
            SmsMessage[] sms = new SmsMessage[messages.length];
            // Create messages for each incoming PDU
            for (int n = 0; n < messages.length; n++) {
                sms[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
            }
            for (SmsMessage msg : sms) {

                String fromNumber = msg.getDisplayOriginatingAddress();
                if (fromNumber.contains("RiceBg")) {
                    codeFromMsg = msg.getMessageBody();
                }
                System.out.print(codeFromMsg);
            }
            //String fromNumber = msg.getDisplayOriginatingAddress();

            Pattern pattern = Pattern.compile("\\w+([0-9]+)");
            Matcher matcher = pattern.matcher(codeFromMsg);
            for (int j = 0; j < matcher.groupCount(); j++) {
                matcher.find();
                OTP = matcher.group();
                System.out.print(OTP);
            }
            editOTP.setText(OTP);

            if (Utility.isNetworkAvailable(mContext)) {
                if (editOTP.getText().toString().length() != 4) {
                    editOTP.setError(AppConstants.INVALIDOTP);
                } else {
                    verifyotp();
                }
            } else {
                Toast.makeText(mContext, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        getSupportActionBar().hide();
        Utility.changeStatusbarColor(this, "#666666");
        mHandler.postDelayed(runnable, 1000);


        if (getIntent().getExtras() != null) {
            phone = getIntent().getExtras().getString(AppConstants.PHONE);
            email = getIntent().getExtras().getString(AppConstants.EMAIL);
            fromActivity = getIntent().getExtras().getString(AppConstants.FROM_ACTIVITY);
        }

        iniializeViews();

        startTimer();

        buttonOnClicks();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
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

//                        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
//                                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                                RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//                        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//                        txtResendOTP.setLayoutParams(relativeParams);
                    }
                });

    }

    private void buttonOnClicks() {
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyotp();
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTP();
            }
        });
    }

    private void resendOTP() {
        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,
                ApiConstants.ApiName.RESENDOTP + "email=" + email + "&phone=" + phone, new Response.Listener<TheRiceBagResponse>() {
            @Override
            public void onResponse(TheRiceBagResponse response) {
                if (response.isSuccess()) {
                    if (response.getResponseBody().toString().equalsIgnoreCase("1")) {
                        Toast.makeText(mContext, "Sent OTP successfully", Toast.LENGTH_SHORT).show(); // TODO Dummy - Have to remove and add functionality
                    } else if (response.getErrorMessage() != null && response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {

                        final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                        networkError_dialog.showDialog(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                networkError_dialog.dismissDialog();

                                resendOTP();
                            }
                        });

                    } else {
                        Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, null);

        NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);
    }

    private void iniializeViews() {
        txtSignUp = (TextView) findViewById(R.id.txtVerify);
        backIcon = (ImageView) findViewById(R.id.backIcon);
        txtResendOTP = (TextView) findViewById(R.id.txtResendOTP);
        editOTP = (EditText) findViewById(R.id.editOTP);
        relativeScreen = (RelativeLayout) findViewById(R.id.relativeScreen);
    }

    private void startTimer() {

        txtResendOTP.setTextColor(Color.parseColor("#FFFFFF"));
        txtResendOTP.setBackgroundColor(Color.parseColor("#B3000000"));
        txtResendOTP.setClickable(false);

        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
//                txtTime.setText(Long.toString(minutes) + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
//                txtTime.setText("0:00");
                txtResendOTP.setTextColor(Color.parseColor("#FFFFFF"));
                txtResendOTP.setBackgroundColor(Color.parseColor("#4D000000"));
                txtResendOTP.setClickable(true);
            }

        }.start();
    }

    private void verifyotp() {

        String OTP = editOTP.getText().toString();
        try {
            email = URLEncoder.encode(email, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,
                ApiConstants.ApiName.VERIFYOTP + "email=" + email + "&otp=" + OTP + "&phone=" + phone, new Response.Listener<TheRiceBagResponse>() {
            @Override
            public void onResponse(TheRiceBagResponse response) {
                if (response.isSuccess()) {
                    LoginResponseBean loginResponse = new Gson().fromJson(response.getResponseBody(), LoginResponseBean.class);
                    if (loginResponse != null && loginResponse.getStatus() != 0 && loginResponse.getStatus() == 1) {
                        Utility.setId(mContext, loginResponse.getId());
                        Utility.setEmail(mContext, loginResponse.getEmail());
                        verifiedOTP(true);
                    } else if (loginResponse != null && loginResponse.getStatus() != 0 && loginResponse.getStatus() == 4) {
                        Toast.makeText(mContext, "error from server", Toast.LENGTH_SHORT).show();
                    } else if (response.getErrorMessage() != null && response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {
                        final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                        networkError_dialog.showDialog(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                networkError_dialog.dismissDialog();
                                verifyotp();
                            }
                        });

                    } else {
                        verifiedOTP(false);
                    }
                }
            }
        }, null);

        NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);
    }

    private void verifiedOTP(boolean isValid) {
        if (isValid) {
            if (fromActivity.equals(AppConstants.FROM_PROFILE_ACTIVITY)) {
                Intent intent = new Intent(mContext, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else if (fromActivity.equals(AppConstants.FROM_CHECKOUT_ACTIVITY)) {
                Intent intent = new Intent(mContext, checkOutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else if (fromActivity.equals(AppConstants.FROM_PRODUCTDETAIL_ACTIVITY)) {
                /*Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                onBackPressed();
                finish();
            } else {
                Intent intent = new Intent(mContext, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(mContext, AppConstants.INVALIDOTP, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        super.onResume();
        requestforCallPhonePermission();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        try {
            this.unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    //-----------------------Run Time Permissions-------------------------------------------------//
    private void requestforCallPhonePermission() {
        final String permission = android.Manifest.permission.READ_SMS;
        if (ContextCompat.checkSelfPermission(mContext, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EnterOTPActivity.this, permission)) {
                showPermissionRationaleDialog("Test", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            launch();
        }
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(EnterOTPActivity.this, new String[]{permission},
                AppConstants.MY_PERMISSIONS_CALL);
    }

    private void launch() {
        IntentFilter intentFilter = new IntentFilter(INTENT_FILTER_FOR_SMS);
        this.registerReceiver(mReceiver, intentFilter);
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new AlertDialog.Builder(EnterOTPActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EnterOTPActivity.this.requestForPermission(permission);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case AppConstants.MY_PERMISSIONS_CALL:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    launch();
                } else {
                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    //----------------------End Of Run Time Permissions------------------------------------------//
}
