package com.jf.djplayer.base.baseadapter;

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
 * "BaseListFragment"里的"ListView"适配器的默认实现
 * 由于应用里很多使用到"BaseListFragment"的地方，对适配器所显示的样式大体一样
 * 所以定义出默认的实现，对于不一样的地方，可以重新定义适配器
 */
public class DefListFragmentAdapter extends BaseAdapter {

    private List<Map<String,String>> mapList;
    private Context context;

    public DefListFragmentAdapter(Context context, List<Map<String, String>> mapList){
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
        viewHolder.title.setText(mapList.get(position).get("title"));
        viewHolder.content.setText(mapList.get(position).get("content")+"首歌曲");
        return convertView;
    }

    /**
     * 设置新数据给列表
     * @param mapList 需显示的新的数据
     */
    public void setData(List mapList){
        this.mapList = mapList;
    }
}
