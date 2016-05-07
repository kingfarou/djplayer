package com.jf.djplayer.localmusicfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jf.djplayer.classifysongshow.ClassifySongFragment;
import com.jf.djplayer.interfaces.ChangeFragment;
import com.jf.djplayer.songscan.ScanningSongActivity;
import com.jf.djplayer.adapter.ListViewFragmentAdapter;
import com.jf.djplayer.basefragment.BaseListFragmentInterface;
import com.jf.djplayer.customview.ListViewPopupWindows;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.R;

import java.util.List;
import java.util.Map;

/**
 * Created by JF on 2016/1/29.
 */
public class SingerFragment extends BaseListFragmentInterface {

    private List<Map<String,String>> singerList;
    private static final int REQUEST_CODE_SCAN_MUSIC = 1;//扫描音乐的请求码
    private ListViewPopupWindows mListViewPopupWindows;
    private View footerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CODE_SCAN_MUSIC){
                refreshDataAsync();
            }
        }//if(resultCode == Activity.RESULT_OK)
    }

    @Override
    protected void initBeforeReturnView() {

    }

    @Override
    protected View getLoadingHintView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.loading_layout,null);
    }

    @Override
    protected View getListViewEmptyView() {
        View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.local_music_no_song,null);
        noDataView.findViewById(R.id.btn_localmusic_nosong_keyscan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragment().startActivityForResult(new Intent(getActivity(), ScanningSongActivity.class), REQUEST_CODE_SCAN_MUSIC);
            }
        });
        return  noDataView ;
    }

    @Override
    protected List getData() {
        singerList = new SongInfoOpenHelper(getActivity()).getValueSongNumber(SongInfoOpenHelper.artist);
        return singerList;
    }

    @Override
    protected BaseAdapter getListViewAdapter(List dataList) {
        return new ListViewFragmentAdapter(getActivity(), (List<Map<String,String>>)dataList);
    }

    @Override
    protected View getListViewFooterView() {
        if(singerList==null){
            return null;
        }
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(singerList.size()+"歌手");
        return footerView;
    }

    public ListViewPopupWindows getListViewPopupWindow(){
        mListViewPopupWindows = new ListViewPopupWindows(getActivity(),new String[]{"扫描音乐","按歌手名排序","按歌曲数量排序"});
        mListViewPopupWindows.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
//                    startActivity(new Intent(getActivity(), ScanMusicActivity.class));
                    getParentFragment().startActivityForResult(new Intent(getActivity(), ScanningSongActivity.class), REQUEST_CODE_SCAN_MUSIC);
                } else if (position == 1) {
                    sortAccordingTitle();
                    listViewAdapter.notifyDataSetChanged();
                } else if (position == 2) {
                    sortAccordingContent();
                    listViewAdapter.notifyDataSetChanged();
                }
                mListViewPopupWindows.dismiss();
            }
        });
        return mListViewPopupWindows;
    }

    @Override
    protected void readDataFinish(List dataList) {
        if(dataList==null){
            return;
        }
        singerList = dataList;
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(singerList.size()+"歌手");

    }


    @Override
    protected void doListViewOnItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    protected void doListViewOnItemLongClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
