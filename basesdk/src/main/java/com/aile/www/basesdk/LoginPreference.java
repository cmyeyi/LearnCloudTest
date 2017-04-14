package com.aile.www.basesdk;

import android.content.Context;
import android.content.SharedPreferences;

import com.aile.www.basesdk.utils.SDKLogger;

import java.io.File;


/**
 * @Description 为实现自动登录和记住用户名密码功能，存储帐号信息和设置信息
 * @author zijianlu
 */
public class LoginPreference {
    private static final String TAG = "login.pref";
    private SharedPreferences mLoginPref;
    private static LoginPreference mInstance;

    private LoginPreference() {
    }

    public void init(Context ctx) {
        if (mLoginPref == null) {
            //清除老版本wtlogin的帐号信息
            File path = new File(ctx.getApplicationInfo().dataDir, "shared_prefs");
            File f =new File(path, "wtlogin.xml.xml");
            if (f.exists()){
                SharedPreferences tempPref = ctx.getSharedPreferences("wtlogin.xml", 0);
                SharedPreferences.Editor localEditor = tempPref.edit();
                localEditor.clear();
                localEditor.commit();
            }
            
            //wtlogin.xml
            mLoginPref = ctx.getSharedPreferences("950935E980E5E2F218.xml", 0);
        }
    }

    public static synchronized LoginPreference getInstance() {
        if (mInstance == null) {
            mInstance = new LoginPreference();
        }
        return mInstance;
    }

    public synchronized void saveLoginData(String loginDataStr) {
        SDKLogger.d(TAG, "save:" + loginDataStr);
        if (loginDataStr != null && mLoginPref != null) {
            SharedPreferences.Editor localEditor = mLoginPref.edit();
            //wtlog_data
            localEditor.putString("11ADFA4C9C0", loginDataStr);
            localEditor.commit();
        }
    }

    public synchronized String getLoginData() {
        if (mLoginPref != null) {
            //wtlog_data
            String loginStr = mLoginPref.getString("11ADFA4C9C0", "");
            SDKLogger.d(TAG, loginStr);
            return loginStr;
        }

        return "";
    }
}
