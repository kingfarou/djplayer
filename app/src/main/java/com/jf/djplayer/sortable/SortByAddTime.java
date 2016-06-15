package com.jf.djplayer.sortable;

import com.jf.djplayer.module.SongInfo;

import java.util.Comparator;
import java.util.List;

/**
 * Created by jf on 2016/6/15.
 * 歌曲列表排序方式__按照添加时间排序
 */
public class SortByAddTime implements SongInfoListSortable,Comparator<SongInfo> {

    @Override
    public void sort(List<SongInfo> songInfoList) {

    }

    @Override
    public int compare(SongInfo lhs, SongInfo rhs) {
        return 0;
    }
}
