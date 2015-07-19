package com.redmadintern.mikhalevich.security.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Alexander on 15.07.2015.
 */
public class PrefsUtil {
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_AUTHORIZED = "is_authorized";
    public static final String KEY_PIN_HASH = "pin_hash";

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void writeToken(Context context, String token) {
        SharedPreferences prefs = getPrefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putBoolean(KEY_AUTHORIZED, true);
        editor.commit();
    }

    public static String readToken(Context context) {
        SharedPreferences prefs = getPrefs(context);
        String token = prefs.getString(KEY_TOKEN, "");
        return token;
    }

    public static boolean isAuthorized(Context context) {
        SharedPreferences prefs = getPrefs(context);
        return prefs.getBoolean(KEY_AUTHORIZED, false);
    }

    public static void writePinHash(Context context, String pinHash) {
        SharedPreferences prefs = getPrefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_PIN_HASH, pinHash);
        editor.commit();
    }

    public static String readPinHash(Context context) {
        SharedPreferences prefs = getPrefs(context);
        String pinHash = prefs.getString(KEY_PIN_HASH, "");
        return pinHash;
    }
}
