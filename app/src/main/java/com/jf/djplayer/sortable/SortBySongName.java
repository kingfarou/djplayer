package com.jf.djplayer.sortable;

import com.jf.djplayer.other.SongInfo;

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
public class SortBySongName implements SongInfoListSortable ,Comparator<SongInfo>{

    private Collator collator = Collator.getInstance(Locale.CHINA);

    @Override
    public void sort(List<SongInfo> songInfoList) {

        Collections.sort(songInfoList,this);//调用自定义比较器“this”进行排序
//        Collections.sort(songInfoList,new Comparator<SongInfo>() {
//            @Override
//            public int compare(SongInfo lhs, SongInfo rhs) {
//                return rhs.getSongName().compareTo(lhs.getSongName());
//            }
//        });
    }
//    因为需要对中文做排序所以使用自定义比较器
    @Override
    public int compare(SongInfo lhs,SongInfo rhs) {
            CollationKey key1 = collator
                    .getCollationKey(lhs.getSongName());
            CollationKey key2 = collator
                    .getCollationKey(rhs.getSongName());
            return key1.compareTo(key2);
        }
    }

