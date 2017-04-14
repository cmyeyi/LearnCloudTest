package com.aile.cloud.net.utils;

import android.content.Context;
import android.content.pm.PackageManager;


/**
 * Created by RP on 2015/10/20.
 */
public class YingYongBaoDeviceHelper {

    private static final String UN_DEFINED = "NA" ;
    public static final String DEFINE = "|" ;

    public static String[] getImeiFromNative(Context context){
        int result = -1;
        try {
            result = context.checkCallingOrSelfPermission(android.Manifest.permission.READ_PHONE_STATE);
        } catch (Exception e) {
        }
        if(result != PackageManager.PERMISSION_GRANTED){
            return null;
        }
        String[] imeiInfo = new String[2] ;
        try{
            //// TODO: 2017/2/22  暂时注释
            /*if(!MobileIssueSettings.isSupportDualSimIMEI){
                imeiInfo = KapalaiAdapterUtil.getKAUInstance().getDualSimIMEIInfoMethod((context));
            }else{
                imeiInfo = KapalaiAdapterUtil.getKAUInstance().getDualSimIMEIInfoNormalMethod((context));
            }*/
            if(imeiInfo != null && imeiInfo.length != 0)
            {
                return imeiInfo;
            }
            return null;
        }catch(Exception e){
            return null;
        }
    }



}
