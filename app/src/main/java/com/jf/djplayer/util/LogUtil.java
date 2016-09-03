package com.jf.djplayer.util;

import android.util.Log;

/**
 * Created by jf on 2016/9/3.
 * 日志打印的工具
 */
public class LogUtil {

    //该类不允许初始化，只提供了打印方法
    private LogUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    //可以在这单独控制是否需要打印日志
    private static boolean isDebug = true;
    private static final String TAG = "com.jf.djplayer.testLog";

    public static void verbose(String msg) {
        if (isDebug) Log.v(TAG, msg);
    }

    public static void debug(String msg) {
        if (isDebug) Log.d(TAG, msg);
    }

    // 下面四个是默认tag的函数
    public static void info(String msg) {
        if (isDebug) Log.i(TAG, msg);
    }

    public static void error(String msg) {
        if (isDebug) Log.e(TAG, msg);
    }

    public static void verbose(String tag, String msg) {
        if (isDebug) Log.i(tag, msg);
    }

    // 下面是传入自定义tag的函数
    public static void info(String tag, String msg) {
        if (isDebug) Log.i(tag, msg);
    }

    public static void debug(String tag, String msg) {
        if (isDebug) Log.i(tag, msg);
    }

    public static void error(String tag, String msg) {
        if (isDebug) Log.i(tag, msg);
    }
}
