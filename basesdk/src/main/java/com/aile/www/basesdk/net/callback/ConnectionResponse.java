/*
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.callback;

/**
 *
 * @title ConnectionResponse
 * @description 用于封装网络连接的返回值
 */
public class ConnectionResponse {

    public static final int RESPONSE_STATUS_TIMEOUT = -1;
    public static final int RESPONSE_STATUS_NETWORK_SOCKETERROR = -2;
    public static final int RESPONSE_STATUS_IOERROR = -3;
    public static final int RESPONSE_STATUS_MALFORMEDURL = -4;
    public static final int RESPONSE_STATUS_EXCEPTION = -5;
    public static final int RESPONSE_STATUS_INVALIDFORMAT = -6;
    /**
     * 没有可用的网络
     */
    public static final int RESPONSE_STATUS_NET_UNABLED = -7;

    public static final int RESPONSE_STATUS_OK = 0;

    private int requestCode;
    private int responseCode;
    private Object requestObject;
    private Object receivedObject;

    public ConnectionResponse(int requestCode, Object requestObj, int responseCode, Object receivedObj) {
        this.requestCode = requestCode;
        this.responseCode = responseCode;
        this.requestObject = requestObj;
        this.receivedObject = receivedObj;
    }

    /**
     *
     * @return 发起请求时传入的code
     * @description
     */
    public int getRequestCode() {
        return requestCode;
    }

    /**
     *
     * @return 网络连接的状态码
     * @description
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     *
     * @return 发起请求时传入的Object
     * @description
     */
    public Object getRequestObject() {
        return requestObject;
    }

    /**
     *
     * @return 网络数据
     * @description
     */
    public Object getRecevicedObject() {
        return receivedObject;
    }

    //todo 添加服务端字符串返回数据记录
    private String responseString;

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }
}
