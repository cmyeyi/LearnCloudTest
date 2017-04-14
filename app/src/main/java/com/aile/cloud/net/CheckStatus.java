package com.aile.cloud.net;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public final class CheckStatus {

    public static final String KEY_DATA = "data";

    private static final String KEY_RET = "ret";
    private static final String KEY_CODE = "code";
    private static final String KEY_RETURNINFO = "returnInfo";

    /**
     * 服务端返回状态,仅限于电影、演出
     * "ret": 0
     */
    private static final int RET_SUCCESS = 0;
    private static final int RET_DEFAULT = -1;
    private int ret = RET_DEFAULT;

    /**
     * 服务端返回状态,仅限于赛事
     * "code": 200
     */
    private static final int RET_MATCH_SUCCESS = 200;

    public void ret(int ret) {
        this.ret = ret;
    }

    public int ret(){
        return ret;
    }

    public static boolean IS_DEFAULT_SUCCESS(String json){
        return RET_SUCCESS == PARSER_RET(json);
    }

    /**
     * 解析ret,适用电影、演出
     *
     * @param json
     * @return
     */
    public static int PARSER_RET(String json) {
        try {
            if (!TextUtils.isEmpty(json) && !"null".equalsIgnoreCase(json)) {
                JSONObject object = new JSONObject(json);
                return object.getInt(KEY_RET);
            } else {
                return RET_DEFAULT;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return RET_DEFAULT;
        }
    }

    public static boolean IS_MATCHH_SUCCESS(String json){
        return RET_MATCH_SUCCESS == PARSER_CODE(json);
    }
    /**
     * 解析ret
     *
     * @param json
     * @return
     */
    public static int PARSER_CODE(String json) {
        try {
            if (!TextUtils.isEmpty(json) && !"null".equalsIgnoreCase(json)) {
                JSONObject object = new JSONObject(json);
                return object.getInt(KEY_CODE);
            } else {
                return RET_DEFAULT;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return RET_DEFAULT;
        }
    }

    public static boolean IS_SUCCESS(String json){
        try {
            if (!TextUtils.isEmpty(json) && !"null".equalsIgnoreCase(json)) {
                JSONObject object = new JSONObject(json);

                if(object.has(KEY_RET)){
                    return IS_DEFAULT_SUCCESS(json);
                }else if(object.has(KEY_CODE)){
                    return IS_MATCHH_SUCCESS(json);
                }else if(object.has(KEY_RETURNINFO)){
                    return IS_MATCHH_SUCCESS(object.getJSONObject(KEY_RETURNINFO).toString());
                }else {
                    return false;
                }

            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean RETURNINFO(JSONObject object){
        if(null != object){
            return IS_DEFAULT_SUCCESS(object.toString());
        }else {
            return false;
        }
    }
}
