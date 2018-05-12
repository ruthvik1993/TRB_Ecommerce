package com.thericebag.application.application.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.thericebag.application.R;
import com.thericebag.application.application.Dialog.NetworkError_Dialog;
import com.thericebag.application.application.Services.ApiConstants;
import com.thericebag.application.application.Services.NetworkManager;
import com.thericebag.application.application.Services.TheRiceBagRequest;
import com.thericebag.application.application.Services.TheRiceBagResponse;
import com.thericebag.application.application.beans.LoginResponseBean;
import com.thericebag.application.application.fragments.LoginFragment;
import com.thericebag.application.application.fragments.RegisterFragment;
import com.thericebag.application.application.utility.AppConstants;
import com.thericebag.application.application.utility.Utility;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    public String fromActivity = "";
    TabLayout tabsHeaading;
    ViewPager viewpagerLogin;
    GoogleApiClient mGoogleApiClient;
    private Context mContext;
    private ProgressDialog progressDialog;
    private RelativeLayout relativeTillTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.login_layout);

        mContext = LoginActivity.this;
        progressDialog = new ProgressDialog(mContext);
        printHashKey(mContext);

        if (getIntent().getExtras() != null) {
            fromActivity = getIntent().getExtras().getString(AppConstants.FROM_ACTIVITY);
        }

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        initializeViews();

        setupViewPager(viewpagerLogin);
        tabsHeaading.setupWithViewPager(viewpagerLogin);
    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.thericebagfinal.application.Activities", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("Hash Key", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("Hash Key", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("Hash Key", "printHashKey()", e);
        }
    }

    private void initializeViews() {

//        RelativeLayout relativeLogin = (RelativeLayout) findViewById(R.id.relativeLogin);
//        int height = setScrollViewHeight();

        /*relativeLogin.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, height));*/


        relativeTillTabs = (RelativeLayout) findViewById(R.id.relativeTillTabs);
        viewpagerLogin = (ViewPager) findViewById(R.id.viewpagerLogin);
        tabsHeaading = (TabLayout) findViewById(R.id.tabsHeaading);
        changeTabsFont();

    }

    @Override
    protected void onResume() {
        super.onResume();

        setViewPagerHeight();
    }

    private void setViewPagerHeight() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        final int height = displayMetrics.heightPixels;

        ViewTreeObserver viewTreeObserver = viewpagerLogin.getViewTreeObserver();
        viewTreeObserver
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        int viewPagerHeight = height - relativeTillTabs.getHeight();
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);

                        int viewPagerWidth = viewpagerLogin.getWidth();
//                        float viewPagerHeight = (float) (viewPagerHeightInt);

                        layoutParams.width = viewPagerWidth;
                        layoutParams.height = viewPagerHeight;
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                        layoutParams.setMargins(40, 0, 40, 0);

                        layoutParams.addRule(RelativeLayout.BELOW, R.id.relativeTillTabs);

                        viewpagerLogin.setLayoutParams(layoutParams);
                        viewpagerLogin.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });

    }

    private int setScrollViewHeight() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return height;
    }

    private void changeTabsFont() {
        Typeface roboto_regular = Typeface.createFromAsset(getAssets(), "fonts/roboto_regular.ttf");
        ViewGroup vg = (ViewGroup) tabsHeaading.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(roboto_regular, Typeface.NORMAL);
                    ((TextView) tabViewChild).setAllCaps(false);
                }
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle loginBundle = new Bundle();
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(loginBundle);

        Bundle registrationBundle = new Bundle();
        RegisterFragment registerFragment = new RegisterFragment();
        registerFragment.setArguments(registrationBundle);

        adapter.addFragment(loginFragment, "Log In");
        adapter.addFragment(registerFragment, "SignUp");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    //-----------------------------------------Google SignIn----------------------------------------//
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            verifyMail(acct.getEmail(), acct.getDisplayName(), acct.getGivenName(), acct.getId(), "google");
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false, AppConstants.FAILED_GOOGLE, "", "");
        }
    }

    public void verifyMail(final String email, String display_name, String fname, String password, String loginType) {

        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,
                ApiConstants.ApiName.VERIFY_MAIL + "email=" + URLEncoder.encode(email) + "&display_name=" + URLEncoder.encode(display_name)
                        + "&fname=" + URLEncoder.encode(fname) + "&password=" + Utility.stringToMD5(password)
                        + "&login_type=" + loginType, new Response.Listener<TheRiceBagResponse>() {
            @Override
            public void onResponse(TheRiceBagResponse response) {
                progressDialog.dismiss();
                if (response.isSuccess()) {
                    LoginResponseBean loginResponse = new Gson().fromJson(response.getResponseBody(), LoginResponseBean.class);
                    if (loginResponse != null && loginResponse.getStatus() != 0 && loginResponse.getStatus() == 1) {
                        Utility.setEmail(mContext, URLDecoder.decode(email));
                        Utility.setId(mContext, loginResponse.getId());
                        updateUI(true, AppConstants.SUCCESS, "", email);
                    } else if (loginResponse != null && loginResponse.getStatus() != 0 && loginResponse.getStatus() == 2) {
                        updateUI(true, AppConstants.SUCCESS, AppConstants.MISSING_PHONE, email);
                    } else if (loginResponse != null && loginResponse.getStatus() != 0 && loginResponse.getStatus() == 4) {
                        updateUI(false, AppConstants.FAILED_TO_REGISTER, "", email);
                    } else if (loginResponse != null && loginResponse.getStatus() != 0 && loginResponse.getStatus() == 5) {
                        updateUI(false, AppConstants.INVALID_PARAMETERS, "", email);
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

    public void updateUI(boolean isSuccess, String reason, String anyMissed, String email) {
        if (isSuccess) {

            if (anyMissed.equals(AppConstants.MISSING_PHONE)) {
                Intent intent = new Intent(mContext, EnterPhoneNumberActivity.class);
                intent.putExtra(AppConstants.EMAIL, email);
                intent.putExtra(AppConstants.FROM_ACTIVITY, fromActivity);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else if (fromActivity.equals(AppConstants.FROM_PROFILE_ACTIVITY)) {
                Toast.makeText(mContext, AppConstants.MESSAGE_LOGIN_SUCCESS, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(mContext, reason, Toast.LENGTH_SHORT).show();
        }
    }

    public void SignUpUpdateUI(boolean isSuccess, String reason, String phone, String email) {
        if (isSuccess) {
            Intent intent = new Intent(mContext, EnterOTPActivity.class);
            intent.putExtra(AppConstants.PHONE, phone);
            intent.putExtra(AppConstants.EMAIL, email);
            intent.putExtra(AppConstants.FROM_ACTIVITY, fromActivity);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(mContext, reason, Toast.LENGTH_SHORT).show();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(android.support.v4.app.FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
