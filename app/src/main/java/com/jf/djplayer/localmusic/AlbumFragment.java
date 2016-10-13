package com.jf.djplayer.localmusic;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jf.djplayer.search.SearchedDataProvider;
import com.jf.djplayer.songscan.ScanSongActivity;
import com.jf.djplayer.songscan.ScanningSongActivity;
import com.jf.djplayer.view.ListViewPopupWindows;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.R;

import java.util.List;
import java.util.Map;

/**
 * Created by JF on 2016/1/29.
 * 本地音乐-专辑列表
 */
public class AlbumFragment extends LocalMusicListFragment
                implements SearchedDataProvider{

    private static final int REQUEST_CODE_SCAN_MUSIC = 1;//扫描音乐的请求码
    private ListViewPopupWindows mListViewPopupWindows;
    private View footerView;//"ListView"的"footerView"

    private static final String KEY_ALBUM_SORT_ACCORDING = AlbumFragment.class.getSimpleName()+"albumSortAccording";
    private static final int VALUES_ALBUM_SORT_ACCORDING_NO_THING = 1;
    private static final int VALUES_ALBUM_SORT_ACCORDING_NAME = 1<<1;
    private static final int VALUES_ALBUM_SORT_ACCORDING_SONG_NUMBER = 1<<2;

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
                getParentFragment().startActivityForResult(new Intent(getActivity(), ScanSongActivity.class), REQUEST_CODE_SCAN_MUSIC);

            }
        });
        return noDataView;
    }

    @Override
    protected List<Map<String, String>> getData() {
        dataList = new SongInfoOpenHelper(getActivity()).getValueSongNumber(SongInfoOpenHelper.album);
        int sortBy = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(KEY_ALBUM_SORT_ACCORDING, VALUES_ALBUM_SORT_ACCORDING_NO_THING);
        if(sortBy == VALUES_ALBUM_SORT_ACCORDING_NAME){
            sortAccordingTitle();
        }else if(sortBy == VALUES_ALBUM_SORT_ACCORDING_SONG_NUMBER){
            sortAccordingContent();
        }
        return dataList;
    }

    @Override
    protected BaseAdapter getListViewAdapter(List<Map<String, String>> dataList) {
        return new LocalMusicListAdapter(getActivity(), dataList);
    }

    @Override
    protected View getListViewFooterView(){
        if(dataList==null){
            return null;
        }
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size()+"专辑");
        return footerView;
    }

    public ListViewPopupWindows getListViewPopupWindow(){
        Resources resources = getResources();
        mListViewPopupWindows = new ListViewPopupWindows(getActivity(),new String[]{resources.getString(R.string.scan_music), resources.getString(R.string.sort_by_album), resources.getString(R.string.sort_by_song_num)});
        mListViewPopupWindows.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    //点击的是扫描音乐
                    getParentFragment().startActivityForResult(new Intent(getActivity(), ScanSongActivity.class), REQUEST_CODE_SCAN_MUSIC);
                }else if(position == 1){
                    //点击按照专辑名字排序
                    sortAccordingTitle();
                    getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(KEY_ALBUM_SORT_ACCORDING, VALUES_ALBUM_SORT_ACCORDING_NAME).commit();
                    baseAdapter.notifyDataSetChanged();
                }else if(position == 2){
                    //点击按照歌曲数量排序
                    sortAccordingContent();
                    getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(KEY_ALBUM_SORT_ACCORDING, VALUES_ALBUM_SORT_ACCORDING_SONG_NUMBER).commit();
                    baseAdapter.notifyDataSetChanged();
                }
                mListViewPopupWindows.dismiss();
            }
        });
        return mListViewPopupWindows;
    }

    @Override
    protected void readDataFinish(List<Map<String, String>> dataList) {
        if(dataList==null) {
            return;
        }
//        this.dataList = dataList;
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size() + "专辑");
    }

    /**
     * 返回待搜索的数据集合，本对象是专辑集合
     * @return 专辑集合
     */
    @Override
    public List returnSearchedDataList() {
        return dataList;
    }
}
