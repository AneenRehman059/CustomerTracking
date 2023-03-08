package com.zasa.superduper.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zasa.superduper.Models.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class PreferencesData {
	
	public static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        sharedPrefs.edit().putString(key, value).commit();
    }

    public static void saveJsonObject(Context context, String key, JSONObject value) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        sharedPrefs.edit().putString(key, value.toString()).commit();
    }
    public static void saveJsonArray(Context context, String key, JSONArray value) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        sharedPrefs.edit().putString(key, value.toString()).commit();
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPrefs.getString(key, defaultValue);
    }

    public static JSONObject getJsonObject(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        try {
            JSONObject obj = new JSONObject(sharedPrefs.getString(key, ""));
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
return null;
        }

    }
    public static JSONArray getJsonArray(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        try {
            JSONArray obj = new JSONArray(sharedPrefs.getString(key, ""));
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}
