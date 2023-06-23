package com.si6a.gemarbaca.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.si6a.gemarbaca.model.GemarBacaData;

public class Utilities {
    public static final String PREFERENCE_FILE_KEY = Utilities.class.getPackage().getName();
    public static final String BASE_URL = "https://expressjs-gemarbaca.vercel.app/";
    private static final String KEY_LOGGED_IN = "loggedIn";
    private static final String KEY_UID = "userId";
    public static GemarBacaData gemarBacaData;

    public static void clearSharedPref(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(KEY_LOGGED_IN);
        editor.remove(KEY_UID);
        editor.clear();
        editor.apply();
    }

    public static void setIsLoggedIn(Context context, Boolean isLoggedIn) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public static Boolean isLoggedIn(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_LOGGED_IN, false);
    }


    public static void setUID(Context context, String UID) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_UID, UID);
        editor.apply();
    }

    public static String getUID(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        return sp.getString(KEY_UID, null);
    }
}
