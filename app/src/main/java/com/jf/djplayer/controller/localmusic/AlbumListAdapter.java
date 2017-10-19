package com.jf.djplayer.controller.localmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.bean.Album;

import java.util.List;

/**
 * Created by Kingfar on 2017/10/17.
 * 本地音乐-专辑列表适配器
 */

public class AlbumListAdapter extends BaseAdapter{

    private List<Album> albumList;
    private Context context;

    public AlbumListAdapter(Context context, List<Album> albumList){
        this.context = context;
        this.albumList = albumList;
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView name;        // 专辑名字
        TextView songNumber;  // 专辑所拥有的歌曲数量
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_def_list_fragment_adapter,null);
            viewHolder.name = (TextView)convertView.findViewById(R.id.item_def_list_fragment_adapter_title);
            viewHolder.songNumber = (TextView)convertView.findViewById(R.id.item_def_list_fragment_adapter_content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.name.setText(albumList.get(position).getName());
        viewHolder.songNumber.setText(albumList.get(position).getSongNumber()+"首歌曲");
        return convertView;
    }

    /**
     * 设置新数据给列表
     * @param albumList 需显示的新的数据
     */
    public void setData(List<Album> albumList){
        this.albumList = albumList;
    }
}
