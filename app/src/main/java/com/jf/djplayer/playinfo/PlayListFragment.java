package com.jf.djplayer.playinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.jf.djplayer.R;
import com.jf.djplayer.base.fragment.BaseListFragment;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.interfaces.PlayInfoObserver;
import com.jf.djplayer.interfaces.PlayInfoSubject;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.bean.PlayInfo;
import com.jf.djplayer.playertool.PlayerOperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jf on 2016/6/12.
 * 播放信息-当前所播放的列表
 */
public class PlayListFragment extends BaseListFragment implements PlayInfoObserver{

    private PlayInfoSubject playInfoSubject;
    private int lastPlayPosition = -1;//保存上一次播放的位置

    static final String MAP_TITLE = "title";
    static final String MAP_CONTENT = "content";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playInfoSubject = PlayerOperator.getInstance();
    }

    @Override
    protected List getData() {
        PlayInfo playInfo = playInfoSubject.getPlayInfo();
        if(playInfo == null){
            return null;
        }
        List<Song> songInfoList = playInfo.getSongList();
        List<Map<String, String>> mapList = new ArrayList<>(songInfoList.size());
        Map<String, String> songInfoMap;
        for(Song songInfo:songInfoList){
            songInfoMap = new HashMap<>();
            songInfoMap.put(MAP_TITLE, songInfo.getSongName());
            songInfoMap.put(MAP_CONTENT, songInfo.getSingerName());
            mapList.add(songInfoMap);
        }
        return mapList;
    }

    @Override
    protected BaseAdapter getListViewAdapter(List dataList) {
        return new PlayListAdapter(getActivity(), dataList);
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
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        //点击需要播放选中歌曲
        ((PlayController)getActivity()).play(playInfoSubject.getPlayInfo().getPlayListName(), playInfoSubject.getPlayInfo().getSongList(), position);
    }

    @Override
    protected void readDataFinish(List dataList) {
        //将最新的播放位置传递给适配器
        ((PlayListAdapter) baseAdapter).setPlayingPosition(lastPlayPosition);
        //当前正播放的位置上面显示特殊图标
        View visibleView = listView.getChildAt(playInfoSubject.getPlayInfo().getPlayPosition()-listView.getFirstVisiblePosition());
        if(visibleView!=null){
            visibleView.findViewById(R.id.iv_item_song_play_list_fragment_play_icon).setVisibility(View.VISIBLE);
        }
    }

    /*"PlayInfoObserver"方法实现_开始*/
    @Override
    public void updatePlayInfo(PlayInfo playInfo) {
        //实现该方法是为了在正播放的歌曲位置，提供一些特殊显示
        //如果没有歌曲信息，或者没有播放列表，直接返回
        if(playInfo == null || playInfo.getSongList() == null || playInfo.getSongList().size() == 0){
            return;
        }
        int newPlayingPosition = playInfo.getPlayPosition();
        //如果新旧位置是一样的，直接返回
        if(lastPlayPosition == newPlayingPosition){
            return;
        }
        //将原来的位置上的特殊显示给取消掉
        View invisibleView = listView.getChildAt(lastPlayPosition-listView.getFirstVisiblePosition());
        if(invisibleView!=null){
            invisibleView.findViewById(R.id.iv_item_song_play_list_fragment_play_icon).setVisibility(View.INVISIBLE);
        }
        //新的位置上面出现特殊显示
        View visibleView = listView.getChildAt(newPlayingPosition-listView.getFirstVisiblePosition());
        if(visibleView!=null){
            visibleView.findViewById(R.id.iv_item_song_play_list_fragment_play_icon).setVisibility(View.VISIBLE);
        }
        //更新最新一次位置
        lastPlayPosition = newPlayingPosition;
        //将新位置传递给适配器，以便用户在滑动时不会显示错乱，
        //注意这里必须判断适配器是不是空的，因为第一次收到消息时适配器未必加载完了
        if(baseAdapter != null){
            ((PlayListAdapter) baseAdapter).setPlayingPosition(newPlayingPosition);
        }
    }
    /*"PlayInfoObserver"方法实现_结束*/

}
