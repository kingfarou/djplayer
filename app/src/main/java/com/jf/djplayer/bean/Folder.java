package com.jf.djplayer.bean;

import java.io.Serializable;

/**
 * Created by Kingfar on 2017/10/18.
 * 本地音乐-“文件夹”对象
 */

public class Folder implements Serializable{

    private String name;    // 名字
    private int songNumber; // 对应的歌曲数量

    public Folder(){

    }

    public Folder(String name, int songNumber) {
        this.name = name;
        this.songNumber = songNumber;
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
