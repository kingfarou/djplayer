package com.jf.djplayer.base.baseadapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.base.basefragment.SongListFragment;
import com.jf.djplayer.dialogfragment.SongOperationDialog;
import com.jf.djplayer.module.Song;

import java.util.List;

/**
 * Created by jf on 2016/7/14.
 * 显示“歌曲”列表的"Fragment"里的"ListView"所用适配器，
 * 包括本地音乐歌曲列表，我的最爱的列表，最近播放的列表
 */
public class SongListFragmentAdapter extends BaseAdapter{

    //由于在适配器里面需要弹出"DialogFragment"，所以需要外层的"Fragment"
    private Fragment fragment;
    //这是要显示的数据
    private List<Song> songList;

    public SongListFragmentAdapter(Fragment fragment, List<Song> songList){
        this.fragment = fragment;
        this.songList = songList;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView songNameTv;//歌曲名字
        TextView singerTv;//歌手名字
        ImageView operationIv;//弹窗按钮
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_song_list_fragment_adapter, null);
            viewHolder.songNameTv = (TextView)convertView.findViewById(R.id.tv_item_song_fragment_adapter_song_name);
            viewHolder.singerTv = (TextView)convertView.findViewById(R.id.tv_item_song_fragment_adapter_singer);
            viewHolder.operationIv = (ImageView)convertView.findViewById(R.id.iv_item_song_fragment_adapter_operation);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Song song = songList.get(position);
        viewHolder.songNameTv.setText(song.getSongName());
        viewHolder.singerTv.setText(song.getSingerName());
        viewHolder.operationIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //传递被点击的位置，以及该位置的歌曲信息
                Bundle arguments = new Bundle();
                arguments.putInt(SongOperationDialog.KEY_POSITION, position);
                arguments.putSerializable(SongOperationDialog.KEY_SONG, songList.get(position));
                //打开歌曲操作弹窗
                SongOperationDialog songOperationDialog = new SongOperationDialog();
                songOperationDialog.setArguments(arguments);
                /*虽然歌曲操作窗口会有多个操作，但是直接操作只有收藏/取消收藏，其他操作会在其他弹窗，
                  所以这里设置的请求码，直接设置收藏以及收藏以及取消收藏的请求码*/
                if(songList.get(position).getCollection() == Song.IS_COLLECTION){
                    songOperationDialog.setTargetFragment(fragment, SongListFragment.REQUEST_CODE_CANCEL_COLLECTION_SONG);
                }else{
                    songOperationDialog.setTargetFragment(fragment, SongListFragment.REQUEST_CODE_COLLECTION_SONG);
                }
                songOperationDialog.show(fragment.getChildFragmentManager(), "SongOperationDialog");
            }
        });
        return convertView;
    }

    public void setSongList(List<Song> songList){
        this.songList = songList;
    }
}
