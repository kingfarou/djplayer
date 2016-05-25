package com.jf.djplayer.other;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by Administrator on 2015/7/20.
 */
public class MyApplication extends Application {

    private static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();
//        this.context = this;
        context = this;
    }
    public static Context getContext(){
        return context;
    }

    /**
     * 这是一个全局打印方法，当该应用不再需要调试
     * 只要注释这个方法里的代码即可，就能关闭所有打印
     * @param info 要打印的调试信息
     */
    public static void printLog(String info){
        Log.i("com.jf.djplayer.test", info);
    }
}
