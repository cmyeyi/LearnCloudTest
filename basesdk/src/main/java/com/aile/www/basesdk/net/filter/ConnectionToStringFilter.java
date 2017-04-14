/*
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.filter;

import com.aile.www.basesdk.net.session.ISession;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 
 * @ClassName: ConnectionToStringFilter
 * @Description: 字符串转化过滤流
 *
 */
public class ConnectionToStringFilter implements IConnectionFilter {
	private static final String TAG = ConnectionToStringFilter.class.getSimpleName();

	public final static int ORI_STREAM = 0;
	public final static int ORI_ARRAY = 1;

	private String charset;
	private int ori;

	public ConnectionToStringFilter(String charset, int ori) {
		this.ori = ori % 2;
		this.charset = charset;
	}

	@Override
	public Object doFilter(int requestCode, Object requestObject, ISession session, Object receivedObject) throws Exception {
		InputStream inputStream = null;
		ByteArrayOutputStream baos = null;

		switch (ori) {
		case ORI_ARRAY:
			if (!(receivedObject instanceof byte[])) {
				return receivedObject;
			}
			byte[] byteArray = (byte[]) receivedObject;
			return new String(byteArray, charset);
		case ORI_STREAM:
			if (!(receivedObject instanceof InputStream)) {
				return receivedObject;
			}
			try {
				inputStream = (InputStream) receivedObject;
				baos = new ByteArrayOutputStream();
				byte[] buff = new byte[1024 * 10];
				int length = 0;
				while ((length = inputStream.read(buff)) >= 0) {
					baos.write(buff, 0, length);
				}
				baos.flush();
				String receivedString = new String(baos.toByteArray(), charset);
				return receivedString;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}

				if (baos != null) {
					try {
						baos.close();
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			}
		}

		return receivedObject;
	}

}
