package com.jf.djplayer.module;

import com.jf.djplayer.module.SongInfo;

/**
 * Created by JF on 2016/5/25.
 * 歌曲播放信息对象，
 * "PlayInfoSubject"类向"PlayInfoObserver"传递该对象，
 * 用来表示当前播放信息
 */
public class SongPlayInfo {

    private SongInfo currentPlaySongInfo;//当前正播放的歌曲信息
    private boolean isPlaying;//歌曲是否正在播放（正在唱着）
    private int progress;//歌曲当前播放进度

    public SongPlayInfo(){
    public SongPlayInfo(SongInfo currentPlaySongInfo, boolean isPlaying, int progress){
        this.currentPlaySongInfo = currentPlaySongInfo;
        this.isPlaying = isPlaying;
        this.progress = progress;
    }

    public SongInfo getCurrentPlaySongInfo() {
        return currentPlaySongInfo;
    }
    public void setCurrentPlaySongInfo(SongInfo currentPlaySongInfo) {
        this.currentPlaySongInfo = currentPlaySongInfo;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public int getProgress() {
        return progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }
}
