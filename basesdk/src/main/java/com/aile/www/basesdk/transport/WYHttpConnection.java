package com.aile.www.basesdk.transport;

import com.aile.www.basesdk.net.connection.AndroidHttpClient;
import com.aile.www.basesdk.net.parameter.HttpConnectionParameter;
import com.aile.www.basesdk.utils.ResponseToString;
import com.aile.www.basesdk.utils.UriBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class WYHttpConnection {

    private AndroidHttpClient client;

    public HttpConnectionParameter parameter() {
        return new HttpConnectionParameter();
    }

    private HttpResponse connectInternal(HttpConnectionParameter httpParameter)
            throws IOException {
        final HttpUriRequest request = getHttpUriRequest(httpParameter);
        // 添加http头域
        if (httpParameter.headParameter != null) {
            final Iterator<Map.Entry<String, String>> iterator = httpParameter.headParameter
                    .entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<String, String> entry = iterator.next();
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return client.execute(request);
    }

    /**
     * @param parameter
     * @return HttpUriRequest
     * @throws java.io.IOException
     * @description 根据输入的参数返回合适的http请求
     */
    private HttpUriRequest getHttpUriRequest(HttpConnectionParameter parameter) throws IOException {

        if (HttpConnectionParameter.METHOD_GET.equalsIgnoreCase(parameter.method)) {

            HttpGet get = new HttpGet(UriBuilder.build(parameter.uri, parameter.parameters));
            return get;

        } else if (HttpConnectionParameter.METHOD_POST.equalsIgnoreCase(parameter.method)) {

            HttpPost post = new HttpPost(parameter.uri);
            // 添加http传输数据
            if (parameter.entity != null) {
                post.setEntity(parameter.entity);
            }
            return post;

        } else if (HttpConnectionParameter.METHOD_PUT.equalsIgnoreCase(parameter.method)) {

            HttpPut put = new HttpPut(parameter.uri);
            // 添加http传输数据
            if (parameter.entity != null) {
                put.setEntity(parameter.entity);
            }
            return put;

        } else if (HttpConnectionParameter.METHOD_DELETE.equalsIgnoreCase(parameter.method)) {

            HttpDelete delete = new HttpDelete(UriBuilder.build(parameter.uri, parameter.parameters));
            return delete;

        }

        return new HttpGet(parameter.uri);
    }

    public BaseResponse connect(HttpConnectionParameter params) {

        if (!params.isValidParameter()) {
            throw new IllegalArgumentException();
        }

        BaseResponse response = null;
        // 获取http连接实例
        if (client == null) {
            client = AndroidHttpClient.newInstance("");
        }

        try {

            HttpResponse httpResponse = connectInternal(params);

            int httpstatus = httpResponse.getStatusLine().getStatusCode();
            response = BaseResponse.CREATE();

            if (HttpStatus.SC_OK == httpstatus || HttpStatus.SC_PARTIAL_CONTENT == httpstatus) {
                long size = null != httpResponse.getEntity() ? httpResponse.getEntity().getContentLength() : 0;
                String responseContent = ResponseToString.streamToString(httpResponse.getEntity().getContent(), "utf-8", size);
                response.content(responseContent);
                response.responseCode(BaseResponse.RESPONSE_STATUS_SUCCESS);
            } else {
                response.responseCode(BaseResponse.RESPONSE_STATUS_NETWORK_SOCKETERROR);
            }

        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            // 建立连接失败
            response = BaseResponse.RESPONSE_STATUS_TIMEOUT();
        } catch (IOException e) {
            // IO失败
            e.printStackTrace();
            response = BaseResponse.RESPONSE_STATUS_IOERROR();
        }

        return response;
    }

    public void close() {
        if (client != null) {
            client.close();
            client = null;
        }
    }
}
