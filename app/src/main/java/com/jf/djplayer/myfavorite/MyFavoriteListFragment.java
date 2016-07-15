package com.jf.djplayer.myfavorite;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.base.baseactivity.BaseActivity;
import com.jf.djplayer.base.basefragment.SongListFragment;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.module.Song;

import java.util.List;

/**
 * Created by jf on 2016/7/14.
 * 我的最爱-曲目列表
 */
public class MyFavoriteListFragment extends SongListFragment{

    private PlayController playController;
    private View footerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected List<Song> getData() {
        return new SongInfoOpenHelper(getActivity()).getCollectionSongInfo();
    }

    //设置数据载入时的提示界面
    @Override
    protected View getLoadingHintView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.loading_layout,null);
    }

    //将"Activity"转变为播放控制者
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        playController = (PlayController)getActivity();
    }

    //点击"ListView"里的"item"播放歌曲
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        playController.play(this.getClass().getSimpleName(), dataList, position);
    }

    //"footerView"显示共有几首歌曲
    @Override
    protected View getListViewFooterView() {
        if(dataList==null) {
            return null;
        }
        if(footerView == null) {
            footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view, null);
        }
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size() + "首歌");
        return footerView;
    }

    @Override
    protected View getListViewEmptyView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_no_favourites, null);
    }

    //如果用户收藏歌曲
    @Override
    protected void onCancelCollectionSong(int position) {
        dataList.remove(position);
        baseAdapter.notifyDataSetChanged();
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size() + "首歌");
        MyApplication.showToast((BaseActivity) getActivity(), "取消收藏");
    }

    //如果用户删除歌曲
    @Override
    protected void onDeleteSong(int position) {
        dataList.remove(position);
        baseAdapter.notifyDataSetChanged();
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(dataList.size() + "首歌");
        MyApplication.showToast((BaseActivity)getActivity(), "删除成功");
    }

    //如果用户修改歌曲信息
    @Override
    protected void onUpdateSongInfo(int position) {
        baseAdapter.notifyDataSetChanged();
    }
}
