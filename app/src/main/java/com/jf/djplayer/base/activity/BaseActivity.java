package com.jf.djplayer.base.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jf.djplayer.util.LogUtil;

/**
 * Created by JF on 2016/4/9.
 * 所有"Activity"基类
 */
abstract public class BaseActivity extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //打印当前Activity名字，方便调试
        LogUtil.i(getClass().getSimpleName());
    }
}
