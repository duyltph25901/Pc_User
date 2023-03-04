package com.example.pc_user.functions.first_install_app;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static final String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    private Context context;

    public MySharedPreferences(Context context) {
        this.context = context;
    }

    public void putBooleanValue(String KEY, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY, value);
        editor.apply();
    }

    public boolean getBooleanValue(String KEY) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(KEY, false);
    }
}
