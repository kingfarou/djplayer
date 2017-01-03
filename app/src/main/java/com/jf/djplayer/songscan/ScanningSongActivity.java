package com.jf.djplayer.songscan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.jf.djplayer.base.activity.BaseActivity;
import com.jf.djplayer.R;


import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2015/8/17.
 * 音乐正在扫描中的信息提示界面，
 * 该界面只做信息提示用，扫描音乐具体功能在子线程里面实现，
 * 该界面接收子线程所发"Message"并且更新扫描信息
 */
public class ScanningSongActivity extends BaseActivity {

    private ProgressBar scanningPb;//正在扫描时显示的圆形的进度条
    private TextView scanInfoTv;//这个用来显示扫描信息（音乐路径以及歌曲总数）
    private ProgressBar progressBar;//这个现实扫描进度
    private ImageView scanFinishIv;//这是扫描完成图标
    private Button scanFinishButton;//这是扫描完成按钮
    private SongScanHandler songScanHandler;//接收子线程的信息用的"Handler"

    //与子线程通信标志
    public static final int HANDLER_WHAT_START_SCAN = 1<<1;
    public static final int HANDLER_WHAT_UPDATE_SCAN_INFO = 1<<2;
    public static final int HANDLER_WHAT_FINISH_SCAN = 1<<3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning_song);
        // 初始化view
        scanningPb = (ProgressBar)findViewById(R.id.pb_activity_scanning_song_scanning);
        scanFinishIv = (ImageView) findViewById(R.id.iv_activity_scanning_song_scanFinish);
        scanInfoTv = (TextView) findViewById(R.id.tv_activity_scanning_song_scanInfo);
        progressBar = (ProgressBar)findViewById(R.id.pb_activity_scanning_song_song_num);
        scanFinishButton = (Button) findViewById(R.id.btn_activity_scanning_song_scanFinish);
        scanFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//点击扫描完成按钮即可关闭界面
            }
        });
        songScanHandler = new SongScanHandler(this);
        // 扫描音乐
        scanSong();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //执行音乐扫描功能
    private void scanSong(){
        ScanThread scanThread = new ScanThread(songScanHandler);
        scanThread.start();
    }

    //音乐扫描开始时，该方法将被"Handler"回调
    private void onScanStart(ScanInfo scanInfo){
        scanFinishIv.setVisibility(View.GONE);
        scanFinishButton.setVisibility(View.GONE);
    }

    //更新扫描的信息时，该方法将被"Handler"回调
    private void onUpdateScanInfo(ScanInfo scanInfo){
        progressBar.setMax(scanInfo.getSongSum());
        //更新正扫描的歌曲绝对路径
        scanInfoTv.setText(scanInfo.getAbsolutePath());
        //更新进度条
        progressBar.setProgress(scanInfo.getCurrentNum());
    }

    //音乐扫描结束时，该方法被"Handler"回调
    private void onScanFinish(ScanInfo scanInfo){
        scanFinishIv.setVisibility(View.VISIBLE);
        scanInfoTv.setText("已扫描到"+progressBar.getMax()+"首歌曲");
        progressBar.setVisibility(View.GONE);
        scanFinishButton.setVisibility(View.VISIBLE);
//        scanningIv.setVisibility(View.GONE);
        scanningPb.setVisibility(View.INVISIBLE);
        //修改页面返回标记
        setResult(RESULT_OK);
    }

    //扫描音乐时接收子线程信息用的"Handler"
    private static class SongScanHandler extends Handler{

        private WeakReference<ScanningSongActivity> weakReference;

        public SongScanHandler(ScanningSongActivity scanningSongActivity){
            weakReference = new WeakReference<>(scanningSongActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            ScanningSongActivity scanningSongActivity = weakReference.get();
            if(scanningSongActivity == null){
                super.handleMessage(msg);
                return;
            }
            if(msg.what == HANDLER_WHAT_START_SCAN){
                //如果收到开始扫描的信息了
                scanningSongActivity.onScanStart((ScanInfo) msg.obj);
            }else if(msg.what == HANDLER_WHAT_UPDATE_SCAN_INFO){
                //如果收到更新扫描信息
                scanningSongActivity.onUpdateScanInfo((ScanInfo) msg.obj);
            }else if(msg.what == HANDLER_WHAT_FINISH_SCAN){
                //如果收到扫描结束的信息了
                scanningSongActivity.onScanFinish((ScanInfo) msg.obj);
            }
            super.handleMessage(msg);
        }
    }
}