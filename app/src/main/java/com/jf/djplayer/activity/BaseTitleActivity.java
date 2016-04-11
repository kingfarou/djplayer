package com.jf.djplayer.activity;

import android.os.Bundle;

import com.jf.djplayer.activity.BaseActivity;

/**
 * Created by JF on 2016/4/9.
 */
abstract public class BaseTitleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doSetContentView();
//        viewInit();
        extrasInit();
        viewInit();
    }


}
