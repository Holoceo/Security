package com.redmadintern.mikhalevich.security.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Alexander on 15.07.2015.
 */
public class PrefsUtil {
    public static final String PREFS_NAME = "MyPrefsFile";

    public static void writeToken(Context context, String token) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("token", token);
        editor.putBoolean("isAuthorized", true);
        editor.commit();
    }

    public static String readToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = settings.getString("token", "");
        return token;
    }

    public static boolean isAuthorized(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean("isAuthorized", false);
    }

    public static void writePinHash(Context context, String pinHash) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("pinHash", pinHash);
        editor.commit();
    }

    public static String readPinHash(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String pinHash = settings.getString("pinHash", "");
        return pinHash;
    }
}
