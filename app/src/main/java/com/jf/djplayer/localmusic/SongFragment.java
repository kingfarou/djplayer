package com.jf.djplayer.localmusic;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jf.djplayer.other.SongInfo;
import com.jf.djplayer.R;
import com.jf.djplayer.search.SearchedDataProvider;
import com.jf.djplayer.songscan.ScanningSongActivity;
import com.jf.djplayer.base.basefragment.BaseExpandFragment;
import com.jf.djplayer.broadcastreceiver.UpdateUiSongInfoReceiver;
import com.jf.djplayer.customview.ListViewPopupWindows;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.interfaces.SongInfoObserver;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.sortable.SongInfoListSortable;
import com.jf.djplayer.sortable.SortByFileName;
import com.jf.djplayer.sortable.SortBySingerName;
import com.jf.djplayer.sortable.SortBySongName;


import java.util.List;

/**
 * Created by Administrator on 2015/9/14.
 * 本地音乐-歌曲列表
 */
public class SongFragment extends BaseExpandFragment
        implements SongInfoObserver, SearchedDataProvider{

    private PlayController playController;
    private SongInfoListSortable songInfoListSortable;//对列表歌曲进行排序的工具
    private UpdateUiSongInfoReceiver updateUiSongInfoReceiver;//接受歌曲信息被修改的通知
    private List<SongInfo> songInfoList;//保存要显示的歌曲信息集合
    private View footerView;

    //当前类所用请求码
    private static final int REQUEST_CODE_SCAN_MUSIC = 1;//扫描音乐的请求码
    public static final int REQUEST_CODE_DELETE_SONG = 1<<1;//删除歌曲的请求码

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //动态注册广播接收
        IntentFilter updateUiFilter = new IntentFilter();
        updateUiFilter.addAction(UpdateUiSongInfoReceiver.ACTION_COLLECTION_SONG);//监听用户收藏歌曲操作
        updateUiFilter.addAction(UpdateUiSongInfoReceiver.ACTION_CANCEL_COLLECTION_SONG);//监听用户取消收藏歌曲操作
        updateUiFilter.addAction(UpdateUiSongInfoReceiver.ACTION_DELETE_SONG_FILE);//监听用户删除歌曲操作
        updateUiSongInfoReceiver = new UpdateUiSongInfoReceiver(this);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateUiSongInfoReceiver, updateUiFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        //注销动态所注册的广播
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateUiSongInfoReceiver);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        playController = (PlayController)activity;//将活动转换成为播放控制者
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == FragmentActivity.RESULT_OK) {
            if(requestCode == REQUEST_CODE_SCAN_MUSIC){//如果是扫描音乐的返回
                refreshDataAsync();
//            }else if(requestCode == REQUEST_CODE_DELETE_SONG){
//                if(data.getIntExtra("position", -1) == -1){
//                    return;
//                }
//                songInfoList.remove(data.getIntExtra("position", -1));//将歌曲从集合里面移除
//                //更新底部所显示的歌曲数量
//                ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(songInfoList.size()+"首歌");
//                baseExpandableListAdapter.notifyDataSetChanged();//让"ExpandableListView"刷新数据
            }
        }
    }

    //返回"ExpandableListView"数据读取完成前的提示界面
    @Override
    protected View getLoadingView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.loading_layout,null);
    }

    //当"ExpandableListView"数据读取完成后且没有数据显式时的提示界面
    @Override
    protected View getExpandListEmptyView() {
        View expandListEmptyView = LayoutInflater.from(getActivity()).inflate(R.layout.local_music_no_song,null);
        //一键扫描按钮点击事件
        expandListEmptyView.findViewById(R.id.btn_local_music_no_song_key_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动"Activity"一键扫描
                getParentFragment().startActivityForResult(new Intent(getActivity(), ScanningSongActivity.class), REQUEST_CODE_SCAN_MUSIC);
            }
        });
        return expandListEmptyView;
    }

    //    提供数据用的方法
    @Override
    protected List getData() {
        songInfoList = new SongInfoOpenHelper(getActivity()).getLocalMusicSongInfo();
        return songInfoList;
    }//_readData()

    /*
    当异步任务执行完，
    这个方法会被回调，
    这里实现自己要做的事
     */
    @Override
    protected void asyncReadDataFinished(List dataList){
        if(footerView!=null&&songInfoList!=null){
            ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(songInfoList.size()+"首歌");
        }
    }

    @Override
    protected BaseExpandableListAdapter getExpandableAdapter() {
//        return new ExpandableFragmentAdapter(getActivity(), expandableListView, songInfoList);
        return new SongFragmentAdapter(this, songInfoList);
    }

    //    "expandableListView"的groupItem被按下时所回调的方法
    @Override
    protected boolean doOnGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        playController.play(songInfoList, groupPosition);//传入当前播放列表以及用户所点击的位置
        return true;
    }

    @Override
    protected View getExpandableFooterView() {
        if(songInfoList==null){
            return null;
        }
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(songInfoList.size() + "首歌");
        return footerView;
    }


    public ListViewPopupWindows getListViewPopupWindow() {
        Resources resources = getResources();
        String[] dataString = new String[]{resources.getString(R.string.scan_music),
                resources.getString(R.string.sort_by_song_name),"按歌手名排序","按添加时间排序","按文件名排序","一键获取词图"};
        final ListViewPopupWindows listPopupWindow = new ListViewPopupWindows(getActivity(),dataString);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                如果用户想要扫描音乐
                if (position == 0) {
                    getParentFragment().startActivityForResult(new Intent(getActivity(), ScanningSongActivity.class), REQUEST_CODE_SCAN_MUSIC);
                } else if (position == 1 || position == 2 || position == 3 || position == 4) {
//                    如果用户选择任意一类排序方式
//                    根据选项创建不同排序方式
                    if (position == 1) {
                        songInfoListSortable = new SortBySongName();//按歌曲的名字排序
                    }
                    else if (position == 2) {
                        songInfoListSortable = new SortBySingerName();//按歌手的名字排序
                    } else if (position == 3) {
                        songInfoListSortable = new SortBySongName();
                    } else {
                        songInfoListSortable = new SortByFileName();
                    }
                    songInfoListSortable.sort(songInfoList);
                    baseExpandableListAdapter.notifyDataSetChanged();
                } else if (position == 5) {

                }
                listPopupWindow.dismiss();
            }
        });
        return listPopupWindow;
    }


    //    当界面上歌曲信息被修改时
//    这个方法会被回调
    @Override
    public void updateSongInfo(Intent updateIntent, int position) {
        collapseGroup(position);
        switch (updateIntent.getAction()){
            //如果用户添加收藏
            case UpdateUiSongInfoReceiver.ACTION_COLLECTION_SONG:
                Toast.makeText(getActivity(),"收藏成功",Toast.LENGTH_SHORT).show();
                break;
            //如果用户取消收藏
            case UpdateUiSongInfoReceiver.ACTION_CANCEL_COLLECTION_SONG:
                Toast.makeText(getActivity(),"取消收藏",Toast.LENGTH_SHORT).show();
                break;
            //如果用户删除歌曲
            case UpdateUiSongInfoReceiver.ACTION_DELETE_SONG_FILE:
                songInfoList.remove(position);//将歌曲从集合里面移除
                //更新底部所显示的歌曲数量
                ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(songInfoList.size()+"首歌");
                baseExpandableListAdapter.notifyDataSetChanged();//让"ExpandableListView"刷新数据
            default:
                break;
        }
    }

    /**
     * 给外层的容器调用，用来返回当前所显示的内容数据列表
     * @return 当前歌曲数据列表
     */
    @Override
    public List returnSearchedDataList() {
        return songInfoList;
    }
}
