package com.jf.djplayer;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2015/7/20.
 */
public class MyApplication extends Application {

    private static Context context = null;
    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
    }
    public static Context getContext(){
        return context;
    }
}
