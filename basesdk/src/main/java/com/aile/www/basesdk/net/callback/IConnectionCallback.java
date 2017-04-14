/*
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.callback;

/**
 * 
 * @ClassName: IConnectionCallback
 * @Description: 网络接口获取到数据后的回调接口，该接口用于异步获取网络数据
 *
 */
public interface IConnectionCallback {

	/**
	 * 
	 * @param requestCode
	 *            发起请求时传入的code
	 * @param requestObj
	 *            发起请求时传入的Object
	 * @param responseCode
	 *            网络连接的状态码
	 * @param recevicedObj
	 *            网络数据
	 * @description 异步处理网络数据的回调接口
	 */
	public void handleNetInput(int requestCode, Object requestObj,
							   int responseCode, Object recevicedObj);
}
