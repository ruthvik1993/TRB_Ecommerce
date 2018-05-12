package com.thericebag.application.application.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
//import com.google.firebase.crash.FirebaseCrash;
import com.thericebag.application.R;
import com.thericebag.application.application.Dialog.NetworkError_Dialog;
import com.thericebag.application.application.Services.ApiConstants;
import com.thericebag.application.application.Services.NetworkManager;
import com.thericebag.application.application.Services.TheRiceBagRequest;
import com.thericebag.application.application.Services.TheRiceBagResponse;
import com.thericebag.application.application.utility.AppConstants;
import com.thericebag.application.application.utility.AppUtil;
import com.thericebag.application.application.utility.Utility;

public class LoadingActivity extends Activity {

    ProgressDialog dialogue;
    ImageView imgLoader, imgLogo;
    RequestQueue requestQueue;
    Context mContext = LoadingActivity.this;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);

        Utility.changeStatusbarColor(this, "transparent");
        requestQueue = Volley.newRequestQueue(this);

        initializeViews();
        buttonClicks();

        /*FirebaseCrash.report(new Exception("My first Android non-fatal error"));*/

        AppUtil.createMessaginGroup(AppConstants.ALL_USERS);

        versionCheck();



		/*if(isNetworkAvailable())
            new getCategoriesAsyncTask().execute();  // get Categories from web service
		else
			Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT);*/

    }

    public void versionCheck() {
        final String version_number = Utility.getBuildVersion(mContext);

        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,
                ApiConstants.ApiName.CHECKVERSION + "version_number=" + version_number, new Response.Listener<TheRiceBagResponse>() {
            @Override
            public void onResponse(TheRiceBagResponse response) {
                if (response.isSuccess()) {
                    if (response.getResponseBody().equalsIgnoreCase("1")) {
                        upToDate();
                    } else if (response.getResponseBody().equalsIgnoreCase("4")) {
                        Toast.makeText(mContext, "error from server", Toast.LENGTH_SHORT).show();
                    } else {
                        UpdatePopUp(response.getResponseBody());
                    }
                } else if (response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {

                    final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                    networkError_dialog.showDialog(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            networkError_dialog.dismissDialog();
                            versionCheck();
                        }
                    });

                }
            }
        }, null);

        NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);

    }

    private void upToDate() {
        navigate();
    }

    private void UpdatePopUp(String response) {

        final Dialog updateAvailable = new Dialog(LoadingActivity.this, android.R.style.Theme_Black_NoTitleBar);
        updateAvailable.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        updateAvailable.setContentView(R.layout.dialog_update_available);
        ImageView img_close = (ImageView) updateAvailable.findViewById(R.id.img_close);
        TextView textSkipUpdate = (TextView) updateAvailable.findViewById(R.id.textSkipUpdate);
        TextView textUpdateNow = (TextView) updateAvailable.findViewById(R.id.textUpdateNow);
        if (response.equalsIgnoreCase("2") || response.equalsIgnoreCase("3")) {
            if (response.equalsIgnoreCase("2")) {       // Update Available
                img_close.setVisibility(View.VISIBLE);
                textSkipUpdate.setVisibility(View.VISIBLE);
                updateAvailable.setCancelable(false);
            } else if (response.equalsIgnoreCase("3")) {    // Force Update
                img_close.setVisibility(View.GONE);
                textSkipUpdate.setVisibility(View.GONE);
                updateAvailable.setCancelable(false);
            }
            updateAvailable.show();
        }

        img_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAvailable.dismiss();
                navigate();
            }
        });

        textSkipUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAvailable.dismiss();
                navigate();
            }
        });

        textUpdateNow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO- update package name before submitting to play store

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.thericebag.application"));
                startActivity(intent);

            }
        });

        /*final Dialog forceUpdateDialogue = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar);
        forceUpdateDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        forceUpdateDialogue.setContentView(R.layout.layout_update_app);

        TextView txtInfo = (TextView) forceUpdateDialogue.findViewById(R.id.txtInfo);
        Button btnUpdate = (Button) forceUpdateDialogue.findViewById(R.id.btnUpdate);
        Button btCancel = (Button) forceUpdateDialogue.findViewById(R.id.btCancel);
        RelativeLayout relativeMain = (RelativeLayout) forceUpdateDialogue.findViewById(R.id.relativeMain);

        if (response.equalsIgnoreCase("2")) {
            txtInfo.setText("Update available");
            forceUpdateDialogue.show();
            relativeMain.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    forceUpdateDialogue.cancel();
                }
            });

        } else if (response.equalsIgnoreCase("3")) {
            txtInfo.setText("Force Update");
            forceUpdateDialogue.setCancelable(false);
            forceUpdateDialogue.show();
        }*/

    }

    private void buttonClicks() {
        imgLogo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate();
            }
        });
    }

    private void initializeViews() {
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        imgLoader = (ImageView) findViewById(R.id.imgLoader);
        imgLoader.setBackgroundResource(R.drawable.animationloader);

        AnimationDrawable frameAnimation = (AnimationDrawable) imgLoader.getBackground();
        frameAnimation.start();
    }

    private void navigate() {
        if (!Utility.isFirstTime(mContext)) {
            Intent intent = new Intent(mContext, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            /*Intent intent = new Intent(mContext, SplashActivity.class);
            startActivity(intent);*/
            Intent intent = new Intent(mContext, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

}
