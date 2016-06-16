package com.jf.djplayer.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.jf.djplayer.R;
import com.jf.djplayer.base.baseactivity.BaseActivity;
import com.jf.djplayer.module.Song;
import com.jf.djplayer.interfaces.FragmentChanger;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.service.PlayerService;


import java.util.List;

/**
 * 主界面-外层容器
 */
public class MainActivity extends BaseActivity
        implements FragmentChanger, PlayController, ServiceConnection{

    private FragmentManager fragmentManager;
    //后台控制播放用的服务
    private PlayerService playerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initBeforeView() {
        //通过两个方式启动服务确保解绑之后服务不会关闭
        Intent startService = new Intent(this,PlayerService.class);
        startService(startService);
        bindService(startService, this, BIND_AUTO_CREATE);
    }

    @Override
    protected void initView() {
        //"fl_activity_main_fragment_container"里的"Fragment"必须是动态添加的，由于这些"Fragment"都会被动态的变更
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fl_activity_main_fragment_container,new MainFragment()).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    //重写该方法是为了"MainActivity"里的"Fragment"的"onActivityResult()"方法能够得到回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*"FragmentChanger"方法覆盖_start*/
    @Override
    public void replaceFragments(Fragment fragment) {
        //更改"fragmentManager"里的"fragment"
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left,
                R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        fragmentTransaction.replace(R.id.fl_activity_main_fragment_container,fragment).addToBackStack(null).commit();
    }

    @Override
    public void popFragments() {
        fragmentManager.popBackStack();
    }
    /*"FragmentChanger"方法覆盖_end*/

    /*"PlayController"方法覆盖_start*/
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
    /*"PlayController"方法覆盖_end*/

    /*"ServiceConnection"方法覆盖_start*/
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        playerService = ((PlayerService.PlayerServiceBinder)service).getPlayerService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
    /*"ServiceConnection"方法覆盖_end*/

    public void exitApp() {
        playerService.stopSelf();
        finish();
    }
}