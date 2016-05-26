package com.jf.djplayer.songplayinfo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jf.djplayer.module.SongInfo;
import com.jf.djplayer.base.baseactivity.BaseNoTitleActivity;
import com.jf.djplayer.R;
import com.jf.djplayer.adapter.SongPlayInfoAdapter;
import com.jf.djplayer.interfaces.PlayInfoObserver;
import com.jf.djplayer.interfaces.PlayInfoSubject;
import com.jf.djplayer.service.PlayerService;
import com.jf.djplayer.tool.RemindUiUpdateThread;
import com.jf.djplayer.tool.SendSongPlayProgress;
import com.jf.djplayer.tool.UserOptionTool;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.playertool.PlayerOperator;

import com.jf.djplayer.customview.FragmentTitleLayout;
import com.jf.djplayer.playertool.SingerPictureTools;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/4.
 * 播放信息"Activity"
 */
public class SongPlayInfoActivity extends BaseNoTitleActivity implements
        ServiceConnection ,SeekBar.OnSeekBarChangeListener,PlayInfoObserver,
        FragmentTitleLayout.FragmentTitleListener,View.OnClickListener{

    //根布局
    private LinearLayout ll_root_view;//这是当前活动布局文件的根容器

    //标题栏的相关控件
    private TextView tv_song_name;//歌手名字
    private TextView tv_singer_name;//歌手名字

//    private FragmentTitleLayout FragmentTitleLayout;//标题
    //进度栏的相关控件
    private TextView tv_current_time;//显示当前播放时间
    private SeekBar seekBar;//音乐播放的滚动条
    private TextView tv_total_time;//显示当前歌曲的总时长

    //底边控制栏的控件
    private ImageView iv_play_mode;//这个表示播放模式
    private ImageView iv_collection;//显示用户是否收藏当前歌曲
    private ImageView iv_play_or_pause;//播放以及暂停按钮

    //其他变量
    private ViewPager viewPager;
    private SongInfo lastSongInfo;//记录最后一次播放的歌

    private PlayInfoSubject mPlayInfoSubject;//持有所播放歌曲信息的主题
    private PlayerService playerService;//指向被绑定的服务用的
    private Handler updateProgressHandler;
    private RemindUiUpdateThread remindUiUpdateThread;
//    private SendSongPlayProgress sendSongPlayProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_song_play_info;
    }

    @Override
    protected void initView() {
        initWidget();//调用方法对视图初始化
        initViewPager();//对ViewPager做初始化
        initHandler();//对Handler做初始化
    }

    @Override
    protected void initExtrasBeforeView() {
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
//        获取更新播放信息用的主题
        mPlayInfoSubject = PlayerOperator.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //将自己注册成为观察者
        mPlayInfoSubject.registerObserver(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        mPlayInfoSubject.removeObserver(this);//从主题里移除当前活动

        // 如果更新进度的子线程还在工作将其关闭
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

    //这里获取各个控件
    private void initWidget() {
        //根布局
        ll_root_view = (LinearLayout)findViewById(R.id.ll_root_view);

        //标题栏控件初始化
        tv_song_name = (TextView)findViewById(R.id.tv_activity_song_play_info_song_name);
        tv_singer_name = (TextView)findViewById(R.id.tv_activity_song_play_info_singer_name);
        findViewById(R.id.iv_activity_song_play_info_return).setOnClickListener(this);//左上角的返回按钮设置监听

        //进度栏控件初始化
        tv_current_time = (TextView) findViewById(R.id.tv_activity_song_play_info_current_time);//显示当前播放时间
        seekBar = (SeekBar) findViewById(R.id.sb_activity_song_play_info);//一个可调节进度条
        seekBar.setOnSeekBarChangeListener(this);
        tv_total_time = (TextView) findViewById(R.id.tv_total_time);//显示歌曲总的时长

        //底边控制栏控件初始化
        findViewById(R.id.iv_previous_song).setOnClickListener(this);//播放前一曲的按钮
        findViewById(R.id.iv_next_song).setOnClickListener(this);//播放下一曲的按钮
        iv_play_mode = (ImageView)findViewById(R.id.iv_activity_song_play_info_play_mode);//这是播放模式按钮
        iv_collection = (ImageView) findViewById(R.id.iv_collection);//收藏或者取消收藏
        iv_play_or_pause = (ImageView)findViewById(R.id.iv_play_or_pause);//这是播放以及暂停按钮
        iv_play_mode.setOnClickListener(this);
        iv_collection.setOnClickListener(this);
        iv_play_mode.setImageResource(getPictureFromPlayMode());//根据播放模式设置不同图片
        iv_play_or_pause.setOnClickListener(this);
    }

    private void initHandler(){
        updateProgressHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == SendSongPlayProgress.updateProgress) {
                    seekBar.setProgress(mPlayInfoSubject.getCurrentPosition());
                    tv_current_time.setText(getTime(mPlayInfoSubject.getCurrentPosition()));
                }
                super.handleMessage(msg);
            }
        };
    }

//    根据播放模式返回对应图片ID
    private int getPictureFromPlayMode(){
        int playMode = new UserOptionTool(this).getPlayModes();
        if(playMode == UserOptionTool.PLAY_MODE_ORDER){//如果需要顺序播放
            return R.drawable.ic_activity_play_song_info_orderplay;
        }else if(playMode == UserOptionTool.PLAY_MODE_RANDOM){//如果需要随机播放
            return R.drawable.ic_activity_play_song_info_random;
        }else if(playMode == UserOptionTool.PLAY_MODE_CIRCULATE){//如果需要列表循环
            return R.drawable.ic_activity_play_song_info_listcirculate;
        }else{//如果需要单曲循环
            return R.drawable.ic_activity_play_song_info_singlecirculate;
        }
    }

    private void initViewPager(){
        viewPager = (ViewPager)findViewById(R.id.vp_activity_song_play_info);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new TwoLineLyricFragment());
        fragmentList.add(new ScrollLyricsFragment());
        viewPager.setAdapter(new SongPlayInfoAdapter(getSupportFragmentManager(), fragmentList));
    }


    //传入以毫秒表示的时间
    //返回如下格式时间：00:00(分钟：秒钟)
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
        switch(v.getId()) {
            //左上角的返回按钮
            case R.id.iv_activity_song_play_info_return:finish();break;
            //点击修改播放模式
            case R.id.iv_activity_song_play_info_play_mode:setNewPlayMode();break;
            //点击播放前一首歌
            case R.id.iv_previous_song:playerService.previousSong();break;
            //点击播放或者暂停
            case R.id.iv_play_or_pause:
                if (playerService.isPlaying()) {
                    playerService.pause();
                } else {
                    playerService.play();
                }
                break;
            //点击播放下一首歌
            case R.id.iv_next_song:playerService.nextSong();break;
            //收藏或者取消收藏
            case R.id.iv_collection:
                SongInfoOpenHelper songInfoOpenHelper = new SongInfoOpenHelper(SongPlayInfoActivity.this);
                if (lastSongInfo.getCollection() == 0) {
                    lastSongInfo.setCollection(1);
                    songInfoOpenHelper.updateCollection(lastSongInfo, 1);
                    iv_collection.setImageResource(R.drawable.activity_song_play_info_collection);
                } else {
                    lastSongInfo.setCollection(0);
                    songInfoOpenHelper.updateCollection(lastSongInfo, 0);
                    iv_collection.setImageResource(R.drawable.activity_song_play_info_no_collection);
                }
                break;
            default:break;
        }

    }//onClick

//    根据当前播放模式设置新的播放模式以及图片
    private void setNewPlayMode(){
        UserOptionTool userOptionTool = new UserOptionTool(SongPlayInfoActivity.this);
        int playMode = userOptionTool.getPlayModes();
//            根据当前播放模式写入新的播放模式
        switch(playMode){
            case UserOptionTool.PLAY_MODE_ORDER:
                userOptionTool.setPlayModes(UserOptionTool.PLAY_MODE_RANDOM);
                break;
            case UserOptionTool.PLAY_MODE_RANDOM:
                userOptionTool.setPlayModes(UserOptionTool.PLAY_MODE_CIRCULATE);
                break;
            case UserOptionTool.PLAY_MODE_CIRCULATE:
                userOptionTool.setPlayModes(UserOptionTool.PLAY_MODE_SINGLE_CIRCLUATE);
                break;
            case UserOptionTool.PLAY_MODE_SINGLE_CIRCLUATE:
                userOptionTool.setPlayModes(UserOptionTool.PLAY_MODE_ORDER);
                break;
            default:break;

        }
        //根据新的播放模式更换图片
        iv_play_mode.setImageResource(getPictureFromPlayMode());
    }

//    seekBar的监听
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
            tv_current_time.setText(getTime(progress));//进度条拖动的同时改变界面上的时间显示
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
        tv_current_time.setText(getTime(progress));//设置当前播放时长
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
        //根据收藏与否进行不同设置
        if(currentPlaySongInfo.getCollection() == 1){
            iv_collection.setImageResource(R.drawable.activity_song_play_info_collection);
        }else {
            iv_collection.setImageResource(R.drawable.activity_song_play_info_no_collection);
        }
    }//updatePlayInfo

//    用来更换新的歌曲信息
    private void setNewPlayInfo(SongInfo currentPlaySongInfo){
//        设置所播放的歌曲名字
//        FragmentTitleLayout.setTitleText(currentPlaySongInfo.getSongName());
        tv_song_name.setText(currentPlaySongInfo.getSongName());
        tv_singer_name.setText(currentPlaySongInfo.getSingerName());
//            设置当前歌手图片
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            SingerPictureTools singerPictureTools = new SingerPictureTools(currentPlaySongInfo.getSingerName());
            if(singerPictureTools.hasSingerPicture()) {
//                ll_root_view.setBackground(singerPictureTools.getSingerPicture());
                ll_root_view.setBackgroundDrawable(singerPictureTools.getSingerPicture());
            }
        }
//            设置进度条最大值
        seekBar.setMax(currentPlaySongInfo.getSongDuration());
//            设置当前播放时长何总时长
        tv_total_time.setText(getTime(currentPlaySongInfo.getSongDuration()));
    }

    //如果歌曲正在播放要进行的相关设置
    private void playSettings(){
        iv_play_or_pause.setImageResource(R.drawable.activity_song_play_info_pause);
//                开启一个新的线程刷新的进度条
        if(remindUiUpdateThread == null){
            remindUiUpdateThread = new RemindUiUpdateThread(updateProgressHandler,100);
            remindUiUpdateThread.start();
        }
    }

//    如果歌曲没在播放要进行的相关设置
    private void notPlaySettings(){
        iv_play_or_pause.setImageResource(R.drawable.activity_song_play_info_play);
//        如果更新播放进度用的县城还在运行，将其关闭
        if(remindUiUpdateThread !=null){
            remindUiUpdateThread.run = false;
            remindUiUpdateThread = null;
        }
    }
}