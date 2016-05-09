package com.jf.djplayer.searchmodule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.jf.djplayer.base.basefragment.BaseExpandFragment;
import com.jf.djplayer.base.basefragment.BaseListFragmentInterface;

import java.util.List;

/**
 * Created by JF on 2016/5/8.
 */
public class ExpandListSearchFragment extends BaseExpandFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
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
        return null;
    }

    @Override
    protected void asyncReadDataFinished(List dataList) {

    }

    @Override
    protected BaseExpandableListAdapter getExpandableAdapter() {
        return null;
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
}
