package com.aile.www.basesdk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.UUID;

/**
 * 获取和保存设备号
 * @author zijianlu
 */
public class DeviceIdHelper {
    public static final String TAG = "elife.notify";
    public static final String SD_FILE_PATH = "/sdcard/.com.tencent.elife/";
    public static final String SD_FILE_NAME = "devId.cfg";
    public static final String PREF_FILE_NAME = "devId";

    /**
     * 需要如下权限： android.permission.WRITE_EXTERNAL_STORAGE
     */
    public synchronized static String getDeviceId(Context context) {
        String sdPath = "";
        String pkgName = context.getPackageName();
        if (TextUtils.isEmpty(pkgName)) {
            sdPath = SD_FILE_PATH;
        } else {
            sdPath = SD_FILE_PATH + pkgName + "/";
        }

        // 本地数据中有的话返回本地数据的
        String devId = getDeviceIdInPref(context);
        if (!TextUtils.isEmpty(devId)) {
            // 判断sd中是否有数据，若没有存储一份到sd卡中
            String tmp = getDeviceIdInSdcard(sdPath);
            if (TextUtils.isEmpty(tmp) || !devId.equals(tmp)) {
                saveDeviceIdToSdcard(sdPath, devId);
            }

            return devId;
        }

        // 本地数据中没有，读sd卡中的
        devId = getDeviceIdInSdcard(sdPath);
        if (!TextUtils.isEmpty(devId)) {
            // sd卡中有，存储到本地去
            saveDeviceIdToPref(context, devId);
            return devId;
        }

        // 如果本地和sd卡中都没有，新生成一个
        devId = createUUID();
        if (!TextUtils.isEmpty(devId)) {
            saveDeviceIdToPref(context, devId);
            saveDeviceIdToSdcard(sdPath, devId);

            return devId;
        }

        // 如果创建uuid都为空，这种情况根据randomUUID的注释是不可能的。如果真有这种情况返回一个标识的字符串
        return "elife.devid.empty";
    }

    /** 生成一个随机的UUID */
    public static String createUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /** 读取存储在应用本地数据目录下的id */
    public synchronized static String getDeviceIdInPref(Context context) {
        try {
            SharedPreferences pref = context.getSharedPreferences(
                PREF_FILE_NAME, 0);
            return pref.getString("devId", "");
        } catch (Exception e) {
            return "";
        }
    }

    /** 保存存储在应用本地数据目录下的id */
    public synchronized static void saveDeviceIdToPref(Context context,
        String deviceId) {
        try {
            SharedPreferences pref = context.getSharedPreferences(
                PREF_FILE_NAME, 0);
            if (!TextUtils.isEmpty(deviceId) && pref != null) {
                SharedPreferences.Editor localEditor = pref.edit();
                localEditor.putString("devId", deviceId);
                localEditor.commit();
            }
        } catch (Exception e) {
        }
    }

    /** 获取保存在本地sd卡的设备号 */
    public synchronized static String getDeviceIdInSdcard(String path) {
        StringBuffer strBuf = new StringBuffer();
        String line = "";
        BufferedReader br = null;

        try {
            File dirFile = new File(path);

            if (!dirFile.exists()) {
                dirFile.mkdirs();
                return "";
            }

            File cfgFile = new File(dirFile, SD_FILE_NAME);
            if (!cfgFile.exists()) {
                return "";
            }

            br = new BufferedReader(new FileReader(cfgFile));
            while ((line = br.readLine()) != null) {
                strBuf.append(line);
            }
        } catch (Exception e) {

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }

        return strBuf.toString().trim();
    }

    /** 保存设备号到本地sd卡 */
    public synchronized static void saveDeviceIdToSdcard(String path,
        String deviceId) {
        if (TextUtils.isEmpty(deviceId)) {
            return;
        }
        BufferedWriter bw = null;
        File dirFile = new File(path);

        try {
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File cfgFile = new File(dirFile, SD_FILE_NAME);
            bw = new BufferedWriter(new FileWriter(cfgFile));
            bw.write(deviceId);
            bw.flush();
        } catch (Exception e) {

        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
