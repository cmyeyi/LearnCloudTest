/*
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.filter;

import com.aile.www.basesdk.net.callback.IProgressCallback;
import com.aile.www.basesdk.net.session.HttpSession;
import com.aile.www.basesdk.net.session.ISession;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 
 * @ClassName: ConnectionDownloadFileFilter
 * @Description: 文件下载过滤器，将文件保存在本地
 *
 */
public class ConnectionDownloadFileFilter implements IConnectionFilter {

	private String filePath;
	private IProgressCallback progressCallback;
	private boolean isResume;

	public ConnectionDownloadFileFilter(String filePath,
			IProgressCallback progressCallback, boolean isResume) {
		this.filePath = filePath;
		this.progressCallback = progressCallback;
		this.isResume = isResume;
	}

	@Override
	public Object doFilter(int requestCode, Object requestObject,
						   ISession session, Object receivedObject) throws Exception {
		if (!(receivedObject instanceof InputStream)) {
			return receivedObject;
		}
		final InputStream in = (InputStream) receivedObject;

		File fileOut = new File(filePath);
		if (!fileOut.exists()) {
			fileOut.getParentFile().mkdirs();
		}
		long downloadCount = 0;
		if (isResume) {
			downloadCount = fileOut.length();
		}

		long contentLength = downloadCount;
		if (session instanceof HttpSession) {
			contentLength += ((HttpSession) session).contentLength;
		}

		if (progressCallback != null) {
			progressCallback.onProgress(requestCode, requestObject,
					downloadCount, contentLength);
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileOut, isResume);
			byte[] bytes = new byte[4096];
			int c;
			while ((c = in.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				downloadCount = downloadCount + c;
				if (progressCallback != null) {
					progressCallback.onProgress(requestCode, requestObject,
							downloadCount, contentLength);
				}
			}
			return fileOut;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
