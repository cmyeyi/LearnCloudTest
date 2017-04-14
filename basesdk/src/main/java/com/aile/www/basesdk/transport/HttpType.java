package com.aile.www.basesdk.transport;

/**
 * <p>
 * http 请求方法类型 get post put delete
 */

public enum HttpType {
    GET,
    PUT,
    POST,
    DELETE;

    /**
     * @param type
     * @return
     */
    public static boolean isGET(HttpType type) {
        return GET == type;
    }

    /**
     * @param type
     * @return
     */
    public static boolean isPUT(HttpType type) {
        return PUT == type;
    }

    /**
     * @param type
     * @return
     */
    public static boolean isPOST(HttpType type) {
        return POST == type;
    }

    /**
     * @param type
     * @return
     */
    public static boolean isDELETE(HttpType type) {
        return DELETE == type;
    }
}
