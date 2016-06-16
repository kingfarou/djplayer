package com.jf.djplayer.module;

import java.util.List;

/**
 * Created by JF on 2016/5/25.
 * 歌曲播放信息对象
 * "PlayInfoSubject"类向"PlayInfoObserver"传递该对象，
 * 用来表示当前播放信息
 */
public class PlayInfo {

    private String playListName;//当前正播放的列表所归属的界面名字
    private List<Song> songList;//当前正播放的歌曲列表
    private int playPosition;//正播放的歌曲所在列表里的位置
    private Song songInfo;//当前正播放的歌曲信息
    private boolean isPlaying;//歌曲是否正在播放（正在唱着）
    private int progress;//歌曲当前播放进度

    public PlayInfo(){}

    public PlayInfo(List<Song> songList, Song songInfo, boolean isPlaying, int progress){
        this.songList = songList;
        this.songInfo = songInfo;
        this.isPlaying = isPlaying;
        this.progress = progress;
    }

    public PlayInfo(String playListName, List<Song> songList, int playPosition, int progress){
        this.playListName = playListName;
        this.songList = songList;
        this.songInfo = songList.get(playPosition);
        this.playPosition = playPosition;
        this.progress = progress;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }
    public String getPlayListName() {
        return playListName;
    }

    public void setPlayPosition(int playPosition) {
        this.playPosition = playPosition;
    }
    public int getPlayPosition() {
        return playPosition;
    }

    public Song getSongInfo() {
        return songInfo;
    }
    public void setSongInfo(Song songInfo) {
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

    public void setSongList(List<Song> songInfoList){
        this.songList = songInfoList;
    }
    public List<Song> getSongList(){
        return this.songList;
    }
}
