package com.jf.djplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jf.djplayer.R;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/15.
 */
public class ListViewFragmentAdapter extends BaseAdapter {


    private List<Map<String,String>> mapList;
    private Context context;
    public ListViewFragmentAdapter(Context context, List<Map<String, String>> mapList){
        this.context = context;
        this.mapList = mapList;
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

    private class ViewHolder{
        TextView position;
        TextView title;
        TextView content;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_view_fragment,null);
            viewHolder.position = (TextView)convertView.findViewById(R.id.tv_item_list_view_fragment_position);
            viewHolder.title = (TextView)convertView.findViewById(R.id.tv_item_list_view_fragment_title);
            viewHolder.content = (TextView)convertView.findViewById(R.id.tv_item_list_view_fragment_content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.position.setText(position+1+"");
        viewHolder.title.setText(mapList.get(position).get("title"));
        viewHolder.content.setText(mapList.get(position).get("content")+"首歌曲");
        return convertView;
    }
}
