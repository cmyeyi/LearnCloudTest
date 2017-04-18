package com.aile.cloud.net.bean;

import android.text.TextUtils;

import com.aile.www.basesdk.UnProguardable;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeyi on 17/4/18.
 *
 * {
 * "result": {
 * "msg": "ok",
 * "code": 200
 * },
 * "body": {
 * "total": null,
 * "rows": {
 * "icon_url": null,
 * "inPrice": null,
 * "description_url": "http://img.emotao.com/20170331/0oaipebbg4imdrsk9g8mp3nnuu.jpg",
 * "sellTotal": 1322,
 * "description": "",
 * "updateTime": null,
 * "isDisplay": null,
 * "outPrice": 10,
 * "total": 36600,
 * "createTime": null,
 * "user_id": null,
 * "goodsTotal": null,
 * "name": "宝马3系 320i 运动型 2017款 颜色随机",
 * "rank": null,
 * "model": 2,
 * "Id": 34,
 * "beginTime": "2017-03-31 18:39:01.0",
 * "imageList": [
 * {"imgUrl": "http://img.emotao.com/20170331/ugcs2ab1vqhakoagi5rrqr402o.jpg", "productId": 34, "createTime": 2017, "Id": 21, "type": 2 }
 * ],
 * "categoryId": 11,
 * "batchNumber": "170331184459"
 * }
 * }
 * }
 */

public class ProductDetail {
    public ProductDetailResult result;
    public Body body;

    public static class ProductDetailResult implements Serializable, UnProguardable {
        public String msg;
        public String code;
    }

    public static class Body {
        public int total;
        public PDetail rows;

        public void setRows(PDetail rows) {
            this.rows = rows;
        }

        public PDetail getRows() {
            return rows;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    /**
     * "rows": {
     * "icon_url": null,
     * "inPrice": null,
     * "description_url": "http://img.emotao.com/20170331/0oaipebbg4imdrsk9g8mp3nnuu.jpg",
     * "sellTotal": 1322,
     * "description": "",
     * "updateTime": null,
     * "isDisplay": null,
     * "outPrice": 10,
     * "total": 36600,
     * "createTime": null,
     * "user_id": null,
     * "goodsTotal": null,
     * "name": "宝马3系 320i 运动型 2017款 颜色随机",
     * "rank": null,
     * "model": 2,
     * "Id": 34,
     * "beginTime": "2017-03-31 18:39:01.0",
     * "imageList": [
     * {"imgUrl": "http://img.emotao.com/20170331/ugcs2ab1vqhakoagi5rrqr402o.jpg", "productId": 34, "createTime": 2017, "Id": 21, "type": 2 }
     * ],
     * "categoryId": 11,
     * "batchNumber": "170331184459"
     * }
     */
    public static class PDetail implements Serializable, UnProguardable {
        public String icon_url;
        public String inPrice;
        public String description_url;
        public int sellTotal;
        public String description;
        public String updateTime;
        public String isDisplay;
        public int outPrice;
        public int total;
        public String createTime;
        public String user_id;
        public String goodsTotal;
        public String name;
        public String rank;
        public int model;
        public String Id;
        public String beginTime;
        public String categoryId;
        public String batchNumber;
        private List<PImage> imageList;

        public List<PImage> getImageList() {
            Gson gson = new Gson();
            String json = gson.toJson(imageList);
            return fromJsonList(json, PImage.class);
        }

        private <T extends PImage> ArrayList fromJsonList(String json, Class<T> cls) {
            if (json == null || TextUtils.isEmpty(json)) {
                return null;
            }
            ArrayList mList = null;
            try {
                mList = new ArrayList();
                Gson gson = new Gson();
                JsonArray array = new JsonParser().parse(json).getAsJsonArray();
                for (final JsonElement elem : array) {
                    PImage banner = gson.fromJson(elem, cls);
                    mList.add(banner);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mList;
        }
    }

    /**
     * {"imgUrl": "http://img.emotao.com/20170331/ugcs2ab1vqhakoagi5rrqr402o.jpg", "productId": 34, "createTime": 2017, "Id": 21, "type": 2 }
     */
    public static class PImage {
        public String imgUrl;
        public int productId;
        public String createTime;
        public int Id;
        public int type;
    }


}
