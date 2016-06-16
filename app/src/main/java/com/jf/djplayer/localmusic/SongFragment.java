package com.jf.djplayer.localmusic;

import android.app.Activity;
import android.content.Context;
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

import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.base.baseactivity.BaseActivity;
import com.jf.djplayer.base.baseadapter.DefExpandFragmentAdapter;
import com.jf.djplayer.module.SongInfo;
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

    //歌曲显示顺序相关变量
    private static final String KEY_SONG_SORT_BY = SongFragment.class.getSimpleName()+"_songSortBy";//存储歌曲排序方式的键
    private static final int VALUES_SONG_SORT_BY_NO = 1;//没有任何排序方式（不需排序）
    private static final int VALUES_SONG_SORT_BY_SONG_NAME = 1<<1;//按照歌曲名称排序
    private static final int VALUES_SONG_SORT_BY_SINGER_NAME = 1<<2;//按照歌手名称排序
    private static final int VALUES_SONG_SORT_BY_ADD_TIME = 1<<3;//按照添加时间排序

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //动态注册广播接收，用来监听用户对歌曲信息的修改操作
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
            if(requestCode == REQUEST_CODE_SCAN_MUSIC){
                //如果是扫描音乐的返回，调用异步任务刷新数据
                refreshDataAsync();
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

    @Override
    protected List getData() {
        //读取数据库里面的所有歌曲数据
        songInfoList = new SongInfoOpenHelper(getActivity()).getLocalMusicSongInfo();
        //根据文件里保存的排序方式进行排序
        int sortBy = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(KEY_SONG_SORT_BY, VALUES_SONG_SORT_BY_NO);
        SongInfoListSortable songInfoListSortable = null;
        if(sortBy == VALUES_SONG_SORT_BY_SONG_NAME){
            songInfoListSortable = new SortBySongName();
        }else if(sortBy == VALUES_SONG_SORT_BY_SINGER_NAME){
            songInfoListSortable = new SortBySingerName();
        }
        if(songInfoListSortable!=null){
            songInfoListSortable.sort(songInfoList);
        }
        return songInfoList;
    }//_readData()

    @Override
    protected void finishGetData(List dataList){
        //异步任务结束之后，刷新列表尾部所显示的歌曲数量
        if( footerView!=null && songInfoList!=null ){
            ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(songInfoList.size()+"首歌");
        }
    }

    @Override
    protected BaseExpandableListAdapter getExpandableAdapter() {
        return new DefExpandFragmentAdapter(this, songInfoList);
    }

    //点击"ExpandableListView"栏目，播放所对应的歌曲
    @Override
    protected boolean doOnGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//        playController.play(songInfoList, groupPosition);//传入当前播放列表以及用户所点击的位置
        playController.play(SongFragment.class.getSimpleName(), songInfoList, groupPosition);//传入当前播放列表以及用户所点击的位置
        return true;
    }

    //返回歌曲列表下面的"FooterView"
    @Override
    protected View getExpandableFooterView() {
        if(songInfoList==null){
            return null;
        }
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
        ((TextView) footerView.findViewById(R.id.tv_list_footer_view)).setText(songInfoList.size() + "首歌");
        return footerView;
    }

    public ListViewPopupWindows getListViewPopupWindow() {
        Resources resources = getResources();
        String[] dataString = new String[]{resources.getString(R.string.scan_music), resources.getString(R.string.sort_by_song_name),
                resources.getString(R.string.sort_by_singer_name), resources.getString(R.string.sort_by_add_time),
                resources.getString(R.string.manage_song_batch)};
        final ListViewPopupWindows listPopupWindow = new ListViewPopupWindows(getActivity(), dataString);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0://扫描音乐
                        getParentFragment().startActivityForResult(new Intent(getActivity(), ScanningSongActivity.class), REQUEST_CODE_SCAN_MUSIC);
                        break;
                    case 1://按照歌曲名字排序歌曲
                        songInfoListSortable = new SortBySongName();
                        songInfoListSortable.sort(songInfoList);
                        getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(KEY_SONG_SORT_BY, VALUES_SONG_SORT_BY_SONG_NAME).commit();
                        baseExpandableListAdapter.notifyDataSetChanged();
                        break;
                    case 2://按照歌手名字排序歌曲
                        songInfoListSortable = new SortBySingerName();
                        songInfoListSortable.sort(songInfoList);
                        getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(KEY_SONG_SORT_BY, VALUES_SONG_SORT_BY_SINGER_NAME).commit();
                        baseExpandableListAdapter.notifyDataSetChanged();
                        break;
                    case 3://按照添加时间排序歌曲
                        MyApplication.showToast((BaseActivity)getActivity(),"该功能还未实现");
                        break;
                    case 4://歌曲进行批量管理
                        MyApplication.showToast((BaseActivity)getActivity(),"该功能还未实现");
                        break;
                    default:break;
                }
                listPopupWindow.dismiss();
            }
        });
        return listPopupWindow;
    }

    /*"SongInfoObserver"方法实现_start*/
    @Override
    public void updateSongInfo(Intent updateIntent, int position) {
//        collapseGroup(position);
        expandableListView.collapseGroup(position);
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
    /*"SongInfoObserver"方法实现_end*/

    /*"SearchedDataProvider"方法实现，给外层的容器调用，用来返回当前所显示的内容数据列表*/
    @Override
    public List returnSearchedDataList() {
        return songInfoList;
    }
    /*"SearchedDataProvider"方法实现_end*/
}
