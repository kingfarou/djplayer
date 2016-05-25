package com.jf.djplayer.other;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/6.
 * 这个类保存与歌曲相关的字段的名称
 * 每个对象就是一个歌曲所有信息
 */
public class SongInfo implements Serializable {

    private String songName;//歌名
    private String singerName;//歌手
    private String songAlbum;//专辑
    private int songDuration = -1;//歌曲播放所用时长
    private int songSize = -1;//表示歌曲文件大小
    private String songAbsolutePath;//以字符串形式所表示的绝对路径
    private int collection = NOT_COLLECTION;//标志当前歌曲是否已被用户收藏
    private long lastPlayTime;//表示最后一次播放时间
    public static final int IS_COLLECTION = 1;
    public static final int NOT_COLLECTION = 0;

    /**
     * 构建一个没有任何信息歌曲文件对象
     */
    public SongInfo() {}

    /**
     * 根据系统媒体库里能读到的信息创建歌曲对象
     * @param songName 歌曲名字
     * @param songArtist 歌手名字
     * @param songAlbum 专辑名字
     * @param songDuration 播放时长
     * @param songSize 文件大小
     * @param songAbsolutePath 绝对路径
     */
    public SongInfo(String songName, String songArtist, String songAlbum,
                    int songDuration, int songSize, String songAbsolutePath){
        this.songName = songName;
        this.singerName = songArtist;
        this.songAlbum = songAlbum;
        this.songDuration = songDuration;
        this.songSize = songSize;
        this.songAbsolutePath = songAbsolutePath;
    }

    /**
     * 我的自定义对象的equals()方法
     * 仅仅根据绝对路径是否一致判断对象石头相同
     * @param o 传入需要被比较的对象
     * @return 返回两者绝对路径是否相同
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof SongInfo) {
            SongInfo info = (SongInfo)o;
            return this.getSongAbsolutePath().equals(info.getSongAbsolutePath());
        } else {
            return false;
        }
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

    public void setSongAlbum(String songAlbum) {
        this.songAlbum = songAlbum;
    }
    public String getSongAlbum() {
        return songAlbum;
    }

    public void setSongDuration(int songDuration) {
        this.songDuration = songDuration;
    }
    public int getSongDuration() {
        return songDuration;
    }

    public void setSongSize(int songSize) {
        this.songSize = songSize;
    }
    public int getSongSize() {
        return songSize;
    }

    public void setSongAbsolutePath(String songAbsolutePath){
        this.songAbsolutePath = songAbsolutePath;
    }
    public String getSongAbsolutePath() {
        return songAbsolutePath;
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
