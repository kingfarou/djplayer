package com.jf.djplayer.recentlyplay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.other.SongInfo;
import com.jf.djplayer.adapter.ExpandableFragmentAdapter;
import com.jf.djplayer.basefragment.BaseExpandableListFragment;
import com.jf.djplayer.database.SongInfoOpenHelper;

import java.util.List;

/**
 * Created by JF on 2016/4/27.
 */
public class RecentlyPlayListFragment extends BaseExpandableListFragment {

    private List<SongInfo> recentlyPlaySongInfo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initBeforeReturnView() {

    }

    @Override
    protected View getExpandableLoadingView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.loading_layout,null);
    }

    @Override
    protected View getExpandableNoDataView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_no_recently_play,null);
    }

    @Override
    protected List readData() {
        recentlyPlaySongInfo = new SongInfoOpenHelper(getActivity()).getRecentlyPlaySong(2);
        if(recentlyPlaySongInfo == null){
            return null;
        }
        //遍历集合过滤所有从未播放过的歌曲（即"lastPlayTime==0"的歌）
        for(int i = recentlyPlaySongInfo.size()-1; i>=0; i--){
            if(recentlyPlaySongInfo.get(i).getLastPlayTime()==0){
                recentlyPlaySongInfo.remove(i);
            }
        }
        return recentlyPlaySongInfo;
    }

    @Override
    protected void asyncReadDataFinished(List dataList) {

    }

    @Override
    protected BaseExpandableListAdapter getExpandableAdapter() {
        return new ExpandableFragmentAdapter(getActivity(),recentlyPlaySongInfo);
    }

    @Override
    protected boolean doOnGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return false;
    }

    @Override
    protected boolean doExpandableItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    protected View getExpandableHeaderView() {
        return null;
    }

    @Override
    protected View getExpandableFooterView() {
        if(recentlyPlaySongInfo==null){
            return null;
        }
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(recentlyPlaySongInfo.size()+"首歌曲");
        return footerView;
    }
}
