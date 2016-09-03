package com.jf.djplayer.recentlyplay;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.base.activity.BaseActivity;
import com.jf.djplayer.base.fragment.SongListFragment;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.module.Song;

import java.util.List;

/**
 * Created by jf on 2016/7/14.
 * 最近播放-歌曲列表
 */
public class RecentlyPlayListFragment extends SongListFragment{

    /**当一首歌最后一次播放时间等于这个常量，表示这首歌从未播放过*/
    public static final int NEVER_PLAY = 0;

    private PlayController playController;//歌曲播放控制接口对象
    private View footerView;//"ListView"的"footerView"

    @Override
    protected void initView(View layoutView) {
        super.initView(layoutView);
        if(footerView == null){
            return;
        }
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size() + "首歌");
    }

    /*获取数据相关代码__start*/
    @Override
    protected List<Song> getData() {
        List<Song> recentlyPlayList = new SongInfoOpenHelper(getActivity()).getRecentlyPlaySong(8);
        //如果当前没有数据，直接返回
        if(recentlyPlayList == null){
            return null;
        }
        //遍历集合过滤所有从未播放过的歌曲（即"lastPlayTime==0"的歌）
        for(int i = recentlyPlayList.size()-1; i>=0; i--){
            if(recentlyPlayList.get(i).getLastPlayTime() == NEVER_PLAY){
                recentlyPlayList.remove(i);
            }
        }
        return recentlyPlayList;
    }

//    @Override
//    protected BaseAdapter getListViewAdapter(List<Song> dataList) {
//        return new SongListFragmentAdapter(this, dataList);
//    }

    /*获取数据相关代码__end*/

    @Override
    protected void onCollectionSong(int position) {
        MyApplication.showToast((BaseActivity) getActivity(), "收藏成功");
    }

    @Override
    protected void onCancelCollectionSong(int position) {
        MyApplication.showToast((BaseActivity) getActivity(), "取消收藏");
    }

    @Override
    protected void onDeleteSong(int position) {
        if(position != VALUES_DEFAULT_POSITION){
            dataList.remove(position);
        }
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size()+"首歌");
        baseAdapter.notifyDataSetChanged();
        MyApplication.showToast((BaseActivity) getActivity(), "删除成功");
    }

    @Override
    protected void onUpdateSongInfo(int position) {
        baseAdapter.notifyDataSetChanged();
    }

    //返回数据载入时的提示界面
    @Override
    protected View getLoadingHintView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.loading_layout, null);
    }

    //返回没有数据时的提示界面
    @Override
    protected View getListViewEmptyView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_no_recently_play, null);
    }

    @Override
    protected View getListViewFooterView() {
        if(dataList == null || dataList.size()==0){
            return null;
        }
        if(footerView == null) {
            footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view, null);
        }
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size()+"首歌曲");
        return footerView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        playController = (PlayController)getActivity();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        playController.play(RecentlyPlayListFragment.class.getSimpleName(), dataList, position);
    }
}
