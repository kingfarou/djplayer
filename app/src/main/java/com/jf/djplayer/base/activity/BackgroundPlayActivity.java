package com.jf.djplayer.base.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.jf.djplayer.backgroundplay.PlayerService;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.interfaces.PlayController;

import java.util.List;

/**
 * Created by Kingfar on 2017/11/9.
 * 具有控制后台播放服务功能的Activity，
 * 任意Activity如果想要控制后台播放服务，应当继承该Activity
 */

public class BackgroundPlayActivity extends BaseActivity
        implements ServiceConnection, PlayController{

    private PlayerService playerService;  // 管理后台音乐播放用的服务

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    // 绑定后台音乐播放服务
    private void bindService(){
        // 绑定后台音乐播放服务
        Intent startService = new Intent(this,PlayerService.class);
        bindService(startService, this, BIND_AUTO_CREATE);
    }

    /****************ServiceConnection接口实现****************/
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        playerService = ((PlayerService.PlayerServiceBinder)service).getPlayerService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
    /****************ServiceConnection接口实现****************/

    /****************PlayController接口实现****************/
    @Override
    public void play(List<Song> songInfoList, int position) {

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

    @Override
    public void seekTo(int position) {
        playerService.seekTo(position);
    }
    /****************PlayController接口实现****************/

}
