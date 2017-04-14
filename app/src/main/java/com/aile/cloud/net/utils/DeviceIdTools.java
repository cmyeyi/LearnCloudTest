package com.aile.cloud.net.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.aile.cloud.AppApplication;
import com.aile.cloud.BuildConfig;

import java.util.UUID;

/**
 * @author alecfeng
 *
 */
public class DeviceIdTools {

    private static String sDeviceId = "";
    private static String sIMEI = "";
    private static String sSimSerialNum = "";

    /**
     * All devices tested returned a value for
     * TelephonyManager.getDeviceId() All GSM devices
     * (all tested with a SIM) returned a value for
     * TelephonyManager.getSimSerialNumber() All CDMA
     * devices returned null for getSimSerialNumber()
     * (as expected) All devices with a Google account
     * added returned a value for ANDROID_ID All CDMA
     * devices returned the same value (or derivation of
     * the same value) for both ANDROID_ID and
     * TelephonyManager.getDeviceId() -- as long as a
     * Google account has been added during setup. I did
     * not yet have a chance to test GSM devices with no
     * SIM, a GSM device with no Google account added,
     * or any of the devices in airplane mode. I know
     * that the IMEI can be broken and set to an android
     * backup IMEI (something like 0049990...). Read out
     * tThe IMSI or Phonenumber is not supported by all
     * SIM-Cards AndroidID can be change by the User MAC
     * Adress only available if WLAN in online Mac
     * Address It may be possible to retrieve a Mac
     * address from a device’s WiFi or Bluetooth
     * hardware. We do not recommend using this as a
     * unique identifier. To start with, not all devices
     * have WiFi. Also, if the WiFi is not turned on,
     * the hardware may not report the Mac address. So
     * if you want something unique to the device
     * itself, TM.getDeviceId() should be sufficient.
     * Obviously some users are more paranoid than
     * others, so it might be useful to hash 1 or more
     * of these identifiers, so that the string is still
     * virtually unique to the device, but does not
     * explicitly identify the user's actual device. For
     * example, using String.hashCode(), combined with a
     * UUID: final TelephonyManager tm =
     * (TelephonyManager)
     * getBaseContext().getSystemService
     * (Context.TELEPHONY_SERVICE); final String
     * tmDevice, tmSerial, tmPhone, androidId; tmDevice
     * = "" + tm.getDeviceId(); tmSerial = "" +
     * tm.getSimSerialNumber(); androidId = "" +
     * android.provider.Settings.Secure.getString(
     * getContentResolver(),
     * android.provider.Settings.Secure.ANDROID_ID);
     * UUID deviceUuid = new UUID(androidId.hashCode(),
     * ((long)tmDevice.hashCode() << 32) |
     * tmSerial.hashCode()); String deviceId =
     * deviceUuid.toString();
     */
    @SuppressLint("HardwareIds")
    public static String getMyDeviceId(Context context) {
        if(TextUtils.isEmpty(sDeviceId)){
            final TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = ""
                    + android.provider.Settings.Secure.getString(
                    context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(),
                    ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            sIMEI = tmDevice;
            sSimSerialNum = tmSerial;
            sDeviceId = deviceUuid.toString();
        }
        return sDeviceId;
    }

    /**
     * 获取系统方式提供的DeviceId
     * @param context
     * @return
     */
    // TODO: 2017/2/4 没有调用暂时注释
//    public static String getIMEI(Context context){
//        if(TextUtils.isEmpty(sIMEI)){
//            getMyDeviceId(context);
//        }
//        return sIMEI  != null && !"null".equalsIgnoreCase(sIMEI) ? sIMEI  : "";
//    }

    /**
     * 获取Sim卡序列号
     * @param context
     * @return
     */
    public static String getSimSerialNum(Context context){
        if(TextUtils.isEmpty(sSimSerialNum)){
            getMyDeviceId(context);
        }
        return sSimSerialNum != null && !"null".equalsIgnoreCase(sSimSerialNum)  ? sSimSerialNum  : "";
    }

    /**
     * 新的imei号
     * @return
     */
    public static String getDeviceIMEI(){
        String[] IMEI = YingYongBaoDeviceHelper.getImeiFromNative(AppApplication.getInstance());
        return IMEI != null && IMEI.length > 0 ? IMEI[0] : "";
    }

    /**
     * 获取程序版本号
     */
    public static int getAppVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    /**
     * 获取程序版本名
     */
    public static String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }
}
