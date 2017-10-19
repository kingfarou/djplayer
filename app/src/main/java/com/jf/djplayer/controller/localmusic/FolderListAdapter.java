package com.jf.djplayer.controller.localmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.bean.Folder;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 * 本地音乐-文件夹列表适配器
 */

public class FolderListAdapter extends BaseAdapter {

    private List<Folder> folderList;
    private Context context;

    public FolderListAdapter(Context context, List<Folder> folderList){
        this.context = context;
        this.folderList = folderList;
    }

    @Override
    public int getCount() {
        return folderList.size();
    }

    @Override
    public Object getItem(int position) {
        return folderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView name;
        TextView songNumber;
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
        viewHolder.name.setText(folderList.get(position).getName());
        viewHolder.songNumber.setText(folderList.get(position).getSongNumber()+"首歌曲");
        return convertView;
    }

    /**
     * 设置新数据给列表
     * @param folderList 需显示的新的数据
     */
    public void setData(List<Folder> folderList){
        this.folderList = folderList;
    }
}
