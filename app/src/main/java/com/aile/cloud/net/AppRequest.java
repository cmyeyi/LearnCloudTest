package com.aile.cloud.net;

import android.os.Handler;
import android.os.Looper;

import com.aile.www.basesdk.UnProguardable;
import com.aile.www.basesdk.transport.BaseParam;
import com.aile.www.basesdk.transport.BaseResponse;
import com.aile.www.basesdk.transport.HTTPP;


/**
 * Created by zhanchaohu on 2016/11/8.
 */

public abstract class AppRequest implements Runnable, IRequest, UnProguardable {

    private BaseParam param;
    protected IRequestListener listener;

    public AppRequest(BaseParam param, IRequestListener listener) {
        this.param = param;
        this.listener = listener;
    }

    @Override
    public void run() {

        BaseResponse response = HTTPP.getInstance().httpRequest(param);

        PARSER_DEFAULT_STATUS(response);

        parserJSON(response);
    }


    /**
     *  static handler 
     */
    private static Handler handler = new Handler(Looper.getMainLooper());
    public static Handler getUIHandler(){
        if(null == handler){
            handler = new Handler(Looper.getMainLooper());
        }

        return handler;
    }

    //// TODO: 2016/11/18
    protected void PARSER_DEFAULT_STATUS(BaseResponse response){

        if(null == response){
            return;
        }

        response.isSuccess(CheckStatus.IS_SUCCESS(response.content()));
    }
}
