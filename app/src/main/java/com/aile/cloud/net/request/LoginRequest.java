package com.aile.cloud.net.request;

import android.util.Log;

import com.aile.cloud.net.IRequestListener;
import com.aile.cloud.net.AppRequest;
import com.aile.www.basesdk.transport.BaseParam;
import com.aile.www.basesdk.transport.BaseResponse;

public class LoginRequest extends AppRequest {
    public LoginRequest(BaseParam param, IRequestListener listener) {
        super(param, listener);
    }

    @Override
    public void parserJSON(BaseResponse response) {
        Log.d("LoginRequest", "callback");
        if (null == listener) {
            return;
        }

        final LoginResponse loginResponse = LoginResponse.parse(response);
        getUIHandler().post(new Runnable() {
            @Override
            public void run() {
                if (listener == null) {
                    return;
                }
                listener.onUIComplete(loginResponse);
            }
        });
    }

    @Override
    public void onSaveAsyn(BaseResponse response) {

    }
}
