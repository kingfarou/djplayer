package com.jf.djplayer.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jf on 2016/9/3.
 * Toast的工具类
 */
public class ToastUtil {

    //该类不允许初始化，只提供有工具方法
    private ToastUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    //你可以在这里统一控制否是显示"Taost"
    private static boolean isShow = true;

    /**
     * 短时间显示Toast
     * @param context
     * @param text
     */
    public static void showShortToast(Context context, CharSequence text) {
        if (isShow) Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时间显示Toast
     * @param context
     * @param message
     */
    public static void showShortToast(Context context, int message) {
        if (isShow) Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     * @param context
     * @param text
     */
    public static void showLongToast(Context context, CharSequence text) {
        if (isShow) Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     * @param context
     * @param message
     */
    public static void showLongToast(Context context, int message) {
        if (isShow) Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     * @param context
     * @param text
     * @param duration
     */
    public static void show(Context context, CharSequence text, int duration) {
        if (isShow) Toast.makeText(context, text, duration).show();
    }

    /**
     * 自定义显示Toast时间
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (isShow) Toast.makeText(context, message, duration).show();
    }
}
