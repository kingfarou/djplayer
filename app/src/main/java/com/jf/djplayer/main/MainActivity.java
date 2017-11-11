package com.jf.djplayer.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.activity.BackgroundPlayActivity;
import com.jf.djplayer.datamanager.SongNumberLoader;
import com.jf.djplayer.controller.localmusic.LocalMusicActivity;
import com.jf.djplayer.controller.myfavorite.MyFavoriteActivity;
import com.jf.djplayer.controller.recentlyplay.RecentlyPlayActivity;
import com.jf.djplayer.util.ToastUtil;
import com.jf.djplayer.widget.TitleBar;


/**
 * 主界面
 */
public class MainActivity extends BackgroundPlayActivity implements View.OnClickListener{

    private TextView songNumberTv;  // 歌曲数量
    private SongNumberLoader songNumberLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initSongNumberLoader();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshSongNumber();
    }

    // 重写该方法是为了"MainActivity"里的"Fragment"的"onActivityResult()"方法能够得到回调
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
        // 歌曲数量
        songNumberTv = (TextView)findViewById(R.id.tv_fragment_main_song_num);
    }

    private void initSongNumberLoader(){
        songNumberLoader = new SongNumberLoader();
        songNumberLoader.setLoadListener(new SongNumberLoader.loadListener() {
            @Override
            public void onSuccess(int songNumber) {
                songNumberTv.setText(songNumber+"首歌曲");
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    // 刷新歌曲数量显示
    private void refreshSongNumber(){
        songNumberLoader.load();
    }

    /**
     * 退出App
     */
    public void exitApp() {
//        playerService.stopSelf();
//        finish();
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