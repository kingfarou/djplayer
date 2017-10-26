package com.jf.djplayer.controller.scansong;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.activity.BaseActivity;
import com.jf.djplayer.util.ScanOptionUtil;
import com.jf.djplayer.widget.TitleBar;

/**
 * Created by jf on 2016/8/5.
 * 音乐扫描-扫描设置
 */
public class ScanSettingActivity extends BaseActivity
        implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, TitleBar.OnTitleClickListener{

    private TextView durationTv;        //扫描时长，文字显示
    private TextView sizeTv;            //文件大小，文字显示

    private int duration;               //用户所设置的扫描时间（秒）
    private int size;                   //用户所设置的文件大小（kb）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_setting);

        // 读取用户所设置的扫描选项
        ScanOptionUtil scanOptionUtil = new ScanOptionUtil();
        duration = scanOptionUtil.getDuration()/1000;
        size = scanOptionUtil.getSize();

        initView();
    }

    private void initView(){
        //对标题栏做初始化
        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar_activity_scan_setting);
        titleBar.setSearchVisibility(View.GONE);
        titleBar.setMenuVisibility(View.GONE);
        titleBar.setOnTitleClickListener(this);

        //以下控件分别是，扫描时长，文字显示和进度条，扫描大小，文件大小和进度条，确定按钮
        durationTv = (TextView)findViewById(R.id.tv_activity_scan_setting_duration);
        SeekBar durationSeekBar = (SeekBar)findViewById(R.id.seek_bar_activity_scan_setting_duration);
        sizeTv = (TextView)findViewById(R.id.tv_activity_scan_setting_size);
        SeekBar sizeSeekBar = (SeekBar)findViewById(R.id.seek_bar_activity_scan_setting_size);
        findViewById(R.id.btn_activity_scan_setting_confirm).setOnClickListener(this);

        //设置"SeekBar"
        durationSeekBar.setProgress(duration);
        durationSeekBar.setOnSeekBarChangeListener(this);
        sizeSeekBar.setProgress(size);
        sizeSeekBar.setOnSeekBarChangeListener(this);

        //设置"TextView"
        durationTv.setText("不要扫描小于"+duration+"秒的歌曲");
        sizeTv.setText("不要扫描小于"+size+"k的歌曲");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int id = seekBar.getId();
        if(id == R.id.seek_bar_activity_scan_setting_duration){
            // 滑动的是调整时长的进度条，设置时长
            durationTv.setText("不要扫描小于"+progress+"秒的歌曲");
        }else if(id == R.id.seek_bar_activity_scan_setting_size){
            //滑动调整大小的进度条，设置大小
            sizeTv.setText("不要扫描小于"+progress+"k的歌曲");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int id = seekBar.getId();
        if(id == R.id.seek_bar_activity_scan_setting_duration){
            //滑动调整时长的进度条
            duration = seekBar.getProgress();
        }else if(id == R.id.seek_bar_activity_scan_setting_size){
            //滑动调整大小的进度条
            size = seekBar.getProgress();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //点击确定
        if(id == R.id.btn_activity_scan_setting_confirm){
//            //将用户的设置存入数据库里
            ScanOptionUtil scanOptionUtil = new ScanOptionUtil();
            scanOptionUtil.setDuration(duration*1000);
            scanOptionUtil.setSize(size);
            scanOptionUtil.saveAllOptions();
            finish();
        }
    }

    @Override
    public void onTitleClick(View titleView) {
        finish();
    }
}
