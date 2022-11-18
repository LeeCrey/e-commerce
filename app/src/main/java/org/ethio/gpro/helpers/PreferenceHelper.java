package org.ethio.gpro.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class PreferenceHelper {
    private static final String EMAIL = "email";
    private static final String PWD = "password";
    private static final String TOKEN = "auth_token";
    private static final String rememberMe = "remember_me";
    private static final String PREFS_FILE = "customer_credentials";
    private static final int PREFS_MODE = Context.MODE_PRIVATE;

    public static void setRememberMe(Context context, Boolean remember) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(rememberMe, remember);
        editor.apply();
    }

    public static Boolean getRememberMe(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        return preferences.getBoolean(rememberMe, false);
    }

    public static void storeCredentials(Context context, @NonNull String email, @NonNull String password) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(EMAIL, email);
        editor.putString(PWD, password);
        editor.apply();
    }

    // put authorization token
    public static void setAuthToken(Context context, String token) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(TOKEN, token);
        editor.apply();
    }

    public static HashMap<String, String> getCredentials(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        HashMap<String, String> data = new HashMap<>();

        data.put("email", pref.getString(EMAIL, null));
        data.put("password", pref.getString(PWD, null));

        return data;
    }

    // get authorization token
    public static String getAuthToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        return pref.getString(TOKEN, null);
    }

    // clear saved data
    public static void clearPref(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
