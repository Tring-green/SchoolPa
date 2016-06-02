package com.example.schoolpa.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 2016/2/18.
 */
public class SharedPreferenceUtils {

    private static SharedPreferences sp;
    private static String name = "config";

    public static String getString(Context context, String key) {
        sp = context.getSharedPreferences(name, Context
                .MODE_PRIVATE);
        return sp.getString(key, "0");
    }

    public static void putString(Context context, String key, String value) {
        sp = context.getSharedPreferences(name, Context
                .MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static void delete(Context context){
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}
