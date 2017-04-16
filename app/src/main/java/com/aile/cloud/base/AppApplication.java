package com.aile.cloud.base;

import android.app.Application;

import com.aile.www.basesdk.LoginPreference;
import com.aile.www.basesdk.SDKConfig;


public class AppApplication extends Application {
    private static AppApplication instance;
    private String userName;
    private String userWorker;
    private String userPhone;

    public static AppApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SDKConfig.init(this);
        LoginPreference.getInstance().init(getApplicationContext());
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

}
