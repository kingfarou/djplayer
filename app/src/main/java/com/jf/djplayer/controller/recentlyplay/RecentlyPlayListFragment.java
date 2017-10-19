package com.jf.djplayer.controller.recentlyplay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.adapter.SongListFragmentAdapter;
import com.jf.djplayer.base.fragment.BaseFragment;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.datamanager.RecentlyPlayLoader;
import com.jf.djplayer.dialogfragment.SongOperationDialog;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.util.ToastUtil;

import java.util.List;

/**
 * Created by Kingfar on 2017/10/18.
 * 最近播放-歌曲列表
 */

public class RecentlyPlayListFragment extends BaseFragment
        implements RecentlyPlayLoader.RecentlyPlayLoadListener, AdapterView.OnItemClickListener{

    // 歌曲列表数据排序相关
    private static final String KEY_SONG_SORT_BY = RecentlyPlayListFragment.class.getSimpleName()+"_songSortBy";
    private static final int VALUES_SONG_SORT_BY_NO = 1;//没有任何排序方式（不需排序）
    private static final int VALUES_SONG_SORT_BY_SONG_NAME = 1<<1;//按照歌曲名称排序
    private static final int VALUES_SONG_SORT_BY_SINGER_NAME = 1<<2;//按照歌手名称排序
    private static final int VALUES_SONG_SORT_BY_ADD_TIME = 1<<3;//按照添加时间排序

    /** 键，表示被操作的歌曲在原列表里的位置，用于Bundle传递*/
    public static final String KEY_POSITION = "key_position";
    /** 键，表示被操作的歌曲对象，用于Bundle传递*/
    public static final String KEY_SONG = "key_song";

    //请求码
    private static final int REQUEST_CODE_SCAN_MUSIC = 1;//扫描音乐

    private ListView listView;     // 歌曲列表
    private SongListFragmentAdapter songListFragmentAdapter;
    private View loadingHintView;  // ListView加载提示
    private View emptyView;        // ListView没数据时的提示
    private View footerView; // ListView的footView
    private boolean isDestroyView = false; // 标识Fragment是否已执行onDestroyView()
    private List<Song> songList;  // 歌曲列表
    private PlayController playController;      // 当点击了歌曲列表，通过该变量来控制音乐播放

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layoutView = inflater.inflate(R.layout.fragment_recently_play_list, container, false);
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
            if(requestCode == REQUEST_CODE_SCAN_MUSIC){
                // 如果是扫描音乐的返回，调用异步任务刷新数据
                loadRecentlyPlaySong();
            } else if(requestCode == SongOperationDialog.REQUEST_CODE_CANCEL_COLLECTION_SONG && data != null) {
                // 取消收藏某一首歌
                int position = data.getIntExtra(KEY_POSITION, -1);
                songList.remove(position);
                songListFragmentAdapter.setSongList(songList);
                songListFragmentAdapter.notifyDataSetChanged();
                ToastUtil.showShortToast(getActivity(), "取消收藏");
                if(songList.size() == 0){
                    emptyView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                    loadingHintView.setVisibility(View.INVISIBLE);
                }else{
                    ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(songList.size()+"首歌");
                }
            } else if(requestCode == SongOperationDialog.REQUEST_CODE_DELETE_SONG && data != null) {
                // 删除歌曲
                int position = data.getIntExtra(KEY_POSITION, -1);
                songList.remove(position);
                songListFragmentAdapter.setSongList(songList);
                songListFragmentAdapter.notifyDataSetChanged();
                ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(songList.size()+"首歌");
                ToastUtil.showShortToast(getActivity(), "删除成功");
            } else if(requestCode == SongOperationDialog.REQUEST_CODE_EDIT_SONG_INFO && data != null){
                // 修改歌曲信息
                songListFragmentAdapter.notifyDataSetChanged();
            }
        }
    }

    // 初始化界面
    private void initView(View layoutView){
        // find view
        listView = (ListView)layoutView.findViewById(R.id.lv_fragment_recently_play_list);
        loadingHintView = layoutView.findViewById(R.id.loading_hint_view_fragment_recently_play_list);
        emptyView = layoutView.findViewById(R.id.empty_view_fragment_recently_play_list);
        // 扫描音乐执行按钮
        loadRecentlyPlaySong();
    }

    // 读取本地音乐
    private void loadRecentlyPlaySong(){
        // 隐藏除了进度条以外的其它View
        listView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        // 加载音乐
        RecentlyPlayLoader recentlyPlayLoader = new RecentlyPlayLoader();
        recentlyPlayLoader.loadRecentlyPlaySong(this, 8);
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
            songListFragmentAdapter = new SongListFragmentAdapter(this, songList);
            listView.setAdapter(songListFragmentAdapter);
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
