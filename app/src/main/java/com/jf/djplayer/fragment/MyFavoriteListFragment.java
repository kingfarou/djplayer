package com.jf.djplayer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;


import com.jf.djplayer.SongInfo;
import com.jf.djplayer.adapter.ExpandableFragmentAdapter;
import com.jf.djplayer.customview.ListViewPopupWindows;
import com.jf.djplayer.interfaces.PlayControls;
import com.jf.djplayer.interfaces.SongInfoObserver;
import com.jf.djplayer.tool.database.SongInfoOpenHelper;
import com.jf.djplayer.tool.sortable.SortBySingerName;
import com.jf.djplayer.R;
import com.jf.djplayer.broadcastreceiver.UpdateUiSongInfoReceiver;
import com.jf.djplayer.tool.sortable.SongInfoListSortable;
import com.jf.djplayer.tool.sortable.SortBySongName;

import java.util.List;

/**
 * Created by Administrator on 2015/8/28.
 * 这个Fragment
 * 用来展示所有用户收藏歌曲
 */
public class MyFavoriteListFragment extends BaseExpandableListFragment
        implements ExpandableListView.OnGroupClickListener, SongInfoObserver{

//    private Context context = null;
    private SongInfoListSortable SongInfoListSortable;
    private PlayControls playControls;
    private View footerView;
    private UpdateUiSongInfoReceiver updateUiSongInfoReceiver;
    private List<SongInfo> favoriteList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        playControls = (PlayControls)activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter updateUiFilter = new IntentFilter();
        updateUiFilter.addAction(UpdateUiSongInfoReceiver.ACTION_COLLECTION_SONG);
        updateUiFilter.addAction(UpdateUiSongInfoReceiver.ACTION_CANCEL_COLLECTION_SONG);
        updateUiFilter.addAction(UpdateUiSongInfoReceiver.ACTION_DELETE_SONG_FILE);
        updateUiSongInfoReceiver = new UpdateUiSongInfoReceiver(this);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateUiSongInfoReceiver, updateUiFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateUiSongInfoReceiver);
    }

    @Override
    protected void initBeforeReturnView() {

    }

    @Override
    protected List readData() {
        favoriteList = new SongInfoOpenHelper(getActivity(),1).getCollectionSongInfo();
        return favoriteList;
    }

    @Override
    protected View getExpandableLoadingView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.loading_layout,null);
    }

    @Override
    protected View getExpandableNoDataView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_no_favourites,null);
    }

    @Override
    protected void asyncReadDataFinished() {
//        如果没有读到数据直接返回
//        if(dataList==null) {
//            return;
//        }
//        favoriteList = (List<SongInfo>)dataList;
    }

    @Override
    protected BaseExpandableListAdapter getExpandableAdapter() {
//        return new ExpandableFragmentAdapter(getActivity(), expandableListView, favoriteList);
        return new ExpandableFragmentAdapter(getActivity(), favoriteList);
    }

    public ListViewPopupWindows getListViewPopupWindow() {
        ListViewPopupWindows listViewPopupWindows = new ListViewPopupWindows(getActivity(),new String[]{"按歌曲名排序","按歌手名排序","按添加时间排序"});
        listViewPopupWindows.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                如果用户想要扫描音乐
                if (position == 0) {
                    SongInfoListSortable = new SortBySongName();
                } else if (position == 1) {
                    SongInfoListSortable = new SortBySingerName();
                } else if (position == 2) {

                }
                SongInfoListSortable.sort(favoriteList);
                expandableFragmentAdapter.notifyDataSetChanged();
                popupWindows.dismiss();
            }
        });
        return listViewPopupWindows;
    }


//    当歌曲的信息被修改了
//    将在这里受到回调
    @Override
    public void updateSongInfo(Intent updateIntent, int position) {
        collapseGroup(position);//现将所操作的栏目收起
        switch(updateIntent.getAction()){
            case UpdateUiSongInfoReceiver.ACTION_CANCEL_COLLECTION_SONG:
                favoriteList.remove(position);//从集合里移除数据
                ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(favoriteList.size() + "首歌");
                expandableFragmentAdapter.notifyDataSetChanged();//通知界面刷新数据
                break;
            case UpdateUiSongInfoReceiver.ACTION_DELETE_SONG_FILE:
                break;
            default:break;
        }
    }

    @Override
    protected boolean doOnGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        playControls.play(favoriteList,groupPosition);
        return true;
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
        if(favoriteList==null) {
            return null;
        }
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(favoriteList.size()+"首歌");
        return footerView;
    }
}
