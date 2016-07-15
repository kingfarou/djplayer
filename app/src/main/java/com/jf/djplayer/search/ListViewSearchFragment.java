package com.jf.djplayer.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.jf.djplayer.localmusic.LocalMusicListAdapter;
import com.jf.djplayer.base.basefragment.BaseListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by JF on 2016/5/8.
 * 以"ListView"方式显示数据的搜索的列表
 */
public class ListViewSearchFragment extends BaseListFragment<Map<String,String>>
                implements SearcherFragment {

    private List<Map<String,String>> searchedList;//这是待搜索的数据
    private List<Map<String,String>> showList;//用户输入关键字后要显示的数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected List<Map<String, String>> getData() {
        //通过"Activity"获取待搜索的数据列表
        searchedList = ((SearchedDataProvider)getActivity()).returnSearchedDataList();
        //注意这里必须创建新的集合，不可以和"searchedList"共用同样一个集合
        showList = new ArrayList<>(searchedList.size());
        return searchedList;
    }

    @Override
    protected BaseAdapter getListViewAdapter(List<Map<String, String>> dataList) {
        if(baseAdapter == null){
            baseAdapter = new LocalMusicListAdapter(getActivity(), (List<Map<String,String>>)dataList);
        }
        return baseAdapter;
    }

    public void search(String keyWord){
        showList.clear();
        for(Map<String,String> content:searchedList){
            if(content.get("title").contains(keyWord)){
                showList.add(content);
            }
        }
        ((LocalMusicListAdapter) baseAdapter).setData(showList);
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    protected void readDataFinish(List dataList) {

    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return super.onItemLongClick(parent, view, position, id);
    }
}
