package com.aile.cloud.net.utils;

import com.aile.www.basesdk.UnProguardable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 implements UnProguardable {
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * MD5 加密处理
     *
     * @param str
     * @return
     */
    public static String getMD5Str(String str) {

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }


    public static String getMD5Str(String str, String str2) {
        return getMD5Str(str + str2);
    }

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * 获取文件的MD5值
     * add by jiyuxuan at 2016年10月25日16:41:11
     *
     * @param filePath
     * @return
     */
    public static String getFileMD5(String filePath) {
        String md5 = null;
        FileInputStream fis = null;
        FileChannel fileChannel = null;
        File file = new File(filePath);
        if (null != file && file.exists()) {
            try {
                fis = new FileInputStream(file);
                fileChannel = fis.getChannel();
                MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());

                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(byteBuffer);
                    md5 = byteArrayToHexString(md.digest());
                } catch (NoSuchAlgorithmException e) {

                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            } finally {
                try {
                    fileChannel.close();
                    fis.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }

        return md5;
    }

    /**
     * 字节数组转十六进制字符串
     *
     * @param digest
     * @return
     */
    private static String byteArrayToHexString(byte[] digest) {

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            buffer.append(byteToHexString(digest[i]));
        }
        return buffer.toString();
    }

    /**
     * 字节转十六进制字符串
     *
     * @param b
     * @return
     */
    private static String byteToHexString(byte b) {
        //  int d1 = n/16;
        int d1 = (b & 0xf0) >> 4;
        //   int d2 = n%16;
        int d2 = b & 0xf;
        return String.valueOf(HEX_DIGITS[d1]) + String.valueOf(HEX_DIGITS[d2]);
    }

    public static String md5sum(String filename) {
        InputStream fis = null;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try {
            fis = new FileInputStream(filename);
            md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            return toHexString(md5.digest());
        } catch (Exception e) {
            return "";
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
