/*
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.connection;

import com.aile.www.basesdk.transport.WYHttpConnection;

/**
 * 
 * @ClassName: ConnectionFactory
 * @Description: 静态工厂方法，提供各种类型的网络连接
 *
 */
public class ConnectionFactory {
	/**
	 * 打开一个默认的http连接，该连接能自动适应http和https，但是https只支持单向认证
	 *
	 * @return
	 * @description 打开一个默认的http连接
	 */
	public static DefaultHttpConnection openDefaultHttpConnection() {
		return new DefaultHttpConnection();
	}

	/**
	 * 打开一个默认的http连接，该连接能自动适应http和https，但是https只支持单向认证
	 * @return
	 * @description 打开一个默认的http连接
	 */
	public static WYHttpConnection CREATE_HTTPCONNECTION() {
		return new WYHttpConnection();
	}
}
