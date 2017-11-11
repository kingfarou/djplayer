package com.jf.djplayer.controller.scansong;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.activity.BaseActivity;
import com.jf.djplayer.bean.ScanInfo;
import com.jf.djplayer.datamanager.SongScanner;

/**
 * Created by Kingfar on 2017/11/5.
 * 音乐扫描进行时界面
 */
public class ScanningActivity extends BaseActivity implements SongScanner.ScanListener {

    RelativeLayout scanningDoingRl;   // 扫描进行时布局
    TextView scanningInfoTv;          // 扫描进行时文字信息
    ProgressBar songNumPb;            // 歌曲数量进度条
    RelativeLayout scanningFinishRl;  // 扫描结束时布局
    TextView scanningFinishInfoTv;    // 扫描结束文字信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        initView();
        startScanSong();
    }

    private void initView(){
        scanningDoingRl  = (RelativeLayout)findViewById(R.id.rl_activity_scanning_doing);
        scanningInfoTv = (TextView)findViewById(R.id.tv_activity_scanning_scan_info);
        songNumPb = (ProgressBar) findViewById(R.id.pb_activity_scanning_song_num);
        scanningFinishRl = (RelativeLayout) findViewById(R.id.rl_activity_scanning_scan_finish);
        scanningFinishInfoTv = (TextView)findViewById(R.id.tv_activity_scanning_scan_finish_info);
        findViewById(R.id.btn_activity_scanning_scan_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 点击扫描完成按钮即可关闭界面
            }
        });
    }

    private void startScanSong(){
        SongScanner songScanner = new SongScanner();
        songScanner.setSongScanListener(this);
        songScanner.startScan();
    }

    /****************SongScanListener方法实现****************/
    @Override
    public void onProgressUpdate(ScanInfo scanInfo) {
        scanningInfoTv.setText(scanInfo.getSongAbsolutePath());
        songNumPb.setMax(scanInfo.getSongNum());
        songNumPb.setProgress(scanInfo.getCurrentNum());
    }

    @Override
    public void onSuccess(ScanInfo scanInfo) {
        scanningFinishInfoTv.setText("已扫描到"+scanInfo.getSongNum()+"首歌曲");
        scanningDoingRl.setVisibility(View.INVISIBLE);
        scanningFinishRl.setVisibility(View.VISIBLE);
        setResult(RESULT_OK);
    }

    @Override
    public void onFailed(Exception e) {

    }
    /****************SongScanListener方法实现****************/
}
