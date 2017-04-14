/*
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.connection;

import com.aile.www.basesdk.net.callback.ConnectionResponse;
import com.aile.www.basesdk.net.callback.IConnectionCallback;
import com.aile.www.basesdk.net.filter.ConnectionFilterChain;
import com.aile.www.basesdk.net.filter.ConnectionToStringFilter;
import com.aile.www.basesdk.net.filter.IConnectionFilter;
import com.aile.www.basesdk.net.parameter.HttpConnectionParameter;
import com.aile.www.basesdk.net.parameter.IConnectionParameter;
import com.aile.www.basesdk.net.session.HttpSession;
import com.aile.www.basesdk.net.session.ISession;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 
 * @ClassName: DefaultHttpConnection
 * @Description: 基于Http的网络连接
 *
 */
public class DefaultHttpConnection implements IURLConnection {

	private AndroidHttpClient client;

	@Override
	public HttpConnectionParameter getParameter() {
		return new HttpConnectionParameter();
	}

	private HttpResponse connectInternal(HttpConnectionParameter httpParameter)
			throws IOException {
		final HttpUriRequest request = getHttpUriRequest(httpParameter);
		// 添加http头域
		if (httpParameter != null && httpParameter.headParameter != null  && httpParameter.headParameter.entrySet() != null) {
			final Iterator<Entry<String, String>> iterator = httpParameter.headParameter
					.entrySet().iterator();
			while (iterator.hasNext()) {
				final Entry<String, String> entry = iterator.next();
				request.addHeader(entry.getKey(), entry.getValue());
			}
		}
		return client.execute(request);
	}


	/**
	 * 真正连接的地方
	 *
	 * @param requestCode
	 * @param requestObject
	 * @param parameter
	 * @param filterChain
	 * @param callback
	 * @return
	 */
	@Override
	public ConnectionResponse connect(int requestCode, Object requestObject,
									  IConnectionParameter parameter, ConnectionFilterChain filterChain,
									  IConnectionCallback callback) {
		if (!(parameter instanceof HttpConnectionParameter)
				|| !parameter.isValidParameter()) {
			throw new IllegalArgumentException();
		}

		ConnectionResponse response = null;
		String responseStr = null;
		final HttpConnectionParameter httpParameter = (HttpConnectionParameter) parameter;
		// 获取http连接实例
		if (client == null) {
			client = AndroidHttpClient.newInstance("");
		}
		try {
			
			HttpResponse httpResponse = connectInternal(httpParameter);

			final int responseCode = httpResponse.getStatusLine()
					.getStatusCode();
			Object receivedObject = null;
			if (HttpStatus.SC_OK == responseCode
					|| HttpStatus.SC_PARTIAL_CONTENT == responseCode) {
				receivedObject = httpResponse.getEntity().getContent();
				if (filterChain != null) {
					try {
						ISession session = new HttpSession(httpParameter.uri,
								httpResponse);
						filterChain.init();
						while (filterChain.hasNext()) {
							final IConnectionFilter filter = filterChain.next();
							receivedObject = filter.doFilter(requestCode,requestObject, session, receivedObject);
							if(null != receivedObject && filter instanceof ConnectionToStringFilter){
								responseStr = String.valueOf(receivedObject);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						receivedObject = null;
					}
				}
			}

			if (callback != null) {
				callback.handleNetInput(requestCode, requestObject,
						responseCode, receivedObject);
			}
			response = new ConnectionResponse(requestCode, requestObject,
					responseCode, receivedObject);
			response.setResponseString(responseStr);

		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			// 建立连接失败
			response = new ConnectionResponse(requestCode, requestObject,
					ConnectionResponse.RESPONSE_STATUS_TIMEOUT, null);
		} catch (IOException e) {
			// IO失败
			e.printStackTrace();
			response = new ConnectionResponse(requestCode, requestObject,
					ConnectionResponse.RESPONSE_STATUS_IOERROR, null);
		}

		return response;
	}

	/**
	 * @param parameter
	 * @return HttpUriRequest
	 * @throws java.io.IOException
	 * @description 根据输入的参数返回合适的http请求
	 */
	private HttpUriRequest getHttpUriRequest(HttpConnectionParameter parameter)
			throws IOException {
		if (HttpConnectionParameter.METHOD_GET
				.equalsIgnoreCase(parameter.method)) {
			HttpGet get = new HttpGet(parameter.uri);
			return get;
		} else if (HttpConnectionParameter.METHOD_POST
				.equalsIgnoreCase(parameter.method)) {
			HttpPost post = new HttpPost(parameter.uri);
			// 添加http传输数据
			if (parameter.entity != null) {
				post.setEntity(parameter.entity);
			}
			return post;
		}
		return new HttpGet(parameter.uri);
	}

	@Override
	public void close() {
		if (client != null) {
			client.close();
			client = null;
		}
	}
}
