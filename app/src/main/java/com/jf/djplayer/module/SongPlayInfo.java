package com.jf.djplayer.module;

/**
 * Created by JF on 2016/5/25.
 * 歌曲播放信息对象，
 * "PlayInfoSubject"类向"PlayInfoObserver"传递该对象，
 * 用来表示当前播放信息
 */
public class SongPlayInfo {

    private SongInfo songInfo;//当前正播放的歌曲信息
    private boolean isPlaying;//歌曲是否正在播放（正在唱着）
    private int progress;//歌曲当前播放进度

    public SongPlayInfo(){}
    
    public SongPlayInfo(SongInfo songInfo, boolean isPlaying, int progress){
        this.songInfo = songInfo;
        this.isPlaying = isPlaying;
        this.progress = progress;
    }

    public SongInfo getSongInfo() {
        return songInfo;
    }
    public void setSongInfo(SongInfo songInfo) {
        this.songInfo = songInfo;
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
