package com.aile.cloud.net.been;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONObject;

/**
 * Created by yeyi on 17/2/23.
 */

public class SearchItem extends BaseBean<SearchItem>{
    private String countCheck;
    private String countSale;
    private String itemName;
    private String showId;
    private String showName;
    private String showStartTime;
    private String venueName;

    public void setCountCheck(String countCheck) {
        this.countCheck = countCheck;
    }

    public String getCountCheck() {
        return countCheck;
    }

    public void setCountSale(String countSale) {
        this.countSale = countSale;
    }

    public String getCountSale() {
        return countSale;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowStartTime(String showStartTime) {
        this.showStartTime = showStartTime;
    }

    public String getShowStartTime() {
        return showStartTime;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueName() {
        return venueName;
    }

    @Override
    public SearchItem parseJSON(JSONObject jsonObj) {
        return null;
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }

    @Override
    public SearchItem cursorToBean(Cursor cursor) {
        return null;
    }

    @Override
    public ContentValues beanToValues() {
        return null;
    }
}
