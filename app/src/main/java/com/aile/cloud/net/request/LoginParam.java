package com.aile.cloud.net.request;

import com.aile.cloud.net.AppParam;
import com.aile.cloud.net.AppSignUtils;
import com.aile.cloud.net.cfg.ApiConfiguration;
import com.aile.www.basesdk.transport.HttpType;

public class LoginParam extends AppParam {

    private LoginParam() {
        url(ApiConfiguration.URL_HOST);
        addParams(params, "method", "login");
    }

    @Override
    public HttpType httpType() {
        return HttpType.GET;
    }

    @Override
    public int getSignType() {
        return AppSignUtils.NEW_SIGN_TYPE;
    }

    public void setParam(String userName, String password) {
        addParams(params, "username", userName);
        addParams(params, "password", password);
    }

    public static LoginParam create(String userName, String password) {
        LoginParam param = new LoginParam();
        param.setParam(userName, password);
        return param;
    }
}
