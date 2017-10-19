package com.jf.djplayer.songscan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jf.djplayer.R;
import com.jf.djplayer.base.activity.BaseActivity;
import com.jf.djplayer.util.ToastUtil;
import com.jf.djplayer.widget.TitleBar;

/**
 * Created by jf on 2016/8/8.
 * 音乐扫描-音乐扫描入口页面
 */
public class ScanSongActivity extends BaseActivity
        implements View.OnClickListener, TitleBar.OnTitleClickListener{

    private static final int REQUEST_CODE_SCAN_MUSIC = 1;//扫描音乐

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_song);
        // view初始化
        // 对标题栏做初始化
        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar_activity_scan_song);
        titleBar.setSearchVisibility(View.GONE);
        titleBar.setMenuVisibility(View.GONE);
        titleBar.setOnTitleClickListener(this);
        // 三个按钮，分别是全盘扫描，自定义扫描，扫描设置
        findViewById(R.id.btn_activity_scan_song_scan_all).setOnClickListener(this);
        findViewById(R.id.btn_activity_scan_song_custom_scan).setOnClickListener(this);
        findViewById(R.id.tv_activity_scan_song_scan_setting).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_activity_scan_song_scan_all){
            //点击全盘扫描按钮
            startActivityForResult(new Intent(this, ScanningSongActivity.class), REQUEST_CODE_SCAN_MUSIC);
        }else if(id == R.id.btn_activity_scan_song_custom_scan){
            //点击自定义路径按钮
            ToastUtil.showShortToast(this, "该功能还未实现");
        }else if(id == R.id.tv_activity_scan_song_scan_setting){
            //点击扫描设置按钮
            startActivity(new Intent(this, ScanSettingActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CODE_SCAN_MUSIC){
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void onTitleClick(View titleView) {
        finish();
    }
}
