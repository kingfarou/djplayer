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

import com.jf.djplayer.base.baseactivity.BaseActivity;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.module.SongInfo;
import com.jf.djplayer.R;
import com.jf.djplayer.adapter.SongPlayInfoAdapter;
import com.jf.djplayer.interfaces.PlayInfoObserver;
import com.jf.djplayer.interfaces.PlayInfoSubject;
import com.jf.djplayer.module.SongPlayInfo;
import com.jf.djplayer.service.PlayerService;
import com.jf.djplayer.util.UserOptionPreferences;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.playertool.PlayerOperator;

import com.jf.djplayer.customview.CustomTitles;
import com.jf.djplayer.playertool.SingerPictureTools;

import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/4.
 * 播放信息"Activity"
 */
public class SongPlayInfoActivity extends BaseActivity implements
        ServiceConnection ,SeekBar.OnSeekBarChangeListener,PlayInfoObserver,
        CustomTitles.FragmentTitleListener,View.OnClickListener,PlayController{

    //布局文件的根布局，用来显示歌手图片
    private LinearLayout ll_root_view;

    //标题栏的相关控件
    private TextView tv_song_name;//歌手名字
    private TextView tv_singer_name;//歌手名字

    //进度显示相关控件
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

    private PlayInfoSubject playInfoSubject;//持有所播放歌曲信息的主题
    private PlayerService playerService;//后台歌曲播放服务
    private UpdatePlayInfoHandler updatePlayInfoHandler;

    //这个是"message.what"标记。用来标记UI更新事件
    private final int WHAT_UPDATE_UI = 0x0004;
    private boolean continueUpdateUI;//'Handler"通过这个变量识别是否可以更UI
    private final int updateUiTime = 200;//"Handler"更新UI间隔时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_song_play_info;
    }

    @Override
    protected void initBeforeView() {
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
        //获取更新播放信息用的主题
        playInfoSubject = PlayerOperator.getInstance();
    }

    @Override
    protected void initView() {
        initWidget();//调用方法对视图初始化
        initViewPager();//对ViewPager做初始化
        initHandler();//对Handler做初始化
    }

    @Override
    protected void onStart() {
        super.onStart();
        //将自己注册成为观察者
        playInfoSubject.registerObserver(this);
        continueUpdateUI = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        playInfoSubject.removeObserver(this);//从主题里移除当前活动
        continueUpdateUI = false;
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

    private void initHandler() {
        updatePlayInfoHandler = new UpdatePlayInfoHandler(this);
        continueUpdateUI = true;
    }

    //根据播放模式返回对应图片ID
    private int getPictureFromPlayMode(){
//        int playMode = new UserOptionPreferences(this).getPlayModes();
        int playMode = new UserOptionPreferences().getIntValues(UserOptionPreferences.KEY_PLAY_MODE, UserOptionPreferences.VALUES_PLAY_MODE_ORDER);
        if(playMode == UserOptionPreferences.VALUES_PLAY_MODE_ORDER){//如果需要顺序播放
            return R.drawable.ic_activity_play_song_info_orderplay;
        }else if(playMode == UserOptionPreferences.VALUES_PLAY_MODE_RANDOM){//如果需要随机播放
            return R.drawable.ic_activity_play_song_info_random;
        }else if(playMode == UserOptionPreferences.VALUES_PLAY_MODE_LIST_CIRCULATE){//如果需要列表循环
            return R.drawable.ic_activity_play_song_info_listcirculate;
        }else{//如果需要单曲循环
            return R.drawable.ic_activity_play_song_info_singlecirculate;
        }
    }


    private void initViewPager(){
        viewPager = (ViewPager)findViewById(R.id.vp_activity_song_play_info);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SongPlayListFragment());//添加当前播放列表显示界面
        fragmentList.add(new TwoLineLyricFragment());//添加两行歌词显示界面
//        fragmentList.add(new ScrollLyricsFragment());//添加滚屏歌词显示界面
        viewPager.setAdapter(new SongPlayInfoAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(1);//初始化在第二个页面里
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

    /*"ServiceConnection"方法实现_开始*/
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
//        Log.i("test","绑定服务");
        this.playerService = ((PlayerService.PlayerServiceBinder) service).getPlayerService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
    /*"ServiceConnection"方法实现_结束*/

    /*"FragmentTitleListener"方法实现_开始*/
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
    /*"FragmentTitleListener"方法实现_结束*/


    //点击事件监听方法
    @Override
    public void onClick(View v) {
        int id = v.getId();
        //如果点击左上角的返回按钮
        if(id == R.id.iv_activity_song_play_info_return){
            finish();
            return;
        }
        //如果点击修改播放模式那个按钮
        if(id == R.id.iv_activity_song_play_info_play_mode){
            setNewPlayMode();
            return;
        }
        //如果点击收藏按钮
        if(id == R.id.iv_collection){
            SongInfoOpenHelper songInfoOpenHelper = new SongInfoOpenHelper(SongPlayInfoActivity.this);
            if(lastSongInfo == null){
                return;
            }
            if (lastSongInfo.getCollection() == 0) {
                lastSongInfo.setCollection(1);
                songInfoOpenHelper.updateCollection(lastSongInfo, 1);
                iv_collection.setImageResource(R.drawable.activity_song_play_info_collection);
            } else {
                lastSongInfo.setCollection(0);
                songInfoOpenHelper.updateCollection(lastSongInfo, 0);
                iv_collection.setImageResource(R.drawable.activity_song_play_info_no_collection);
            }
            return;
        }
        //获取当前播放信息
        SongPlayInfo songPlayInfo = playInfoSubject.getPlayInfo();
        //如果当前没有播放信息，直接返回，后面前一曲、播放暂停、下一曲的操作都不用做
        if(songPlayInfo == null){
            return;
        }
        //如果点击播放前一曲的按钮
        if(id == R.id.iv_previous_song){
            playerService.previousSong();
            return;
        }
        //如果点击播放或者暂停按钮
        if(id == R.id.iv_play_or_pause){
            if(songPlayInfo.isPlaying()){
                playerService.pause();
            }else{
                playerService.play();
            }
            return;
        }
        //如果点击播放下一曲的按钮
        if(id == R.id.iv_next_song){
            playerService.nextSong();
            return;
        }
    }//onClick

//    根据当前播放模式设置新的播放模式以及图片
    private void setNewPlayMode(){
//        UserOptionPreferences userOptionTool = new UserOptionPreferences(SongPlayInfoActivity.this);
        UserOptionPreferences userOptionPreferences = new UserOptionPreferences();
        int playMode = userOptionPreferences.getIntValues(UserOptionPreferences.KEY_PLAY_MODE, UserOptionPreferences.VALUES_PLAY_MODE_ORDER);
//            根据当前播放模式写入新的播放模式
        switch(playMode){
//            case UserOptionPreferences.VALUES_PLAY_MODE_ORDER:
//                userOptionTool.setPlayModes(UserOptionPreferences.VALUES_PLAY_MODE_RANDOM);
//                break;
//            case UserOptionPreferences.VALUES_PLAY_MODE_RANDOM:
//                userOptionTool.setPlayModes(UserOptionPreferences.VALUES_PLAY_MODE_LIST_CIRCULATE);
//                break;
//            case UserOptionPreferences.VALUES_PLAY_MODE_LIST_CIRCULATE:
//                userOptionTool.setPlayModes(UserOptionPreferences.VALUES_PLAY_MODE_SINGLE_CIRCULATE);
//                break;
//            case UserOptionPreferences.VALUES_PLAY_MODE_SINGLE_CIRCULATE:
//                userOptionTool.setPlayModes(UserOptionPreferences.VALUES_PLAY_MODE_ORDER);
//                break;
//            default:break;
            case UserOptionPreferences.VALUES_PLAY_MODE_ORDER:
                userOptionPreferences.putIntValues(UserOptionPreferences.KEY_PLAY_MODE, UserOptionPreferences.VALUES_PLAY_MODE_RANDOM);
                break;
            case UserOptionPreferences.VALUES_PLAY_MODE_RANDOM:
                userOptionPreferences.putIntValues(UserOptionPreferences.KEY_PLAY_MODE, UserOptionPreferences.VALUES_PLAY_MODE_LIST_CIRCULATE);
                break;
            case UserOptionPreferences.VALUES_PLAY_MODE_LIST_CIRCULATE:
                userOptionPreferences.putIntValues(UserOptionPreferences.KEY_PLAY_MODE, UserOptionPreferences.VALUES_PLAY_MODE_SINGLE_CIRCULATE);
                break;
            case UserOptionPreferences.VALUES_PLAY_MODE_SINGLE_CIRCULATE:
                userOptionPreferences.putIntValues(UserOptionPreferences.KEY_PLAY_MODE, UserOptionPreferences.VALUES_PLAY_MODE_ORDER);
                break;
            default:break;

        }
        //根据新的播放模式更换图片
        iv_play_mode.setImageResource(getPictureFromPlayMode());
    }

    /*"SeekBar"滑动事件监听方法_开始*/
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
            tv_current_time.setText(getTime(progress));//进度条拖动的同时改变界面上的时间显示
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        continueUpdateUI = false;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
//        设置当前播放进度
        playerService.seekTo(seekBar.getProgress());
        continueUpdateUI = true;
//        circulations = true;

    }
    /*"SeekBar"滑动事件监听方法_结束*/


    @Override
    public void updatePlayInfo(SongPlayInfo songPlayInfo) {
        //如果没有任何被选中的歌曲
        if(songPlayInfo == null || songPlayInfo.getSongInfo() == null) {
            seekBar.setEnabled(false);
            return;
        }
        //恢复进度条可拖动状态
        seekBar.setEnabled(true);
        SongInfo songInfo = songPlayInfo.getSongInfo();//获取当前歌曲信息
        tv_current_time.setText(getTime(songPlayInfo.getProgress()));//设置当前播放时长
        seekBar.setProgress(songPlayInfo.getProgress());//设置当前播放进度
//        如果原来没有播放任何歌曲，或者原来所播放的歌曲和现在的不同
        if(lastSongInfo==null||!lastSongInfo.getSongAbsolutePath().equals(songInfo.getSongAbsolutePath())){
            setNewPlayInfo(songInfo);//设置新的歌曲信息
            lastSongInfo = songInfo;//更新当前播放的那首歌
        }
//        根据播放与否进行不同设置
        if(songPlayInfo.isPlaying()) {
            playSettings();
        }
        else {
            notPlaySettings();
        }//if(isPlaying)
        //根据收藏与否进行不同设置
        if(songInfo.getCollection() == SongInfo.IS_COLLECTION){
            iv_collection.setImageResource(R.drawable.activity_song_play_info_collection);
        }else {
            iv_collection.setImageResource(R.drawable.activity_song_play_info_no_collection);
        }
    }

    //    用来更换新的歌曲信息
    private void setNewPlayInfo(SongInfo songInfo){
//        设置所播放的歌曲名字
        tv_song_name.setText(songInfo.getSongName());
        tv_singer_name.setText(songInfo.getSingerName());
//            设置当前歌手图片
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            SingerPictureTools singerPictureTools = new SingerPictureTools(songInfo.getSingerName());
            if(singerPictureTools.hasSingerPicture()) {
//                ll_root_view.setBackground(singerPictureTools.getSingerPicture());
                ll_root_view.setBackgroundDrawable(singerPictureTools.getSingerPicture());
            }
        }
        //设置进度条最大值
        seekBar.setMax(songInfo.getSongDuration());
        //设置当前播放时长何总时长
        tv_total_time.setText(getTime(songInfo.getSongDuration()));
    }

    //如果歌曲正在播放要进行的相关设置
    private void playSettings(){
        iv_play_or_pause.setImageResource(R.drawable.activity_song_play_info_pause);
        continueUpdateUI = true;//修改允许更新UI标志
        updatePlayInfoHandler.sendEmptyMessageDelayed(WHAT_UPDATE_UI, 200);
    }

//    如果歌曲没在播放要进行的相关设置
    private void notPlaySettings(){
        iv_play_or_pause.setImageResource(R.drawable.activity_song_play_info_play);
        continueUpdateUI = false;//修改允许更新UI标志
    }

    /*"PlayController"方法实现_开始*/
    @Override
    public void play(List<SongInfo> songInfoList, int position) {
    }

    @Override
    public void play(String playListName, List<SongInfo> songList, int playPosition) {

    }

    @Override
    public void play() {
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void pause() {

    }

    @Override
    public void nextSong() {

    }

    @Override
    public void previousSong() {

    }

    @Override
    public void stop() {

    }
    /*"PlayController"方法实现_结束*/


    //定时更新UI用的"Handler"
    private static class UpdatePlayInfoHandler extends Handler{

        private WeakReference<SongPlayInfoActivity> activityWeakReference;

        public UpdatePlayInfoHandler(SongPlayInfoActivity songPlayInfoActivity){
            activityWeakReference = new WeakReference<>(songPlayInfoActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            SongPlayInfoActivity songPlayInfoActivity = activityWeakReference.get();
            //如果界面已经为空或者界面不需更新UI
            if( songPlayInfoActivity == null || !songPlayInfoActivity.continueUpdateUI){
                super.handleMessage(msg);
                return;
            }
            if (msg.what == songPlayInfoActivity.WHAT_UPDATE_UI) {
                SongPlayInfo songPlayInfo = songPlayInfoActivity.playInfoSubject.getPlayInfo();
                songPlayInfoActivity.seekBar.setProgress(songPlayInfo.getProgress());
                songPlayInfoActivity.tv_current_time.setText(songPlayInfoActivity.getTime(songPlayInfo.getProgress()));
                sendEmptyMessageDelayed(songPlayInfoActivity.WHAT_UPDATE_UI, songPlayInfoActivity.updateUiTime);
            }
            super.handleMessage(msg);
        }
    }
}