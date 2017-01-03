package com.jf.djplayer.sortable;

import com.jf.djplayer.bean.Song;

import java.util.List;

/**
 * Created by JF on 2016/1/5.
 * 对装有SongInfo对像的List集合
 * 排序
 * 将输入的那个集合按特定的方法排序
 * 直接在原集合进行排序没有返回
 */
public interface SongListSortable {

    /**
     * 对一个装有SongInfo对像的集合进行排序
     * 实现类按自己想要方式进行排序
     * @param songInfoList 待排序的那个集合
     */
    public void sort(List<Song> songInfoList);
}
