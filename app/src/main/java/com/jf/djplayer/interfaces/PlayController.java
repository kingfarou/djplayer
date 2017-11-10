package com.jf.djplayer.interfaces;

import com.jf.djplayer.bean.Song;

import java.util.List;

/**
 * Created by JF on 2016/1/26.
 * Activity通过实现这个接口成为音乐播放的控制者，
 * Fragment通过这个接口控制Activity从而控制播放
 */
public interface PlayController {

    /**
     * 播放歌曲
     * @param songInfoList 要播放的歌曲列表
     * @param position 播放列表里第几首歌
     */
    public void play(List<Song> songInfoList,int position);

    /**
     * 播放歌曲
     * @param playListName 播放列表的名字
     * @param songList 要播放的歌曲列表
     * @param playPosition 播放列表里第几首歌
     */
    public void play(String playListName, List<Song> songList, int playPosition);

    /** 暂停之后继续播放用的*/
    public void play();

    /** 查询当前是否正在播放*/
    public boolean isPlaying();

    /** 暂停*/
    public void pause();

    /** 播放列表里的下一首歌*/
    public void nextSong();

    /** 播放列表里面的前一手歌曲*/
    public void previousSong();

    /** 停止进行歌曲播放*/
    public void stop();

    /**
     * 跳转播放位置
     * @param position 播放位置
     */
    public void seekTo(int position);
}
