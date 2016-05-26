package com.jf.djplayer.base.baseactivity;

import android.os.Bundle;

/**
 * Created by JF on 2016/4/9.
 */
abstract public class BaseTitleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        initExtrasBeforeView();
        initView();
    }


}
