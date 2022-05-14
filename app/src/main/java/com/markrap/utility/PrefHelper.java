package com.markrap.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.markrap.BuildConfig;

/**
 * Created by root on 22/5/17.
 */
public class PrefHelper {

    public static final String MyPREFERENCES = BuildConfig.APPLICATION_ID + ".PREF";
    public static final String DEVICE_TOKEN = BuildConfig.APPLICATION_ID + ".DEVICE_TOKEN";

    public static PrefHelper cInstance;
    private Context _ctx;
    private SharedPreferences sharedpreferences;

    /* Static 'instance' method */
    public static PrefHelper getInstance() {
        return cInstance;
    }

    public static PrefHelper getInstance(Context context) {
        if (cInstance == null) {
            cInstance = new PrefHelper(context);
        }
        return cInstance;
    }

    public static void initInstance(Context context) {

        if (cInstance == null) {
            cInstance = new PrefHelper(context);
        }
    }


    public PrefHelper(Context _ctx) {
        this._ctx = _ctx;
        sharedpreferences = _ctx.getSharedPreferences(
                MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void storeSharedValue(String key, String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getSharedValue(String key) {
        return sharedpreferences.getString(key, "");
    }

    public void clearSharedValue(String key) {

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(key);
        editor.commit();

    }

    public void setDeviceToken(String deviceToken) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(DEVICE_TOKEN, deviceToken);
        editor.commit();
    }

    public String getDeviceToken() {
        return sharedpreferences.getString(DEVICE_TOKEN, "");
    }

    public void storeBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }




}
