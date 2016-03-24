package com.jf.djplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jf.djplayer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/12.
 * 显示所选择路径的文件
 * 只提供了“路径”显示以及音乐文件显示
 * 不得传入非音乐类型的文件
 * 同时提供两个方法
 * 1,updateList，用于更新适配器的数据
 * 2,getSelectedFileList,用于获取用户所选择的所有文件
 */
public class SelectedFileAdapter extends BaseAdapter{

    private List<File> fileList = null;//用来接收要显示的数据
    private Map<Integer,Boolean> checkedMap = null;//用来记录每个CheckBox状态
    private Context context = null;
    private List<File> selectedFileList = null;//用来保存用户所选择的扫描路径

    public SelectedFileAdapter(Context context, List<File> fileList){
        this.context = context;
        this.fileList = fileList;
        this.checkedMap = new HashMap<Integer,Boolean>();//创建
        this.selectedFileList = new ArrayList<File>();
        //初始化时根据传入集合长度
        //将所有的复选框的状态设置未选
        for (int i = 0; i<fileList.size();i++){
            checkedMap.put(i,false);
        }
    }

    /**
     * 外部通过这个方法获取用户所选择的所有文件
     * @return 用户在当前窗体下所选择的所有扫描路径
     */
    public List<File> getSelectedFileList(){
        return this.selectedFileList;
    }

    /**
     * 这是用来刷新数据用的
     * 所传入的集合里面只能装着两个类型File
     * 1,路径
     * 2,mp3
     * @param fileList 装着新的要现实的数据集合
     */
    public void updateData(List<File> fileList){
        this.fileList = fileList;
        this.checkedMap.clear();
        this.selectedFileList.clear();
        //初始化时根据传入集合长度
        //将所有的复选框的状态设置未选
        for (int i = 0; i<fileList.size();i++){
            checkedMap.put(i,false);
        }
    }
    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        final File file = fileList.get(position);
        final File file = fileList.get(position);
        final ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_selected_file_or_song,null);
            viewHolder.fileImageView = (ImageView)convertView.findViewById(R.id.iv_item_selected_file_fileImage);
            viewHolder.fileNameTv = (TextView)convertView.findViewById(R.id.tv_item_selected_file_filename);
            viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.cb_item_selected_file_checkBox);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //根据文件类型不同设置不同图标显示
        if (fileList.get(position).isDirectory())
            viewHolder.fileImageView.setImageResource(R.drawable.icon_folder);
        else {
            viewHolder.fileImageView.setImageResource(R.drawable.icon_file);
        }
        //设置要显示的文件名字
        viewHolder.fileNameTv.setText(fileList.get(position).getName());
        viewHolder.checkBox.setChecked(checkedMap.get(position));//设置选择框的状态
//        Log.i("test",position+"--"+this.checkedMap.get(position)+"");
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                CheckBox checkBox = (CheckBox)v;//获取CheckBox
                checkedMap.put(position,checkBox.isChecked());//保存他的点击状态
                if (checkBox.isChecked())  selectedFileList.add(file);
                else selectedFileList.remove(file);
            }
        });

        return convertView;
    }

    class ViewHolder{
        public ImageView fileImageView = null;//图片
        public TextView fileNameTv = null;//文件
        public CheckBox checkBox = null;
    }
}
