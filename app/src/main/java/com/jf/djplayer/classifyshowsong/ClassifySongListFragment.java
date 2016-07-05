package com.jf.djplayer.classifyshowsong;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.baseadapter.BaseExpandFragmentAdapter;
import com.jf.djplayer.base.basefragment.BaseExpandFragment;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.module.Song;

import java.util.List;

/**
 * Created by JF on 2016/5/1.
 * 分类显示-歌曲列表
 */
public class ClassifySongListFragment extends BaseExpandFragment {

    private View footerView;//"ExpandableListView"的"FooterView"
    private List<Song> songInfoList;//数据
    private String columnName;//需要显示的类的字段名
    private String columnValues;//需要显示的类的字段具体值

    @Override
    protected View getLoadingView() {
        return LayoutInflater.from(getParentFragment().getActivity()).inflate(R.layout.loading_layout, null);
    }

    @Override
    protected List getData() {
        columnName = getArguments().getString(ClassifySongFragment.COLUMN_NAME);
        columnValues = getArguments().getString(ClassifySongFragment.COLUMN_VALUES);
        songInfoList = new SongInfoOpenHelper(getActivity()).getClassifySongInfo(columnName, columnValues);
        return songInfoList;
    }

    @Override
    protected BaseExpandableListAdapter getExpandableAdapter() {
        return new BaseExpandFragmentAdapter(this, songInfoList);
    }

    @Override
    protected View getExpandableFooterView() {
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view, null);
        if(songInfoList == null){
            return null;
        }
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(songInfoList.size()+"首歌");
        return footerView;
    }
}
