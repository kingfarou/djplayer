package com.jf.djplayer.classifyshowsong;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.adapter.SongListFragmentAdapter;
import com.jf.djplayer.base.fragment.BaseListFragment;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.bean.Song;

import java.util.List;

/**
 * Created by jf on 2016/7/14.
 * 分类显示-歌曲列表
 */
public class ClassifySongListFragment extends BaseListFragment<Song>{

    private View footerView;//"ExpandableListView"的"FooterView"
    private String columnName;//需要显示的类的字段名
    private String columnValues;//需要显示的类的字段具体值

    @Override
    protected List<Song> getData() {
        columnName = getArguments().getString(ClassifySongFragment.COLUMN_NAME);
        columnValues = getArguments().getString(ClassifySongFragment.COLUMN_VALUES);
        return new SongInfoOpenHelper(getActivity()).getClassifySongInfo(columnName, columnValues);
    }

    @Override
    protected BaseAdapter getListViewAdapter(List<Song> dataList) {
        return new SongListFragmentAdapter(this, dataList);
    }

    @Override
    protected View getListViewFooterView() {
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view, null);
        if(dataList == null){
            return null;
        }
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size()+"首歌");
        return footerView;
    }
}
