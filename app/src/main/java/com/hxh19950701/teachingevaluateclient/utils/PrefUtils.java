package com.hxh19950701.teachingevaluateclient.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class PrefUtils {

    private PrefUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    private static SharedPreferences sharedPreferences = null;

    //init  
    public static void init(Context context) {
        init(context, "config");
    }

    public static void init(Context context, String name) {
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    //get
    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    //Boolean
    public static boolean putBoolean(String key, boolean value) {
        return sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    //String  
    public static boolean putString(String key, String value) {
        return sharedPreferences.edit().putString(key, value).commit();
    }

    public static String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    //Int  
    public static boolean putInt(String key, int value) {
        return sharedPreferences.edit().putInt(key, value).commit();
    }

    public static int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    //Float  
    public static boolean putFloat(String key, float value) {
        return sharedPreferences.edit().putFloat(key, value).commit();
    }

    public static float getFloat(String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    //Long  
    public static boolean putLong(String key, long value) {
        return sharedPreferences.edit().putLong(key, value).commit();
    }

    public static long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    //StringSet  
    public static boolean putStringSet(String key, Set<String> value) {
        return sharedPreferences.edit().putStringSet(key, value).commit();
    }

    public static Set<String> getStringSet(String key, Set<String> defValue) {
        return sharedPreferences.getStringSet(key, defValue);
    }

    //clear  
    public static boolean clear() {
        return sharedPreferences.edit().clear().commit();
    }

    //remove  
    public static boolean remove(String key) {
        return sharedPreferences.edit().remove(key).commit();
    }
}  