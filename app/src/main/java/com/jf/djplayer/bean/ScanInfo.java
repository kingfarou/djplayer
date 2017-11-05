package com.jf.djplayer.bean;

/**
 * Created by Kingfar on 2017/11/5.
 * 歌曲扫描进行时信息
 */

public class ScanInfo {

    private int songNum;               // 扫描到的歌曲总数
    private int currentNum;            // 当前正在处理第几首歌曲
    private String songAbsolutePath;   // 正处理的歌曲绝对路径

    /**
     * 创建扫描进行时信息对象
     * @param songNum 已扫描到的歌曲总数量
     * @param currentNum 当前正在处理第几首歌曲
     * @param songAbsolutePath 当前正处理的歌曲的绝对路径
     */
    public ScanInfo(int songNum, int currentNum, String songAbsolutePath) {
        this.songNum = songNum;
        this.currentNum = currentNum;
        this.songAbsolutePath = songAbsolutePath;
    }

    public int getSongNum() {
        return songNum;
    }

    public void setSongNum(int songNum) {
        this.songNum = songNum;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public String getSongAbsolutePath() {
        return songAbsolutePath;
    }

    public void setSongAbsolutePath(String songAbsolutePath) {
        this.songAbsolutePath = songAbsolutePath;
    }
}
