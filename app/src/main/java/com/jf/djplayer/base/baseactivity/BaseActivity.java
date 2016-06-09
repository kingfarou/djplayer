package com.jf.djplayer.base.baseactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Created by JF on 2016/4/9.
 * 所有"Activity"基类
 * 通过类似工程方法模式，实现对子类的框架约束
 *
 */
public abstract class BaseActivity extends FragmentActivity{

//    public static final String WHICH_ACTIVITY = "which_activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        打印当前Activity名字，方便调试
        Log.i("activity_name",this.getClass().getSimpleName());
    }

    /**
     * 子类需要在此返回"Activity"布局文件的ID，这是一个工厂方法
     */
    abstract protected int getContentViewId();

    /**
     * 子类在此对控件进行初始化，这是一个工厂方法
     */
    abstract protected void initView();

    /**
     * 子类在此进行其他的初始化，这是一个工厂方法
     */
    abstract protected void initExtrasBeforeView();
}
