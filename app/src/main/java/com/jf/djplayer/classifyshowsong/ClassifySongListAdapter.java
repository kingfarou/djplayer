package com.jf.djplayer.classifyshowsong;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.jf.djplayer.R;
import com.jf.djplayer.base.baseadapter.BaseExpandFragmentAdapter;
import com.jf.djplayer.other.SongInfo;

import java.util.List;

/**
 * Created by jf on 2016/5/20.
 */
public class ClassifySongListAdapter extends BaseExpandFragmentAdapter{

    public ClassifySongListAdapter(Fragment fragment, List<SongInfo> dataList) {
        super(fragment, dataList);
    }

    @Override
    protected void initBeforeChildViewReturn(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(dataList.get(groupPosition).getCollection() == 1){
            childItemImageId[0] = R.drawable.fragment_song_collection;
        }
    }
}
