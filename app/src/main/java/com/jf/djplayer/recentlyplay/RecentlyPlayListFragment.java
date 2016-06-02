package com.jf.djplayer.recentlyplay;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.baseadapter.DefExpandFragmentAdapter;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.module.SongInfo;
import com.jf.djplayer.base.basefragment.BaseExpandFragment;
import com.jf.djplayer.database.SongInfoOpenHelper;

import java.util.List;

/**
 * Created by JF on 2016/4/27.
 * 最近播放数据列表，显示最近所播放的歌曲
 */
public class RecentlyPlayListFragment extends BaseExpandFragment {

    /**当一首歌最后一次播放时间等于这个常量，表示这首歌从未播放过*/
    public static final int NEVER_PLAY = 0;

    private List<SongInfo> recentlyPlaySongInfo;//数据
    private PlayController playController;//歌曲播放控制接口对象
    private View footerView;//"ListView"的"footerView"

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        playController = (PlayController)getActivity();
    }

    @Override
    protected void initBeforeReturnView() {
        if(footerView == null){
            return;
        }
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(recentlyPlaySongInfo.size()+"首歌");
    }

    @Override
    protected View getLoadingView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.loading_layout,null);
    }

    @Override
    protected View getExpandListEmptyView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_no_recently_play,null);
    }

    @Override
    protected List getData() {
        recentlyPlaySongInfo = new SongInfoOpenHelper(getActivity()).getRecentlyPlaySong(8);
        //如果当前没有数据，直接返回
        if(recentlyPlaySongInfo == null){
            return null;
        }
        //遍历集合过滤所有从未播放过的歌曲（即"lastPlayTime==0"的歌）
        for(int i = recentlyPlaySongInfo.size()-1; i>=0; i--){
            if(recentlyPlaySongInfo.get(i).getLastPlayTime() == NEVER_PLAY){
                recentlyPlaySongInfo.remove(i);
            }
        }
        return recentlyPlaySongInfo;
    }

    @Override
    protected BaseExpandableListAdapter getExpandableAdapter() {
//        return new SongInfoExpandAdapter(this, recentlyPlaySongInfo);
        return new DefExpandFragmentAdapter(this, recentlyPlaySongInfo);
    }

    @Override
    protected boolean doOnGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        playController.play(recentlyPlaySongInfo, groupPosition);
        return true;
    }

    @Override
    protected View getExpandableFooterView() {
        if(recentlyPlaySongInfo==null || recentlyPlaySongInfo.size()==0){
            return null;
        }
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(recentlyPlaySongInfo.size()+"首歌曲");
        return footerView;
    }
}
