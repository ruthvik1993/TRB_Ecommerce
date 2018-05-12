package com.thericebag.application.application;

import android.content.Context;

import com.facebook.appevents.AppEventsLogger;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Abhilash on 9/26/2016.
 */

public class TRBApplication extends android.app.Application {

    private static Context context;

    public static Context getAppContext() {
        return TRBApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        TRBApplication.context = getApplicationContext();
        AppEventsLogger.activateApp(this);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
