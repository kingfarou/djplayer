package com.jf.djplayer.controller.myfavorite;

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
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.dialogfragment.SongOperationDialog;

import java.util.List;

/**
 * Created by Kingfar on 2016/7/14.
 * 我的最爱-歌曲列表适配器
 */
public class MyFavoriteListAdapter extends BaseAdapter implements View.OnClickListener{

    private Fragment fragment;    // 适配器内需要弹出DialogFragment，传入的Fragment作为targetFragment
    private List<Song> songList;  // 歌曲列表

    public MyFavoriteListAdapter(Fragment fragment, List<Song> songList){
        this.fragment = fragment;
        this.songList = songList;
    }

    @Override
    public int getCount() {
        return songList == null ? 0 : songList.size();
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
        Song song = songList.get(position);
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
            arguments.putSerializable(SongOperationDialog.KEY_SONG, songList.get(position));
            //打开歌曲操作弹窗
            SongOperationDialog songOperationDialog = new SongOperationDialog();
            songOperationDialog.setArguments(arguments);
            songOperationDialog.setTargetFragment(fragment, SongOperationDialog.REQUEST_CODE_SELECT_OPERATION);
            songOperationDialog.show(fragment.getChildFragmentManager(), "SongOperationDialog");
        }
    }

    public void setSongList(List<Song> songList){
        this.songList = songList;
    }
}
