package org.ethio.gpro.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class CustomUrlHelper {
    private static final String PREFS_FILE = "custom_url";
    private static final int PREFS_MODE = Context.MODE_PRIVATE;

    public static void setUrl(Context context, String url) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFS_FILE, url);
        editor.apply();
    }

    public static String getUrl(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        return preferences.getString(PREFS_FILE, null);
    }
}
