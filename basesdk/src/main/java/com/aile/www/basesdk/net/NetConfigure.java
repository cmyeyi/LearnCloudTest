package com.aile.www.basesdk.net;

/**
 * Created by RP on 2015/11/20.
 */
public class NetConfigure {

    private static NetConfigure instance = new NetConfigure();

    private boolean httpsDebug = false;

    private NetConfigure(){}

    public static NetConfigure getInstance(){
        return instance;
    }

    public void setHttpsDebugMode(boolean debug){
        httpsDebug = debug;
    }

    public boolean isHttpsDebugMode(){
        return httpsDebug;
    }
}
