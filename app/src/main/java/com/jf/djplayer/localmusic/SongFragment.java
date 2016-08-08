package com.jf.djplayer.localmusic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.base.baseactivity.BaseActivity;
import com.jf.djplayer.base.basefragment.SongListFragment;
import com.jf.djplayer.base.baseadapter.SongListFragmentAdapter;
import com.jf.djplayer.broadcastreceiver.UpdateUiSongInfoReceiver;
import com.jf.djplayer.customview.ListViewPopupWindows;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.interfaces.SongInfoObserver;
import com.jf.djplayer.module.Song;
import com.jf.djplayer.search.SearchedDataProvider;
import com.jf.djplayer.songscan.ScanSettingActivity;
import com.jf.djplayer.songscan.ScanSongActivity;
import com.jf.djplayer.songscan.ScanningSongActivity;
import com.jf.djplayer.sortable.SongListSortable;
import com.jf.djplayer.sortable.SortBySingerName;
import com.jf.djplayer.sortable.SortBySongName;

import java.util.List;

/**
 * Created by jf on 2016/7/12.
 */
public class SongFragment extends SongListFragment implements SongInfoObserver,SearchedDataProvider {

    //歌曲显示顺序相关变量
    //存储歌曲排序方式的键
    private static final String KEY_SONG_SORT_BY = SongFragment.class.getSimpleName()+"_songSortBy";
    private static final int VALUES_SONG_SORT_BY_NO = 1;//没有任何排序方式（不需排序）
    private static final int VALUES_SONG_SORT_BY_SONG_NAME = 1<<1;//按照歌曲名称排序
    private static final int VALUES_SONG_SORT_BY_SINGER_NAME = 1<<2;//按照歌手名称排序
    private static final int VALUES_SONG_SORT_BY_ADD_TIME = 1<<3;//按照添加时间排序

    //启动其他"Activity"的请求码
    private static final int REQUEST_CODE_SCAN_MUSIC = 1;//扫描音乐
//    public static final int REQUEST_CODE_DELETE_SONG = 1<<1;//删除歌曲

    //其他变量
    private PlayController playController;//当点击了歌曲列表，通过该变量来控制音乐播放
    private View footerView;//显示列表里面有多少条歌曲
    private UpdateUiSongInfoReceiver updateUiSongInfoReceiver;//接收歌曲信息被修改的通知
    private SongListSortable songListSortable;//对列表歌曲进行排序的工具

    /*获取列表数据相关代码__start*/
    @Override
    protected List<Song> getData() {
        //读取数据库里面的所有歌曲数据
        List<Song> songList = new SongInfoOpenHelper(getActivity()).getLocalMusicSongInfo();
        //根据文件里保存的排序方式进行排序
        int sortBy = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(KEY_SONG_SORT_BY, VALUES_SONG_SORT_BY_NO);
        SongListSortable songListSortable = null;
        //根据参数创建不同的排序器
        if(sortBy == VALUES_SONG_SORT_BY_SONG_NAME){
            songListSortable = new SortBySongName();
        }else if(sortBy == VALUES_SONG_SORT_BY_SINGER_NAME){
            songListSortable = new SortBySingerName();
        }
        //执行排序
        if(songListSortable != null){
            songListSortable.sort(songList);
        }
        return songList;
    }

    @Override
    protected BaseAdapter getListViewAdapter(List<Song> dataList) {
        return new SongListFragmentAdapter(this, dataList);
    }
    /*获取列表数据相关代码__end*/

    @Override
    protected View getLoadingHintView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.loading_layout,null);
    }

    @Override
    protected View getListViewEmptyView() {
        View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.local_music_no_song,null);
        noDataView.findViewById(R.id.btn_local_music_no_song_key_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragment().startActivityForResult(new Intent(getActivity(), ScanningSongActivity.class), REQUEST_CODE_SCAN_MUSIC);
            }
        });
        return noDataView ;
    }

    @Override
    protected View getListViewFooterView() {
        if(dataList==null){
            return null;
        }
        if(footerView == null){
            footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
        }
        ((TextView) footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size() + "首歌");
        return footerView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //获取歌曲播放的控制者
        playController = (PlayController)getActivity();
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        //传入当前播放列表以及用户所点击的位置
        playController.play(SongFragment.class.getSimpleName(), dataList, position);
    }

    /*"SongInfoObserver"方法实现_start*/
    @Override
    public void updateSongInfo(Intent updateIntent, int position) {
        String action = updateIntent.getAction();
        //如果用户添加收藏
        if(action.equals(UpdateUiSongInfoReceiver.ACTION_COLLECTION_SONG)){
            MyApplication.showToast((BaseActivity)getActivity(), "收藏成功");
            return;
        }
        //如果用户取消收藏
        if(action.equals(UpdateUiSongInfoReceiver.ACTION_CANCEL_COLLECTION_SONG)){
            MyApplication.showToast((BaseActivity)getActivity(), "取消收藏");
            return;
        }
        //如果用户删除歌曲
        if(action.equals(UpdateUiSongInfoReceiver.ACTION_DELETE_SONG_FILE)){
            dataList.remove(position);//将歌曲从集合里面移除
            //更新底部所显示的歌曲数量
            ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size()+"首歌");
            baseAdapter.notifyDataSetChanged();//让"ExpandableListView"刷新数据
        }
    }
    /*"SongInfoObserver"方法实现_end*/

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_CODE_SCAN_MUSIC){
                //如果是扫描音乐的返回，调用异步任务刷新数据
                refreshDataAsync();
                return;
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
//                    getParentFragment().startActivityForResult(new Intent(getActivity(), ScanningSongActivity.class), REQUEST_CODE_SCAN_MUSIC);
                    getParentFragment().startActivity(new Intent(getActivity(), ScanSongActivity.class));
                } else if(position == 1){
                    //按照歌曲名字排序歌曲
                    songListSortable = new SortBySongName();
                    songListSortable.sort(dataList);
                    getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(KEY_SONG_SORT_BY, VALUES_SONG_SORT_BY_SONG_NAME).commit();
                    baseAdapter.notifyDataSetChanged();
                }else if(position == 2){
                    //按照歌手名字排序歌曲
                    songListSortable = new SortBySingerName();
                    songListSortable.sort(dataList);
                    getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(KEY_SONG_SORT_BY, VALUES_SONG_SORT_BY_SINGER_NAME).commit();
                    baseAdapter.notifyDataSetChanged();
                }else if(position == 3){
                    //按照添加时间排序歌曲
                    MyApplication.showToast((BaseActivity) getActivity(), "该功能还未实现");
                }else if(position == 4){
                    //歌曲进行批量管理
                    MyApplication.showToast((BaseActivity) getActivity(), "该功能还未实现");
                }
                listPopupWindow.dismiss();
//                switch (position) {
//                    case 0://扫描音乐
//                        getParentFragment().startActivityForResult(new Intent(getActivity(), ScanningSongActivity.class), REQUEST_CODE_SCAN_MUSIC);
//                        break;
//                    case 1://按照歌曲名字排序歌曲
//                        songListSortable = new SortBySongName();
//                        songListSortable.sort(dataList);
//                        getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(KEY_SONG_SORT_BY, VALUES_SONG_SORT_BY_SONG_NAME).commit();
//                        baseAdapter.notifyDataSetChanged();
//                        break;
//                    case 2://按照歌手名字排序歌曲
//                        songListSortable = new SortBySingerName();
//                        songListSortable.sort(dataList);
//                        getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(KEY_SONG_SORT_BY, VALUES_SONG_SORT_BY_SINGER_NAME).commit();
//                        baseAdapter.notifyDataSetChanged();
//                        break;
//                    case 3://按照添加时间排序歌曲
//                        MyApplication.showToast((BaseActivity) getActivity(), "该功能还未实现");
//                        break;
//                    case 4://歌曲进行批量管理
//                        MyApplication.showToast((BaseActivity) getActivity(), "该功能还未实现");
//                        break;
//                    default:
//                        break;
//                }
//                listPopupWindow.dismiss();
            }
        });
        return listPopupWindow;
    }

    @Override
    protected void readDataFinish(List<Song> dataList) {
        //异步任务结束之后，刷新列表尾部所显示的歌曲数量
        if( footerView!=null && dataList!=null ){
            ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size()+"首歌");
        }
    }

    @Override
    public List returnSearchedDataList() {
        return dataList;
    }
}
