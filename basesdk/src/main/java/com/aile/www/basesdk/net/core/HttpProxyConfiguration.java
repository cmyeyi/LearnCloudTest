/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.aile.www.basesdk.net.core;

import android.content.Context;

import com.aile.www.basesdk.net.filter.ConnectionFilterChain;
import com.aile.www.basesdk.net.filter.ConnectionToStringFilter;
import com.aile.www.basesdk.net.thread.HttpAsyncGlobalHandler;

import java.util.HashMap;


/**
 * http连接相关的配置类
 * 
 * @title HttpProxy
 */
public class HttpProxyConfiguration {
    Context context;
    ConnectionFilterChain filterChain;
    HashMap<String, String> httpHeader;
    HttpAsyncGlobalHandler handler;

    private HttpProxyConfiguration(Builder builder) {
        context = builder.context;
        filterChain = builder.filterChain;
        httpHeader = builder.httpHeader;
        handler = builder.handler;
    }

    public static class Builder {
        private Context context;
        private ConnectionFilterChain filterChain;
        private HashMap<String, String> httpHeader;
        private HttpAsyncGlobalHandler handler;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public void setFilterChain(ConnectionFilterChain filterChain) {
            this.filterChain = filterChain;
        }

        public void setHttpHeader(HashMap<String, String> httpHeader) {
            this.httpHeader = httpHeader;
        }

        public void setHttpAsyncGlobalHandler(HttpAsyncGlobalHandler handler) {
            this.handler = handler;
        }

        public HttpProxyConfiguration build() {
            initEmptyFieldsWithDefaultValues();
            return new HttpProxyConfiguration(this);
        }

        private void initEmptyFieldsWithDefaultValues() {
            if (filterChain == null) {
                filterChain = new ConnectionFilterChain();
                filterChain.addFilter(new ConnectionToStringFilter("utf-8", ConnectionToStringFilter.ORI_STREAM));
            }
        }
    }
}
