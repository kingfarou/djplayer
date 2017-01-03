package com.jf.djplayer.base.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.base.fragment.SongListFragment;
import com.jf.djplayer.dialogfragment.SongOperationDialog;
import com.jf.djplayer.bean.Song;

import java.util.List;

/**
 * Created by jf on 2016/7/14.
 * 显示“歌曲”列表的"Fragment"里的"ListView"所用适配器，
 * 包括本地音乐歌曲列表，我的最爱的列表，最近播放的列表
 */
public class SongListFragmentAdapter extends MyBaseAdapter<Song> implements View.OnClickListener{

    //由于在适配器里面需要弹出"DialogFragment"，所以需要外层的"Fragment"
    private Fragment fragment;

    public SongListFragmentAdapter(Fragment fragment, List<Song> songList){
        this.fragment = fragment;
        this.dataList = songList;
    }

    private class ViewHolder{
        TextView songNameTv;        //歌曲名字
        TextView singerTv;          //歌手名字
        ImageView operationIv;      //弹窗按钮
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
        Song song = dataList.get(position);
        viewHolder.songNameTv.setText(song.getSongName());
        viewHolder.singerTv.setText(song.getSingerName());
        viewHolder.operationIv.setTag(position);
        viewHolder.operationIv.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        //点击弹窗的按钮
        if(view.getId() == R.id.iv_item_song_fragment_adapter_operation){
            //获取被点击的位置
            int position = (int)view.getTag();
            //传递被点击的位置，以及该位置的歌曲信息
            Bundle arguments = new Bundle();
            arguments.putInt(SongOperationDialog.KEY_POSITION, position);
            arguments.putSerializable(SongOperationDialog.KEY_SONG, dataList.get(position));
            //打开歌曲操作弹窗
            SongOperationDialog songOperationDialog = new SongOperationDialog();
            songOperationDialog.setArguments(arguments);
//            /*虽然歌曲操作窗口会有多个操作，但是直接操作只有收藏/取消收藏，其他操作会在其他弹窗，
//              所以这里设置的请求码，直接设置收藏以及收藏以及取消收藏的请求码*/
//            if(dataList.get(position).getCollection() == Song.IS_COLLECTION){
//                songOperationDialog.setTargetFragment(fragment, SongListFragment.REQUEST_CODE_CANCEL_COLLECTION_SONG);
//            }else{
//                songOperationDialog.setTargetFragment(fragment, SongListFragment.REQUEST_CODE_COLLECTION_SONG);
//            }
            songOperationDialog.setTargetFragment(fragment, SongListFragment.REQUEST_CODE_SELECT_OPERATION);
            songOperationDialog.show(fragment.getChildFragmentManager(), "SongOperationDialog");
            return;
        }
    }

    public void setSongList(List<Song> songList){
        this.dataList = songList;
    }
}
