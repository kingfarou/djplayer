package com.jf.djplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jf.djplayer.activity.ScanningSongActivity;
import com.jf.djplayer.R;
import com.jf.djplayer.activity.ScanMusicActivity;
import com.jf.djplayer.adapter.ListViewFragmentAdapter;

import com.jf.djplayer.tool.database.SongInfoOpenHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by JF on 2016/1/29.
 */
public class FolderFragment extends ListViewFragment {

    private View noDataView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected List<Map<String, String>> getData() {
        List<Map<String,String>> dataList = null;
        dataList = new SongInfoOpenHelper(getActivity(),1).getValueSongNumber(SongInfoOpenHelper.folderPath);
        return dataList;
    }

    @Override
    protected View getPopupWindowView() {

//        popupWindows布局文件的初始化
        View popupWindowsView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindows_list_view_fragment,null);//绘制自定义的布局
        ListView popupWindowsListView = (ListView)popupWindowsView.findViewById(R.id.lv_popupWindows_list_view_fragment);//获取ListView
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,new String[]{"扫描音乐","按文件夹排序","按歌曲数量排序"});
        popupWindowsListView.setAdapter(arrayAdapter);
        popupWindowsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    startActivity(new Intent(getActivity(),ScanMusicActivity.class));
                }else if(position==1){
                    sortAccordingTitle();
                    listViewFragmentAdapter.notifyDataSetChanged();
                }else if(position == 2){
                    sortAccordingContent();
                    listViewFragmentAdapter.notifyDataSetChanged();
                }
                popupWindows.dismiss();
            }
        });
        return popupWindowsView;
    }

    @Override
    protected void readDataFinish() {
        if(dataList==null){
            noDataSettings();
            return;
        }
//        先将数据设置给适配器
        listViewFragmentAdapter = new ListViewFragmentAdapter(getActivity(), dataList);
//        添加footerView
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer,null);
        ((TextView)footerView.findViewById(R.id.tv_list_footer_number)).setText(dataList.size()+"文件夹");
        listView.addFooterView(footerView);
//        设置适配器和点击事件
        listView.setAdapter(listViewFragmentAdapter);
        listView.setOnItemClickListener(this);
    }

    private void noDataSettings(){
        noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.local_music_no_song,null);
        ((ViewGroup)layoutView).addView(noDataView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        noDataView.findViewById(R.id.btn_scan_music_localmusic_no_song).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScanningSongActivity.class));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
