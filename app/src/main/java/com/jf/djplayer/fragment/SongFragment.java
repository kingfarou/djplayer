package com.jf.djplayer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jf.djplayer.SongInfo;
import com.jf.djplayer.R;
import com.jf.djplayer.activity.ScanningSongActivity;
import com.jf.djplayer.adapter.ExpandableFragmentAdapter;
import com.jf.djplayer.broadcastreceiver.UpdateUiSongInfoReceiver;
import com.jf.djplayer.interfaces.PlayControls;
import com.jf.djplayer.tool.database.SongInfoOpenHelper;
import com.jf.djplayer.tool.sortable.SongInfoListSortable;
import com.jf.djplayer.tool.sortable.SortByFileName;
import com.jf.djplayer.tool.sortable.SortBySingerName;
import com.jf.djplayer.tool.sortable.SortBySongName;


import java.util.List;

/**
 * Created by Administrator on 2015/9/14.
 * 如果用户有收藏有歌曲
 * 这个Fragment将被加载
 */
public class SongFragment extends ExpandableFragment implements ExpandableListView.OnGroupClickListener{

//    private SongInfoListSortable songListCollator;
    private PlayControls playControls;
    private SongInfoListSortable SongInfoListSortable;
    private View noDataView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        playControls = (PlayControls)activity;//将活动转换成为播放控制者
    }

    //    实现超类所定义的抽象方法
//    这是子类所获取的数据
    @Override
    protected List<SongInfo> getSongInfoList() {
        List<SongInfo> songInfoList = null;
//        读取自己数据库的所有歌曲
        SongInfoOpenHelper songInfoOpenHelper = new SongInfoOpenHelper(getActivity(),1);
        songInfoList = songInfoOpenHelper.getLocalMusicSongInfo();
        return songInfoList;
    }//getSongInfoList

    /*
    当异步任务执行完，
    这个方法会被回调，
    这里实现自己要做的事
     */
    @Override
    protected void readDataFinish(){
//        如果没有读到任何数据
        if(songInfoList==null) {
            noDataSettings();
            return;
        }
//        如果数据库由歌曲
//            ExpandableListView做初始化
        expandableListView.setOnGroupClickListener(this);
        expandableFragmentAdapter = new ExpandableFragmentAdapter(getActivity(),expandableListView,songInfoList);
//        添加一个底部视图
        if(footerView==null){
            footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer,null);
            expandableListView.addFooterView(footerView);
        }
        ((TextView)footerView.findViewById(R.id.tv_list_footer_number)).setText(songInfoList.size() + "首歌");
        expandableListView.setAdapter(expandableFragmentAdapter);
    }

//    集合里面没数据是调此方法
    private void noDataSettings(){
        noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.local_music_no_song,null);
        ((ViewGroup)layoutView).addView(noDataView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        noDataView.findViewById(R.id.btn_scan_music_localmusic_no_song).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScanningSongActivity.class));
            }
        });
    }


//    这个方法返回当前子类里的
//    PopupWindows的View
    @Override
    protected View getPopupWindowsView() {
//        popupWindows布局文件的初始化
        View popupWindowsView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindows_list_view_fragment,null);//绘制自定义的布局
        ListView popupWindowsListView = (ListView)popupWindowsView.findViewById(R.id.lv_popupWindows_list_view_fragment);//获取ListView
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,
                new String[]{"扫描音乐","按歌曲名排序","按歌手名排序","按添加时间排序","按文件名排序","一键获取词图","被删除的歌曲"});
        popupWindowsListView.setAdapter(arrayAdapter);
        popupWindowsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                如果用户想要扫描音乐
                if (position == 0) {
                    startActivity(new Intent(getActivity(), ScanningSongActivity.class));
                } else if (position == 1 || position == 2 || position == 3 || position == 4) {
//                    如果用户选择任意一类排序方式
//                    根据选项创建不同排序方式
                    if (position == 1) SongInfoListSortable = new SortBySongName();//按歌曲的名字排序
                    else if (position == 2) {
                        SongInfoListSortable = new SortBySingerName();//按歌手的名字排序
                    } else if (position == 3) {
                        SongInfoListSortable = new SortBySongName();
                    } else {
                        SongInfoListSortable = new SortByFileName();
                    }
                    SongInfoListSortable.sort(songInfoList);
                    expandableFragmentAdapter.notifyDataSetChanged();
                } else if (position == 5) {
                } else if (position == 6) {
                }
                popupWindows.dismiss();
            }
        });
        return popupWindowsView;
    }

//    当界面上歌曲信息被修改时
//    这个方法会被回调
    @Override
    public void updateSongInfo(String updateAction, int position) {
        expandableListView.collapseGroup(position);
//        如果用户收藏歌曲
        if(updateAction.equals(UpdateUiSongInfoReceiver.COLLECTION_SONG)){
            Toast.makeText(getActivity(),"收藏成功",Toast.LENGTH_SHORT).show();
        }else if(updateAction.equals(UpdateUiSongInfoReceiver.CANCEL_COLLECTION_SONG)){
//            如果用户取消收藏
            Toast.makeText(getActivity(),"取消收藏",Toast.LENGTH_SHORT).show();
        }else if(updateAction.equals(UpdateUiSongInfoReceiver.DELETE_SONG)){
//            如果用户删除歌曲
            songInfoList.remove(position);//从集合里移除歌曲
            expandableFragmentAdapter.notifyDataSetChanged();//通知界面更新数据
//            更新底下所显示的歌曲数量
            ((TextView)footerView.findViewById(R.id.tv_list_footer_number)).setText(songInfoList.size() + "首歌");
        }
    }

    /*
        关于监听“ExpandableListView”点击事件，
        需要注意不要监听栏目展开动作，
        因为这个动作已在超类里面监听好了
         */
    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        playControls.play(songInfoList, groupPosition);//传入当前播放列表以及用户所点击的位置
        return true;
    }


}
