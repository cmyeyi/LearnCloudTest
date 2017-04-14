package com.aile.cloud.net.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceUtil {

	public static void applyToEditor(Editor editor) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    /**
     * @param preferences
     * @param key
     * @param defValue
     * @return
     */
    public static int getSafetyInt(SharedPreferences preferences, String key, int defValue){
        if(null == preferences){
            return defValue;
        }

        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.KITKAT) {
            try{
                return preferences.getInt(key,defValue);
            }catch (Exception e){
                e.printStackTrace();
                return defValue;
            }
        } else {
           return preferences.getInt(key,defValue);
        }
    }

    /**
     * @param preferences
     * @param key
     * @param defValue
     * @return
     */
    public static long getSafetyLong(SharedPreferences preferences, String key, long defValue){
        if(null == preferences){
            return defValue;
        }

        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.KITKAT) {
            try{
                return preferences.getLong(key,defValue);
            }catch (Exception e){
                e.printStackTrace();
                return defValue;
            }
        } else {
            return preferences.getLong(key,defValue);
        }
    }

    /**
     * @param preferences
     * @param key
     * @param defValue
     * @return
     */
    public static String getSafetyString(SharedPreferences preferences, String key, String defValue){
        if(null == preferences){
            return defValue;
        }

        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.KITKAT) {
            try{
                return preferences.getString(key,defValue);
            }catch (Exception e){
                e.printStackTrace();
                return defValue;
            }
        } else {
            return preferences.getString(key,defValue);
        }
    }

    /**
     * @param preferences
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getSafetyBoolean(SharedPreferences preferences, String key, boolean defValue){
        if(null == preferences){
            return defValue;
        }

        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.KITKAT) {
            try{
                return preferences.getBoolean(key,defValue);
            }catch (Exception e){
                e.printStackTrace();
                return defValue;
            }
        } else {
            return preferences.getBoolean(key,defValue);
        }
    }
}
