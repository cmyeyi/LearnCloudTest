/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.aile.www.basesdk.net.thread;

/**
 * 
 * @ClassName: HttpAsyncListener
 * @Description: http异步连接的回调接口
 *
 */
public interface HttpAsyncListener {

	void onSuccess(int requestCode, Object requestObject,
				   Object successResponseObj);

	void onError(int requestCode, Object requestObject, Object errorResponseObj);

	void onException(int requestCode, Object requestObject, int responseCode);
}
