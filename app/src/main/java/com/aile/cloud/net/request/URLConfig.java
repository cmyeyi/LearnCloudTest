package com.aile.cloud.net.request;

public class URLConfig {
    public final static String BASE_HOST = "https://emotao.com/miaotao/api/";
    //首页广告位
    public final static String HOME_BANNER = BASE_HOST + "banner/list.d";
    //首页公告
    public final static String HOME_NOTICE = BASE_HOST + "notice/getNotice.d";
    //首页产品列表
    public final static String HOME_PRODUCT_LIST = BASE_HOST + "product/getHomeProducts/1.d";
    //产品详情
    public final static String HOME_PRODUCT_DETAIL = BASE_HOST + "product/getProductDetail.d?productId=";
    //奖品揭晓列表
    public final static String AWARD_ANNOUNCE_LIST = BASE_HOST + "awardlog/announceList.d?page=1&pageSize=10";
    //大转盘
    public final static String GAME_RECORD = BASE_HOST + "game/getDzpDate/c54b7fbedd5e46b49f680040319e17e4.d";
    //大转盘个人记录
    public final static String GAME_RECORD_PERSONAL = BASE_HOST + "game/listDzpReocord.d?token=XXX&page=1&pageSize=1";
    //中奖纪录列表
    public final static String AWARD_RECORD = BASE_HOST + "awardlog/list.d?batch_number=20161221-0006";
    //大转盘最新中奖纪录
    public final static String GAME_LATEST_AWARD = BASE_HOST + "game/listDzpLastReocord.d";
    //揭晓详情
    public final static String AWARD_LOG = BASE_HOST + "awardlog/get.d?id=1&product_id=1&member_id=10002";
    //登出
    public final static String USER_LOGOUT = BASE_HOST + "user/logout.d?token=";
    //登录
    public final static String USER_LOGIN = BASE_HOST + "user/login.d?phone=";
    //保存收货地址
    public final static String SAVE_ADDRESS = BASE_HOST + "address/add.d?token=xxx&phone=xxx&city=xxx&area=xxx&address=xxx";
    //获取用户设置的地址信息
    public final static String USER_ADDRESS = BASE_HOST + "address/get.d?token=";
    //获取用户信息
    public final static String USER_INFOR = BASE_HOST + "user/get.d?token=xxx";
    //获取用户信息
    public final static String USER_INFOMATION = BASE_HOST + "member/get.d?memberId=2";
    //获取省市区
    public final static String USER_LOCATION = BASE_HOST + "location/list.d?parent_id=0";
    //某期订单列表
    public final static String USER_ORDER_LIST = BASE_HOST + "order/listOrderRecord.d?batch_number=";
    //个人中心- 参与记录/秒拍记录
    public final static String ME_ORDER_RECORD = BASE_HOST + "bitorder/listByMemberId.d?token=xxx";
    //红包记录
    public final static String ME_BOUND_RECORD = BASE_HOST + "walletlog/listByMember.d?token=";
    //比特记录
    public final static String ME_BIT_RECORD = BASE_HOST + "bitlog/list.d?token=";
    //个人中心-进行中
    public final static String PERSONAL_CENTER_GOING = BASE_HOST + "order/listWaitingOpenOrder.d?token=";
    //个人中心- 已揭晓
    public final static String PERSONAL_CENTER_ORDER_OPENED = BASE_HOST + "order/listOpenedOrder.d?token=";
    //个人中心- 中奖记录
    public final static String ME_AWARD_RECODER = BASE_HOST + "awardlog/listUserA wardRecord.d?token=";
    //个人中心- 他人中奖记录
    public final static String PERSONAL_CENTER_OTHERS_AWARD_RECORD = BASE_HOST + "awardlog/listOhterUserA wardRecord.d?member_id=2&page=1& pageSize=1";
    //充值
    public final static String BIT_ORDER = BASE_HOST + "bitorder/saveBitOrder.d?productId=xxx&openId=xxx&payWayCode= WEIXIN&payTypeCode=XXX&price=XXX&token=XXX";
    //比特币支付
    public final static String BIT_PAY = BASE_HOST + "bitorder/payFromBit.d?productId=17&openId=1&qty=1&batchN umber=XXX&token=XXX";
    //充值产品列表
    public final static String BIT_PRODUCT = BASE_HOST + "bitproduct/list.d";
    //用户参与记录
    public final static String USER_PARTICIPATE_RECORD = BASE_HOST + "order/listUserOrder.d?token=fb9a2eedaea940f7b7e50202dc30dde 6&page=1&pageSize=1";
    //幸运号详情
    public final static String Lucky_number_detail = BASE_HOST + "awardlog/listProA wardNumber.d?token=XXX&productId=XXX";
    //他人个人中心
    public final static String OTHER_PERSONAL_CENTER = BASE_HOST + "order/listOtherUserOrderRecord.d?memberId=2&page=1&pageS ize=2";
}
