package br.com.leonardo.waller;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Leonardo de Matos on 26/04/17.
 */

public class Preferences {
    @SuppressWarnings("all")
    private static final String PREFERENCE_NAME = "WALL";
    public static final String RANDOM_BUCKET = "RANDOM";
    public static final String LAST_IMAGE = "LAST_IMAGE";


    public static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static void saveSharedPreference(Context context, String key, String value) {
        SharedPreferences prefs = getSharedPreference(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getSharedPreference(Context context, String key) {
        SharedPreferences prefs = getSharedPreference(context);
        return prefs.getString(key, null);
    }
}
