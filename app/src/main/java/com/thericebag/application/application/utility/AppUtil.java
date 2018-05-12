package com.thericebag.application.application.utility;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.messaging.FirebaseMessaging;

//import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by abhilash on 01-09-2016.
 */

public final class AppUtil {

    private static final String TAG = AppUtil.class.getSimpleName();

    private static String sAndroidId;

    /**
     * Hidden constructor
     */
    private AppUtil() {

    }

    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED);
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean isConnectedToNetwork = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            isConnectedToNetwork = networkInfo.isConnected();
        }
        return isConnectedToNetwork;
    }

    public static void createMessaginGroup(String text) {
        FirebaseMessaging.getInstance().subscribeToTopic(text);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showSnackBar(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }

    public static String getAndroidId(Context context) {
        if (TextUtils.isEmpty(sAndroidId)) {
            sAndroidId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        return sAndroidId;
    }

}