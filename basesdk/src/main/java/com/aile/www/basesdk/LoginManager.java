package com.aile.www.basesdk;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.aile.www.basesdk.utils.SDKLogger;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.ListIterator;


/**
 * @Description 登录管理类, 实现，保存登录成功后的信息，自动登录，注销功能
 */
public class LoginManager {

    private static final String TAG = "login.manager";
    private static LoginManager mInstance;
    private WtAccount mOldAccount;
    private Context mAppContext = null;
    private String mLoginAction = "";
    private String mLogoutAction = "";
    private String mDeviceId = "";
    private LoginInfo mAccount;
    private ArrayList<WeakReference<ILoginVerify>> mLoginListeners = new ArrayList<>();
    private ILoginVerify mFirstListener;

    private LoginManager() {}

    public static synchronized LoginManager getInstance() {
        if (mInstance == null) {
            mInstance = new LoginManager();
        }
        return mInstance;
    }

    public String getLoginAction() {
        return mLoginAction;
    }

    public String getLogoutAction() {
        return mLogoutAction;
    }

    public void logout() {
        synchronized (LoginManager.this) {
            if (mAccount != null) {
                LoginInfo currentAccount = mAccount;
                mAccount = null;
                saveWYUserInfo("");
                notifyListeners(false, currentAccount);

                if (mOldAccount != null && WtAccount.LOGIN_WX == mOldAccount.getLoginType()) {
                    LoginPreference.getInstance().saveLoginData("");
                }
                mOldAccount = null;
//                sendLogoutBroadcast();
            }
        }
    }


    /**
     * 登录成功的广播
     */
    private void sendLoginSuccessBroadcast() {
        Intent intent = new Intent();
        intent.setAction(mLoginAction);
        LocalBroadcastManager.getInstance(mAppContext).sendBroadcast(intent);
    }

    /**
     * 注销的广播
     */
    private void sendLogoutBroadcast() {
        Intent intent = new Intent();
        intent.setAction(mLogoutAction);
        LocalBroadcastManager.getInstance(mAppContext).sendBroadcast(intent);
    }

    /**
     *
     * @param appContext
     * @param deviceId
     * @param firstListener  在登录登出时被执行的第一个监听器
     */
    public void init(Context appContext, String deviceId, @NonNull ILoginVerify firstListener) {
        if (mAppContext == null) {
            mAppContext = appContext.getApplicationContext();
            mDeviceId = deviceId;
            mFirstListener = firstListener;
            String pkgName = appContext.getPackageName();
            mLoginAction = pkgName + ".LOGIN.ACTION";
            mLogoutAction = pkgName + ".LOGOUT.ACTION";

            SDKLogger.d(TAG, "LoginManager init:" + mLoginAction + " " + mLogoutAction);

            LoginPreference.getInstance().init(mAppContext);
        }
    }

    /**
     * 注册一个登陆监听器，并保持为弱引用，同一监听器不会重复注册
     *
     * @param loginVerify
     */
    public synchronized void registerLoginListener(ILoginVerify loginVerify) {
        if(mFirstListener == loginVerify){
            return;
        }
        for (WeakReference<ILoginVerify> weakReference : mLoginListeners) {
            ILoginVerify listener = weakReference.get();
            if (listener != null && listener == loginVerify) {
                //已注册，直接返回
                return;
            }
        }
        pruneListeners();
        mLoginListeners.add(new WeakReference<ILoginVerify>(loginVerify));
    }

    /**
     * 解除注册一个登陆监听器，不能解除第一个必须的监听器
     *
     * @param loginVerify
     */
    public synchronized void unregisterLoginListener(ILoginVerify loginVerify) {
        for (WeakReference<ILoginVerify> weakReference : mLoginListeners) {
            ILoginVerify listener = weakReference.get();
            if (listener != null && listener == loginVerify) {
                //已注册，移除
                weakReference.clear();
                mLoginListeners.remove(weakReference);
                return;
            }
        }
        pruneListeners();
    }

    /**
     * 通知监听器
     */
    private synchronized void notifyListeners(boolean isLogined, LoginInfo userInfo) {
        if(mFirstListener != null){
            mFirstListener.onLoginChanged(isLogined, userInfo);
        }
        for (WeakReference<ILoginVerify> weakReference : mLoginListeners) {
            ILoginVerify listener = weakReference.get();
            if (listener != null) {
                listener.onLoginChanged(isLogined, userInfo);
            }
        }
        pruneListeners();
    }

    /**
     * 通知监听器,中途退出了登录操作
     */
    public synchronized void notifyLoginCancel() {
        if(mFirstListener != null){
            mFirstListener.onLoginCancel();
        }
        for (WeakReference<ILoginVerify> weakReference : mLoginListeners) {
            ILoginVerify listener = weakReference.get();
            if (listener != null) {
                listener.onLoginCancel();
            }
        }
    }

    /**
     * 清理监听器
     */
    private synchronized void pruneListeners(){
        if(mLoginListeners != null) {
            ListIterator<WeakReference<ILoginVerify>> listIterator = mLoginListeners.listIterator();
            WeakReference<ILoginVerify> weakReference = null;
            while (listIterator.hasNext()) {
                weakReference = listIterator.next();
                ILoginVerify listener = weakReference.get();
                if (listener == null) {
                    listIterator.remove();
                }
            }
        }
    }

    /**
     * 获取用户信息
     */
    public synchronized LoginInfo getUserInfo() {
        if (mAccount == null) {
                try {
                    String data = LoginPreference.getInstance().getLoginData();
                    if (!TextUtils.isEmpty(data)) {
//                        String json = SimpleCrypto.decrypt(mDeviceId, data);
                        mAccount = new Gson().fromJson(data, LoginInfo.class);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return mAccount;
    }

    /**
     * 保存用户信息
     */
    private synchronized void saveWYUserInfo(String json) {
//        String encryt = null;
//        try {
//            encryt = SimpleCrypto.encrypt(mDeviceId, json);
//        } catch (Exception e) {
//            encryt = "";
//        }
        LoginPreference.getInstance().saveLoginData(json);
    }

    /**
     * 保存用户信息
     */
    public synchronized void setUserInfo(LoginInfo user) {
        mAccount = null;
        String json = user != null ? new Gson().toJson(user) : "";
        saveWYUserInfo(json);
        mAccount = getUserInfo();
    }

    /**
     * 清除用户信息，退出登录状态，但不发出退出登录的广播
     */
    public synchronized void clearUserInfo() {
        setUserInfo(null);
    }

    /**
     * 保存用户信息,并发送登录广播
     */
    public synchronized void loignUser(LoginInfo user) {
        setUserInfo(user);
        sendLoginSuccessBroadcast();
        notifyListeners(true, mAccount);
    }

    public synchronized boolean isUserLogin() {
        return getUserInfo() != null;
    }
}