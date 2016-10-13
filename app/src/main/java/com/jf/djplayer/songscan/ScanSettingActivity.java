package com.jf.djplayer.songscan;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.activity.BaseActivity;
import com.jf.djplayer.view.TitleBar;

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
    protected int getContentViewId() {
        return R.layout.activity_scan_setting;
    }

    @Override
    protected void initOther() {
        //读取用户所设置的扫描选项
        ScanOptionHelper scanOptionHelper = new ScanOptionHelper();
        duration = scanOptionHelper.getDuration()/1000;
        size = scanOptionHelper.getSize();
    }

    @Override
    protected void initView() {
        //对标题栏做初始化
        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
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
        durationTv.setText(duration+"");
        sizeTv.setText(size+"");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int id = seekBar.getId();
        if(id == R.id.seek_bar_activity_scan_setting_duration){
            //滑动的是调整时长的进度条，设置时长
            durationTv.setText(progress+"");
        }else if(id == R.id.seek_bar_activity_scan_setting_size){
            //滑动调整大小的进度条，设置大小
            sizeTv.setText(progress+"");
        }
//        if(seekBar == durationSeekBar){
//            //如果滑动的是调整时长的进度条
//            durationTv.setText(progress+"");
//        }else{
//            //滑动的是调整大小的进度条
//            sizeTv.setText(progress+"");
//        }
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
//        if(seekBar == durationSeekBar){
//            duration = seekBar.getProgress();
//        }else{
//            size = seekBar.getProgress();
//        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //点击确定
        if(id == R.id.btn_activity_scan_setting_confirm){
            //将用户的设置存入数据库里
            ScanOptionHelper scanOptionHelper = new ScanOptionHelper();
            //需要进行单位转变
            scanOptionHelper.setDuration(duration*1000);
            scanOptionHelper.setSize(size);
            finish();
        }
    }

    @Override
    public void onTitleClick(View titleView) {
        finish();
    }
}
