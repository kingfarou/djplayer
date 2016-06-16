package com.jf.djplayer.localmusic;

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
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jf.djplayer.base.basefragment.BaseListFragment;
import com.jf.djplayer.search.SearchedDataProvider;
import com.jf.djplayer.songscan.ScanningSongActivity;
import com.jf.djplayer.R;

import com.jf.djplayer.base.baseadapter.DefListFragmentAdapter;
import com.jf.djplayer.customview.ListViewPopupWindows;
import com.jf.djplayer.database.SongInfoOpenHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by JF on 2016/1/29.
 * 本地音乐-文件夹列表
 */
public class FolderFragment extends LocalMusicListFragment
                implements SearchedDataProvider{

    private View footerView;//"ListView"的"footerView"
//    private List<Map<String,String>> folderList;//数据
    private static final int REQUEST_CODE_SCAN_MUSIC = 1;//扫描音乐的请求码
    private ListViewPopupWindows mListViewPopupWindows;

    private static final String KEY_FOLDER_SORT_ACCORDING = FolderFragment.class.getSimpleName()+"folderSortAccording";
    private static final int VALUES_FOLDER_SORT_ACCORDING_NO_THING = 1;
    private static final int VALUES_FOLDER_SORT_ACCORDING_NAME = 1<<1;
    private static final int VALUES_FOLDER_SORT_ACCORDING_SONG_NUMBER = 1<<2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CODE_SCAN_MUSIC){
                refreshDataAsync();
            }
        }//if(resultCode == Activity.RESULT_OK)
    }


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
        return noDataView;
    }

    @Override
    protected List getData() {
        dataList = new SongInfoOpenHelper(getActivity()).getValueSongNumber(SongInfoOpenHelper.folderPath);
        int sortBy = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(KEY_FOLDER_SORT_ACCORDING, VALUES_FOLDER_SORT_ACCORDING_NO_THING);
        if(sortBy == VALUES_FOLDER_SORT_ACCORDING_NAME){
            sortAccordingTitle();
        }else if(sortBy == VALUES_FOLDER_SORT_ACCORDING_SONG_NUMBER){
            sortAccordingContent();
        }
        return dataList;
    }

    @Override
    protected BaseAdapter getListViewAdapter(List dataList) {
        return new DefListFragmentAdapter(getActivity(), (List<Map<String,String>>)dataList);

    }

    @Override
    protected View getListViewFooterView() {
        if(dataList==null) {
            return null;
        }
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size()+"文件夹");
        return footerView;
    }

    public ListViewPopupWindows getListViewPopupWindow(){
        Resources resources = getResources();
        mListViewPopupWindows =
                new ListViewPopupWindows(getActivity(),new String[]{resources.getString(R.string.scan_music),
                        resources.getString(R.string.sort_by_folder_name),
                        resources.getString(R.string.sort_by_song_num)});
        mListViewPopupWindows.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        getParentFragment().startActivityForResult(new Intent(getActivity(), ScanningSongActivity.class), REQUEST_CODE_SCAN_MUSIC);
                        break;
                    case 1:
                        sortAccordingTitle();
                        getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(KEY_FOLDER_SORT_ACCORDING, VALUES_FOLDER_SORT_ACCORDING_NAME).commit();
                        listViewAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        sortAccordingContent();
                        getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(KEY_FOLDER_SORT_ACCORDING, VALUES_FOLDER_SORT_ACCORDING_SONG_NUMBER).commit();
                        listViewAdapter.notifyDataSetChanged();
                        break;
                    default:break;
                }
//                if (position == 0) {
//                    getParentFragment().startActivityForResult(new Intent(getActivity(), ScanningSongActivity.class), REQUEST_CODE_SCAN_MUSIC);
//                } else if (position == 1) {
//                    sortAccordingTitle();
//                    listViewAdapter.notifyDataSetChanged();
//                } else if (position == 2) {
//                    sortAccordingContent();
//                    listViewAdapter.notifyDataSetChanged();
//                }
                mListViewPopupWindows.dismiss();
            }
        });
        return mListViewPopupWindows;
    }

    @Override
    protected void readDataFinish(List dataList) {
        if(dataList==null){
            return;
        }
        dataList = dataList;
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size() + "文件夹");
    }

    @Override
    public List returnSearchedDataList() {
        return dataList;
    }
}
