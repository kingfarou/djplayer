package com.jf.djplayer.search;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.jf.djplayer.base.baseadapter.BaseExpandFragmentAdapter;
import com.jf.djplayer.base.basefragment.BaseExpandFragment;
import com.jf.djplayer.module.SongInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JF on 2016/5/8.
 * 查找/搜索歌曲-歌曲列表
 */
public class ExpandListSearchFragment extends BaseExpandFragment
                implements SearcherFragment {

    private List<SongInfo> searchedList;//这是待搜索的数据列表
    private List<SongInfo> showList;//这是得到关键字后需展示的数据列表
    //搜索数据的提供者，从搜索数据提供者那里获取待搜索的数据集合
    private SearchedDataProvider searchedDataProvider;

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
    protected List getData() {
        searchedList = searchedDataProvider.returnSearchedDataList();
        //注意这里必须创建新的集合，不可以和"searchedList"指向同样一个集合
        showList = new ArrayList<>(searchedList.size());
        return searchedList;
    }

    @Override
    protected BaseExpandableListAdapter getExpandableAdapter() {
//        baseExpandableListAdapter = new SongInfoExpandAdapter((BaseActivity)searchedDataProvider, searchedList);
//        baseExpandableListAdapter = new SongInfoExpandAdapter(this, searchedList);
        baseExpandableListAdapter = new ExpandListSearchAdapter(this, searchedList);
        return baseExpandableListAdapter;
    }

    //根据输入的关键字搜索数据
    @Override
    public void search(String keyword) {
        //清空原有展示数据列表
        showList.clear();
        //遍历待搜索的数据列表，搜索条件：当歌曲的名字或者歌手名字包含关键字了，即为符合
        for(SongInfo songInfo:searchedList){
            if(songInfo.getSongName().contains(keyword) || songInfo.getSingerName().contains(keyword)){
                showList.add(songInfo);
            }
        }
        //遍历完毕更新数据
        ((BaseExpandFragmentAdapter)baseExpandableListAdapter).setDataList(showList);
        baseExpandableListAdapter.notifyDataSetChanged();
    }
}
