package com.aile.cloud.net.been;

import java.io.Serializable;

/**
 * Created by niechao on 2017/2/22.
 */

public class BaseInfo implements Serializable {
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return 200 == code;
    }
}
