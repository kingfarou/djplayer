package com.jf.djplayer.songscan;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.jf.djplayer.R;
import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.base.baseactivity.BaseActivity;

/**
 * Created by jf on 2016/8/8.
 * 音乐扫描-音乐扫描入口页面
 */
public class ScanSongActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected int getContentViewId() {
        return R.layout.activity_scan_song;
    }

    @Override
    protected void initExtra() {

    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_activity_scan_song_scan_all).setOnClickListener(this);
        findViewById(R.id.btn_activity_scan_song_custom_scan).setOnClickListener(this);
        findViewById(R.id.tv_activity_scan_song_scan_setting).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //如果选择全盘扫描
            case R.id.btn_activity_scan_song_scan_all:
                MyApplication.showToast(this, "该功能还未完成");
                break;
            //如果选择自定义路径的扫描
            case R.id.btn_activity_scan_song_custom_scan:
                MyApplication.showToast(this, "该功能还未完成");
                break;
            //点击进入扫描设置界面
            case R.id.tv_activity_scan_song_scan_setting:
                startActivity(new Intent(this, ScanSettingActivity.class));
                break;
            default:break;
        }
    }
}
