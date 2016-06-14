package com.jf.djplayer.songplayinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.jf.djplayer.R;
import com.jf.djplayer.base.basefragment.BaseListFragment;
import com.jf.djplayer.interfaces.PlayInfoObserver;
import com.jf.djplayer.interfaces.PlayInfoSubject;
import com.jf.djplayer.module.SongInfo;
import com.jf.djplayer.module.SongPlayInfo;
import com.jf.djplayer.playertool.PlayerOperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jf on 2016/6/12.
 * 播放信息-当前所播放的列表
 */
public class SongPlayListFragment extends BaseListFragment implements PlayInfoObserver{

    private PlayInfoSubject playInfoSubject;

    static final String MAP_TITLE = "title";
    static final String MAP_CONTENT = "content";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playInfoSubject = PlayerOperator.getInstance();
    }

    @Override
    protected List getData() {
        SongPlayInfo songPlayInfo = playInfoSubject.getPlayInfo();
        if(songPlayInfo == null){
            return null;
        }
        List<SongInfo> songInfoList = songPlayInfo.getSongList();
        List<Map<String, String>> mapList = new ArrayList<>(songInfoList.size());
        Map<String, String> songInfoMap;
        for(SongInfo songInfo:songInfoList){
            songInfoMap = new HashMap<>();
            songInfoMap.put(MAP_TITLE, songInfo.getSongName());
            songInfoMap.put(MAP_CONTENT, songInfo.getSingerName());
            mapList.add(songInfoMap);
        }
        return mapList;
    }

    @Override
    protected BaseAdapter getListViewAdapter(List dataList) {
        return new SongPlayListAdapter(getActivity(), dataList);
    }

    @Override
    protected View getListViewEmptyView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_song_play_list_no_song, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        //添加到观察者里面
        playInfoSubject.registerObserver(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //从观察者里面移除
        playInfoSubject.removeObserver(this);
    }

    @Override
    protected void doListViewOnItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.doListViewOnItemClick(parent, view, position, id);
        //点击需要播放选中歌曲
    }

    /*"PlayInfoObserver"方法实现_开始*/
    @Override
    public void updatePlayInfo(SongPlayInfo songPlayInfo) {

    }
    /*"PlayInfoObserver"方法实现_结束*/

}