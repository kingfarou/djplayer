package com.jf.djplayer.search;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.jf.djplayer.base.baseactivity.BaseActivity;
import com.jf.djplayer.base.baseadapter.BaseExpandFragmentAdapter;
import com.jf.djplayer.base.baseadapter.BaseListFragmentAdapter;
import com.jf.djplayer.base.basefragment.BaseExpandFragment;
import com.jf.djplayer.other.SongInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by JF on 2016/5/8.
 */
public class ExpandListSearchFragment extends BaseExpandFragment
                implements SearcherFragment {

    private List<SongInfo> searchedList;//这是待搜索的数据列表
    private List<SongInfo> showList;//这是得到关键字后需展示的数据列表
    private SearchedDataProvider searchedDataProvider;//搜索数据的提供者

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        searchedDataProvider = (SearchedDataProvider)getActivity();
    }

    @Override
    protected void initBeforeReturnView() {

    }

    @Override
    protected View getExpandableLoadingView() {
        return null;
    }

    @Override
    protected View getExpandListEmptyView() {
        return null;
    }

    @Override
    protected List getData() {
        searchedList = searchedDataProvider.returnSearchedDataList();
        //注意这里必须创建新的集合，不可以和"searchedList"指向同样一个集合
        showList = new ArrayList<>(searchedList.size());
        return searchedList;
    }

    @Override
    protected void asyncReadDataFinished(List dataList) {

    }

    @Override
    protected BaseExpandableListAdapter getExpandableAdapter() {
        baseExpandableListAdapter = new BaseExpandFragmentAdapter((BaseActivity)searchedDataProvider, searchedList);
        return baseExpandableListAdapter;
    }

    @Override
    protected boolean doOnGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return false;
    }

    @Override
    protected boolean doExpandableItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    protected View getExpandableHeaderView() {
        return null;
    }

    @Override
    protected View getExpandableFooterView() {
        return null;
    }

    @Override
    public void search(String keyword) {
        showList.clear();
        //遍历待搜索的数据列表
        for(SongInfo songInfo:searchedList){
            if(songInfo.getSongName().contains(keyword) || songInfo.getSingerName().contains(keyword)){
                showList.add(songInfo);
            }
        }
        ((BaseExpandFragmentAdapter)baseExpandableListAdapter).setData(showList);
        baseExpandableListAdapter.notifyDataSetChanged();
    }
}
