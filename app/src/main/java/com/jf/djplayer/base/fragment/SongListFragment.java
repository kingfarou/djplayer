package com.jf.djplayer.base.fragment;

import android.app.Activity;
import android.content.Intent;
import android.widget.BaseAdapter;

import com.jf.djplayer.base.adapter.SongListFragmentAdapter;
import com.jf.djplayer.module.Song;

import java.util.List;

/**
 * Created by jf on 2016/7/14.
 * 这是歌曲播放列表的"Fragment"，
 * 包括本地音乐歌曲列表所用"Fragment"，我的最爱列表所用"Fragment"，最近播放列表所用"Fragment"，
 * 在播放列表里面，打开弹窗之后对歌曲的操作，均有"DialogFragment"完成，这些操作成功之后，将反馈给播放列表，
 * 该类封装了对这些反馈信息传递的回调。子类根据需要重写相关方法，即可实现对操作的响应
 */
abstract public class SongListFragment extends BaseListFragment<Song>{

    //用来发给"DialogFragment"用的请求码
    /**收藏歌曲的请求码*/
    public static final int REQUEST_CODE_COLLECTION_SONG = 1<<2;
    /**取消收藏歌曲的请求码*/
    public static final int REQUEST_CODE_CANCEL_COLLECTION_SONG = 1<<3;
    /**这是删除歌曲的请求码*/
    public static final int REQUEST_CODE_DELETE_SONG = 1<<4;
    /**更新歌曲信息的请求码*/
    public static final int REQUEST_CODE_UPDATE_SONG_FILE_INFO = 1<<5;

    /**键，表示被操作的歌曲在原列表里的位置，用于Bundle传递*/
    public static final String KEY_POSITION = "key_position";
    /**默认位置*/
    public static final int VALUES_DEFAULT_POSITION = -1;
    /**键，表示被操作的歌曲对象，用于Bundle传递*/
    public static final String KEY_SONG = "key_song";

    @Override
    protected BaseAdapter getListViewAdapter(List<Song> dataList) {
        //必须指定这一个适配器，他里面有打开弹窗用的代码
        return new SongListFragmentAdapter(this, dataList);
    }

    /*接收来自各个弹窗回调，这些请求的发送在当前"Fragment"的"ListView"的适配器里面完成，
      在"Fragment"里面只要里面只要接收即可*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            //如果用户收藏歌曲，通知子类
            if(requestCode == REQUEST_CODE_COLLECTION_SONG){
                onCollectionSong(data.getIntExtra(KEY_POSITION, VALUES_DEFAULT_POSITION));
                return;
            }
            //如果用户取消收藏歌曲，通知子类
            if(requestCode == REQUEST_CODE_CANCEL_COLLECTION_SONG) {
                onCancelCollectionSong(data.getIntExtra(KEY_POSITION, VALUES_DEFAULT_POSITION));
                return;
            }
            //如果用户删除歌曲，通知子类
            if(requestCode == REQUEST_CODE_DELETE_SONG) {
                onDeleteSong(data.getIntExtra(KEY_POSITION, VALUES_DEFAULT_POSITION));
                return;
            }
            //如果用户修改歌曲信息，通知子类
            if(requestCode == REQUEST_CODE_UPDATE_SONG_FILE_INFO){
                onUpdateSongInfo(data.getIntExtra(KEY_POSITION, VALUES_DEFAULT_POSITION));
            }
        }
    }

    /**
     * 用户点击收藏歌曲时的回调
     * @param position 被收藏的歌曲在列表的位置
     */
    protected void onCollectionSong(int position){
    }

    /**
     * 用户点击取消收藏时的回调
     * @param position 被取消的歌曲在列表的位置
     */
    protected void onCancelCollectionSong(int position){
    }

    /**
     * 用户删除歌曲时的回调
     * @param position 被删除得歌曲在列表的位置
     */
    protected void onDeleteSong(int position){
    }

    /**
     * 用户修改歌曲信息时的回调
     * @param position 被修改得歌曲在列表的位置
     */
    protected void onUpdateSongInfo(int position){
    }
}
