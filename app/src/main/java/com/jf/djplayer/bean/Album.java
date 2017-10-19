package com.jf.djplayer.bean;

/**
 * Created by Administrator on 2017/10/17.
 * 专辑
 */

public class Album {

    private String imgUrl;  // 专辑图片的地址
    private String name;    // 名字
    private int songNumber; // 对应的歌曲数量

    public Album(){

    }

    public Album(String imgUrl, String name, int songNumber) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.songNumber = songNumber;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSongNumber() {
        return songNumber;
    }

    public void setSongNumber(int songNumber) {
        this.songNumber = songNumber;
    }
}
