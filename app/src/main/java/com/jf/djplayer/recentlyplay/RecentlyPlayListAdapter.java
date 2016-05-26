package com.jf.djplayer.recentlyplay;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.jf.djplayer.R;
import com.jf.djplayer.base.baseadapter.BaseExpandFragmentAdapter;
import com.jf.djplayer.module.SongInfo;

import java.util.List;

/**
 * Created by jf on 2016/5/20.
 * 最近播放-"RecentlyPlayListFragment"列表所用适配器
 */
public class RecentlyPlayListAdapter extends BaseExpandFragmentAdapter{

    public RecentlyPlayListAdapter(Fragment fragment, List<SongInfo> recentlyPlayList) {
        super(fragment, recentlyPlayList);
    }

    @Override
    protected void initBeforeChildViewReturn(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //如果歌曲被收藏了，需要改变一下图标
        if(dataList.get(groupPosition).getCollection() == 1){
            childItemImageId[0] = R.drawable.fragment_song_collection;
        }
    }
}
