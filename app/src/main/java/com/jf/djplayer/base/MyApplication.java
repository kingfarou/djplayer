package com.jf.djplayer.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.jf.djplayer.base.baseactivity.BaseActivity;

/**
 * Created by Administrator on 2015/7/20.
 * 自定义"Application"类，用来代替系统原有那个
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
    public static void showLog(String info){
        Log.i("com.jf.djplayer.test", info);
    }

    /**
     * 弹出"Toast"
     * @param baseActivity 当前环境
     * @param content "Toast"里的内容
     */
    public static void showToast(BaseActivity baseActivity, String content){
        Toast.makeText(baseActivity, content, Toast.LENGTH_SHORT).show();
    }
}
