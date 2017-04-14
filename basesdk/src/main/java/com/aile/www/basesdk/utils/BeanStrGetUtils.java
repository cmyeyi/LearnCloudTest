package com.aile.www.basesdk.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从网络拉取过来的原始字符串中提取出json数据
 * @author alecfeng, guotongyi
 */
public class BeanStrGetUtils {
    public static String getBeanJsonStr(String rawData) {
        if (TextUtils.isEmpty(rawData)) {
            return "";
        }
        
        if (!rawData.contains("MovieData.set")) {
            return "";
        }
        
        Pattern pattern = Pattern.compile("MovieData.set\\s*");
        String result[] = pattern.split(rawData);
        Matcher matcher = null;
        StringBuffer jsonStr = new StringBuffer();
        //jsonStr.append("{");
        for(int i=1; i<result.length; i++) {
            pattern = Pattern.compile("'.*'");
            matcher = pattern.matcher(result[i]);
            if (matcher.find()) {
                jsonStr.append(matcher.group().replace("'", "\""));
                jsonStr.append(":");
            }
            
            pattern = Pattern.compile("\\{.*\\}");
            matcher = pattern.matcher(result[i]);
            if (matcher.find()) {
                jsonStr.append(matcher.group());
            }
            
            if(i < result.length-1) {
                jsonStr.append(",");
            }
        }
        //jsonStr.append("}");
        
        return jsonStr.toString();
    }
    
    /**
     * 解析普通json
     * @param rawData
     * @return json字符串
     */
    public static String getJsonString(String rawData) {
        return getJsonString(rawData, 0);
    }
    
    public static String getCgiJson(String rawData) {
        if (TextUtils.isEmpty(rawData)) {
            return "";
        }

        try {
            String subString = rawData;
            
            int startIndex = subString.indexOf("{");
            int endIndex = subString.lastIndexOf("}");
            if (startIndex <= endIndex) {
                return subString.substring(startIndex, endIndex + 1);
            } else {
                return "";
            }  
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * 解析指定位置的json
     * @param rawData
     * @param index
     * @return
     */
    public static String getJsonString(String rawData, int index) {
        if (TextUtils.isEmpty(rawData)) {
            return "";
        }

        try {
            String splitString[] = rawData.split("\n");
            String subString = null;
            if (splitString.length > 0) {
                subString= splitString[index];
            } else {
                subString = rawData;
            }
             
            int startIndex = subString.indexOf("{");
            int endIndex = subString.lastIndexOf("}");
            if (startIndex <= endIndex) {
                return subString.substring(startIndex, endIndex + 1);
            } else {
                return "";
            }  
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * 解析json数组
     * @param rawData
     * @return json字符串
     */
    public static String getJsonArrayString(String rawData) {
        return getJsonArrayString(rawData, 0);
    }
    
    /**
     * 解析指定位置的json数组
     * @param rawData
     * @param index
     * @return
     */
    public static String getJsonArrayString(String rawData, int index) {
        if (TextUtils.isEmpty(rawData)) {
            return "";
        }

        try {
            String splitString[] = rawData.split("\n");
            String subString = null;
            if (splitString.length > 0) {
                subString= rawData.split("\n")[index];
            } else {
                subString = rawData;
            }
             
            int startIndex = subString.indexOf("[");
            int endIndex = subString.lastIndexOf("]");
            if (startIndex <= endIndex) {
                return subString.substring(startIndex, endIndex + 1);
            } else {
                return "";
            }  
        } catch (Exception e) {
            return "";
        }
    }
}
