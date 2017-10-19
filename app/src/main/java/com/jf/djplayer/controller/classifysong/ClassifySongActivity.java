package com.jf.djplayer.controller.classifysong;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.jf.djplayer.R;
import com.jf.djplayer.base.activity.BaseActivity;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.service.PlayerService;
import com.jf.djplayer.widget.TitleBar;

import java.util.List;

/**
 * 分类歌曲显示界面，比如显示某一歌手所有歌曲，某个专辑所有歌曲，某个路径下的所有歌曲
 */
public class ClassifySongActivity extends BaseActivity implements
        PlayController, ServiceConnection, ClassifySongListFragment.ClassifySongListCallback{

    public static final String TYPE_NAME = "TYPE_NAME";   // 类型名字，比如“歌手”、“专辑”、“文件夹”
    public static final String TYPE_VALUE = "TYPE_VALUE"; // 类型的值，比如某个歌手名字，专辑名字

    // 类型名字可选值
    /** 歌手类型*/
    public static final String TYPE_NAME_SINGER = SongInfoOpenHelper.artist;
    /** 专辑类型*/
    public static final String TYPE_NAME_ALBUM = SongInfoOpenHelper.album;
    /** 文件夹类型*/
    public static final String TYPE_NAME_FOLDER = SongInfoOpenHelper.folderPath;

    private String typeName = "";
    private String typeValue = "";

    private PlayerService playerService;                  // 管理后台音乐播放用的服务

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取要现实的类型名字和值
        Intent intent = getIntent();
        typeName = intent.getStringExtra(TYPE_NAME);
        typeValue = intent.getStringExtra(TYPE_VALUE);
        // init view
        setContentView(R.layout.activity_classify_song);
        initView();
        bindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    private void initView(){
        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar_activity_classify_song);
        titleBar.setTitleText(typeValue);
    }

    private void bindService(){
        // 绑定后台音乐播放服务
        Intent startService = new Intent(this,PlayerService.class);
        bindService(startService, this, BIND_AUTO_CREATE);
    }

    /****************PlayController接口方法实现*****************/
    @Override
    public void play(List<Song> songInfoList,int position) {
//        playerService.play(_songInfoList, position);
    }

    @Override
    public void play(String playListName, List<Song> songList, int playPosition) {
        playerService.play(playListName, songList, playPosition);
    }

    @Override
    public void play() {
        playerService.play();
    }

    @Override
    public boolean isPlaying() {
        return playerService.isPlaying();
    }

    @Override
    public void pause() {
        playerService.pause();
    }

    @Override
    public void nextSong() {
        playerService.nextSong();
    }

    @Override
    public void previousSong() {
        playerService.previousSong();
    }

    @Override
    public void stop() {

    }
    /****************PlayController接口方法实现****************/

    /****************ServiceConnection方法实现****************/
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        playerService = ((PlayerService.PlayerServiceBinder)iBinder).getPlayerService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
    /****************ServiceConnection方法实现****************/

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public String getTypeValue() {
        return typeValue;
    }
}
