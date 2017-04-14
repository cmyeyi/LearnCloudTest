/*
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.connection;

import com.aile.www.basesdk.net.callback.ConnectionResponse;
import com.aile.www.basesdk.net.callback.IConnectionCallback;
import com.aile.www.basesdk.net.filter.ConnectionFilterChain;
import com.aile.www.basesdk.net.parameter.IConnectionParameter;

import java.io.IOException;

/**
 * @ClassName: IURLConnection
 * @Description: 网络连接接口
 */
public interface IURLConnection {

    /**
     * @return
     * @description 获取网络连接所需要的参数
     */
    public IConnectionParameter getParameter();

    /**
     * @param requestCode
     * @param requestObject
     * @param parameter
     * @param filterChain
     * @param callback
     * @return 如果是同步获取到网络数据，则会返回ConnectionResponse。否则返回null
     * @throws IllegalArgumentException
     * @throws java.io.IOException
     * @description 发起网络连接，如果是同步获取到网络数据，则会返回ConnectionResponse，如果是异步，则通过IInputCallback回调
     */
    public ConnectionResponse connect(int requestCode, Object requestObject,
                                      IConnectionParameter parameter, ConnectionFilterChain filterChain,
                                      IConnectionCallback callback) throws IllegalArgumentException,
            IOException;

    /**
     * @description 关闭该连接
     */
    public void close();
}
