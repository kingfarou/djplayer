package com.jf.djplayer.tool.sortable;

import com.jf.djplayer.SongInfo;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by JF on 2016/1/5.
 *
 */
public class SortBySingerName implements SongInfoListSortable,Comparator<SongInfo> {

    Collator collator = Collator.getInstance(Locale.CHINA);


    @Override
    public void sort(List<SongInfo> songInfoList) {
        Collections.sort(songInfoList, this);
    }

    @Override
    public int compare(SongInfo lhs, SongInfo rhs) {
        CollationKey key1 = collator
                .getCollationKey(lhs.getSongSinger());
        CollationKey key2 = collator
                .getCollationKey(rhs.getSongSinger());
        return key1.compareTo(key2);
    }
}
