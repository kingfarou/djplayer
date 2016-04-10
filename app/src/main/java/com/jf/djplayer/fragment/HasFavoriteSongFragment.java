package com.jf.djplayer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;


import com.jf.djplayer.SongInfo;
import com.jf.djplayer.adapter.ExpandableFragmentAdapter;
import com.jf.djplayer.base.fragment.BaseExpandableListFragment;
import com.jf.djplayer.customview.ListViewPopupWindows;
import com.jf.djplayer.interfaces.PlayControls;
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
public class HasFavoriteSongFragment extends BaseExpandableListFragment implements ExpandableListView.OnGroupClickListener{

//    private Context context = null;
    private SongInfoListSortable SongInfoListSortable;
    private PlayControls playControls;
    private View footerView;

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
    protected List<SongInfo> getSongInfoList() {
        SongInfoOpenHelper collectionOpenHelper = new SongInfoOpenHelper(getActivity(),1);
        return collectionOpenHelper.getCollectionSongInfo();
    }

    @Override
    protected void asyncReadDataFinished() {
//        如果没有读到数据直接返回
        if(songInfoList==null) {
            return;
        }
        //        expandableListView.setGroupIndicator(null);
//        expandableFragmentAdapter = new ExpandableFragmentAdapter(getActivity(),expandableListView,songInfoList);
        //        添加一个底部视图
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer,null);
        ((TextView)footerView.findViewById(R.id.tv_list_footer_number)).setText(songInfoList.size()+"首歌");
        expandableListView.addFooterView(footerView);
//        expandableListView.setAdapter(expandableFragmentAdapter);
    }

    @Override
    public ListViewPopupWindows getListViewPopupWindow() {
        ListViewPopupWindows listViewPopupWindows = new ListViewPopupWindows(getActivity(),new String[]{"按歌曲名排序","按歌手名排序","按添加时间排序"});
        listViewPopupWindows.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                如果用户想要扫描音乐
                if (position == 0) {
                    SongInfoListSortable = new SortBySongName();
                }else if(position==1){
                    SongInfoListSortable = new SortBySingerName();
                }else if(position==2){

                }
                SongInfoListSortable.sort(songInfoList);
                expandableFragmentAdapter.notifyDataSetChanged();
                popupWindows.dismiss();
            }
        });
        return listViewPopupWindows;
    }


//    当歌曲的信息被修改了
//    将在这里受到回调
    @Override
    public void updateSongInfo(String updateAction, int position) {
        expandableListView.collapseGroup(position);//现将所操作的栏目收起
        if(updateAction.equals(UpdateUiSongInfoReceiver.CANCEL_COLLECTION_SONG)){
            songInfoList.remove(position);//从集合里移除数据
            expandableFragmentAdapter.notifyDataSetChanged();//通知界面刷新数据
            ((TextView)footerView.findViewById(R.id.tv_list_footer_number)).setText(songInfoList.size()+"首歌");
        }else if(updateAction.equals(UpdateUiSongInfoReceiver.DELETE_SONG)){

        }
    }

    @Override
    protected void doExpandableOnItemClick(ExpandableListView parent, View v, int groupPosition, long id) {
        playControls.play(songInfoList,groupPosition);
    }

    @Override
    protected void doExpandableItemLongClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected View getExpandableEmptyView() {
        return null;
    }

    //    @Override
//    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
////        Log.i("test","点击播放");
//        playControls.play(songInfoList,groupPosition);//传入当前播放列表以及用户所点击的位置
//        return true;
//    }


}
