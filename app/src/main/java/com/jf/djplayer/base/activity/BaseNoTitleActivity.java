package com.jf.djplayer.base.activity;

import android.os.Bundle;
import android.view.Window;

import com.jf.djplayer.base.activity.BaseActivity;

/**
 * Created by JF on 2016/4/9.
 * 所有不带"ActionBar"的"Activity"的基类
 */
abstract public class BaseNoTitleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        去掉系统自带的的ActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        调用工厂方法进行子类的初始化
        doSetContentView();
        viewInit();
        extrasInit();
    }
}
