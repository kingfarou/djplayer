package com.jf.djplayer.controller.myfavorite;

import android.os.Bundle;

import com.jf.djplayer.R;
import com.jf.djplayer.base.activity.BackgroundPlayActivity;
import com.jf.djplayer.widget.TitleBar;


/**
 * 我的最爱
 */
public class MyFavoriteActivity extends BackgroundPlayActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        initView();
    }

    private void initView(){
        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar_activity_my_favorite);
        titleBar.setTitleText("我的最爱");
    }
}
