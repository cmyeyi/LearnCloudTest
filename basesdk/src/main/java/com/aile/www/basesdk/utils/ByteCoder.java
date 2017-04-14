package com.aile.www.basesdk.utils;

import android.text.TextUtils;

/**
 * @Description 存储大大票时用
 * @author zijianlu
 */
public class ByteCoder {
    /** 十六进制字符串转字节数组 */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    /** 字节数组转十六进制字符串 */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /** 将字符串str中的字符，按每charPerPart个分组，而且每组内的字符逆序 */
    public static String partReverseString(String str, int charPerPart) {
        if (TextUtils.isEmpty(str) || charPerPart <= 1
            || str.length() < charPerPart) {
            return str;
        }

        int len = str.length();
        char[] encodeChar = new char[len];
        int partNum = len / charPerPart;
        int n = 0;
        for (int i = 0; i < partNum; i++) {
            int bIdx = i * charPerPart;
            int eIdx = i * charPerPart + charPerPart;
            for (int j = eIdx - 1; j >= bIdx; j--) {
                encodeChar[n++] = str.charAt(j);
            }
        }

        if (n < len) {
            for (int i = n; i < len; i++) {
                encodeChar[n++] = str.charAt(i);
            }
        }

        return new String(encodeChar);
    }

    /** 将按默认编码方式的字节流转换成字符串，在分组逆序 */
    public static String encode(String str) {
        if (!TextUtils.isEmpty(str)) {
            String hexStr = ByteCoder.bytesToHexString(str.getBytes());
            return ByteCoder.partReverseString(hexStr, 3);
        }
        return str;
    }

    /** 先分组逆序, 将字符串转换成字节流，在按默认编码方式转换成字符串 */
    public static String decode(String str) {
        if (!TextUtils.isEmpty(str)) {
            String decode = ByteCoder.partReverseString(str, 3);
            return new String(ByteCoder.hexStringToByte(decode));
        }
        return str;
    }
}
