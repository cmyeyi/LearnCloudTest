package com.aile.www.basesdk;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.aile.www.basesdk.utils.SDKLogger;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @Description 存储登录帐号信息的类
 */
public class WtAccount {
    private static final String TAG = "elife.login.account";

    public static final int LOGIN_QQ = 0;
    public static final int LOGIN_WX = 1;

    private String uin;
    private String nickName;
    private String pwdSig;
    private String lsKey;
    private Bitmap faceIcon;
    private String faceIconUrl;
    private int loginType;

    public WtAccount() {
        this.uin = "";
        this.pwdSig = "";
        this.nickName = "";
        this.lsKey = "";
        this.faceIconUrl = "";
        this.loginType = LOGIN_QQ;
    }

    public WtAccount(String uin, String nickName, String pwdSig, String lsKey) {
        this(LOGIN_QQ, uin, nickName, pwdSig, lsKey, "");
    }

    public WtAccount(int loginType, String uin, String nickName, String pwdSig, String lsKey, String faceIconUrl) {
        this.loginType = loginType;
        this.uin = uin;
        this.nickName = nickName;
        this.pwdSig = pwdSig;
        this.lsKey = lsKey;
        this.faceIconUrl = faceIconUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof WtAccount) {
            WtAccount account = (WtAccount) o;
            return this.uin.equals(account.uin);
        }
        return false;
    }

    @Override
    public String toString() {
        try {
            JSONObject json = new JSONObject();
            json.put("uin", uin);
            json.put("pwdSig", pwdSig);
            json.put("loginType", loginType);
            json.put("nickName", nickName);
            json.put("faceIconUrl", faceIconUrl);

            return json.toString();
        } catch (JSONException e) {
            SDKLogger.e(TAG, "toString:" + e.toString());
            return "";
        }
    }

    public static WtAccount parseFromJsonStr(String accountJsonStr) {
        if (!TextUtils.isEmpty(accountJsonStr)) {
            try {
                WtAccount account = new WtAccount();
                JSONObject accountJson = new JSONObject(accountJsonStr);
                account.uin = accountJson.getString("uin");
                account.pwdSig = accountJson.getString("pwdSig");

                try {
                    // 兼容旧版本的登录信息,因为旧版本没有这个字段,会抛异常
                    account.loginType = accountJson.getInt("loginType");
                    if (LOGIN_WX == account.loginType) {
                        account.nickName = accountJson.getString("nickName");
                        account.faceIconUrl = accountJson.getString("faceIconUrl");
                        account.lsKey = accountJson.getString("pwdSig");
                    }

                } catch (Exception e) {
                    account.loginType = LOGIN_QQ;
                }

                return account;

            } catch (Exception e) {
                SDKLogger.e(TAG, "parseFromJsonStr:" + e.toString());
            }
        }

        return null;
    }

    // //////////////////////////////////////////////////////////

    public Bitmap getFaceIcon() {
        return faceIcon;
    }

    public void setFaceIcon(Bitmap faceIcon) {
        this.faceIcon = faceIcon;
    }

    public String getUin() {
        return uin;
    }

    public void setUin(String uin) {
        this.uin = uin;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPwdSig() {
        return pwdSig;
    }

    public void setPwdSig(String pwdSig) {
        this.pwdSig = pwdSig;
    }

    public String getLsKey() {
        return lsKey;
    }

    public void setLsKey(String lsKey) {
        this.lsKey = lsKey;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public String getFaceIconUrl() {
        return faceIconUrl;
    }

    public void setFaceIconUrl(String faceIconUrl) {
        this.faceIconUrl = faceIconUrl;
    }
}
