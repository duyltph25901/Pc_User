package com.example.pc_user.functions.first_install_app;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManagement.init(getApplicationContext());
    }
}
