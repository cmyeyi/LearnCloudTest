package com.aile.cloud.net;

import android.support.annotation.WorkerThread;

import com.aile.www.basesdk.transport.BaseResponse;

public interface IRequest {

    /**
     * 网络数据解析
     *
     * @param response
     */
    @WorkerThread
    void parserJSON(BaseResponse response);

    /**
     * 本地缓存
     *
     * @param response
     */
    @WorkerThread
    void onSaveAsyn(BaseResponse response);

}
