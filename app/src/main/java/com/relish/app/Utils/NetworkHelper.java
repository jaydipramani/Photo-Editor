package com.relish.app.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper {
    private static NetworkInfo getNetworkInfo(Context context) {
        return ((ConnectivityManager) context.getApplicationContext().getSystemService(context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context.getApplicationContext());
        return info != null && info.isConnected();
    }

    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context.getApplicationContext());
        return info != null && info.isConnected() && info.getType() == 1;
    }

    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context.getApplicationContext());
        return info != null && info.isConnected() && info.getType() == 0;
    }

    private static boolean isRoaming(Context context) {
        return getNetworkInfo(context.getApplicationContext()).isRoaming();
    }
}
