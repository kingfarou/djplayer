package com.jf.djplayer.sortable;

import com.jf.djplayer.module.Song;

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
public class SortBySingerName implements SongListSortable,Comparator<Song> {

    Collator collator = Collator.getInstance(Locale.CHINA);


    @Override
    public void sort(List<Song> songInfoList) {
        Collections.sort(songInfoList, this);
    }

    @Override
    public int compare(Song lhs, Song rhs) {
        CollationKey key1 = collator
                .getCollationKey(lhs.getSingerName());
        CollationKey key2 = collator
                .getCollationKey(rhs.getSingerName());
        return key1.compareTo(key2);
    }
}
