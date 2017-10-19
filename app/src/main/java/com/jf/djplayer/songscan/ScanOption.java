package com.jf.djplayer.songscan;

import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 * 该对象封装了扫描歌曲时的过滤选项，
 * 包括诸如歌曲时长，文件大小，扫描路径之类条件
 */

public class ScanOption {

    private int duration;           // 歌曲时长，单位：毫秒
    private int size;               // 歌曲文件的大小，单位：kb
    private List<String> pathList;  // 扫描哪些路径下的歌曲

    /**
     * 获取要过滤的歌曲时长
     * @return 要过滤的歌曲时长，单位：毫秒
     */
    public int getDuration() {
        return duration;
    }

    /**
     * 设置要过滤的歌曲时长
     * @param duration 要过滤的歌曲时长，单位：毫秒
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * 获取要过滤的歌曲文件大小
     * @return 要过滤的歌曲文件大小，单位：kb
     */
    public int getSize() {
        return size;
    }

    /**
     * 设置要过滤的歌曲文件大小
     * @param size 要过滤的歌曲文件大小，单位：kb
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * 获取要扫描哪些路径下面的歌曲
     * @return 要扫描的那些路径
     */
    public List<String> getPathList() {
        return pathList;
    }

    /**
     * 设置要扫描哪些路径下面的歌曲
     * @param pathList 要扫描的那些路径
     */
    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }
}
