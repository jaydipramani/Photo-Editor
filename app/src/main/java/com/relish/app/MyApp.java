package com.relish.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.relish.app.Ad.AdUtils;

public class MyApp extends Application {
    private static MyApp app;
    private int numStarted = 0;
    public static boolean fast_start = true;

    public void onCreate() {
        super.onCreate();
        app = this;
        MobileAds.initialize(this);
//        FirebaseMessaging.getInstance().subscribeToTopic("all");

        loadAd();

        if (Build.VERSION.SDK_INT >= 26) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Context getContext() {
        return getContext();
    }

    private void loadAd() {
        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            public void onActivityCreated(Activity activity, Bundle bundle) {
                Log.d("MyApplication", "onActivityCreated");
            }

            public void onActivityStarted(Activity activity) {
                String str = "MyApplication";
                Log.d(str, "onActivityStarted");
                if (MyApp.this.numStarted == 0) {
                    Log.d(str, "foreground = " + MyApp.fast_start);
                    if (MyApp.fast_start) {
                        MyApp.fast_start = false;
                    } else {

                        AdUtils.OpenAppAds(activity);

                    }
                }
                MyApp.this.numStarted = MyApp.this.numStarted + 1;
            }

            public void onActivityResumed(Activity activity) {
                Log.d("MyApplication", "onActivityResumed");
            }

            public void onActivityPaused(Activity activity) {
                Log.d("MyApplication", "onActivityPaused");
            }

            public void onActivityStopped(Activity activity) {

                Log.d("activity", "onActivityStopped");
                MyApp.this.numStarted = MyApp.this.numStarted - 1;
                if (MyApp.this.numStarted == 0) {
                    Log.d("activity", "background");
                }
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                Log.d("MyApplication", "onActivitySaveInstanceState");
            }

            public void onActivityDestroyed(Activity activity) {
                Log.d("MyApplication", "onActivityDestroyed");
            }
        });

        try {
            Class.forName("android.os.AsyncTask");
        } catch (Throwable th) {
        }
        app = this;
    }

}
