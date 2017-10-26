package com.jf.djplayer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/26.
 * 表示用户选择的扫描音乐的选项
 */

public class ScanOptions implements Serializable{

    private int duration;          // 歌曲时长，单位：毫秒
    private int size;              // 歌曲尺寸，单位：kb
    private List<String> pathList; // 扫描路径的集合

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }
}
