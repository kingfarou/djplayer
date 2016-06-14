package com.jf.djplayer.songplayinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jf.djplayer.R;

import java.util.List;
import java.util.Map;

/**
 * Created by jf on 2016/6/12.
 * 播放信息-当前播放列表界面的适配器
 */
public class SongPlayListAdapter extends BaseAdapter {

    private List<Map<String, String>> mapList;//数据集合
    private Context context;//环境
    private int playingPosition;//当前正播放的歌曲位置

    public SongPlayListAdapter(Context context, List<Map<String, String>> mapList) {
        this.context = context;
        this.mapList = mapList;
        this.playingPosition = -1;
    }

    @Override
    public int getCount() {
        return mapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mapList.get(position);
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
        viewHolder.tv_song_name.setText(mapList.get(position).get(SongPlayListFragment.MAP_TITLE));
        viewHolder.tv_singer_name.setText(mapList.get(position).get(SongPlayListFragment.MAP_CONTENT));
        if(position == this.playingPosition){
            viewHolder.iv_play_icon.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    /**
     * 设置数据，该方法会调用刷新数据那个方法
     *
     * @param mapList 需显示的新的数据
     */
    public void setData(List mapList) {
        this.mapList = mapList;
    }

    public void setPlayingPosition(int playingPosition){

    }
}
