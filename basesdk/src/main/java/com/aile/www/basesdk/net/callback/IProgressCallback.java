/*
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.callback;

/**
 * 网络在下载数据过程中的回调接口，用于显示进度
 * 
 * @title IProgressCallback
 * @version 3.0
 */
public interface IProgressCallback {

    /**
     * 此方法不在UI线程回调
     * 
     * @param requestCode 发起请求时传入的code
     * @param currentNum 当前进度值
     * @param maxNum 总进度值
     * @description 网络获取数据过程中的回调接口
     */
    public void onProgress(int requestCode, Object requestObject, long currentNum, long maxNum);

}
