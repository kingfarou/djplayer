package com.jf.djplayer.localmusic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jf.djplayer.classifyshowsong.ClassifySongFragment;
import com.jf.djplayer.interfaces.FragmentChanger;
import com.jf.djplayer.search.SearchedDataProvider;
import com.jf.djplayer.songscan.ScanningSongActivity;
import com.jf.djplayer.customview.ListViewPopupWindows;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.R;

import java.util.List;
import java.util.Map;

/**
 * Created by JF on 2016/1/29.
 * 本地音乐-歌手列表
 */
public class SingerFragment extends LocalMusicListFragment implements SearchedDataProvider{

    private static final int REQUEST_CODE_SCAN_MUSIC = 1;//扫描音乐的请求码
    private ListViewPopupWindows mListViewPopupWindows;
    private View footerView;

    //歌手列表排序相关变量
    private static final String KEY_SINGER_SORT_BY = SingerFragment.class.getSimpleName()+"_singerSortBy";
    private static final int VALUES_SINGER_SORT_ACCORDING_NO = 1;//没有任何排序方式（不需排序）
    private static final int VALUES_SINGER_SORT_ACCORDING_SINGER_NAME = 1<<1;//按照歌手名称排序
    private static final int VALUES_SINGER_SORT_ACCORDING_SONG_NUMBER = 1<<2;//按照歌曲数量排序

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
        return  noDataView ;
    }

    @Override
    protected List<Map<String, String>> getData() {
        dataList = new SongInfoOpenHelper(getActivity()).getValueSongNumber(SongInfoOpenHelper.artist);
        int sortBy = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(KEY_SINGER_SORT_BY, VALUES_SINGER_SORT_ACCORDING_NO);
        if(sortBy == VALUES_SINGER_SORT_ACCORDING_SINGER_NAME){
            sortAccordingTitle();
        }else if(sortBy == VALUES_SINGER_SORT_ACCORDING_SONG_NUMBER){
            sortAccordingContent();
        }
        return dataList;
    }

    @Override
    protected BaseAdapter getListViewAdapter(List<Map<String, String>> dataList) {
        return new LocalMusicListAdapter(getActivity(), dataList);
    }

    @Override
    protected View getListViewFooterView() {
        if(dataList==null){
            return null;
        }
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size()+"歌手");
        return footerView;
    }

    public ListViewPopupWindows getListViewPopupWindow(){
        Resources resources = getResources();
        mListViewPopupWindows =
                new ListViewPopupWindows(getActivity(),new String[]{resources.getString(R.string.scan_music),
                        resources.getString(R.string.sort_by_singer_name),
                        resources.getString(R.string.sort_by_song_num)});
        mListViewPopupWindows.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        getParentFragment().startActivityForResult(new Intent(getActivity(), ScanningSongActivity.class), REQUEST_CODE_SCAN_MUSIC);
                        break;
                    case 1:
                        sortAccordingTitle();
                        getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(KEY_SINGER_SORT_BY, VALUES_SINGER_SORT_ACCORDING_SINGER_NAME).commit();
                        baseAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        sortAccordingContent();
                        getActivity().getPreferences(Context.MODE_PRIVATE).edit().putInt(KEY_SINGER_SORT_BY, VALUES_SINGER_SORT_ACCORDING_SONG_NUMBER).commit();
                        baseAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
                mListViewPopupWindows.dismiss();
            }
        });
        return mListViewPopupWindows;
    }

    @Override
    protected void readDataFinish(List dataList) {
        if(dataList == null){
            return;
        }
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size() + "歌手");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            //如果是扫描音乐的返回
            if(requestCode == REQUEST_CODE_SCAN_MUSIC){
                refreshDataAsync();
            }
        }//if(resultCode == Activity.RESULT_OK)
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        //设置"fragment.setArguments()"参数
        Bundle bundle = new Bundle();
        bundle.putString(ClassifySongFragment.COLUMN_NAME, SongInfoOpenHelper.artist);//读取数据库里面的"歌手"字段
        bundle.putString(ClassifySongFragment.COLUMN_VALUES, ((List<Map<String, String>>) dataList).get(position).get("title"));//读取具体哪个歌手
        //将"Bundle"设置到待启动那个"Fragment"
        ClassifySongFragment fragment = new ClassifySongFragment();
        fragment.setArguments(bundle);
        //启动"Fragment"
        ((FragmentChanger)getParentFragment().getActivity()).replaceFragments(fragment);
    }

    @Override
    public List returnSearchedDataList() {
        return dataList;
    }
}
