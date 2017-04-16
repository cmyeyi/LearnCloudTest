package com.aile.cloud.net.bean;

import com.aile.cloud.ad.IADBanner;
import com.aile.www.basesdk.UnProguardable;

import java.io.Serializable;

/**
 * Created by yeyi on 17/4/16.
 */

public class ADBanner implements IADBanner, Serializable, UnProguardable {
    private String imgUrl;
    private String description;
    private String rank;
    private String usrId;
    private String id;
    private String bigImgUrl;
    private String samllImgUrl;
    private String url;
    private int status;

    @Override
    public String getImgUrl() {
        return this.imgUrl;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getRank() {
        return this.rank;
    }

    @Override
    public String getUsrId() {
        return this.usrId;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getBigImgUrl() {
        return this.bigImgUrl;
    }

    @Override
    public String getSamllImgUrl() {
        return this.samllImgUrl;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public int getStatus() {
        return this.status;
    }
}
