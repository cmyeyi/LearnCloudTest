package com.aile.cloud.ad;

import java.util.List;

public interface IADBanner {

    /**
     * imgUrl: "http://img.emotao.com/20170316/58un1idrjmit8qtmj9207gf745.png
     * description: "充值送礼"
     * "rank: 0,
     * usrId: 0
     * id: 2
     * bigImgUrl: "http://img.emotao.com/20170316/58un1idrjmit8qtmj9207gf745.png
     * samllImgUrl: "http://img.emotao.com/20170316/58un1idrjmit8qtmj9207gf745.png
     * url: "http://img.emotao.com/20170320/5bksl6qfrei57om7vnlf28mmhl.png
     * status: 1
     */

    public String getImgUrl();

    public String getDescription();

    public String getRank();

    public String getUsrId();

    public String getId();

    public String getBigImgUrl();

    public String getSamllImgUrl();

    public String getUrl();

    public int getStatus();
}
