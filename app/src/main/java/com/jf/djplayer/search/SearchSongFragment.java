package com.jf.djplayer.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jf.djplayer.base.adapter.SongListFragmentAdapter;
import com.jf.djplayer.base.fragment.BaseListFragment;
import com.jf.djplayer.module.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jf on 2016/7/14.
 */
public class SearchSongFragment extends BaseListFragment<Song> implements SearcherFragment{

    private List<Song> showList;//这是得到关键字后需展示的数据列表
    //搜索数据的提供者，从搜索数据提供者那里获取待搜索的数据集合
    private SearchedDataProvider searchedDataProvider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected List<Song> getData() {
        List<Song> searchList = searchedDataProvider.returnSearchedDataList();//获取待搜索的数据列表
        //注意这里必须创建新的集合，不可以和"searchedList"指向同样一个集合
        if(searchList != null) showList = new ArrayList<>(searchList.size());
        return searchList;
    }

    @Override
    protected BaseAdapter getListViewAdapter(List<Song> dataList) {
        return new SongListFragmentAdapter(this, dataList);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        searchedDataProvider = (SearchedDataProvider)getActivity();
    }

    //根据输入的关键字搜索数据
    @Override
    public void search(String keyword) {
        //清空原有展示数据列表
        showList.clear();
        //遍历待搜索的数据列表，搜索条件：当歌曲的名字或者歌手名字包含关键字了，即为符合
        for(Song songInfo:dataList){
            if(songInfo.getSongName().contains(keyword) || songInfo.getSingerName().contains(keyword)){
                showList.add(songInfo);
            }
        }
        //遍历完毕更新数据
        ((SongListFragmentAdapter) baseAdapter).setSongList(showList);
        baseAdapter.notifyDataSetChanged();
    }
}
