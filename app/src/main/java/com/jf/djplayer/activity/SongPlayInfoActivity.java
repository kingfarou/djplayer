package com.jf.djplayer.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jf.djplayer.SongInfo;
import com.jf.djplayer.fragment.TwoLineLyricFragment;
import com.jf.djplayer.R;
import com.jf.djplayer.adapter.SongPlayInfoAdapter;
import com.jf.djplayer.fragment.ScrollLyricsFragment;
import com.jf.djplayer.interfaces.PlayInfoObserver;
import com.jf.djplayer.interfaces.PlayInfoSubject;
import com.jf.djplayer.service.PlayerService;
import com.jf.djplayer.tool.RemindUiUpdateThread;
import com.jf.djplayer.tool.SendSongPlayProgress;
import com.jf.djplayer.tool.UserOptionTool;
import com.jf.djplayer.tool.database.SongInfoOpenHelper;
import com.jf.djplayer.tool.playertool.PlayerOperator;

import com.jf.djplayer.customview.FragmentTitleLayout;
import com.jf.djplayer.tool.playertool.SingerPictureTools;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/4.
 * 这是专门用于显示音乐播放信息窗体
 */
public class SongPlayInfoActivity extends FragmentActivity implements
        ServiceConnection ,SeekBar.OnSeekBarChangeListener,PlayInfoObserver,
        FragmentTitleLayout.FragmentTitleListener,View.OnClickListener{

    private FragmentTitleLayout FragmentTitleLayout;//标题
    private PlayInfoSubject playInfoSubject;//主题
    private TextView currentTime;//显示当前播放时间
    private ViewPager viewPager;
    private SongInfo lastSongInfo;//记录最后一次播放的歌
    private SeekBar seekBar;//音乐播放的滚动条
    private TextView totalTime;//显示当前歌曲的总时长
    private ImageView circulationsMode;//显示当前播放模式
    private ImageView playOrPaused;//播放以及暂停按钮
    private ImageView collectionIv;//显示用户是否收藏当前歌曲
    private ImageView playModeIv;//这个表示播放模式
    private PlayerService playerService;//指向被绑定的服务用的
    private LinearLayout singerPictureLinearLayout;//这是当前活动布局文件的根容器
    private Handler updateProgressHandler;
    private RemindUiUpdateThread remindUiUpdateThread;
//    private SendSongPlayProgress sendSongPlayProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//先去掉ActionBar
        setContentView(R.layout.activity_song_play_info);
//        绑定控制播放用的服务
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
//        获取更新播放信息用的主题
        playInfoSubject = PlayerOperator.getInstance();
        viewInit();//调用方法对视图初始化
        handlerInits();//对Handler做初始化
        viewPagerInits();//对ViewPager做初始化
    }

//    这里注册为观察者
//    可以保证不论活动是新启动
//    还是进入后台之后重启
//    都能获取主题里的信息
    @Override
    protected void onStart() {
        super.onStart();
        playInfoSubject.registerObserver(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        playInfoSubject.removeObserver(this);//从主题里移除当前活动
//        如果更新进度德子线程还在工作将其关闭
        if(remindUiUpdateThread !=null){
            remindUiUpdateThread.run = false;
            remindUiUpdateThread = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);//取消对服务的绑定
    }



    /*
    这里获取各个控件
     */
    private void viewInit() {
        currentTime = (TextView) findViewById(R.id.tv_activity_song_playInfo_currenTime);//显示当前播放时间
        singerPictureLinearLayout = (LinearLayout)findViewById(R.id.ll_activity_song_play_info);
        FragmentTitleLayout = (FragmentTitleLayout)findViewById(R.id.fragmentTitleLinearLayout_activity_song_play_info);
        seekBar = (SeekBar) findViewById(R.id.sb_activity_song_play_info);//一个可调节进度条
        totalTime = (TextView) findViewById(R.id.tv_activity_song_playInfo_totalTime);//显示歌曲总的时长
        circulationsMode = (ImageView) findViewById(R.id.iv_activity_song_playInfo_play_mode);
        findViewById(R.id.iv_activity_song_playInfo_previousSong).setOnClickListener(this);//播放前一曲的按钮
        findViewById(R.id.iv_activity_song_playInfo_nextSong).setOnClickListener(this);//播放下一曲的按钮
        playOrPaused = (ImageView) findViewById(R.id.activity_song_play_info_playOrPaused);//播放以及控制按钮
        collectionIv = (ImageView) findViewById(R.id.iv_activity_song_playInfo_collection);//收藏或者取消收藏
        playModeIv = (ImageView)findViewById(R.id.iv_activity_song_playInfo_play_mode);
//        各个空间设置好监听器
        FragmentTitleLayout.setItemClickListener(this);
        playOrPaused.setOnClickListener(this);
        collectionIv.setOnClickListener(this);
        circulationsMode.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
//        根据播放模式设置不同图片
        playModeIv.setImageResource(getPictureFromPlayMode());
    }

    private void handlerInits(){
        updateProgressHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == SendSongPlayProgress.updateProgress) {
                    seekBar.setProgress(playInfoSubject.getCurrentPosition());
                    currentTime.setText(getTime(playInfoSubject.getCurrentPosition()));
                }
                super.handleMessage(msg);
            }
        };
    }//handlerInits

//    根据播放模式返回对应图片ID
    private int getPictureFromPlayMode(){
        int playMode = new UserOptionTool(this).getPlayModes();
        if(playMode == UserOptionTool.PLAY_MODE_ORDER){//如果需要顺序播放
            return R.drawable.ic_activity_play_song_info_orderplay;
        }
        if(playMode == UserOptionTool.PLAY_MODE_RANDOM){//如果需要随机播放
            return R.drawable.ic_activity_play_song_info_random;
        }
        if(playMode == UserOptionTool.PLAY_MODE_CIRCULATE){//如果需要列表循环
            return R.drawable.ic_activity_play_song_info_listcirculate;
        }
        //如果需要单曲循环
        return R.drawable.ic_activity_play_song_info_singlecirculate;
    }

    private void viewPagerInits(){
        viewPager = (ViewPager)findViewById(R.id.vp_activity_song_play_info);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new TwoLineLyricFragment());
        fragmentList.add(new ScrollLyricsFragment());
        viewPager.setAdapter(new SongPlayInfoAdapter(getSupportFragmentManager(),fragmentList));
    }


    //    传入以毫秒表示的时间
//    返回如下格式时间：00:00(分钟：秒钟)
    private String getTime(int time){
        int minutes = time/1000/60;//获取里面的分钟数
        int second = time/1000%60;//获取里面的秒钟数(除分钟外剩余的秒钟书)
        int minutesTensDigit = minutes/10;//分钟的十位数表示
        int minuteUnitsDigit = minutes%10;//分钟的个位数表示
        int secondTensDigit = second/10;//秒钟的十位数来的
        int secondUnitsDigit = second%10;//秒钟的个位数表示
        return minutesTensDigit+""+minuteUnitsDigit+":"+secondTensDigit+""+secondUnitsDigit;
    }


//    覆盖与服务相关的方法
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
//        Log.i("test","绑定服务");
        this.playerService = ((PlayerService.PlayerServiceBinder) service).getPlayerService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

//    FragmentTitleListener
//    里面几个监听方法覆盖
    @Override
    public void onTitleClick() {
        finish();//如果标题被按下了结束活动
    }

    @Override
    public void onSearchIvOnclick() {

    }

    @Override
    public void onMoreIvOnclick() {

    }

//    点击事件监听方法
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.iv_activity_song_playInfo_play_mode){//如果用户修改播放模式
            setNewPlayMode();
        } else if(id == R.id.iv_activity_song_playInfo_previousSong){//如果用户点击前面一首歌曲
            playerService.previousSong();
        }else if(id == R.id.activity_song_play_info_playOrPaused){//如果用户点击播放或者暂停
            if(playerService.isPlaying()){
                playerService.pause();
            }else{
                playerService.play();
            }
        }else if(id == R.id.iv_activity_song_playInfo_nextSong){//如果用户点击了下一曲
            playerService.nextSong();
        }else if(id == R.id.iv_activity_song_playInfo_collection){//如果用户点击收藏或者取消收藏
            SongInfoOpenHelper songInfoOpenHelper = new SongInfoOpenHelper(SongPlayInfoActivity.this,1);
            if(lastSongInfo.getCollection()==0){
                lastSongInfo.setCollection(1);
                songInfoOpenHelper.updateCollection(lastSongInfo,1);
            }else{
                lastSongInfo.setCollection(0);
                songInfoOpenHelper.updateCollection(lastSongInfo,0);
            }
        }

    }//onClick

//    根据当前播放模式设置新的播放模式以及图片
    private void setNewPlayMode(){
        UserOptionTool userOptionTool = new UserOptionTool(SongPlayInfoActivity.this);
        int playMode = userOptionTool.getPlayModes();
//            根据当前播放模式写入新的播放模式
        if(playMode == UserOptionTool.PLAY_MODE_ORDER){
            userOptionTool.setPlayModes(UserOptionTool.PLAY_MODE_RANDOM);
        } else if(playMode == UserOptionTool.PLAY_MODE_RANDOM){
            userOptionTool.setPlayModes(UserOptionTool.PLAY_MODE_CIRCULATE);
        } else if(playMode == UserOptionTool.PLAY_MODE_CIRCULATE){
            userOptionTool.setPlayModes(UserOptionTool.PLAY_MODE_SINGLE_CIRCLUATE);
        }else if(playMode == UserOptionTool.PLAY_MODE_SINGLE_CIRCLUATE){
            userOptionTool.setPlayModes(UserOptionTool.PLAY_MODE_ORDER);
        }
//            根据新的播放模式更换图片
        playModeIv.setImageResource(getPictureFromPlayMode());
    }

//    seekBar的监听
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
            currentTime.setText(getTime(progress));//进度条拖动的同时改变界面上的时间显示
        }
    }

//    用户接触进度条的那一瞬间
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if(remindUiUpdateThread !=null) {
            remindUiUpdateThread.run = false;//滑块滑动过程当中子线程应停止他的工作
            remindUiUpdateThread = null;
        }
    }

//    当用户停止拖动进度条
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
//        重新启动子线程的更新工作
        if(remindUiUpdateThread == null){
            remindUiUpdateThread = new RemindUiUpdateThread(updateProgressHandler,100);
            remindUiUpdateThread.start();
        }
//        设置当前播放进度
        playerService.seekTo(seekBar.getProgress());
//        circulations = true;

    }


    //这个是对观察者接口的方法实现
    @Override
    public void updatePlayInfo(final SongInfo currentPlaySongInfo, final boolean isPlaying, int progress) {
//        如果没有任何歌曲被选中的直接返回
        if(currentPlaySongInfo == null) {
            return;
        }
        currentTime.setText(getTime(progress));//设置当前播放时长
        seekBar.setProgress(progress);//设置当前播放进度
//        如果原来没有播放任何歌曲，或者原来所播放的歌曲和现在的不同
        if(lastSongInfo==null||!lastSongInfo.getSongAbsolutePath().equals(currentPlaySongInfo.getSongAbsolutePath())){
            setNewPlayInfo(currentPlaySongInfo);//设置新的歌曲信息
            lastSongInfo = currentPlaySongInfo;//更新当前播放的那首歌
        }
//        根据播放与否进行不同设置
        if(isPlaying) {
            playSettings();
        }
        else {
            notPlaySettings();
        }//if(isPlaying)
    }//updatePlayInfo

//    用来更换新的歌曲信息
    private void setNewPlayInfo(SongInfo currentPlaySongInfo){
//        设置所播放的歌曲名字
        FragmentTitleLayout.setTitleText(currentPlaySongInfo.getSongName());
//            设置当前歌手图片
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            SingerPictureTools singerPictureTools = new SingerPictureTools(currentPlaySongInfo.getSongSinger());
            if(singerPictureTools.hasSingerPicture()) singerPictureLinearLayout.setBackground(singerPictureTools.getSingerPicture());
        }
//            设置进度条最大值
        seekBar.setMax(currentPlaySongInfo.getSongDuration());
//            设置当前播放时长何总时长
        totalTime.setText(getTime(currentPlaySongInfo.getSongDuration()));
    }

    //如果歌曲正在播放要进行的相关设置
    private void playSettings(){
        playOrPaused.setImageResource(R.drawable.activity_song_play_info_pause);
//                开启一个新的线程刷新的进度条
        if(remindUiUpdateThread == null){
            remindUiUpdateThread = new RemindUiUpdateThread(updateProgressHandler,100);
            remindUiUpdateThread.start();
        }
    }

//    如果歌曲没在播放要进行的相关设置
    private void notPlaySettings(){
        playOrPaused.setImageResource(R.drawable.activity_song_play_info_play);
//        如果更新播放进度用的县城还在运行，将其关闭
        if(remindUiUpdateThread !=null){
            remindUiUpdateThread.run = false;
            remindUiUpdateThread = null;
        }
    }
}