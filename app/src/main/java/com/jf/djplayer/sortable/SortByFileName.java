package com.jf.djplayer.sortable;

import com.jf.djplayer.module.SongInfo;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by JF on 2016/1/29.
 * 根据文件名来进行排序
 */
public class SortByFileName implements SongInfoListSortable,Comparator<SongInfo> {

    File lhsFile;
    File rhsFile;

    @Override
    public void sort(List<SongInfo> songInfoList) {
        Collections.sort(songInfoList,this);
    }

    @Override
    public int compare(SongInfo lhs, SongInfo rhs) {
        lhsFile = new File(lhs.getSongAbsolutePath());
        rhsFile = new File(rhs.getSongAbsolutePath());
        return rhsFile.getName().compareTo(lhsFile.getName());
    }
}
