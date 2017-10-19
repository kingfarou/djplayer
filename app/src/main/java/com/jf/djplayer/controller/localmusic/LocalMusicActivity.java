package com.jf.djplayer.controller.localmusic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jf.djplayer.R;
import com.jf.djplayer.base.activity.BaseActivity;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.service.PlayerService;
import com.jf.djplayer.widget.TitleBar;
import com.viewpagerindicator.TabPageIndicator;

import java.util.List;

/**
 * 本地音乐
 */
public class LocalMusicActivity extends BaseActivity implements PlayController, ServiceConnection,
        TitleBar.OnTitleClickListener, TitleBar.OnSearchClickListener, TitleBar.OnMenuClickListener{

    private ViewPager viewPager;
    private TabPageIndicator tabPageIndicator;
    protected LocalMusicViewPagerAdapter localMusicViewPagerAdapter;
    private PlayerService playerService;  // 管理后台音乐播放用的服务

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
        initTitleBar();
        initViewPager();
        bindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }

    // 初始化标题栏
    private void initTitleBar(){
        TitleBar titleBar = (TitleBar)findViewById(R.id.title_bar_activity_local_music);
        titleBar.setTitleText("本地音乐");
        titleBar.setOnTitleClickListener(this);
        titleBar.setOnSearchClickListener(this);
        titleBar.setOnMenuClickListener(this);
    }

    // 初始化ViewPager
    private void initViewPager(){
        //"ViewPager"初始化
        viewPager = (ViewPager)findViewById(R.id.vp_activity_local_music);
        localMusicViewPagerAdapter =
                new LocalMusicViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(localMusicViewPagerAdapter);

        // 初始化TabPageIndicator
        tabPageIndicator = (TabPageIndicator)findViewById(R.id.tab_page_indicator_activity_local_music);
        tabPageIndicator.setViewPager(viewPager);
    }

    // 绑定后台音乐播放服务
    private void bindService(){
        // 绑定后台音乐播放服务
        Intent startService = new Intent(this,PlayerService.class);
        bindService(startService, this, BIND_AUTO_CREATE);
    }

    /****************ServiceConnection方法实现****************/
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        playerService = ((PlayerService.PlayerServiceBinder)iBinder).getPlayerService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
    /****************ServiceConnection方法实现****************/

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

    /****************TitleBar接口方法实现****************/
    @Override
    public void onMenuClick(View menuView) {
        int currentItem = viewPager.getCurrentItem();
        Fragment fragment = localMusicViewPagerAdapter.getItem(currentItem);
        if(currentItem == 0){
            ((SongListFragment)fragment).getListViewPopupWindow().showAsDropDown(menuView, 0, 0);
        }else if(currentItem == 1){
//            ((SingerListFragment)fragment).getListViewPopupWindow();
        }else if(currentItem == 2){
//            ((AlbumListFragment)fragment).getListViewPopupWindow();
        }else{
//            ((FolderListFragment)fragment).getListViewPopupWindow();
        }
    }

    @Override
    public void onSearchClick(View SearchView) {

    }

    @Override
    public void onTitleClick(View titleView) {
        finish();
    }
    /****************TitleBar接口方法实现****************/

}
