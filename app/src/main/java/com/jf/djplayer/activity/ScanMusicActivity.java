package com.jf.djplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.jf.djplayer.R;
import com.jf.djplayer.base.activity.BaseTitleActivity;

/**
 * Created by Administrator on 2015/8/12.
 * 修改：由原来的继承自"Activity"改成"BaseTitleActivity"
 */
public class ScanMusicActivity extends BaseTitleActivity implements OnClickListener{

    private Button keyScanMusicButton = null;
    private Button customScanButton = null;
    private Button wifiButton = null;
    public static final int CUSTOM_SCAN_SONG_REQUEST_CODE = 1<<1;//自定义扫描方式请求码
    public static final int ONE_KEY_SCAN_REQUEST_CODE = 1<<2;//一键扫描的请求码
    private Button scanSettingsButton = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        以下代码分别前移到对应的工厂方法里面
//        setContentView(R.layout.activity_scan_music);
//        keyScanMusicButton = (Button)findViewById(R.id.btn_activity_scan_music_key_scan);
//        customScanButton = (Button)findViewById(R.id.btn_activity_scan_song_custom_scan);
//        wifiButton = (Button)findViewById(R.id.btn_activity_scan_music_wifi);
//        scanSettingsButton = (Button)findViewById(R.id.btn_activity_scan_music_key_scan);
//
//        keyScanMusicButton.setOnClickListener(this);
//        customScanButton.setOnClickListener(this);
//        wifiButton.setOnClickListener(this);
//        scanSettingsButton.setOnClickListener(this);

    }

    @Override
    protected void doSetContentView() {
        setContentView(R.layout.activity_scan_music);
    }

    @Override
    protected void viewInit() {
        keyScanMusicButton = (Button)findViewById(R.id.btn_activity_scan_music_key_scan);
        customScanButton = (Button)findViewById(R.id.btn_activity_scan_song_custom_scan);
        wifiButton = (Button)findViewById(R.id.btn_activity_scan_music_wifi);
        scanSettingsButton = (Button)findViewById(R.id.btn_activity_scan_music_key_scan);

        keyScanMusicButton.setOnClickListener(this);
        customScanButton.setOnClickListener(this);
        wifiButton.setOnClickListener(this);
        scanSettingsButton.setOnClickListener(this);
    }

    @Override
    protected void extrasInit() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_activity_scan_music_key_scan://这是一件扫描按钮
                Log.i("test","一键扫描");
                break;
            case R.id.btn_activity_scan_song_custom_scan://这是自定义扫描的按钮
                Intent selectedFileIntent = new Intent(this,SelectedFileActivity.class);
                startActivityForResult(selectedFileIntent, CUSTOM_SCAN_SONG_REQUEST_CODE);
                break;
            case R.id.btn_activity_scan_music_wifi://这是wi-fi传歌
                break;
            case R.id.btn_activity_scan_music_scan_settings://这是扫描设置按钮
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==CUSTOM_SCAN_SONG_REQUEST_CODE){
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
