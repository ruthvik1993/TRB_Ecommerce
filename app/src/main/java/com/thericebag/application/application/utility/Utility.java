package com.thericebag.application.application.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thericebag.application.application.beans.ProductBean;

import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by abhilash on 16-08-2016.
 */
public class Utility {

    static private SharedPreferences sharedPreferences;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void changeStatusbarColor(Activity activity, String statusBarColor) {
        Window window = activity.getWindow();
        if (statusBarColor.equalsIgnoreCase("transparent")) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.parseColor(statusBarColor));
            }
        }
    }

    public static boolean isLoggedIn(Context context) {
        sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(AppConstants.isLogged, false);
    }

    public static boolean isFirstTime(Context context) {
        sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(AppConstants.ISFIRSTTIME, true);
    }

    public static void setFirstTime(Context context) {
        sharedPreferences = context.getSharedPreferences(AppConstants
                .SHARED_PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor1 = sharedPreferences.edit();
        editor1.putBoolean(AppConstants.ISFIRSTTIME, false);
        editor1.commit();
    }

    public static void setMobileNumber(Context context, String phoneNumber) {
        sharedPreferences = context.getSharedPreferences(AppConstants
                .SHARED_PREFERENCES, 0); // 0 - for private mode
        SharedPreferences.Editor editor1 = sharedPreferences.edit();
        editor1.putString(AppConstants.PHONE_NUMBER_SHARED, phoneNumber);
        editor1.commit();
    }

    public static void logout(Context context) {
        sharedPreferences = context.getSharedPreferences(AppConstants
                .SHARED_PREFERENCES, 0);
        sharedPreferences.edit().clear().commit();
    }

    public static String getMobileNumber(Context context) {
        sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCES, Context
                .MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(AppConstants.PHONE_NUMBER_SHARED, "");
        return phoneNumber;
    }


    public static String getEmail(Context context) {
        sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String returnEmail = sharedPreferences.getString(AppConstants.EMAIL, null);
        if (returnEmail != null) {
            return URLDecoder.decode(returnEmail);
        } else {
            return returnEmail;
        }
    }

    public static void setEmail(Context context, String email) {
        sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.EMAIL, email);
        editor.commit();
    }


    public static String getId(Context context) {
        sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AppConstants.ID, null);
    }

    public static void setId(Context context, String id) {
        sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.ID, id);
        editor.commit();
    }


    public static String getBuildVersion(Context context) {
        PackageInfo pInfo = null;
        String version = " ";
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static ArrayList<ProductBean> getKartProducts(Context mContext) {
        ArrayList<ProductBean> productsList = new ArrayList<ProductBean>();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String kartProductsString = sharedPrefs.getString(AppConstants.KARTPRODUCTS, null);

        Gson gson = new Gson();
        productsList = gson.fromJson(kartProductsString, new TypeToken<ArrayList<ProductBean>>() {
        }.getType());

        return productsList;
    }

    public static void saveKartProducts(ArrayList<ProductBean> addToKartList, Context mContext) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        Gson gson = new Gson();
        String addToKartString = gson.toJson(addToKartList);
        editor.putString(AppConstants.KARTPRODUCTS, addToKartString);
        editor.commit();

    }

    public static void updateCartProduct(ProductBean productBeanObj, Context mContext) {
        ArrayList<ProductBean> addToKartList = new ArrayList<ProductBean>();

        // Get Previous List that are added to Kart
        addToKartList = getKartProducts(mContext);

        if (addToKartList != null) {
            for (int i = 0; i < addToKartList.size(); i++) {
                if (addToKartList.get(i).getId() == productBeanObj.getId()) {
                    addToKartList.set(i, productBeanObj);
                    break;
                }
            }
        }
    }

    public static void addCartProduct(ProductBean productBeanObj, Context mContext) {
        ArrayList<ProductBean> addToKartList = new ArrayList<ProductBean>();

        // Get Previous List that are added to Kart
        addToKartList = getKartProducts(mContext);

        if (addToKartList == null) {
            addToKartList = new ArrayList<ProductBean>();
        }
        // Save the current Item to Kart
        addToKartList.add(productBeanObj);

        Utility.saveKartProducts(addToKartList, mContext);
    }

    public static void removeKartProduct(ProductBean productBeanObj, Context mContext, int position) {
        ArrayList<ProductBean> addToKartList = new ArrayList<ProductBean>();

        // Get Previous List that are added to Kart
        addToKartList = getKartProducts(mContext);

        // Remove item from kart
        addToKartList.remove(position);

        Utility.saveKartProducts(addToKartList, mContext);
    }

    public static String stringToMD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            System.out.println("Digest(in hex format):: " + sb.toString());

            //convert the byte to hex format method 2
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String encodeURL(String url) {
        try {
            url = URLEncoder.encode(url, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return url;
    }

    public static List<String> seperateImages(String images) {
        return new ArrayList<String>(Arrays.asList(images.split(",")));
    }

    public static int getperKGPrice(int offerPrice, int weight) {
        return offerPrice / weight;
    }

    public static boolean isValidEmail(String target) {
        if (target == null || target.length() < 6) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void StartTrackingScreen(Context mContext, String id, String screenName) {
        FirebaseAnalytics mFirebaseAnalytics;
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        Bundle bundle = new Bundle();
        bundle.putString("id_custom", id);
        bundle.putString("screenName_custom", screenName);
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        mFirebaseAnalytics.logEvent(screenName, bundle);
    }

}
