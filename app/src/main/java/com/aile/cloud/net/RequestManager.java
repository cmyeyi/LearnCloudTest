package com.aile.cloud.net;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestManager {

    private static RequestManager instance;
    private ExecutorService executors;

    private RequestManager() {
        this.executors = executors == null ? Executors.newCachedThreadPool() : executors;
    }

    public static RequestManager getInstance() {
        if (instance == null) {
            synchronized (RequestManager.class) {
                if (instance == null) {
                    instance = new RequestManager();
                }
            }
        }
        return instance;
    }

    /**
     *  todo 后续需要完善接口和实现
     * @param request   请求任务
     */
    public void requestAsync(AppRequest request){
        executors.submit(request);
    }
}
