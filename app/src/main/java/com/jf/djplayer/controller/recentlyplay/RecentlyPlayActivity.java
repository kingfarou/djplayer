package com.jf.djplayer.controller.recentlyplay;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.jf.djplayer.R;
import com.jf.djplayer.base.activity.BaseActivity;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.service.PlayerService;
import com.jf.djplayer.view.TitleBar;

import java.util.List;

/**
 * 最近播放
 */
public class RecentlyPlayActivity extends BaseActivity implements PlayController, ServiceConnection{

    //管理后台音乐播放用的服务
    private PlayerService playerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_play);
        initView();
        bindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    private void initView(){
        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar_activity_recently_play);
        titleBar.setTitleText("最近播放");
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

}
