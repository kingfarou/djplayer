package com.jf.djplayer.songscan;

/**
 * Created by jf on 2016/8/3.
 * 扫描歌曲时，子线程通过该对象向界面传递扫描的信息
 */
public class ScanInfo {

    private int songSum;//扫描到的歌曲总数
    private String absolutePath;//当前歌曲的路径
    private int currentNum;//当前歌曲是第几首歌曲

    public ScanInfo(){
    }

    public ScanInfo(int songSum, int currentNum, String absolutePath) {
        this.songSum = songSum;
        this.currentNum = currentNum;
        this.absolutePath = absolutePath;
    }

    public int getSongSum() {
        return songSum;
    }
    public void setSongSum(int songSum) {
        this.songSum = songSum;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }
    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public int getCurrentNum() {
        return currentNum;
    }
    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }
}
