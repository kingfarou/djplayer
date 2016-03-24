package com.jf.djplayer.tool.playertool;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.jf.djplayer.SongInfo;
import com.jf.djplayer.interfaces.PlayInfoObserver;
import com.jf.djplayer.MyApplication;
import com.jf.djplayer.interfaces.PlayInfoSubject;
import com.jf.djplayer.tool.UserOptionTool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/23.
 * 该类是一个强化版MediaPlayer
 * 在MediaPlayer功能之上，添加作为观察者的功能
 * 同时覆盖了MediaPlayer部分功能
 * 可以传入一个列表歌曲信息，以及当前所选中的歌曲位置
 * 能够进行各种播放模式
 * 只有后台播放的服务类能与该类进行通信
 */
public class PlayerOperator implements PlayInfoSubject{

    private static PlayerOperator playerOperator;
    private List<SongInfo> songInfoList;//序播放的歌曲列表
    private Context context;
    private MediaPlayer mMediaPlayer;
    private List<PlayInfoObserver> playInfoObserverList;
    private SongInfo lastSongInfo;//记录最后一次播放的歌
    private int lastPosition = -1;//用来记录最新播放歌曲的位置的
    private boolean canPlay = false;//当音频焦点变化时根据它来判定是否可以播放

//    将这个类写成单例
    private PlayerOperator(){
        context = MyApplication.getContext();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置所用的音频流
//        配置各类所用的监听器
//        这些监听器都是自己的内部的类
        mMediaPlayer.setOnPreparedListener(new PlayerOnPreparedListener());
        mMediaPlayer.setOnCompletionListener(new PlayerOnCompletion());
        mMediaPlayer.setOnErrorListener(new PlayerOnErrorListener());
//        请求音频焦点同时设置对焦点的监听
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(new PlayerAudioFocusChange(), audioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        playInfoObserverList = new ArrayList<PlayInfoObserver>();
    }

    /**
     * 获取PlayerOperator
     * @return PlayerOperator对象
     */
    public static PlayerOperator getInstance(){
        if(playerOperator==null){
            synchronized (PlayerOperator.class){
                if(playerOperator==null) playerOperator = new PlayerOperator();
            }
        }
        return playerOperator;
    }


    /**
     * 一旦用户点击列表上的歌曲
     * 就会调用这个方法
     * @param songInfoList 被管理的歌曲信息
     * @param clickPosition 用户所点击的歌曲位置
     */
    public void continuePlay(List<SongInfo> songInfoList, int clickPosition){
        this.songInfoList = songInfoList;//保存当前播放列表
//        如果原来没有播放任何歌曲
        if (lastSongInfo ==null){
//           Uri fileUri = Uri.fromFile(new File(songInfoList.get(clickPosition).getSongAbsolutePath()));
            try{
                //与播放相关的设置工作
//                setDataSource(context, fileUri);
                mMediaPlayer.setDataSource(new File(songInfoList.get(clickPosition).getSongAbsolutePath()).getAbsolutePath());
                mMediaPlayer.prepareAsync();
//
            }catch (IOException e){
                Log.i("test","异常——"+e.toString()+"\n"+"位置——"+this.toString());
                Toast.makeText(context, "所点击的歌曲文件有误", Toast.LENGTH_SHORT).show();
            }
        }//if(lastSongAbsolutePath==null)
//        如果两次点击不同首歌
        else if ( !songInfoList.get(clickPosition).getSongAbsolutePath().equals(lastSongInfo.getSongAbsolutePath())){
            changeSong(songInfoList.get(clickPosition).getSongAbsolutePath());//根据歌曲绝对路径更换歌曲
        }
        //如果两次点击同一首歌
        else {
            //如果歌曲正在播放那就暂停
            if (mMediaPlayer.isPlaying()){
                this.pause();
                canPlay = false;//标记他是手动暂停
//                isPlaying = false;
            }else{//否则的话继续播放
                this.continuePlay();
                this.canPlay = true;
//                isPlaying = true;
            }
        }
        this.canPlay = true;//修改允许播放标志
        //更新所选择的歌曲文件
        lastSongInfo = songInfoList.get(clickPosition);
        lastPosition = clickPosition;
    }

    public void setCanPlay(boolean canPlay){
        this.canPlay = canPlay;
    }


    /**
     * 播放
     * 某一首歌暂停后又继续播放
     * 应当调用这个方法
     */
    public void continuePlay(){
        mMediaPlayer.start();
//        通知所有的观察着歌曲信息
        notifyObservers();

    }

    /**
     * 暂停当前所播放的歌曲
     * 同时通知所有的关察着
     */
    public void pause(){
        mMediaPlayer.pause();
//        通知所有的观察着歌曲信息
        notifyObservers();
    }


    /**
     * 播放列表里面的下一首歌曲
     */
    public void nextSong() {
        lastPosition = (lastPosition + 1) % (songInfoList.size());
        lastSongInfo = songInfoList.get(lastPosition);
        changeSong(lastSongInfo.getSongAbsolutePath());
    }

    /**
     * 播放列表里面的前一首歌曲
     */
    public void previousSong(){
        //修改previousPosition
        if (lastPosition == 0) lastPosition = songInfoList.size()-1;
        else lastPosition = (lastPosition - 1) % songInfoList.size();
        //更换要播放的歌曲
        lastSongInfo = songInfoList.get(lastPosition);
        changeSong(lastSongInfo.getSongAbsolutePath());

    }

    /**
     * 当应用程序退出时调用这个方法彻底放弃音频资源
     */
    public void over(){
//        先要释放掉MediaPlayer
        if (mMediaPlayer!=null){
            if (mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
            }
//            playerOperator.cancelNotification();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
//        在释放掉当前对象
        if(playerOperator!=null) playerOperator = null;
    }


    public void seekTo(int msec){
        mMediaPlayer.seekTo(msec);
        notifyObservers();
    }

    /*
     * 根据新的歌曲绝对路径更换歌曲
     */
    private void changeSong(String absolutePath){
        this.canPlay = true;
        mMediaPlayer.stop();//停止播放当前歌曲
        notifyObservers();//通知所有的观察者状态改变
        mMediaPlayer.reset();
        try {
            //更换歌曲相关设置
            mMediaPlayer.setDataSource(absolutePath);
//            create()
//            Log.i("test", "更换歌曲");
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            Toast.makeText(this.context, "音频资源设置失败，请确认文件是否有效", Toast.LENGTH_SHORT).show();
        }
    }

//    作为观察者模式的主题所实现的几个方法

    /**
     * 添加新的观察者来
     * @param playInfoObserver 实现了观察者接口的类
     */
    @Override
    public void registerObserver(PlayInfoObserver playInfoObserver) {
        playInfoObserverList.add(playInfoObserver);
//        对每一个新添加进的观察者单独发送一次通知
        playInfoObserver.updatePlayInfo(lastSongInfo, mMediaPlayer.isPlaying(), getCurrentPosition());
    }

    /**
     * 移除制定的观察着
     * @param playInfoObserver
     */
    @Override
    public void removeObserver(PlayInfoObserver playInfoObserver) {
        playInfoObserverList.remove(playInfoObserver);
    }

//    开始播放或者暂停或者停止那时才会发送更新
    @Override
    public void notifyObservers() {
        for(PlayInfoObserver playInfoObserver:playInfoObserverList){
            playInfoObserver.updatePlayInfo(lastSongInfo, mMediaPlayer.isPlaying(), getCurrentPosition());
        }
    }

    /**
     * 获取当前歌曲播放进度
     * @return 当前歌曲播放进度（毫秒）
     */
    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    /**
     * return the information of the song which is playing now
     * @return null if there no song be
     *
     */
    @Override
    public SongInfo getCurrentSongInfo() {
        return lastSongInfo;
    }

    /**
     * 返回歌曲是否正在播放
     * @return true:当前歌曲正在播放，false:歌曲暂停或者压根没有选择任何歌曲
     */
    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }
    /*
    负责外部类焦点监听的内部类
    */
    private class PlayerAudioFocusChange implements AudioManager.OnAudioFocusChangeListener{

        @Override
        public void onAudioFocusChange(int focusChange) {
//            如果外部类还没有被实例化
//            直接返回就可以了
            if(playerOperator==null){
                return;
            }
            if(focusChange==AudioManager.AUDIOFOCUS_GAIN){
                Log.i("test", "重新获取音频焦点--"+PlayerAudioFocusChange.class);
                if (!mMediaPlayer.isPlaying() && PlayerOperator.this.canPlay) {
                    continuePlay();
                }
            }else if(focusChange==AudioManager.AUDIOFOCUS_LOSS){
                Log.i("test","长期失去音频焦点--"+PlayerAudioFocusChange.class);
                try{
//                    if (mMediaPlayer.isPlaying()) {
//                        mMediaPlayer.stop();
//                        mMediaPlayer.release();
//                    }
                    over();
                }catch (IllegalStateException illegalStateException){
                    Log.i("test","异常——"+illegalStateException.toString()+"\n"+"位置——"+this.toString());
                }
            }else if(focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
                //暂时失去音频焦点
                //很可能在短时间内获得
//                比如接听一个打进来的电话
                Log.i("test", "暂时失去音频焦点");
                if (mMediaPlayer.isPlaying()) PlayerOperator.this.pause();
            }else if(focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                Log.i("test","暂时失去音频焦点允许小声播放");
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.setVolume(0.1f, 0.1f);//降低当前音量大小
//                    一秒钟后设置回去
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mMediaPlayer.setVolume(1f, 1f);
                        }
                    }, 1000);

                }//if
            }

        }//onAudioFocusChange
    }//PlayerAudioFocusChange

    /*
    负责外部类播放完成监听的内部类
     */
    private class PlayerOnCompletion implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
    //        读取用户当前播放模式
            if(mp==null||songInfoList==null){
                return;
            }
            UserOptionTool playOption = new UserOptionTool(MyApplication.getContext());
            int playMode = playOption.getPlayModes();
    //        根据播放模式进行控制判断
            if(playMode==UserOptionTool.PLAY_MODE_SINGLE_CIRCLUATE){//如果处于单曲循环模式
                if (!mp.isLooping()){
                    mp.setLooping(true);
                    mp.start();
                    notifyObservers();
                }
            }else if(playMode==UserOptionTool.PLAY_MODE_ORDER){//如果处于顺序播放模式
                if (mp.isLooping()) {
                    mp.setLooping(false);
                }
                if (lastPosition==songInfoList.size()-1) {//如果已到最后一首应该停止播放
                    mp.stop();
                }else{
                    nextSong();//进行下一首歌曲的播放
                }
            }else if(playMode==UserOptionTool.PLAY_MODE_RANDOM){//如果处于随机播放模式
//                如果处于随机播放模式
//                暂时没想到些什么
            }else if(playMode==UserOptionTool.PLAY_MODE_CIRCULATE){//如果处于列表循环模式
//                如果处于列表循环模式
                if (mp.isLooping()) {
                    mp.setLooping(false);
                }
                nextSong();
            }
        }

    }//PlayerOnCompletion

    /*
    负责外部类准备完成监听的内部类
     */
    private class PlayerOnPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            notifyObservers();
        }
    }

    /*
    负责外部类错误监听的内部的类
     */
    private class PlayerOnErrorListener implements MediaPlayer.OnErrorListener {

       @Override
       public boolean onError(MediaPlayer mp, int what, int extra) {
           if (what== MediaPlayer.MEDIA_ERROR_UNKNOWN){
               Toast.makeText(context, "未知错误"+"--"+this.toString(), Toast.LENGTH_SHORT).show();
           }
           if (extra== MediaPlayer.MEDIA_ERROR_UNSUPPORTED){
               Toast.makeText(context,"不支持的音频文件类型--"+this.toString(),Toast.LENGTH_SHORT).show();
           }
//           Log.i("test","what:"+what+"--extra:"+extra);
           return false;
       }
    }

}
