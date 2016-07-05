package com.jf.djplayer.base.baseactivity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jf.djplayer.base.MyApplication;

/**
 * Created by JF on 2016/4/9.
 * 所有"Activity"基类
 */
abstract public class BaseActivity extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //打印当前Activity名字，方便调试
        MyApplication.showLog(this.getClass().getSimpleName());
        //调用工厂方法进行初始化了
        setContentView(getContentViewId());
        initExtra();
        initView();
    }

    /**
     * 获取"Activity"要加载的布局文件id
     * @return 要加载的布局文件id
     */
    abstract protected int getContentViewId();

    /**
     * 在控件初始化之前的其他初始化
     */
    protected void initExtra(){}

    /**
     * 执行控件相关的初始化
     */
    protected void initView(){}
}
