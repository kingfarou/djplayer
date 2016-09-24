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
 * 包括本地音乐“歌曲”列表，“我的最爱”列表，“最近播放”列表都使用该"Fragment"，
 * 在播放列表里面，打开弹窗之后对歌曲的操作，均有"DialogFragment"完成，这些操作成功之后，将反馈给播放列表，
 * 该类封装了对这些反馈信息传递的回调。子类根据需要重写相关方法，即可实现对操作的响应
 */
abstract public class SongListFragment extends BaseListFragment<Song>{

    //用来发给"DialogFragment"用的请求码
    /**选择要对歌曲做的操作*/
    public static final int REQUEST_CODE_SELECT_OPERATION = 1<<2;

    /**收藏歌曲的请求码*/
    public static final int REQUEST_CODE_COLLECTION_SONG = 1<<3;

    /**取消收藏歌曲的请求码*/
    public static final int REQUEST_CODE_CANCEL_COLLECTION_SONG = 1<<4;

    /**这是删除歌曲的请求码*/
    public static final int REQUEST_CODE_DELETE_SONG = 1<<5;

    /**更新歌曲信息的请求码*/
    public static final int REQUEST_CODE_EDIT_SONG_INFO = 1<<6;

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

    /*接收来自各个弹窗回调*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            //收藏歌曲
            if(requestCode == REQUEST_CODE_COLLECTION_SONG && data != null){
                onCollectionSong(data.getIntExtra(KEY_POSITION, VALUES_DEFAULT_POSITION));
                return;
            }
            //取消收藏某一首歌
            if(requestCode == REQUEST_CODE_CANCEL_COLLECTION_SONG && data != null) {
                onCancelCollectionSong(data.getIntExtra(KEY_POSITION, VALUES_DEFAULT_POSITION));
                return;
            }
            //删除歌曲
            if(requestCode == REQUEST_CODE_DELETE_SONG && data != null) {
                onDeleteSong(data.getIntExtra(KEY_POSITION, VALUES_DEFAULT_POSITION));
                return;
            }
            //修改歌曲信息
            if(requestCode == REQUEST_CODE_EDIT_SONG_INFO && data != null){
                onUpdateSongInfo(data.getIntExtra(KEY_POSITION, VALUES_DEFAULT_POSITION));
                return;
            }
        }
    }//onActivityResult()

    /**
     * 用户点击收藏歌曲时的回调
     * @param position 被收藏的歌曲在列表的位置
     */
    protected void onCollectionSong(int position){}

    /**
     * 用户点击取消收藏时的回调
     * @param position 被取消的歌曲在列表的位置
     */
    protected void onCancelCollectionSong(int position){}

    /**
     * 用户删除歌曲时的回调
     * @param position 被删除得歌曲在列表的位置
     */
    protected void onDeleteSong(int position){}

    /**
     * 用户修改歌曲信息时的回调
     * @param position 被修改得歌曲在列表的位置
     */
    protected void onUpdateSongInfo(int position){}
}
