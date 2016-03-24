package com.jf.djplayer.service;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.jf.djplayer.InfoClass;
import com.jf.djplayer.SongInfo;
import com.jf.djplayer.tool.playertool.PlayInfoNotification;
import com.jf.djplayer.tool.playertool.PlayerOperator;

import java.util.List;
import java.util.Set;

/**
 * 当前服务管理后台播放状况
 * 所有界面对播放的控制全部需要通过服务完成（包括播放暂停以及歌曲变换）
 * 服务接收播放控制的命令后分发任务（包括使用PlayerOperator控制播放，使用PlayerNotification——发送通知）
 */
public class PlayerService extends Service {


    private List<SongInfo> songInfoList;//保存所有音乐文件的信息的
    private PlayerOperator playerOperator;//用这个类控制音乐播放
    private SongControlReceiver songControlReceiver;
    private PlayStatusChangeReceiver playStatusChangeReceiver;
    private PlayInfoNotification playInfoNotification;

    //这个用来给外界绑定服务的
    private PlayerServiceBinder playerServiceBinder = new PlayerServiceBinder();

    /*Notification广播用的相关常量*/
    //由于广播是在服务里面接收，所以常量放在服务里面
    public static final int BROAD_CAST_SONG_PLAY = 1;
    public static final int BROAD_CAST_SONG_PAUSE = 2;
    public static final int BROAD_CAST_SONG_NEXT = 3;
    public static final String SONG_CONTROL_BROADCAST_EXTRA = "song_control_broadCast_extra";
    public static final int BROAD_CAST_SONG_PREVIOUS = 4;
    public static final String NOTIFICATION_TO_SERVICE_ACTION = "com.danjuan.www.djplayer.SongControlBoradCast";
    /*Notification通知用的相关常量*/


    @Override
    public void onCreate() {
        super.onCreate();
//        Log.i("test","--PlayerService--onCreate");
        playerOperator = PlayerOperator.getInstance();
        playInfoNotification = new PlayInfoNotification(this);
        //动态注册广播接收
        registerReceivers();
    }

//    方法里面注册两个不同广播
    private void registerReceivers(){
//        一共需要注册两个广播
//        一个用来监听Notification控制
//        另外一个用来监听歌曲播放状态变化
        songControlReceiver = new SongControlReceiver();
        IntentFilter songControlFilter = new IntentFilter();
        songControlFilter.addAction(PlayerService.NOTIFICATION_TO_SERVICE_ACTION);
        registerReceiver(songControlReceiver, songControlFilter);
        playStatusChangeReceiver = new PlayStatusChangeReceiver();
        IntentFilter playStatusChangeFilter = new IntentFilter(InfoClass.ActionString.SONG_STATUS_CHANGE);
        playStatusChangeFilter.addCategory(InfoClass.CategoryString.SONG_STATUS_PLAY);
        playStatusChangeFilter.addCategory(InfoClass.CategoryString.SONG_STATUS_PAUSE);
        registerReceiver(playStatusChangeReceiver, playStatusChangeFilter);
    }

//    取消注册两个广播
    private void unregisterReceivers(){
        unregisterReceiver(songControlReceiver);
        unregisterReceiver(playStatusChangeReceiver);
    }
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



    /**
     * 服务仅仅接受用户所选择的歌曲列表
     * 以及被点歌曲位置
     * 所有播放相关控制
     * 以及消息栏的消息更新全部交由其他的类去做
     * @param intent
     * @param flags
     * @param startId
     * @return
     *
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;//服务一旦关闭之后禁止重启
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("test", "服务销毁");
        playerOperator.over();//结束音频控制所有服务
        playInfoNotification.cancelNotification();//关闭相关歌曲信息通知
//        动态注册的广播要动态解除
        unregisterReceivers();
    }


    /**
     * 播放指定列表里面指定位置的歌
     * @param songInfoList 要播放的歌曲列表
     * @param position 要播放的那首歌曲位置
     */
    public void play(List<SongInfo> songInfoList,int position){
//        Log.i("test","服务开始播放歌曲");
        this.songInfoList = songInfoList;//服务保存当前播放列表
        playerOperator.continuePlay(songInfoList, position);
//        用户点击播放列表既有可能开始播放歌曲
//        也有可能暂停播放歌曲
        if (playerOperator.isPlaying()) playInfoNotification.updateNotification(songInfoList.get(position));
        else playInfoNotification.cancelNotification();
//        Log.i("test","开始播放");
    }


    /*
    以下方法都是用来供给绑定该服务的活动用的
     */
    public void play() {
        playerOperator.setCanPlay(true);
        playerOperator.continuePlay();
        playInfoNotification.updateNotification(playerOperator.getCurrentSongInfo());
    }



    //暂停
    public void pause(){
        playerOperator.setCanPlay(false);
        playerOperator.pause();
        playInfoNotification.cancelNotification();
    }

    public void nextSong(){
        playerOperator.nextSong();
        playInfoNotification.updateNotification(playerOperator.getCurrentSongInfo());
    }

    public int getCurrentPosition(){
        return playerOperator.getCurrentPosition();
    }

    public void seekTo(int currentPosition){
        playerOperator.seekTo(currentPosition);
    }

    public void previousSong(){
        playerOperator.previousSong();
    }

    public boolean isPlaying(){
        return playerOperator.isPlaying();
    }

    /**
     * 获取当前正播放的歌曲信息
     * @return 歌曲信息
     */
    public SongInfo getSongInformation(){
//        Log.i("test","getSongInformation");
        return playerOperator.getCurrentSongInfo();
    }

    /**
     * 获取当前正播放的音乐列表
     * @return 歌曲列表
     */
    public List getSongList(){
        return songInfoList;
    }

    //这个广播用来接收
    //来自Notification上的按钮发的广播
    private class SongControlReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String myBroadCastAction = intent.getAction();//获取action
            int songBroadCastExtra = intent.getIntExtra(PlayerService.SONG_CONTROL_BROADCAST_EXTRA, -1);
            //先确定广播的发送来源
            if (myBroadCastAction.equals(PlayerService.NOTIFICATION_TO_SERVICE_ACTION)){
                //行为
                switch(songBroadCastExtra){
                    case PlayerService.BROAD_CAST_SONG_PLAY:
//                        Log.i("test", "播放");
                        playerOperator.continuePlay();
                        break;
                    case PlayerService.BROAD_CAST_SONG_PAUSE:
                        playerOperator.pause();
                        break;
                    case PlayerService.BROAD_CAST_SONG_NEXT:
                        playerOperator.nextSong();
                        Log.i("test","service--next");
                        break;
                    case PlayerService.BROAD_CAST_SONG_PREVIOUS:
//                        Log.i("test","上一曲");
                        playerOperator.previousSong();
                        break;
                }
            }//if
        }//onReceive
    }//SongControlReceiver

    /*
    用来监听播放状态变化用的广播
     */
    private class PlayStatusChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Set<String> categorysSet = intent.getCategories();
            if(intent.getAction().equals(InfoClass.ActionString.SONG_STATUS_CHANGE)){
//                如果歌曲开始播放
                if(categorysSet.contains(InfoClass.CategoryString.SONG_STATUS_PLAY)){
                    playInfoNotification.updateNotification((SongInfo)(intent.getSerializableExtra("songInfo")));
                }else if(categorysSet.contains(InfoClass.CategoryString.SONG_STATUS_PAUSE)) {
                    playInfoNotification.cancelNotification();
                }
            }
        }//onReceives
    }//playStatusChangeReceiver

}