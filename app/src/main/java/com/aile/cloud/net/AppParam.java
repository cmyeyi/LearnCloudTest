package com.aile.cloud.net;

import android.text.TextUtils;

import com.aile.cloud.AppApplication;
import com.aile.cloud.net.utils.ChannelUtils;
import com.aile.cloud.net.utils.DeviceIdTools;
import com.aile.www.basesdk.UnProguardable;
import com.aile.www.basesdk.transport.BaseParam;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * param基类
 * 1.基础请求参数封装
 * 2.公共接口处理
 */

public abstract class AppParam extends BaseParam implements UnProguardable {

    //http 请求头
    protected static Map<String, String> STATIC_HEADER = new HashMap<String, String>();

    /**
     * 公共参数
     */
    protected static Map<String, String> STATIC_PARAMS = new HashMap<String, String>();

    /**
     * 请求参数
     */
    protected Map<String, String> params = new HashMap<String, String>();

    public static final String FROM_DEFAULT = ChannelUtils.getChannelId(AppApplication.getInstance());
    public static final String FROM_MEDIAL_LIB = "7000020002"; //媒体库接口应使用的from值

    public static String FROM_CURRENT = FROM_DEFAULT;//为H5参数追踪而定义


    // 默认请求参数
    protected static final String PARAMS_V = "v";
    protected static final String PARAMS_FROM = "from";
    protected static final String PARAMS_APPKEY = "appkey";
    protected static final String PARAMS_CHANNELID = "channelId"; // 评论中心需要这个参数
    protected static final String PARAMS_APPVER = "appver";
    protected static final String PARAMS_DEVICEID = "deviceid";
    protected static final String PARAMS_IMEI = "imei";
    protected static final String PARAMS_T = "t";
    protected static final String PARAMS_APPCHANNELID = "appChannelId";

    protected static final String PARAMS_UID = "uid";
    protected static final String PARAMS_USERID = "userId";
    protected static final String PARAMS_OPENID = "openId";
    protected static final String PARAMS_UNIONID = "unionId";
    protected static final String PARAMS_TOKEN = "token";

    public AppParam() {
        if (STATIC_PARAMS.isEmpty()) {
            // 添加默认的参数
            addParams(STATIC_PARAMS, PARAMS_FROM, FROM_CURRENT);

            addParams(STATIC_PARAMS, PARAMS_APPVER, DeviceIdTools.getAppVersionName());

            addParams(STATIC_PARAMS, PARAMS_DEVICEID, DeviceIdTools.getMyDeviceId(AppApplication.getInstance()));
            addParams(STATIC_PARAMS, PARAMS_IMEI, DeviceIdTools.getDeviceIMEI());

            addParams(STATIC_PARAMS, PARAMS_APPCHANNELID, ChannelUtils.getChannelId(AppApplication.getInstance()));
        }

//        addUserInfo();

        //时间
        addParams(params, PARAMS_T, String.valueOf(System.currentTimeMillis() / 1000L));

        params.putAll(STATIC_PARAMS);
    }

//    /**、

    @Override
    public Map<String, String> params() {
        // 将所有参数进行sign签名

        if(TextUtils.isEmpty(params.get(AppSignUtils.DEFAULT_SIGN_NAME))){
            AppSignUtils.SIGN(params, AppSignUtils.DEFAULT_SIGN_NAME, getSignType());
        }
        return params;
    }

    @Override
    public Map<String, String> headers() {
        return null;
    }

    /**
     * 请求参数添加
     *
     * @param params
     * @param key
     * @param value
     * @return
     */
    public static Map<String, String> addParams(Map<String, String> params, String key, String value) {

        if (null != params && !TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            params.put(key, value);
        }

        return params;
    }

    /**
     * 移除参数
     *
     * @param params
     * @param key
     * @return
     */
    public static Map<String, String> removeParams(Map<String, String> params, String key) {

        if (null != params) {
            params.remove(key);
        }

        return params;
    }

    public int getSignType(){
        return AppSignUtils.DEFAULT_TYPE;
    }
}
