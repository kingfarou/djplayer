package com.jf.djplayer.interfaces;

import com.jf.djplayer.other.SongInfo;

/**
 * Created by JF on 2016/1/28.
 * 这是歌曲信息的观察着接口
 * 谁想获取歌曲播放实时信息
 * 就要实现这个接口
 */
public interface PlayInfoObserver {
    /**
     * 观察者被通知用的方法
     * @param currentPlaySongInfo 当前正播放的歌曲
     * @param isPlaying 是否还在播放当中
     * @param progress 进度（注意进度的单位是毫秒）
     */
    public void updatePlayInfo(SongInfo currentPlaySongInfo, boolean isPlaying, int progress);
}
