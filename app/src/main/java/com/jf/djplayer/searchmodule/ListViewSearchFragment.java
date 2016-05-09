package com.jf.djplayer.searchmodule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.jf.djplayer.adapter.ListViewFragmentAdapter;
import com.jf.djplayer.base.basefragment.BaseListFragment;
import com.jf.djplayer.base.basefragment.BaseListFragmentInterface;
import com.jf.djplayer.customview.ListViewPopupWindows;

import java.util.List;
import java.util.Map;

/**
 * Created by JF on 2016/5/8.
 * 以"ListView"方式显示数据的搜索的列表
 */
public class ListViewSearchFragment extends BaseListFragmentInterface {

    private List searchList;//这是待搜索的数据

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initBeforeReturnView() {

    }


    @Override
    protected List getData() {
        //通过"Activity"获取待搜索的数据列表
        searchList = ((ListSearchInterface)getActivity()).getListDatas();
        return searchList;
    }

    @Override
    protected BaseAdapter getListViewAdapter(List dataList) {
        return new ListViewFragmentAdapter(getActivity(), (List<Map<String,String>>)dataList);
    }

    @Override
    public ListViewPopupWindows getListViewPopupWindow() {
        return null;
    }

    @Override
    protected void readDataFinish(List dataList) {

    }

    @Override
    protected void doListViewOnItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void doListViewOnItemLongClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * 从"Activity"获取列表数据用的接口
     */
    public interface ListSearchInterface{
        /**
         * 返回要显示的数据
         * @return
         */
        public List getListDatas();
    }
}
