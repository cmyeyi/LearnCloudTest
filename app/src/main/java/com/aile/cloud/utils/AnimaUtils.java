package com.aile.cloud.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.aile.cloud.R;


public class AnimaUtils {

    /**
     * 从右向左展开，能得到Activity时，应使用{@link #openActivity(Activity, Intent)}
     */
    public static void openNewWindow(Context context, Intent intent) {
        if (context != null) {
            boolean isActivity = context instanceof Activity;
            if (!isActivity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            if (isActivity) {
                ((Activity) context).overridePendingTransition(R.anim.common_right_in, R.anim.common_left_out);
            }
        }
    }

    /**
     * 使用Activity在配置文件里默认的动画进行启动
     *
     * @param context
     * @param intent
     */
    public static void openWithActivityDefaultAnim(Context context, Intent intent) {
        if (context != null) {
            boolean isActivity = context instanceof Activity;
            if (!isActivity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    public static void openWithActivityDefaultAnim(Activity activity, Intent intent, int requestCode) {
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 点击返回键时
     */
    public static void returnPreActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activity.overridePendingTransition(R.anim.common_left_in, R.anim.common_right_out);
        }
    }

    /**
     * 从上到下收起
     */
    public static void closeActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activity.overridePendingTransition(R.anim.common_no_anim, R.anim.common_bottom_out);
        }
    }

    /**
     * 正常打开，从右向左展开
     */
    public static void openActivity(Activity activity, Intent intent) {
        if (activity != null) {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.common_right_in, R.anim.common_left_out);
        }
    }

    /**
     * 正常打开，从右向左展开
     */
    public static void openActivity(Activity activity, Intent intent, int requestCode) {
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode);
            activity.overridePendingTransition(R.anim.common_right_in, R.anim.common_left_out);
        }
    }

    public static void showActivity(Activity activity, Intent intent, int requestCode) {
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode);
            activity.overridePendingTransition(0, 0);
        }
    }

    /**
     * 从下往上弹出
     */
    public static void showUp(Activity activity, Intent intent) {
        if (activity != null) {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.common_bottom_in, R.anim.common_no_anim);
        }
    }


    /**
     * 从下往上弹出
     */
    public static void showUp(Activity activity, Intent intent, int requestCode) {
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode);
            activity.overridePendingTransition(R.anim.common_bottom_in, R.anim.common_no_anim);
        }
    }

}
