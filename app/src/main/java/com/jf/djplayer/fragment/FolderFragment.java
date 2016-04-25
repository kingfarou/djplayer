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

import com.jf.djplayer.activity.ScanningSongActivity;
import com.jf.djplayer.R;
import com.jf.djplayer.activity.ScanMusicActivity;

import com.jf.djplayer.adapter.ListViewFragmentAdapter;
import com.jf.djplayer.basefragment.BaseListFragmentInterface;
import com.jf.djplayer.customview.ListViewPopupWindows;
import com.jf.djplayer.database.SongInfoOpenHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by JF on 2016/1/29.
 */
public class FolderFragment extends BaseListFragmentInterface {

    private View footerView;
    private List<Map<String,String>> folderList;

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
        noDataView.findViewById(R.id.btn_localmusic_nosong_keyscan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScanningSongActivity.class));
            }
        });
        return noDataView;
    }

    @Override
    protected List getData() {
        return new SongInfoOpenHelper(getActivity()).getValueSongNumber(SongInfoOpenHelper.folderPath);
    }

    @Override
    protected BaseAdapter getListViewAdapter(List dataList) {
        return new ListViewFragmentAdapter(getActivity(), (List<Map<String,String>>)dataList);

    }

    @Override
    protected View getListViewFooterView() {
        if(folderList==null) {
            return null;
        }
//        如果有数据则返回对应"footerView"
//        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
//        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(folderList.size()+"文件夹");
        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(folderList.size()+"文件夹");
        return footerView;
    }

    public ListViewPopupWindows getListViewPopupWindow(){
        ListViewPopupWindows listViewPopupWindows = new ListViewPopupWindows(getActivity(),new String[]{"扫描音乐","按文件夹排序","按歌曲数量排序"});
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
        folderList = dataList;
    }

    @Override
    protected void doListViewOnItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void doListViewOnItemLongClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
