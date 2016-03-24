package com.jf.djplayer.interfaces;

import com.jf.djplayer.SongInfo;

import java.util.List;

/**
 * Created by JF on 2016/1/26.
 * 活动通过实现这个接口成为音乐播放的控制着
 * Fragment通过这个接口控制活动从而
 * 控制播放
 */
public interface PlayControls {

    /**
     * 传入要播放的歌曲列表
     * 以及要播放的歌曲所在序号
     * @param songInfoList
     * @param position
     */
    public void play(List<SongInfo> songInfoList,int position);

    /**
     * 暂停之后继续播放用的
     */
    public void play();

    /**
     * 查询当前是否正在播放
     */
    public boolean isPlaying();
    /**
     * 暂停
     */
    public void pause();

    /**
     * 播放列表里的下一首歌
     */
    public void nextSong();

    /**
     * 播放列表里面的前一手歌曲
     */
    public void previousSong();

    /**
     * 停止进行歌曲播放
     */
    public void stop();
}
