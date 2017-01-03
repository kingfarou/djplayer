package com.jf.djplayer.sortable;

import com.jf.djplayer.bean.Song;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by JF on 2016/1/29.
 * 根据文件名来进行排序
 */
public class SortByFileName implements SongListSortable,Comparator<Song> {

    File lhsFile;
    File rhsFile;

    @Override
    public void sort(List<Song> songInfoList) {
        Collections.sort(songInfoList,this);
    }

    @Override
    public int compare(Song lhs, Song rhs) {
        lhsFile = new File(lhs.getFileAbsolutePath());
        rhsFile = new File(rhs.getFileAbsolutePath());
        return rhsFile.getName().compareTo(lhsFile.getName());
    }
}
