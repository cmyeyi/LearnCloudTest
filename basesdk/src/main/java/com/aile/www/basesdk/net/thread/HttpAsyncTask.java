/**
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.thread;

import android.os.AsyncTask;

import com.aile.www.basesdk.net.callback.ConnectionResponse;
import com.aile.www.basesdk.net.core.HttpProxy;

import org.apache.http.HttpStatus;

import java.util.Map;

/**
 * 
 * @ClassName: HttpAsyncTask
 * @Description: http异步连接的AsyncTask实现类
 *
 */
@Deprecated
public class HttpAsyncTask<T> extends AsyncTask<Void, Void, ConnectionResponse> {
	// 常量
	private T adapter;
	private String url;
	private int requestCode;
	private Object requestObj;
	private HttpAsyncListener listener;
	private HttpAsyncGlobalHandler handler;
	private Map headers;


	public HttpAsyncTask(int requestCode, Object requestObj, Map headers, String url,
						 T adapter, HttpAsyncListener listener) {
		this.adapter = adapter;
		this.url = url;
		this.requestCode = requestCode;
		this.requestObj = requestObj;
		this.listener = listener;
		this.headers = headers;
	}

	public void setHttpAsyncGlobalHandler(HttpAsyncGlobalHandler handler) {
		this.handler = handler;
	}

	@Override
	protected ConnectionResponse doInBackground(Void... params) {

		ConnectionResponse response = HttpProxy.getInstace().simpleHttpRequest(
				requestCode, requestObj, url, headers, adapter);
		return response;
	}

	@Override
	protected void onPostExecute(ConnectionResponse response) {
		if (listener == null && handler == null) {
			return;
		}

		// 处理异常情况
		if (response.getResponseCode() != HttpStatus.SC_OK) {
			boolean consume = false;
			if (handler != null) {
				consume = handler.onException(requestCode, requestObj,
						response.getResponseCode());
			}

			if (!consume && listener != null) {
				listener.onException(requestCode, requestObj,
						response.getResponseCode());
			}
			return;
		}

		// 处理返回错误协议情况
		if (handler != null
				&& handler.isError(requestCode, requestObj,
						response.getRecevicedObject())) {
			boolean consume = true;
			consume = handler.onError(requestCode, requestObj,
					response.getRecevicedObject());
			if (!consume && listener != null) {
				listener.onError(requestCode, requestObj,
						response.getRecevicedObject());
			}
			return;
		}

		// 处理返回正常的情况
		boolean consume = false;
		if (handler != null) {
			consume = handler.onSuccess(requestCode, requestObj,
					response.getRecevicedObject());
		}

		if (!consume) {
			listener.onSuccess(requestCode, requestObj,
					response.getRecevicedObject());
		}

	}
}