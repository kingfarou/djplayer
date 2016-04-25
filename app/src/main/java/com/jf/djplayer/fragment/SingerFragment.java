package com.jf.djplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jf.djplayer.activity.ScanMusicActivity;
import com.jf.djplayer.activity.ScanningSongActivity;
import com.jf.djplayer.adapter.ListViewFragmentAdapter;
import com.jf.djplayer.basefragment.BaseListFragmentInterface;
import com.jf.djplayer.customview.ListViewPopupWindows;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.R;

import java.util.List;
import java.util.Map;

/**
 * Created by JF on 2016/1/29.
 */
public class SingerFragment extends BaseListFragmentInterface {

    private List<Map<String,String>> singerList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initBeforeReturnView() {

    }

    @Override
    protected View getLoadingHintView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.loading_layout,null);
    }

    @Override
    protected View getNoDataHintView() {
        View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.local_music_no_song,null);
//        ((ViewGroup)layoutView).addView(noDataView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        noDataView.findViewById(R.id.btn_localmusic_nosong_keyscan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScanningSongActivity.class));
            }
        });
        return  noDataView ;
    }

    @Override
    protected List getData() {
        return new SongInfoOpenHelper(getActivity()).getValueSongNumber(SongInfoOpenHelper.artist);
    }

    @Override
    protected BaseAdapter getListViewAdapter(List dataList) {
        return new ListViewFragmentAdapter(getActivity(), (List<Map<String,String>>)dataList);
    }

    @Override
    protected View getListViewFooterView() {
        if(singerList==null){
            return null;
        }
//        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
//        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(singerList.size()+"歌手");
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(singerList.size()+"歌手");
        return footerView;
    }

    public ListViewPopupWindows getListViewPopupWindow(){
        final ListViewPopupWindows listViewPopupWindows = new ListViewPopupWindows(getActivity(),new String[]{"扫描音乐","按歌手名排序","按歌曲数量排序"});
        listViewPopupWindows.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(getActivity(), ScanMusicActivity.class));
                } else if (position == 1) {
                    sortAccordingTitle();
                    listViewAdapter.notifyDataSetChanged();
                } else if (position == 2) {
                    sortAccordingContent();
                    listViewAdapter.notifyDataSetChanged();
                }
                popupWindows.dismiss();
            }
        });
        return listViewPopupWindows;
    }

    @Override
    protected void readDataFinish(List dataList) {
        if(dataList==null){
            return;
        }
        singerList = dataList;
    }


    @Override
    protected void doListViewOnItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void doListViewOnItemLongClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
