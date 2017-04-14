package com.aile.www.basesdk.transport;

import com.aile.www.basesdk.SDKConfig;
import com.aile.www.basesdk.net.connection.ConnectionFactory;
import com.aile.www.basesdk.net.parameter.HttpConnectionParameter;
import com.aile.www.basesdk.utils.NetWorkUtils;
import com.aile.www.basesdk.utils.SDKLogger;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 为上层提供常用的网络连接代理方法
 */

public class HTTPP {

    private static HTTPP instance;

    private HTTPP() {

    }

    public static HTTPP getInstance() {
        if (instance == null) {
            synchronized (HTTPP.class) {
                if (instance == null) {
                    instance = new HTTPP();
                }
            }
        }
        return instance;
    }

    /**
     * 使用指定的配置进行http访问请求
     *
     * @param param 请求附带的变量
     * @return http的请求结果
     */

    public BaseResponse httpRequest(BaseParam param) {

        StringBuilder sb = null;
        if (SDKLogger.DEBUG()) {
            sb = new StringBuilder();
            log(sb, "**************** HTTPP httpRequest ****************");
        }

        // 超时（网络）异常
        if (!NetWorkUtils.isNetworkAvailable(SDKConfig.context())) {

            if (SDKLogger.DEBUG()) {
                log(sb, "network not Available", "**************** HTTPP httpRequest end ****************");
                print(sb);
            }

            return BaseResponse.RESPONSE_STATUS_NET_UNABLED();
        }

        if (null == param) {

            if (SDKLogger.DEBUG()) {
                log(sb, "BaseParam null", "**************** HTTPP httpRequest end ****************");
                print(sb);
            }

            return BaseResponse.RESPONSE_STATUS_MALFORMEDURL();
        }

        WYHttpConnection httpConnection = ConnectionFactory.CREATE_HTTPCONNECTION();
        HttpConnectionParameter params = httpConnection.parameter();
        try {
            // 设置url
            params.uri = param.url();
            if (HttpType.isGET(param.httpType())) {

                params.method = HttpConnectionParameter.METHOD_GET;
                params.parameters = param.params();

            } else if (HttpType.isPOST(param.httpType())) {

                params.method = HttpConnectionParameter.METHOD_POST;
                // http方法
                byte[] data = param.httpContent();
                if (null != data) {
                    params.setData(data, param.isGzip());
                }
                if (null != param.params() && !param.params().isEmpty()) {
                    params.setData(param.params());
                }

            } else if (HttpType.isPUT(param.httpType())) {

                params.method = HttpConnectionParameter.METHOD_PUT;
                // http方法
                byte[] data = param.httpContent();
                if (null != data) {
                    params.setData(data, param.isGzip());
                }
                if (null != param.params() && !param.params().isEmpty()) {
                    params.setData(param.params());
                }

            } else if (HttpType.isDELETE(param.httpType())) {

                params.method = HttpConnectionParameter.METHOD_DELETE;
                params.parameters = param.params();

            }

            //添加http请求头
            if (null != param.headers() && !param.headers().isEmpty()) {
                params.headParameter = param.headers();
            }

            if (SDKLogger.DEBUG()) {
                log_params(sb, params);
            }

            BaseResponse response = httpConnection.connect(params);
            if (response == null) {

                if (SDKLogger.DEBUG()) {
                    log(sb, "response == null", "**************** HTTPP httpRequest end ****************");
                    print(sb);
                }

                return BaseResponse.RESPONSE_STATUS_EXCEPTION();
            }

            if (SDKLogger.DEBUG()) {
                log_respones(sb, response);
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();

            if (SDKLogger.DEBUG()) {
                log(sb, String.format("Exception : %s", e.getMessage()), "**************** HTTPP httpRequest end ****************");
                print(sb);

                throw new NullPointerException(e.getMessage());
            }

            return BaseResponse.RESPONSE_STATUS_EXCEPTION();
        } finally {

            print(sb);

            // release the connection
            httpConnection.close();
        }
    }

    private void print(StringBuilder sb) {
        if (null != sb) {
            SDKLogger.http(sb.toString());
        }
    }

    private void log(StringBuilder sb, String... msgs) {
        if (null != sb && null != msgs && msgs.length > 0) {
            for (String msg : msgs) {
                sb.append("\n");
                sb.append(msg);
            }
        }
    }

    private void log_params(StringBuilder sb, HttpConnectionParameter params) {
        if (null != params) {
            log(sb, String.format("method : %s", params.method));
            log(sb, String.format("url : %s", params.uri));
            log_map(sb, params.headParameter, true);
            log_map(sb, params.parameters, false);
        }
    }

    private void log_map(StringBuilder sb, Map<String, String> map, boolean isHeader) {
        if (null != map && !map.isEmpty()) {

            if (isHeader) {
                log(sb, "header");
            } else {
                log(sb, "params");
            }

            Set<String> keys = map.keySet();
            for (String key : keys) {
                log(sb, String.format("%s : %s", key, map.get(key)));
            }

            log(sb, "\n");
        }
    }

    private void log_respones(StringBuilder sb, BaseResponse response) {
        log(sb, String.format("content : %s", null != response ? response.content() : "null"),
                String.format("Http Status Code %s", null != response ? (String.valueOf(response.responseCode())) : "null"),
                "**************** HTTPP httpRequest end ****************");
    }
}
