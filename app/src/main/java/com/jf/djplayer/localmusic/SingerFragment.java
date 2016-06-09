package com.jf.djplayer.localmusic;

import android.app.Activity;
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
import com.jf.djplayer.classifyshowsong.ClassifySongFragment;
import com.jf.djplayer.interfaces.FragmentChanger;
import com.jf.djplayer.search.SearchedDataProvider;
import com.jf.djplayer.songscan.ScanningSongActivity;
import com.jf.djplayer.base.baseadapter.BaseListFragmentAdapter;
import com.jf.djplayer.customview.ListViewPopupWindows;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.R;

import java.util.List;
import java.util.Map;

/**
 * Created by JF on 2016/1/29.
 * 本地音乐-歌手列表
 */
public class SingerFragment extends BaseListFragment
                implements SearchedDataProvider{

    private List<Map<String,String>> singerList;//"ListView"数据集合
    private static final int REQUEST_CODE_SCAN_MUSIC = 1;//扫描音乐的请求码
    private ListViewPopupWindows mListViewPopupWindows;
    private View footerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
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
    protected List getData() {
        singerList = new SongInfoOpenHelper(getActivity()).getValueSongNumber(SongInfoOpenHelper.artist);
        return singerList;
    }

    @Override
    protected BaseAdapter getListViewAdapter(List dataList) {
        return new BaseListFragmentAdapter(getActivity(), (List<Map<String,String>>)dataList);
    }

    @Override
    protected View getListViewFooterView() {
        if(singerList==null){
            return null;
        }
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(singerList.size()+"歌手");
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
                if (position == 0) {
//                    startActivity(new Intent(getActivity(), ScanMusicActivity.class));
                    getParentFragment().startActivityForResult(new Intent(getActivity(), ScanningSongActivity.class), REQUEST_CODE_SCAN_MUSIC);
                } else if (position == 1) {
                    sortAccordingTitle();
                    listViewAdapter.notifyDataSetChanged();
                } else if (position == 2) {
                    sortAccordingContent();
                    listViewAdapter.notifyDataSetChanged();
                }
                mListViewPopupWindows.dismiss();
            }
        });
        return mListViewPopupWindows;
    }

    @Override
    protected void readDataFinish(List dataList) {
        if(singerList == null){
            return;
        }
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(singerList.size()+"歌手");
    }


    @Override
    protected void doListViewOnItemClick(AdapterView<?> parent, View view, int position, long id) {
        //设置"fragment.setArguments()"参数
        Bundle bundle = new Bundle();
        bundle.putString(ClassifySongFragment.COLUMN_NAME, SongInfoOpenHelper.artist);//读取数据库里面的"歌手"字段
        bundle.putString(ClassifySongFragment.COLUMN_VALUES, singerList.get(position).get("title"));//读取具体哪个歌手
        //将"Bundle"设置到待启动那个"Fragment"
        ClassifySongFragment fragment = new ClassifySongFragment();
        fragment.setArguments(bundle);
        //启动"Fragment"
        ((FragmentChanger)getParentFragment().getActivity()).replaceFragments(fragment);
    }

    @Override
    public List returnSearchedDataList() {
        return singerList;
    }
}
