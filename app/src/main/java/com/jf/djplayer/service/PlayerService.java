package com.jf.djplayer.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.jf.djplayer.module.SongPlayInfo;
import com.jf.djplayer.other.IntentConstant;
import com.jf.djplayer.module.SongInfo;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.interfaces.PlayInfoObserver;
import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.playertool.PlayInfoNotification;
import com.jf.djplayer.playertool.PlayerOperator;

import java.util.List;

/**
 * 当前服务管理后台播放状况
 * 所有界面对播放的控制全部需要通过服务完成（包括播放暂停以及歌曲变换）
 * 服务接收播放控制的命令后分发任务（包括使用PlayerOperator控制播放，使用PlayerNotification——发送通知）
 */
public class PlayerService extends Service implements PlayInfoObserver{

//    private List<SongInfo> songInfoList;//保存当前正播放的歌曲列表
    private PlayerOperator playerOperator;//用这个类控制音乐播放
    private PlayInfoNotification playInfoNotification;
    private SongInfoOpenHelper songInfoOpenHelper;//用来更新歌曲最后一次播放时间

    //这个用来给外界绑定服务的
    private PlayerServiceBinder playerServiceBinder = new PlayerServiceBinder();


    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.showLog("创建服务");
//        Log.i("test","--PlayerService--onCreate");
        //音乐播放工具类初始化
        playerOperator = PlayerOperator.getInstance();
        //音乐播放通知栏初始化
        playInfoNotification = new PlayInfoNotification(this);
        //服务登记为播放信息的观察者
        playerOperator.registerObserver(this);
        //创建数据库是为了更新歌曲最后一次播放时间
        songInfoOpenHelper = new SongInfoOpenHelper(this);
        //动态注册广播接收
//        registerReceivers();
    }

    /*"PlayInfoObserver"方法实现_开始*/
    @Override
    public void updatePlayInfo(SongPlayInfo songPlayInfo) {
        if(songPlayInfo == null || songPlayInfo.getSongInfo() == null) {
            return;
        }
        //只有歌曲播放了才需要更新最后一次播放时间
        if(songPlayInfo.isPlaying()){
            songInfoOpenHelper.setLastPlayTime(songPlayInfo.getSongInfo(), System.currentTimeMillis());
        }
        //不论歌曲是否播放，都要更新通知栏的信息
        playInfoNotification.updateNotification(songPlayInfo.getSongInfo(), songPlayInfo.isPlaying());
    }
    /*"PlayInfoObserver"方法实现_结束*/

    /**
     * 该类用于返回当前服务对象实例
     * */
    public class PlayerServiceBinder extends Binder{
        public PlayerService getPlayerService(){
            return PlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
//        Log.i("test", "service-onBind");
        return playerServiceBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null || intent.getAction() == null){
            return START_NOT_STICKY;
        }
        //如果收到了来自于"PlayNotification"的意图，自行向对应的操作
        switch (intent.getAction()) {
            case IntentConstant.ACTION_PLAY_PREVIOUS_SONG:
                playerOperator.previousSong();
                break;
            case IntentConstant.ACTION_PLAY_SONG:
                if(playerOperator.isPlaying()){
                    playerOperator.pause();
                }else{
                    playerOperator.play();
                }
                break;
            case IntentConstant.ACTION_PLAY_NEXT_SONG:
                playerOperator.nextSong();
                break;
            default:
                break;
        }
        return START_NOT_STICKY;//服务一旦关闭之后禁止重启
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.showLog("销毁服务");
        playerOperator.over();//结束音频控制所有服务
        playInfoNotification.cancelNotification();//关闭相关歌曲信息通知
    }


//    /**
//     * 播放指定列表里面指定位置的歌
//     * @param songInfoList 要播放的歌曲列表
//     * @param playPosition 要播放的那首歌曲位置
//     */
//    public void play(List<SongInfo> songInfoList,int playPosition){
//        if(songInfoList == null || playPosition<0 || playPosition>=songInfoList.size()){
//            return;
//        }
////        this.songInfoList = songInfoList;//服务保存当前播放列表
//        playerOperator.play(songInfoList, playPosition);//播放
//    }

    /**
     * 播放指定列表里的指定位置歌曲
     * @param playListName 这是播放列表名字
     * @param songList 这是待播放的歌曲列表
     * @param playPosition 被选中的歌曲所在位置
     */
    public void play(String playListName, List<SongInfo> songList, int playPosition){
//        if(songList == null || playPosition<0 || playPosition>=songList.size()){
//            return;
//        }
        playerOperator.play(playListName, songList, playPosition);
    }

    /*供给绑定服务的"Activity"用的方法_开始*/
    /**
     * 播放歌曲，播放那些被暂停的歌曲
     */
    public void play() {
        playerOperator.setCanPlay(true);
        playerOperator.play();
    }

    /**
     * 暂停播放
     */
    public void pause(){
        playerOperator.setCanPlay(false);
        playerOperator.pause();
    }

    /**
     * 播放当前播放列表当前歌曲的下一首
     */
    public void nextSong(){
        playerOperator.nextSong();
    }

//    public int getCurrentPosition(){
//        return playerOperator.getCurrentPosition();
//    }

    public void seekTo(int currentPosition){
        playerOperator.seekTo(currentPosition);
    }

    /**
     * 播放当前播放列表当前歌曲的前一首
     */
    public void previousSong(){
        playerOperator.previousSong();
    }

    public boolean isPlaying(){
        return playerOperator.isPlaying();
    }

    /**
     * 获取当前正播放的音乐列表
     * @return 歌曲列表
     */
    public List getSongList(){
        return playerOperator.getSongInfoList();
    }

    /*供给邦迪服务的"Activity"用的方法_结束*/

}