package com.jf.djplayer.controller.playinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.fragment.BaseFragment;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.interfaces.PlayInfoObserver;
import com.jf.djplayer.interfaces.PlayInfoSubject;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.bean.PlayInfo;
import com.jf.djplayer.backgroundplay.PlayerOperator;

import java.util.List;

/**
 * Created by Kingfar on 2016/6/12.
 * 播放信息-当前所播放的列表
 */
public class PlayListFragment extends BaseFragment implements
        PlayInfoObserver, AdapterView.OnItemClickListener{

    private ListView listView;               // 播放列表
    private PlayListAdapter playListAdapter; // 播放列表适配器
    private View loadingHintView;            // ListView加载提示
    private View emptyView;                  // ListView没数据时的提示
    private View footerView;                 // ListView的footView

    private PlayInfoSubject playInfoSubject;
    private int lastPlayPosition = -1;//保存上一次播放的位置

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playInfoSubject = PlayerOperator.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layoutView = inflater.inflate(R.layout.fragment_play_list, container, false);
        initView(layoutView);
        return layoutView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 添加到观察者里面
        playInfoSubject.registerObserver(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // 从观察者里面移除
        playInfoSubject.removeObserver(this);
    }

    private void initView(View layoutView){
        // find view
        listView = (ListView)layoutView.findViewById(R.id.lv_fragment_play_list);
        loadingHintView = layoutView.findViewById(R.id.ll_fragment_play_list_loading_view);
        emptyView = layoutView.findViewById(R.id.ll_fragment_play_list_empty_view);
        listView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        // 获取播放列表
        PlayInfo playInfo = playInfoSubject.getPlayInfo();
        // 播放列表有歌曲
        if(playInfo != null && playInfo.getSongList() != null && playInfo.getSongList().size() != 0){
            List<Song> songList = playInfo.getSongList();
            listView.setVisibility(View.VISIBLE);
            loadingHintView.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
            // 初始化ListView
            playListAdapter = new PlayListAdapter(getActivity(), songList);
            //将最新的播放位置传递给适配器
            playListAdapter.setPlayingPosition(lastPlayPosition);
            //当前正播放的位置上面显示特殊图标
            View visibleView = listView.getChildAt(playInfoSubject.getPlayInfo().getPlayPosition()-listView.getFirstVisiblePosition());
            if(visibleView!=null){
                visibleView.findViewById(R.id.iv_item_song_play_list_fragment_play_icon).setVisibility(View.VISIBLE);
            }
            listView.setAdapter(playListAdapter);
        }else{
            // 播放列表没有歌曲
            emptyView.setVisibility(View.VISIBLE);
            loadingHintView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        //点击需要播放选中歌曲
        ((PlayController)getActivity()).play(playInfoSubject.getPlayInfo().getPlayListName(), playInfoSubject.getPlayInfo().getSongList(), position);
    }

//    @Override
//    protected void readDataFinish(List dataList) {
//        //将最新的播放位置传递给适配器
//        ((PlayListAdapter) baseAdapter).setPlayingPosition(lastPlayPosition);
//        //当前正播放的位置上面显示特殊图标
//        View visibleView = listView.getChildAt(playInfoSubject.getPlayInfo().getPlayPosition()-listView.getFirstVisiblePosition());
//        if(visibleView!=null){
//            visibleView.findViewById(R.id.iv_item_song_play_list_fragment_play_icon).setVisibility(View.VISIBLE);
//        }
//    }

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
        if(playListAdapter != null){
            playListAdapter.setPlayingPosition(newPlayingPosition);
        }
    }
    /*"PlayInfoObserver"方法实现_结束*/

}
