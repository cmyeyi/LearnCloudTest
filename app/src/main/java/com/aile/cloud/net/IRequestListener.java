package com.aile.cloud.net;

import android.support.annotation.UiThread;

import com.aile.www.basesdk.transport.BaseResponse;

/**
 *  request任务回调
 */

public interface IRequestListener<T extends BaseResponse> {
   /**
    * 回调
    * @param response
     */
   @UiThread
   void onUIComplete(T response);
}
