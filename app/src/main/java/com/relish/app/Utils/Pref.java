package com.relish.app.Utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.relish.app.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Pref {

    private static SharedPreferences sharedPreferences = null;


    private static void openPref(Context context) {
        try {
            sharedPreferences = context.getSharedPreferences(R.string.app_name + "_PREF", MODE_PRIVATE);
        } catch (Exception e) {
        }
    }

    /*Fore String Value Store*/
    public static String getStringValue(Context context, String key, String defaultValue) {
        Pref.openPref(context);
        String result = Pref.sharedPreferences.getString(key, defaultValue);
        Pref.sharedPreferences = null;
        return result;
    }

    public static void setStringValue(Context context, String key, String value) {
        Pref.openPref(context);
        SharedPreferences.Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putString(key, value);
        prefsPrivateEditor.apply();
        prefsPrivateEditor = null;
        Pref.sharedPreferences = null;
    }

    /*For Integer Value*/

    public static Integer setIntValue(Context context, String key, int value) {
        Pref.openPref(context);
        SharedPreferences.Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putInt(key, value);
        prefsPrivateEditor.commit();
        prefsPrivateEditor = null;
        Pref.sharedPreferences = null;
        return null;
    }

    public static int getIntValue(Context context, String key, int defaultValue) {
        Pref.openPref(context);
        int result = Pref.sharedPreferences.getInt(key, defaultValue);
        sharedPreferences = null;
        return result;
    }

    /*For boolean Value Store*/

    public static boolean getBooleanValue(Context context, String key, boolean defaultValue) {
        Pref.openPref(context);
        boolean result = Pref.sharedPreferences.getBoolean(key, defaultValue);
        sharedPreferences = null;
        return result;
    }

    public static void setBooleanValue(Context context, String key, boolean value) {
        Pref.openPref(context);
        SharedPreferences.Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.putBoolean(key, value);
        prefsPrivateEditor.commit();
        prefsPrivateEditor = null;
        Pref.sharedPreferences = null;
    }


    /*For Remove variable from pref*/

    public static void removeValue(Context context, String key) {
        Pref.openPref(context);
        SharedPreferences.Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
        prefsPrivateEditor.remove(key);
        prefsPrivateEditor.commit();
        prefsPrivateEditor = null;
        Pref.sharedPreferences = null;
    }

    public static void storeArray(Context context, ArrayList addOnsOption) {
        SharedPreferences prefs = context.getSharedPreferences("addOns", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        Set<String> set = new HashSet<String>();
        set.addAll(addOnsOption);
        edit.putStringSet("addOnsOptions", set);
        edit.commit();
    }

    public static ArrayList retrieveArray(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("addOns", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        Set<String> set = prefs.getStringSet("addOnsOptions", null);
        ArrayList sample = new ArrayList<String>(set);
        return sample;
    }

    public static void removeArray(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("addOns", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove("addOnsOptions");
        edit.commit();
        prefs = null;
    }

}