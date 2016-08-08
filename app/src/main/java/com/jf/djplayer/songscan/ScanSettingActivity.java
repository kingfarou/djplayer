package com.jf.djplayer.songscan;

import android.widget.SeekBar;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.base.baseactivity.BaseActivity;

/**
 * Created by jf on 2016/8/5.
 * 音乐扫描-扫描设置
 */
public class ScanSettingActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener{

    private TextView durationTv;
    private SeekBar durationSeekBar;
    private TextView sizeTv;
    private SeekBar sizeSeekBar;

    private int duration;//用户所设置的扫描时间（秒）
    private int size;//用户所设置的文件大小（kb）

    @Override
    protected int getContentViewId() {
        return R.layout.activity_scan_setting;
    }

    @Override
    protected void initExtra() {
        //读取用户所设置的扫描选项
        ScanOptionHelper scanOptionHelper = new ScanOptionHelper();
        duration = scanOptionHelper.getDuration()/1000;
        size = scanOptionHelper.getSize();
    }

    @Override
    protected void initView() {
        durationTv = (TextView)findViewById(R.id.tv_activity_scan_setting_duration);
        durationSeekBar = (SeekBar)findViewById(R.id.seek_bar_activity_scan_setting_duration);
        sizeTv = (TextView)findViewById(R.id.tv_activity_scan_setting_size);
        sizeSeekBar = (SeekBar)findViewById(R.id.seek_bar_activity_scan_setting_size);

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
        if(seekBar == durationSeekBar){
            durationTv.setText(progress+"");
        }else{
            sizeTv.setText(progress+"");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar == durationSeekBar){
            duration = seekBar.getProgress();
        }else{
            size = seekBar.getProgress();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将用户的设置存入数据库里
        ScanOptionHelper scanOptionHelper = new ScanOptionHelper();
        scanOptionHelper.setDuration(duration*1000);//需要进行单位转变
        scanOptionHelper.setSize(size);
    }
}
