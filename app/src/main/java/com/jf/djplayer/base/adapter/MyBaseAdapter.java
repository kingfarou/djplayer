package com.jf.djplayer.base.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by jf on 2016/8/29.
 * 项目所有"ListView"、"GridView"所用适配器的基类
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    protected List<T> dataList;

    @Override
    public int getCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
