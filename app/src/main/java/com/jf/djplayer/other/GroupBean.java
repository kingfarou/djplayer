package com.jf.djplayer.other;

/**
 * Created by Administrator on 2015/7/19.
 * 一个对象代表着在"ExpandableListView"一栏上面所显示的所有数据
 */
public class GroupBean {
    private String title = null;//歌名
    private String artist = null;//歌手

    public GroupBean( String title, String artist){
        this.title = title;
        this.artist = artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }
}

