package com.jf.djplayer.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/17.
 * 歌手
 */

public class Singer implements Serializable {

    private String imgUrl;  // 头像
    private String name;    // 名字
    private int songNumber; // 对应的歌曲数量

    public Singer(){

    }

    public Singer(String imgUrl, String name, int songNumber) {
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
