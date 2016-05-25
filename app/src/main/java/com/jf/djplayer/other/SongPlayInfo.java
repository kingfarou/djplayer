package com.jf.djplayer.other;

/**
 * Created by JF on 2016/5/25.
 * 歌曲播放信息对象，
 * "PlayInfoSubject"类向"PlayInfoObserver"传递该对象，
 * 用来表示当前播放信息
 */
public class SongPlayInfo {

    private SongInfo currentPlaySongInfo;//当前正播放的歌曲信息
    private boolean isPlaying;//歌曲是否正在播放（正在唱着）
    private long progress;//歌曲当前播放进度

    public SongPlayInfo(SongInfo currentPlaySongInfo, boolean isPlaying, long progress){
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

    public long getProgress() {
        return progress;
    }
    public void setProgress(long progress) {
        this.progress = progress;
    }
}
