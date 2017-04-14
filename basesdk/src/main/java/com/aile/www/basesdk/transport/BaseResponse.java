package com.aile.www.basesdk.transport;


import com.aile.www.basesdk.UnProguardable;

/**
 * <p>
 * 网络请求结果基类
 * 请求状态，返回状态
 */
public class BaseResponse implements UnProguardable {

    public static final int RESPONSE_STATUS_SUCCESS = 0;
    public static final int RESPONSE_STATUS_NET_UNABLED = -1;//没有网络
    public static final int RESPONSE_STATUS_TIMEOUT = -2; //超时异常
    public static final int RESPONSE_STATUS_NETWORK_SOCKETERROR = -3;//socket异常
    public static final int RESPONSE_STATUS_IOERROR = -4;//IO异常
    public static final int RESPONSE_STATUS_MALFORMEDURL = -5;//URL异常
    public static final int RESPONSE_STATUS_EXCEPTION = -6;//异常

    private int responseCode;
    private String content;
    private boolean isSuccess = false;

    public int responseCode() {
        return responseCode;
    }

    public void responseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void content(String content) {
        this.content = content;
    }

    public String content() {
        return content;
    }

    /**
     * 网络请求
     * @return
     */
    public boolean isSuccess(){
        return isSuccess;
    }

    public void isSuccess(boolean isSuccess){
        this.isSuccess = isSuccess;
    }

    /**
     * 判断请求状态
     * @return
     */
    public boolean isHttpSuccess(){
        return RESPONSE_STATUS_SUCCESS == responseCode;
    }

    /**
     * 网络不可用
     *
     * @return
     */
    public static BaseResponse RESPONSE_STATUS_NET_UNABLED() {
        return CREATE(RESPONSE_STATUS_NET_UNABLED);
    }

    /**
     * url为空
     *
     * @return
     */
    public static BaseResponse RESPONSE_STATUS_MALFORMEDURL() {
        return CREATE(RESPONSE_STATUS_MALFORMEDURL);
    }

    /**
     * 异常信息
     *
     * @return
     */
    public static BaseResponse RESPONSE_STATUS_EXCEPTION() {
        return CREATE(RESPONSE_STATUS_EXCEPTION);
    }

    /**
     * 请求超时
     *
     * @return
     */
    public static BaseResponse RESPONSE_STATUS_TIMEOUT() {
        return CREATE(RESPONSE_STATUS_TIMEOUT);
    }

    /**
     * IO异常
     *
     * @return
     */
    public static BaseResponse RESPONSE_STATUS_IOERROR() {
        return CREATE(RESPONSE_STATUS_IOERROR);
    }

    /**
     * @param responseCode
     * @return
     */
    public static BaseResponse CREATE(int responseCode) {
        BaseResponse response = new BaseResponse();
        response.responseCode(responseCode);
        return response;
    }

    /**
     * @return
     */
    public static BaseResponse CREATE() {
        BaseResponse response = new BaseResponse();
        return response;
    }
}
