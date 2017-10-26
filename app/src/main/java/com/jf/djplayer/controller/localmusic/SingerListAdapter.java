package com.jf.djplayer.controller.localmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.bean.Singer;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 * 本地音乐-歌手列表适配器
 */

public class SingerListAdapter extends BaseAdapter {

    private List<Singer> singerList;
    private Context context;

    public SingerListAdapter(Context context, List<Singer> singerList){
        this.context = context;
        this.singerList = singerList;
    }

    @Override
    public int getCount() {
        return singerList.size();
    }

    @Override
    public Object getItem(int position) {
        return singerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView title;
        TextView content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_def_list_fragment_adapter,null);
            viewHolder.title = (TextView)convertView.findViewById(R.id.item_def_list_fragment_adapter_title);
            viewHolder.content = (TextView)convertView.findViewById(R.id.item_def_list_fragment_adapter_content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.title.setText(singerList.get(position).getName());
        viewHolder.content.setText(singerList.get(position).getSongNumber()+"首歌曲");
        return convertView;
    }

    /**
     * 设置新数据给列表
     * @param singerList 需显示的新的数据
     */
    public void setData(List<Singer> singerList){
        this.singerList = singerList;
    }
}
