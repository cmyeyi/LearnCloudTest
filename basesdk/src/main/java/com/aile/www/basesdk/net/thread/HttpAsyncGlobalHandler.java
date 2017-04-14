/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.aile.www.basesdk.net.thread;

/**
 * 
 * @ClassName: HttpAsyncGlobalHandler
 * @Description: 网络异步处理回调的全局接口，如果有对网络回调的全局处理，实现这个接口并配置到
 *               {@link HttpProxyConfiguration}中去
 *
 */
public interface HttpAsyncGlobalHandler {

	boolean onSuccess(int requestCode, Object requestObject,
					  Object successResponseObj);

	boolean onError(int requestCode, Object requestObject,
					Object errorResponseObj);

	boolean onException(int requestCode, Object requestObject, int responseCode);

	boolean isError(int requestCode, Object requestObject, Object orginObject);
}
