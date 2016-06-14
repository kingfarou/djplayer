package com.jf.djplayer.playertool;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.os.Handler;
import android.widget.Toast;

import com.jf.djplayer.module.SongInfo;
import com.jf.djplayer.interfaces.PlayInfoObserver;
import com.jf.djplayer.module.SongPlayInfo;
import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.interfaces.PlayInfoSubject;
import com.jf.djplayer.util.UserOptionPreferences;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/23.
 * 歌曲播放的控制类
 * 该类以装饰的方式，在"MediaPlayer"类的基础上，添加App需要的功能
 * 同时该类是"PlayInfoSubject"接口实现类
 * 同时，该类使用单例模式
 * 可以传入一个列表歌曲信息，以及当前所选中的歌曲位置
 * 能够进行各种播放模式
 * 只有后台播放的服务类能与该类进行通信
 */
public class PlayerOperator implements
        PlayInfoSubject, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        AudioManager.OnAudioFocusChangeListener{

    private volatile static PlayerOperator playerOperator;//单例引用
    private Context mContext;//上下文
    private MediaPlayer mMediaPlayer;//系统媒体的播放类
    private List<PlayInfoObserver> playInfoObserverList;//这个是观察者列表
    private SongPlayInfo playInfo;//该对象封装当前正播放的歌曲的信息
//    private int lastPosition = -1;//用来记录最新播放歌曲的位置的
    private boolean canPlay = false;//当音频焦点变化时根据它来判定是否可以播放

//    将这个类写成单例
    private PlayerOperator(){
        mContext = MyApplication.getContext();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置所用的音频流
        //配置各类所用的监听器，这些监听器全部由当前类来实现
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
//        请求音频焦点同时设置对焦点的监听
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(this, audioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        playInfoObserverList = new ArrayList<>();
    }

    /**
     * 获取PlayerOperator
     * @return PlayerOperator对象
     */
    public static PlayerOperator getInstance(){
        if(playerOperator==null){
            synchronized (PlayerOperator.class){
                if(playerOperator==null) {
                    playerOperator = new PlayerOperator();
                    MyApplication.showLog("创建单例");
                }
            }
        }
        return playerOperator;
    }

    /**
     * 播放指定列表下的指定位置歌曲
     * @param playListName 当前播放列表名字
     * @param songList 歌曲列表
     * @param playPosition 被选中的歌曲在列表的位置
     */
    public void play(String playListName, List<SongInfo> songList, int playPosition){
        //如果输入参数不对
        if(songList == null || playPosition<0 || playPosition>=songList.size()){
            MyApplication.showLog("所选择的播放列表为空，或者位置不正确");
            return;
        }
        //如果原来没有播放任何歌曲
        if(playInfo == null){
            //创建新的播放信息对象
            int progress = 0;//进度值初始化为零
            playInfo = new SongPlayInfo(playListName, songList, playPosition, progress);
            //开始播放
            try{
                File playFile = new File(playInfo.getSongInfo().getSongAbsolutePath());
                mMediaPlayer.setDataSource(playFile.getAbsolutePath());
                mMediaPlayer.prepareAsync();
            }catch (IOException e){
                MyApplication.showLog("异常--"+e.toString()+"-位置-"+"playerOperator.play(List, int)方法");
                Toast.makeText(mContext, "所点击的歌曲文件有误", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        /*到这说明原来已有播放歌曲*/
        String oldPlayListName = playInfo.getPlayListName();
        String oldSongAbsolutePath = playInfo.getSongInfo().getSongAbsolutePath();
        String newSongAbsolutePath = songList.get(playPosition).getSongAbsolutePath();
        //如果两次播放不是同一首歌（判断规则：播放列表名字相同而且歌曲绝对路径相同，这被视为同一首歌）
        if( !( oldPlayListName.equals(playListName) && newSongAbsolutePath.equals(oldSongAbsolutePath) ) ){
            //如果两次点击播放不同歌曲，更新新的播放信息
            playInfo.setPlayListName(playListName);
            playInfo.setSongList(songList);
            playInfo.setPlayPosition(playPosition);
            playInfo.setSongInfo(songList.get(playPosition));
            playInfo.setProgress(0);//新的歌曲播放进度初始化零
            changeSong(songList.get(playPosition).getSongAbsolutePath());//根据歌曲绝对路径更换歌曲
        }else{
            //以下处理两次点击同一首歌
            if (mMediaPlayer.isPlaying()){
                this.pause();
                canPlay = false;//标记他是手动暂停
            }else{//否则的话继续播放
                this.play();
                this.canPlay = true;
            }
        }//如果两次点击的是同一首歌
    }

    public void setCanPlay(boolean canPlay){
        this.canPlay = canPlay;
    }


    /**
     * 播放，某一首歌暂停后又继续播放，应当调用这个方法
     */
    public void play(){
        mMediaPlayer.start();
        //通知所有的观察着歌曲信息
        notifyObservers();
    }

    /**
     * 暂停当前所播放的歌曲
     */
    public void pause(){
        mMediaPlayer.pause();
        //通知所有的观察着歌曲信息
        notifyObservers();
    }


    /**
     * 播放歌曲列表里的下一首歌
     */
    public void nextSong() {
        //记录新的歌曲信息
        playInfo.setPlayPosition( (playInfo.getPlayPosition()+1)%playInfo.getSongList().size() );
        playInfo.setSongInfo(playInfo.getSongList().get(playInfo.getPlayPosition()));
        changeSong(playInfo.getSongInfo().getSongAbsolutePath());
    }

    /**
     * 播放列表里面的前一首歌曲
     */
    public void previousSong(){
        //修改previousPosition
        if(playInfo.getPlayPosition() == 0){
            playInfo.setPlayPosition(playInfo.getSongList().size()-1);
        }else{
//            playInfo.setPlayPosition( (playInfo.getPlayPosition()-1)%playInfo.getSongList().size() );
            playInfo.setPlayPosition(playInfo.getPlayPosition()-1);
        }
        playInfo.setSongInfo(playInfo.getSongList().get(playInfo.getPlayPosition()));
        changeSong(playInfo.getSongInfo().getSongAbsolutePath());
    }

    /**
     * 当应用程序退出时调用这个方法彻底放弃音频资源
     */
    public void over(){
        //先要释放掉MediaPlayer
        if (mMediaPlayer!=null){
            if (mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
            }
//            playerOperator.cancelNotification();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        //在释放掉当前对象
        if(playerOperator!=null) {
            playerOperator = null;
            MyApplication.showLog("销毁单例");
        }
    }

    /**
     * 设置当前播放进度（毫秒单位）
     * @param msec
     */
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
            Toast.makeText(this.mContext, "音频资源设置失败，请确认文件是否有效", Toast.LENGTH_SHORT).show();
        }
    }

    /*"PlayInfoSubject"方法实现_开始*/
     /**
     * 添加新的观察者来
     * @param playInfoObserver 实现了观察者接口的类
     */
    @Override
    public void registerObserver(PlayInfoObserver playInfoObserver) {
        playInfoObserverList.add(playInfoObserver);
        //对每一个新添加进的观察者单独发送一次通知
        playInfoObserver.updatePlayInfo(playInfo);
    }

    /**
     * 移除指定的观察者
     * @param playInfoObserver 需要移除的观察者
     */
    @Override
    public void removeObserver(PlayInfoObserver playInfoObserver) {
        playInfoObserverList.remove(playInfoObserver);
    }

    //发送最新消息给观察者
    @Override
    public void notifyObservers() {
        //先获取到最新数据
        playInfo.setPlaying(mMediaPlayer.isPlaying());
        playInfo.setProgress(mMediaPlayer.getCurrentPosition());
        for(PlayInfoObserver playInfoObserver:playInfoObserverList){
            playInfoObserver.updatePlayInfo(playInfo);
        }
    }

    /**
     * 返回当前正播放的歌曲信息
     * @return 如果当前没有播放任何歌曲，或者歌曲播放状态不可访问，将返回null。
     * 否则，返回当前正播放的歌曲信息
     */
    @Override
    public SongPlayInfo getPlayInfo() {
        if(playInfo == null){
            return null;
        }
        boolean isPlaying;
        try{
            isPlaying = mMediaPlayer.isPlaying();
        }catch (IllegalStateException e){
            return null;
        }
        playInfo.setProgress(mMediaPlayer.getCurrentPosition());
        playInfo.setPlaying(isPlaying);
        return playInfo;
    }
    /*"PlayInfoSubject"方法实现_结束*/

    public List<SongInfo> getSongInfoList(){
//        return this.songInfoList;
        return this.playInfo.getSongList();
    }

    /**
     * 返回歌曲是否正在播放
     * @return true:当前歌曲正在播放，false:歌曲暂停或者压根没有选择任何歌曲
     */
    public boolean isPlaying() {
        boolean isPlaying;
        try{
            isPlaying = mMediaPlayer.isPlaying();
        }catch (IllegalStateException e){
            return false;
        }
        return isPlaying;
    }

    /*"OnPreparedListener"方法实现_开始*/
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        playInfo.setPlaying(true);
        notifyObservers();
    }
    /*"OnPreparedListener"方法实现_结束*/

    /*"OnCompletionListener"方法实现_开始*/
    @Override
    public void onCompletion(MediaPlayer mp) {
        //读取用户当前播放模式
//        if(mp == null || songInfoList == null){
//            return;
//        }
        if(mp == null || playInfo == null || playInfo.getSongInfo() == null){
            return;
        }
//        UserOptionPreferences playOption = new UserOptionPreferences(MyApplication.getContext());
//        int playMode = playOption.getPlayModes();
        UserOptionPreferences playOption = new UserOptionPreferences();
        int playMode = playOption.getIntValues(UserOptionPreferences.KEY_PLAY_MODE, UserOptionPreferences.VALUES_PLAY_MODE_ORDER);
        //根据播放模式进行控制判断
        if(playMode== UserOptionPreferences.VALUES_PLAY_MODE_SINGLE_CIRCULATE){//如果处于单曲循环模式
            if (!mp.isLooping()){
                mp.setLooping(true);
                mp.start();
                notifyObservers();
            }
        }else if(playMode== UserOptionPreferences.VALUES_PLAY_MODE_ORDER){//如果处于顺序播放模式
            if (mp.isLooping()) {
                mp.setLooping(false);
            }
            if (playInfo.getPlayPosition() == playInfo.getSongList().size() -1) {//如果已到最后一首应该停止播放
                mp.stop();
            }else{
                nextSong();//进行下一首歌曲的播放
            }
        }else if(playMode== UserOptionPreferences.VALUES_PLAY_MODE_RANDOM){//如果处于随机播放模式
//                如果处于随机播放模式
//                暂时没想到些什么
        }else if(playMode== UserOptionPreferences.VALUES_PLAY_MODE_LIST_CIRCULATE){//如果处于列表循环模式
//                如果处于列表循环模式
            if (mp.isLooping()) {
                mp.setLooping(false);
            }
            nextSong();
        }
    }
    /*"OnCompletionListener"方法实现_结束*/

    /*"OnErrorListener"方法实现_开始*/
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (what== MediaPlayer.MEDIA_ERROR_UNKNOWN){
            Toast.makeText(mContext, "未知错误"+"--"+this.toString(), Toast.LENGTH_SHORT).show();
        }
        if (extra== MediaPlayer.MEDIA_ERROR_UNSUPPORTED){
            Toast.makeText(mContext,"不支持的音频文件类型--"+this.toString(),Toast.LENGTH_SHORT).show();
        }
//           Log.i("test","what:"+what+"--extra:"+extra);
        return false;
    }
    /*"OnErrorListener"方法实现_结束*/

    @Override
    public void onAudioFocusChange(int focusChange) {
        //如果外部类还没有被实例化，直接返回就可以了
        if(playerOperator==null){
            return;
        }
        if(focusChange==AudioManager.AUDIOFOCUS_GAIN){
            MyApplication.showLog("重新获取音频焦点");
            if (!mMediaPlayer.isPlaying() && PlayerOperator.this.canPlay) {
                play();
            }
        }else if(focusChange==AudioManager.AUDIOFOCUS_LOSS){
            MyApplication.showLog("长期失去音频焦点");
            //这里绝对不能调用"over()"方法，不然的话整个单例都会出现问题
            //over();
        }else if(focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
            //暂时失去音频焦点
            //很可能在短时间内获得
//                比如接听一个打进来的电话
            MyApplication.showLog("暂时失去音频焦点");
            if (mMediaPlayer.isPlaying()) {
                PlayerOperator.this.pause();
            }
        }else if(focusChange==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
            MyApplication.showLog("暂时失去音频焦点允许小声播放");
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
    }//onAudioFocusChange()

}
