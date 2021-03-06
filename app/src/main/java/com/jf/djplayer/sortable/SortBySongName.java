package com.jf.djplayer.sortable;

import com.jf.djplayer.bean.Song;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by JF on 2016/1/5.
 * 根据歌曲名字进行排序
 */
public class SortBySongName implements SongListSortable,Comparator<Song>{

    private Collator collator = Collator.getInstance(Locale.CHINA);

    @Override
    public void sort(List<Song> songInfoList) {

        Collections.sort(songInfoList,this);//调用自定义比较器“this”进行排序
//        Collections.sort(songInfoList,new Comparator<_SongInfo>() {
//            @Override
//            public int compare(_SongInfo lhs, _SongInfo rhs) {
//                return rhs.getSongName().compareTo(lhs.getSongName());
//            }
//        });
    }
//    因为需要对中文做排序所以使用自定义比较器
    @Override
    public int compare(Song lhs,Song rhs) {
            CollationKey key1 = collator
                    .getCollationKey(lhs.getSongName());
            CollationKey key2 = collator
                    .getCollationKey(rhs.getSongName());
            return key1.compareTo(key2);
        }
    }

