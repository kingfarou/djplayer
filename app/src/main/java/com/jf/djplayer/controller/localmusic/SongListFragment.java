package com.jf.djplayer.controller.localmusic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.fragment.BaseFragment;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.datamanager.LocalMusicLoader;
import com.jf.djplayer.dialogfragment.SongOperationDialog;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.controller.scansong.ScanSongEntranceActivity;
import com.jf.djplayer.sortable.SongListSortable;
import com.jf.djplayer.sortable.SortBySingerName;
import com.jf.djplayer.sortable.SortBySongName;
import com.jf.djplayer.util.LogUtil;
import com.jf.djplayer.util.ToastUtil;
import com.jf.djplayer.widget.ListViewPopupWindows;

import java.util.List;

/**
 * Created by Kingfar on 2017/10/17.
 * 本地音乐-歌曲列表
 */

public class SongListFragment extends BaseFragment
        implements LocalMusicLoader.LoadListener, AdapterView.OnItemClickListener{

    // 歌曲列表数据排序相关
    private static final String KEY_SONG_SORT_BY = SongListFragment.class.getSimpleName()+"_songSortBy";
    private static final int VALUES_SONG_SORT_BY_NO = 1;//没有任何排序方式（不需排序）
    private static final int VALUES_SONG_SORT_BY_SONG_NAME = 1<<1;//按照歌曲名称排序
    private static final int VALUES_SONG_SORT_BY_SINGER_NAME = 1<<2;//按照歌手名称排序
    private static final int VALUES_SONG_SORT_BY_ADD_TIME = 1<<3;//按照添加时间排序

    private ListView listView;     // 歌曲列表
    private SongListAdapter songListAdapter;
    private View loadingHintView;  // ListView加载提示
    private View emptyView;        // ListView没数据时的提示
    private View footerView; // ListView的footView
    private boolean isDestroyView = false; // 标识Fragment是否已执行onDestroyView()
    private List<Song> songList;  // 歌曲列表
    private PlayController playController;      // 当点击了歌曲列表，通过该变量来控制音乐播放
    private SongListSortable songListSortable;  //对列表歌曲进行排序的工具

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layoutView = inflater.inflate(R.layout.layout_local_music_list, container, false);
        isDestroyView = false;
        initView(layoutView);
        return layoutView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // 宿主Activity转为音乐播放控制器
        playController = (PlayController)getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyView = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == ScanSongEntranceActivity.REQUEST_CODE_SCAN_MUSIC){
                // 如果是扫描音乐的返回，调用异步任务刷新数据
                loadLocalMusic();
            } else if(requestCode == SongOperationDialog.REQUEST_CODE_COLLECTION_SONG && data != null){
                // 收藏歌曲
                ToastUtil.showShortToast(getActivity(), "收藏成功");
            } else if(requestCode == SongOperationDialog.REQUEST_CODE_CANCEL_COLLECTION_SONG && data != null) {
                // 取消收藏某一首歌
                ToastUtil.showShortToast(getActivity(), "取消收藏");
            } else if(requestCode == SongOperationDialog.REQUEST_CODE_DELETE_SONG && data != null) {
                // 删除歌曲
                int position = data.getIntExtra(SongOperationDialog.KEY_POSITION, -1);
                if( position != -1 ) {
                    songList.remove(position);
                }
                ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(songList.size()+"首歌");
                songListAdapter.notifyDataSetChanged();
                ToastUtil.showShortToast(getActivity(), "删除成功");
            } else if(requestCode == SongOperationDialog.REQUEST_CODE_EDIT_SONG_INFO && data != null){
                // 修改歌曲信息
                songListAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 界面菜单栏的功能列表
     * @return
     */
    public ListViewPopupWindows getListViewPopupWindow() {
        Resources resources = getResources();
        final String[] dataString = new String[]{resources.getString(R.string.scan_music), resources.getString(R.string.sort_by_song_name),
                resources.getString(R.string.sort_by_singer_name), resources.getString(R.string.sort_by_add_time),
                resources.getString(R.string.manage_song_batch)};
        final ListViewPopupWindows listPopupWindow = new ListViewPopupWindows(getActivity(), dataString);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    //扫描音乐
                    //position == 0代码暂时作为测试用
                    startActivityForResult(new Intent(getActivity(), ScanSongEntranceActivity.class), ScanSongEntranceActivity.REQUEST_CODE_SCAN_MUSIC);
                } else if(position == 1){
                    //按照歌曲名字排序歌曲
                    songListSortable = new SortBySongName();
                    songListSortable.sort(songList);
                    getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(KEY_SONG_SORT_BY, VALUES_SONG_SORT_BY_SONG_NAME).commit();
                    songListAdapter.notifyDataSetChanged();
                }else if(position == 2){
                    //按照歌手名字排序歌曲
                    songListSortable = new SortBySingerName();
                    songListSortable.sort(songList);
                    getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(KEY_SONG_SORT_BY, VALUES_SONG_SORT_BY_SINGER_NAME).commit();
                    songListAdapter.notifyDataSetChanged();
                }else if(position == 3){
                    //按照添加时间排序歌曲
//                    MyApplication.showToast((BaseActivity) getActivity(), "该功能还未实现");
                    ToastUtil.showShortToast(getActivity(), "该功能还未实现");
                }else if(position == 4){
                    //歌曲进行批量管理
//                    MyApplication.showToast((BaseActivity) getActivity(), "该功能还未实现");
                    ToastUtil.showShortToast(getActivity(), "该功能还未实现");
                }
                listPopupWindow.dismiss();
            }
        });
        return listPopupWindow;
    }

    // 初始化界面
    private void initView(View layoutView){
        // find view
        listView = (ListView)layoutView.findViewById(R.id.lv_layout_local_music_list);
        loadingHintView = layoutView.findViewById(R.id.ll_layout_local_music_list_loading_view);
        emptyView = layoutView.findViewById(R.id.ll_layout_local_music_list_empty_view);
        // 扫描音乐执行按钮
        View scanMusicBtn = emptyView.findViewById(R.id.btn_layout_local_music_list_scan_music);
        scanMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.i("扫描音乐");
                startActivityForResult(new Intent(getActivity(), ScanSongEntranceActivity.class), ScanSongEntranceActivity.REQUEST_CODE_SCAN_MUSIC);
            }
        });
        loadLocalMusic();
    }

    // 读取本地音乐
    private void loadLocalMusic(){
        // 隐藏除了进度条以外的其它View
        listView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        // 加载音乐
        LocalMusicLoader localMusicLoader = new LocalMusicLoader();
        localMusicLoader.setLoadListener(this);
        localMusicLoader.load();
    }

    /****************本地音乐读取器回调接口****************/
    @Override
    public void onSuccess(List<Song> songList) {
        if(isDestroyView){
            // no thing to do
        }else if( songList == null || songList.size() == 0 ){
            this.songList = songList;
            // 数据库没有歌曲
            emptyView.setVisibility(View.VISIBLE);
            loadingHintView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }else{
            this.songList = songList;
            emptyView.setVisibility(View.INVISIBLE);
            loadingHintView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            // 设置ListView
            footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view, null);
            ((TextView) footerView.findViewById(R.id.tv_list_footer_view)).setText(songList.size() + "首歌");
            listView.addFooterView(footerView);
            listView.setOnItemClickListener(this);
            songListAdapter = new SongListAdapter(this, songList);
            listView.setAdapter(songListAdapter);
        }
    }

    @Override
    public void onFailed(Exception exception) {

    }
    /****************本地音乐读取器回调接口****************/

    /****************ListView的Item点击事件****************/
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        //传入当前播放列表以及用户所点击的位置
        playController.play(getClass().getSimpleName(), songList, position);
    }
    /****************ListView的Item点击事件****************/
}
