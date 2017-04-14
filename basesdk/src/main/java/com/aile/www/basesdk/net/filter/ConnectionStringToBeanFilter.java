/**
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.filter;

import com.aile.www.basesdk.net.adapter.AbsHttpProtocolAdapter;
import com.aile.www.basesdk.net.session.ISession;

/**
 * 
 * @ClassName: ConnectionStringToBeanFilter
 * @Description: 字符串转换为bean过滤流
 *
 */
public class ConnectionStringToBeanFilter implements IConnectionFilter {
	AbsHttpProtocolAdapter adapter;

	public ConnectionStringToBeanFilter(AbsHttpProtocolAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public Object doFilter(int requestCode, Object requestObject,
						   ISession session, Object receivedObject) throws Exception {
		if (!(receivedObject instanceof String)) {
			return receivedObject;
		}
		String str = (String) receivedObject;
		return adapter.stringToBean(str);
	}
}
