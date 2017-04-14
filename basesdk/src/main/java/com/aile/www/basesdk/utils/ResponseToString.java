package com.aile.www.basesdk.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ResponseToString {

    public static String streamToString(InputStream inputStream, String charset, long size) {
        ByteArrayOutputStream baos = byteStream(size);
        try {
            byte[] buff = new byte[1024 * 10];
            int length = 0;
            while ((length = inputStream.read(buff)) >= 0) {
                baos.write(buff, 0, length);
            }
            baos.flush();
            return new String(baos.toByteArray(), charset);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }

            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
        }
        return null;
    }

    /**
     * Create byte array output stream
     *
     * @return stream
     */
    protected static ByteArrayOutputStream byteStream(long size) {
        if (size > 0) {
            return new ByteArrayOutputStream((int) size);
        } else {
            return new ByteArrayOutputStream();
        }
    }
}
