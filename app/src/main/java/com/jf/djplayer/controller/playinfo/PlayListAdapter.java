package com.jf.djplayer.controller.playinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.bean.Song;

import java.util.List;

/**
 * Created by jf on 2016/6/12.
 * 播放信息-当前播放列表界面的适配器
 */
public class PlayListAdapter extends BaseAdapter {

    private List<Song> songList;// 歌曲集合
    private Context context;// 环境
    private int playingPosition = -1;

    public PlayListAdapter(Context context, List<Song> songList) {
        this.context = context;
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

    private class ViewHolder {
        TextView tv_song_name;//歌曲名字
        TextView tv_singer_name;//歌手名字
        ImageView iv_play_icon;//正在播放时的标志
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_song_play_list_fragment, null);
            viewHolder.tv_song_name = (TextView) convertView.findViewById(R.id.tv_item_song_play_list_fragment_song_name);
            viewHolder.tv_singer_name = (TextView) convertView.findViewById(R.id.tv_item_song_play_list_fragment_singerName);
            viewHolder.iv_play_icon = (ImageView) convertView.findViewById(R.id.iv_item_song_play_list_fragment_play_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_song_name.setText(songList.get(position).getSongName());
        viewHolder.tv_singer_name.setText(songList.get(position).getSingerName());
        //如果这个位置是当前正播放的歌曲的位置，将标志的控件显示出来，否则隐藏标志控件
        if(position == playingPosition){
            viewHolder.iv_play_icon.setVisibility(View.VISIBLE);
        }else{
            viewHolder.iv_play_icon.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    /**
     * 设置数据，该方法会调用刷新数据那个方法
     *
     * @param songList 需显示的新的数据
     */
    public void setData(List<Song> songList) {
        this.songList = songList;
    }

    public void setPlayingPosition(int playingPosition){
        this.playingPosition = playingPosition;
    }
}
