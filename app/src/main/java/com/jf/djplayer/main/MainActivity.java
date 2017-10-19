package com.jf.djplayer.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.activity.BaseActivity;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.controller.localmusic.LocalMusicActivity;
import com.jf.djplayer.controller.myfavorite.MyFavoriteActivity;
import com.jf.djplayer.controller.recentlyplay.RecentlyPlayActivity;
import com.jf.djplayer.service.PlayerService;
import com.jf.djplayer.util.ToastUtil;
import com.jf.djplayer.view.TitleBar;


import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements
        PlayController, ServiceConnection, View.OnClickListener{

    //管理后台音乐播放用的服务
    private PlayerService playerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化服务，通过两个方式启动服务确保解绑之后服务不会关闭
        Intent startService = new Intent(this,PlayerService.class);
        startService(startService);
        bindService(startService, this, BIND_AUTO_CREATE);

        initView();
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

    private void initView(){
        //对标题栏的初始化
        TitleBar titleBar = (TitleBar)findViewById(R.id.fragmentTitleLinearLayout_fragment_mine);
        titleBar.setSearchVisibility(View.GONE);
        titleBar.setMenuVisibility(View.GONE);

        //各个按钮点击事件，分别是，本地音乐、我的最爱、我的下载、我的歌单、最近播放、随机播放
        findViewById(R.id.ll_fragment_mine_local_music).setOnClickListener(this);
        findViewById(R.id.btn_fragment_main_my_favorite).setOnClickListener(this);
        findViewById(R.id.btn_fragment_main_my_down).setOnClickListener(this);
        findViewById(R.id.btn_fragment_mine_song_menu).setOnClickListener(this);
        findViewById(R.id.btn_fragment_mine_recently_play).setOnClickListener(this);
        findViewById(R.id.imgBtn_fragment_mine_dice).setOnClickListener(this);

        // 设置歌曲数量
        int songNum = getIntent().getIntExtra(WelcomeActivity.KEY_CODE_SONG_NUM, 0);
        TextView songNumberTv = (TextView)findViewById(R.id.tv_fragment_main_song_num);
        songNumberTv.setText(songNum + "首歌曲");
    }

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

    /**
     * 退出App
     */
    public void exitApp() {
        playerService.stopSelf();
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.ll_fragment_mine_local_music){
            // 本地音乐按钮
            startActivity(new Intent(this, LocalMusicActivity.class));
        }else if(id == R.id.btn_fragment_main_my_favorite){
            // 我的最爱按钮
            startActivity(new Intent(this, MyFavoriteActivity.class));
        }else if(id == R.id.btn_fragment_main_my_down){
            // 我的下载按钮
            ToastUtil.showShortToast(this, "该功能还未实现");
        }else if(id == R.id.btn_fragment_mine_song_menu){
            // 我的歌单按钮
            ToastUtil.showShortToast(this, "该功能还未实现");
        }else if(id == R.id.btn_fragment_mine_recently_play){
            // 最近播放按钮
            startActivity(new Intent(this, RecentlyPlayActivity.class));
        }else if(id == R.id.imgBtn_fragment_mine_dice){
            // 随机播放按钮，界面上的那个色子
            ToastUtil.showShortToast(this, "该功能还未实现");
        }
    }
}