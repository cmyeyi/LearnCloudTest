package com.aile.cloud.net;

import android.os.Build;
import android.text.TextUtils;

import java.io.File;
import java.net.URLEncoder;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 签名加密工具类
 */

public class AppSignUtils {

    public static final String DEFAULT_SIGN_NAME = "sign";
    public static final int DEFAULT_TYPE = 1;
    public static final int NEW_SIGN_TYPE = 7;

    public static final int TYPE_WEISAI = 6;

    public static final String WEISAI_SIGN_NAME = "s";

    /**
     * 按照字典顺序加密
     * @param params    参数
     * @param signName  sign
     * @param signType  加密方式
     * @return
     */
    public static Map SIGN(Map params, String signName, int signType){
        if (params != null) {
            String sign = getSignStr(params,signName,signType);
            params.put(signName, sign);
        }
        return params;
    }

    /**
     * 仅获取sign的结果
     */
    private static String getSignStr(Map params, String signName, int signType) {
        String sortedParamsStr = getSortedParamsStr(params,signName);
        return signStr(sortedParamsStr,signType);
    }

    private static String getSortedParamsStr(Map params, String signName) {
        String sortedParamsStr = null;
        try {
            List<String> keylist = new ArrayList<>();
            Set<String> keyset = params.keySet();
            Iterator<String> iterator = keyset.iterator();
            while (iterator.hasNext()) {
                keylist.add(iterator.next());
            }
            // 排序
            Collections.sort(keylist);

            StringBuilder sb = new StringBuilder();

            for (String key : keylist) {
                if (signName.equals(key)) {
                    continue;
                }
                if (params.get(key) instanceof File) {
                } else {
                    sb.append("&").append(key).append("=").append(params.get(key));
                }

            }
            sortedParamsStr = sb.substring(1);
        } catch (Exception e) {

        }
        return sortedParamsStr;
    }

    private static String signStr(String sortedParamsStr, int signType) {
        return null;
    }

    public static Map<String,String> SIGN_WEISAI(Map<String, String> params, String t) {

        String sign = signStr(getSortedParams(params,WEISAI_SIGN_NAME) + t,TYPE_WEISAI);
        params.put(WEISAI_SIGN_NAME, sign);

        return params;
    }

    /***
     * 参数安装规则拼接
     * @param params 传递的请求参数
     * @return
     */
    private static String getSortedParams(Map params, String signName) {
        String sortedParamsStr = "";
        try {
            List<String> keylist = new ArrayList<>();
            Set<String> keyset = params.keySet();
            Iterator<String> iterator = keyset.iterator();
            while (iterator.hasNext()) {
                keylist.add(iterator.next());
            }
            // 排序
            Collections.sort(keylist, new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    return Collator.getInstance(Locale.ENGLISH).compare(getPrintableString(lhs),getPrintableString(rhs));
                }
            });

            StringBuilder sb = new StringBuilder();
            for (String key : keylist) {
                if (signName.equals(key)) {
                    continue;
                }
                Object param = params.get(key);
                if ((param instanceof File)
                        || (param instanceof List)
                        || (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && (param instanceof Objects[]))) {
                } else if (param != null) {
                    sb.append("&").append(encodeValue(key)).append("=").append(encodeValue(params.get(key).toString()));
                }
            }

            if (sb.length() > 0)
                sortedParamsStr = sb.substring(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sortedParamsStr;
    }

    /***
     * key,value特殊字符处理
     * @param value
     * @return
     */
    private static String encodeValue(String value) {
        String result = "";

        try {
            result = URLEncoder.encode(value, "UTF-8");
            result = result.replaceAll("\\+", "%20");
            result = result.replaceAll("\\*", "%2A");
            result = result.replaceAll("%7E", "~");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String getPrintableString(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        char[] nameArray = string.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char x : nameArray) {
            if (TextUtils.isGraphic(x)) {
                builder.append(x);
            }
        }
        return builder.toString();
    }
}
