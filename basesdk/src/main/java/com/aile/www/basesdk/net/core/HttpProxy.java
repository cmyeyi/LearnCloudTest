/**
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.core;

import android.text.TextUtils;

import com.aile.www.basesdk.net.adapter.AbsHttpFileProtocolAdapter;
import com.aile.www.basesdk.net.adapter.AbsHttpProtocolAdapter;
import com.aile.www.basesdk.net.adapter.AbsHttpTextProtocolAdapter;
import com.aile.www.basesdk.net.callback.ConnectionResponse;
import com.aile.www.basesdk.net.callback.IProgressCallback;
import com.aile.www.basesdk.net.connection.ConnectionFactory;
import com.aile.www.basesdk.net.connection.DefaultHttpConnection;
import com.aile.www.basesdk.net.filter.ConnectionDownloadFileFilter;
import com.aile.www.basesdk.net.filter.ConnectionFilterChain;
import com.aile.www.basesdk.net.filter.ConnectionStringToBeanFilter;
import com.aile.www.basesdk.net.filter.ConnectionToStringFilter;
import com.aile.www.basesdk.net.parameter.HttpConnectionParameter;
import com.aile.www.basesdk.net.sign.ISignConstructor;
import com.aile.www.basesdk.utils.NetWorkUtils;

import org.apache.http.HttpStatus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 为上层提供常用的网络连接代理方法
 * 
 * @title HttpProxy
 * @version 3.0
 */
public class HttpProxy {

	private static HttpProxy instance;
	private HttpProxyConfiguration config;

	private HttpProxy() {

	}

	public static HttpProxy getInstace() {
		if (instance == null) {
			instance = new HttpProxy();
		}

		return instance;
	}

	public void init(HttpProxyConfiguration config) {
		this.config = config;
	}

	public ConnectionResponse simpleHttpRequest(int requestCode,
												Object requestObj, String url, Object adapter, Map headers,
												ISignConstructor signConstructor) {
		// TODO Auto-generated method stub
		if (adapter instanceof AbsHttpTextProtocolAdapter) {
			return simpleHttpTextRequest(requestCode, requestObj, url,
					(AbsHttpTextProtocolAdapter) adapter, headers, signConstructor);
		} else if (adapter instanceof AbsHttpFileProtocolAdapter) {
			return simpleHttpFileRequest(requestCode, requestObj, url,
					(AbsHttpFileProtocolAdapter) adapter, headers,signConstructor);
		}
		return null;
	}

	public ConnectionResponse simpleHttpRequest(int requestCode, Object requestObj, String url, Map headers, Object adapter) {

		return simpleHttpRequest(requestCode, requestObj, url, adapter, headers, null);
	}

	/**
	 * 进行http访问请求,该请求用于处理基于文本协议的http请求
	 * 
	 * @param requestCode
	 *            请求代码
	 * @param requestObj
	 *            请求附带的变量
	 * @param url
	 *            请求网址
	 * @param adapter
	 *            文本协议适配器
	 * @return http的请求结果
	 */
	public ConnectionResponse simpleHttpTextRequest(int requestCode,
													Object requestObj, String url, AbsHttpTextProtocolAdapter adapter, Map headers,
													ISignConstructor signConstructor) {

		// 超时（网络）异常
		if (!NetWorkUtils.isNetworkAvailable(config.context)) {
			return new ConnectionResponse(requestCode, requestObj,
					ConnectionResponse.RESPONSE_STATUS_NET_UNABLED, null);
		}

		DefaultHttpConnection httpRequest = ConnectionFactory
				.openDefaultHttpConnection();

		try {
			// 设置http参数
			HttpConnectionParameter params = httpRequest.getParameter();

			// 设置url
			if (TextUtils.isEmpty(url)) {
				return new ConnectionResponse(requestCode, requestObj,
						ConnectionResponse.RESPONSE_STATUS_MALFORMEDURL, null);
			}
			params.uri = url;
			// http方法
			int type = adapter.getPostType();
			if (type == AbsHttpProtocolAdapter.TYPE_KV) {
				Map map = adapter.beanToMap();
				if (signConstructor != null) {
					map = signConstructor.sign(map);
				}
				if (map == null) {
					params.method = HttpConnectionParameter.METHOD_GET;
				} else {
					params.method = HttpConnectionParameter.METHOD_POST;
					params.setData(map);
				}
			} else if (type == AbsHttpProtocolAdapter.TYPE_TEXT) {
				String text = adapter.beanToString(requestObj);
				if (TextUtils.isEmpty(text)) {
					params.method = HttpConnectionParameter.METHOD_GET;
				} else {
					params.method = HttpConnectionParameter.METHOD_POST;
					params.setString(text);
				}
			}
			// http头域
			params.headParameter = config.httpHeader;

			if (headers != null) {
				params.headParameter = new HashMap<>(params.headParameter);
				params.headParameter.putAll(headers);
			}
			// http过滤链
			ConnectionFilterChain filterChain = new ConnectionFilterChain();
			filterChain.addFilter(new ConnectionToStringFilter("utf-8",
					ConnectionToStringFilter.ORI_STREAM));
			filterChain.addFilter(new ConnectionStringToBeanFilter(adapter));

			ConnectionResponse response = httpRequest.connect(requestCode,
					requestObj, params, filterChain, null);

			if (response == null) {
				return new ConnectionResponse(requestCode, requestObj,
						ConnectionResponse.RESPONSE_STATUS_EXCEPTION, null);
			}

			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return new ConnectionResponse(requestCode, requestObj,
					ConnectionResponse.RESPONSE_STATUS_EXCEPTION, null);
		} finally {
			// release the connection
			httpRequest.close();
		}
	}

	public ConnectionResponse simpleHttpFileRequest(int requestCode,
													Object requestObj, String url, AbsHttpFileProtocolAdapter adapter, Map headers,
													ISignConstructor signConstructor) {
		//
		// 超时（网络）异常
		if (!NetWorkUtils.isNetworkAvailable(config.context)) {
			return new ConnectionResponse(requestCode, requestObj,
					ConnectionResponse.RESPONSE_STATUS_NET_UNABLED, null);
		}

		DefaultHttpConnection httpRequest = ConnectionFactory
				.openDefaultHttpConnection();

		try {
			// 设置http参数
			HttpConnectionParameter params = httpRequest.getParameter();

			// 设置url
			if (TextUtils.isEmpty(url)) {
				return new ConnectionResponse(requestCode, requestObj,
						ConnectionResponse.RESPONSE_STATUS_MALFORMEDURL, null);
			}
			params.uri = url;

			// http方法
			Map map = adapter.beanToMap();
			if (signConstructor != null) {
				map = signConstructor.sign(map);
			}
			params.method = HttpConnectionParameter.METHOD_POST;
			params.setMultipartData(map);

			// http头域
			params.headParameter = config.httpHeader;
			if (headers != null) {
				params.headParameter = new HashMap<>(params.headParameter);
				params.headParameter.putAll(headers);
			}
			// http过滤链
			ConnectionFilterChain filterChain = new ConnectionFilterChain();
			filterChain.addFilter(new ConnectionToStringFilter("utf-8",
					ConnectionToStringFilter.ORI_STREAM));
			filterChain.addFilter(new ConnectionStringToBeanFilter(adapter));

			ConnectionResponse response = httpRequest.connect(requestCode,
					requestObj, params, filterChain, null);

			if (response == null) {
				return new ConnectionResponse(requestCode, requestObj,
						ConnectionResponse.RESPONSE_STATUS_EXCEPTION, null);
			}

			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return new ConnectionResponse(requestCode, requestObj,
					ConnectionResponse.RESPONSE_STATUS_EXCEPTION, null);
		} finally {
			// release the connection
			httpRequest.close();
		}
	}

	/**
	 * 从网上下载文件到本地 该方法为同步方法
	 * 
	 * @param filePath
	 *            文件保存路径
	 * @param progressCallback
	 *            下载过程中进度回调接口。如果不需要则置空
	 * @param resume
	 *            是否需要断点续传
	 * @return 文件下载是否成功
	 */
	public boolean httpGetFile(String url, String filePath, IProgressCallback progressCallback, boolean resume) {
		if (TextUtils.isEmpty(url) || TextUtils.isEmpty(filePath)) {
			return false;
		}
		DefaultHttpConnection httpRequest = ConnectionFactory.openDefaultHttpConnection();
		// 设置http参数
		try {
			HttpConnectionParameter params = httpRequest.getParameter();
			params.setDefaultValue();
			params.uri = url;
//			String dirPath = url.substring(0, url.lastIndexOf("/"));

			// 断点续传的判断
			File fileOut = new File(filePath);
			if (resume && fileOut.exists()) {
				final long size = fileOut.length();
				params.headParameter.put("Range", "bytes=" + size + "-");
			} else {
				fileOut.createNewFile();
			}

			// http过滤链
			ConnectionFilterChain filterChain = new ConnectionFilterChain();
			filterChain.addFilter(new ConnectionDownloadFileFilter(filePath,
					progressCallback, resume));
			ConnectionResponse response = httpRequest.connect(0, filePath,
					params, filterChain, null);

			if (response != null) {
				int statusCode = response.getResponseCode();
				if (HttpStatus.SC_OK == statusCode
						|| HttpStatus.SC_PARTIAL_CONTENT == statusCode) {
					Object result = response.getRecevicedObject();
					if (result instanceof File) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpRequest.close();
		}

		return false;
	}

}
