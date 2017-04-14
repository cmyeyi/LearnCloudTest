/*
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.filter;


import com.aile.www.basesdk.net.session.ISession;

public interface IConnectionFilter {

    /**
     * 
     * @param requestCode
     * @param requestObject
     * @param session
     * @param receivedObject
     * @return
     * @throws Exception
     * @description 网络数据过滤器，将receivedObject转换为输出Object
     */
    public Object doFilter(int requestCode, Object requestObject, ISession session, Object receivedObject)
            throws Exception;
}
