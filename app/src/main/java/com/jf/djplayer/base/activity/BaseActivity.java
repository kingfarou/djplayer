package com.jf.djplayer.base.activity;

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

        setContentView(getContentViewId());
        initOther();
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
    protected void initOther(){}

    /**
     * 执行控件相关的初始化
     */
    protected void initView(){}
}
