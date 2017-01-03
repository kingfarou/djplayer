package com.jf.djplayer.bean;

/**
 * Created by JF on 2016/2/13.
 * 播放信息-歌词对应数据结构
 * 每个该类对象代表一句歌词里的属性
 */
public class LyricLine {

    private int startTime;          // 一句歌词开始时间
    private String lyricContent;    // 一句歌词歌词内容

    /**
     * 传入歌词开始时间以及歌词内容构建歌词对象
     * @param startTime 该句歌词开始时间（毫秒）
     * @param lyricContent 歌词内容
     */
    public LyricLine(int startTime, String lyricContent){
        this.startTime = startTime;
        this.lyricContent = lyricContent;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public String getLyricContent() {
        return lyricContent;
    }

    public void setLyricContent(String lyricContent) {
        this.lyricContent = lyricContent;
    }
}
