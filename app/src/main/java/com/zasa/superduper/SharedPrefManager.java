package com.zasa.superduper;

import android.content.Context;
import android.content.SharedPreferences;

import com.zasa.superduper.retrofit.ApiEndpoints;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "sharedPref";
    private SharedPreferences sharedPreferences;
    Context context;
    SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        this.context = context;
    }

    public boolean isLoggedIn() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("logged", false);
    }

    public static void saveTokenPrefrences(Context mContext, String key){
        SharedPreferences token_prefrence = mContext.getSharedPreferences(ApiEndpoints.PREF_TOKEN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = token_prefrence.edit();
        editor1.clear().apply();
    }
}
