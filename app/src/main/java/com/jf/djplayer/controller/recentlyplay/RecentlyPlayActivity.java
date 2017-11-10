package com.jf.djplayer.controller.recentlyplay;

import android.os.Bundle;

import com.jf.djplayer.R;
import com.jf.djplayer.base.activity.BackgroundPlayActivity;
import com.jf.djplayer.widget.TitleBar;


/**
 * 最近播放
 */
public class RecentlyPlayActivity extends BackgroundPlayActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_play);
        initView();
    }

    private void initView(){
        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar_activity_recently_play);
        titleBar.setTitleText("最近播放");
    }

}
