/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.aile.www.basesdk.net.thread;

import android.os.AsyncTask;

import com.aile.www.basesdk.net.callback.IProgressCallback;
import com.aile.www.basesdk.net.core.HttpProxy;

import java.io.File;

/**
 * 
 * @ClassName: DownloadFileAsyncTask
 * @Description: 下载文件的异步task实现类
 *
 */
public class DownloadFileAsyncTask extends AsyncTask<Void, Void, Boolean>
		implements IProgressCallback {
	private String url;
	private String filePath;
	private boolean resume;
	private HttpAsyncListener listener;
	private IProgressCallback progressCallback;

	private int requestCode;
	private Object requestObject;
	private long currentNum;
	private long maxNum;

	public DownloadFileAsyncTask(String url, String filePath, boolean resume,
								 HttpAsyncListener listener) {
		this(0, null, url, filePath, resume, listener);
	}

	public DownloadFileAsyncTask(int requestCode, Object requestObject,
								 String url, String filePath, boolean resume,
								 HttpAsyncListener listener) {
		this.filePath = filePath;
		this.url = url;
		this.resume = resume;
		this.listener = listener;
		this.requestCode = requestCode;
		this.requestObject = requestObject;
	}

	public void setProgressCallback(IProgressCallback callback) {
		progressCallback = callback;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		return HttpProxy.getInstace().httpGetFile(url, filePath, this, resume);
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if (listener == null) {
			return;
		}

		if (!success) {
			listener.onException(0, null, 0);
			return;
		}

		listener.onSuccess(0, null, new File(filePath));
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		if (progressCallback != null) {
			progressCallback.onProgress(requestCode, requestObject, currentNum,
					maxNum);
		}
	}

	@Override
	public void onProgress(int requestCode, Object requestObject,
			long currentNum, long maxNum) {
		this.currentNum = currentNum;
		this.maxNum = maxNum;
	}
}
