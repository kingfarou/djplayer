package com.jf.djplayer.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/6.
 * “歌曲”对象
 */
public class Song implements Serializable {

    /** 歌曲是否已被收藏标记，已收藏*/
    public static final int IS_COLLECTION = 1;

    /** 歌曲是否已被收藏标记，没有收藏*/
    public static final int NOT_COLLECTION = 0;

    private String songName;            // 歌名
    private String singerName;          // 歌手
    private String album;               // 专辑
    private int duration;               // 歌曲播放所用时长
    private int size;                   // 表示歌曲文件大小
    private String fileAbsolutePath;    // 以字符串形式所表示的绝对路径
    private long lastPlayTime;          // 表示最后一次播放时间

    // 当前歌曲是否已被用户收藏
    private int collection = NOT_COLLECTION;

    /**
     * 构建一个没有任何信息歌曲文件对象
     */
    public Song() {}

    /**
     * 根据系统媒体库里能读到的信息创建歌曲对象
     * @param songName 歌曲名字
     * @param singerName 歌手名字
     * @param album 专辑名字
     * @param duration 播放时长
     * @param size 文件大小
     * @param fileAbsolutePath 绝对路径
     */
    public Song(String songName, String singerName, String album,
                int duration, int size, String fileAbsolutePath){
        this.songName = songName;
        this.singerName = singerName;
        this.album = album;
        this.duration = duration;
        this.size = size;
        this.fileAbsolutePath = fileAbsolutePath;
    }

    /**
     * 判断两首歌曲是否相同
     * @param o 传入需要被比较的对象
     * @return 当两首歌曲的绝对路径相同：true，否则：false
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Song) {
            Song info = (Song)o;
            return this.getFileAbsolutePath().equals(info.getFileAbsolutePath());
        }
        return false;
    }

    /**
     * 判断两首歌曲hashCode是否相同
     * @return 当两首歌曲绝对路径的hashCode相同：true，否则：false
     */
    @Override
    public int hashCode() {
        return fileAbsolutePath.hashCode();
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
    public String getSongName() {
        return songName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }
    public String getSingerName() {
        return singerName;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
    public String getAlbum() {
        return album;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getDuration() {
        return duration;
    }

    public void setSize(int size) {
        this.size = size;
    }
    public int getSize() {
        return size;
    }

    public void setFileAbsolutePath(String fileAbsolutePath){
        this.fileAbsolutePath = fileAbsolutePath;
    }
    public String getFileAbsolutePath() {
        return fileAbsolutePath;
    }

    public void setCollection(int collection){
        this.collection = collection;

    }
    public int getCollection(){
        return this.collection;
    }

    public void setLastPlayTime(long lastPlayTime){
        this.lastPlayTime = lastPlayTime;
    }
    public long getLastPlayTime(){
        return this.lastPlayTime;
    }
}
