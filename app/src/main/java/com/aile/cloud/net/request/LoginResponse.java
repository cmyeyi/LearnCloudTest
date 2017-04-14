package com.aile.cloud.net.request;

import com.aile.cloud.net.been.BaseInfo;
import com.aile.www.basesdk.LoginInfo;
import com.aile.www.basesdk.transport.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LoginResponse extends BaseResponse {
    private BaseInfo returnInfo;
    private LoginInfo loginInfo;

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public BaseInfo getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(BaseInfo returnInfo) {
        this.returnInfo = returnInfo;
    }

    public boolean isSuccess() {
        if (null == returnInfo) {
            return false;
        }
        return returnInfo.isSuccess();
    }

    public static LoginResponse parse(BaseResponse response) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(response.content(),new TypeToken<LoginResponse>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
