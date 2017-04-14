/*
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.session;

import org.apache.http.HttpResponse;

public class HttpSession implements ISession {

    public String url;
    public long contentLength;

    public HttpSession(String url, HttpResponse response) {
        this.url = url;
        contentLength = response.getEntity().getContentLength();
    }
}
