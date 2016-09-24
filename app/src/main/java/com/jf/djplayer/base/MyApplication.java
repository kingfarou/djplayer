package com.jf.djplayer.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.jf.djplayer.base.activity.BaseActivity;

/**
 * Created by Administrator on 2015/7/20.
 * 自定义"Application"类，用来代替系统原有那个
 */
public class MyApplication extends Application {

    private static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
    public static Context getContext(){
        return context;
    }

}
