package com.jf.djplayer;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/6.
 * 这个类保存与歌曲相关的字段的名称
 * 每个对象就是一个歌曲所有信息
 */
public class SongInfo implements Serializable {
    private String songName ;//歌名
    private String singerName;//歌手
    private String songAlbum;//专辑
    private int songDuration = -1;//歌曲播放所用时长
    private int songSize = -1;//表示歌曲文件大小
    private String songAbsolutePath;//以字符串形式所表示的绝对路径
    private int collection = 0;//标志当前歌曲是否已被用户收藏

    /**
     * 构建一个没有任何信息歌曲文件对象
     */
    public SongInfo() {}

//    输入完整歌曲信息创建对象
    public SongInfo(String songName, String songArtist, String songAlbum, int songDuration,
                    int songSize, String songAbsolutePath,int collection) {

        this.songName = songName;
        this.singerName = songArtist;
        this.songAlbum = songAlbum;
        this.songDuration = songDuration;
        this.songSize = songSize;
        this.songAbsolutePath = songAbsolutePath;
        this.collection = collection;

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


    /**
     * 设置当前歌曲名字
     * @param songName 歌曲名字
     */
    public void setSongName(String songName) {
        this.songName = songName;
    }

    /**
     * 设置当前歌手名字
     * @param singerName 歌手名字
     */
    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    /**
     * 设置当前歌曲专辑
     * @param songAlbum 歌曲专辑
     */
    public void setSongAlbum(String songAlbum) {
        this.songAlbum = songAlbum;
    }

    /**
     * 设置当前歌曲播放时长
     * @param songDuration 毫秒数表示的歌曲播放时长
     */
    public void setSongDuration(int songDuration) {
        this.songDuration = songDuration;
    }

    /**
     * 传入当前歌曲文件大小
     * @param songSize 文件大小
     */
    public void setSongSize(int songSize) {
            this.songSize = songSize;

    }

    /**
     * 设置当前歌曲文件绝对路径
     * @param songAbsolutePath 字符串形式表示的绝对路径
     */
    public void setSongAbsolutePath(String songAbsolutePath){
            this.songAbsolutePath = songAbsolutePath;

    }

    /**
     * 设置歌曲是否被用户说收藏
     * @param collection 1表示被用户收藏，0则表示没有收藏
     */
    public void setCollection(int collection){
            this.collection = collection;

    }


    public String getSongName() {
        return songName;
    }

    public String getSingerName() {
        return singerName;
    }

    public String getSongAlbum() {
        return songAlbum;
    }
    public int getSongDuration() {
        return songDuration;
    }

    public int getSongSize() {
        return songSize;
    }

    public String getSongAbsolutePath() {
        return songAbsolutePath;
    }
    public int getCollection(){
        return this.collection;
    }
}
