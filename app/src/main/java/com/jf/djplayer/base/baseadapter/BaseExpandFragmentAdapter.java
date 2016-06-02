package com.jf.djplayer.base.baseadapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;

import com.jf.djplayer.R;
import com.jf.djplayer.module.SongInfo;

import java.util.List;

/**
 * Created by jf on 2016/5/20.
 * 适配器基类-"ExpandableListView"通用适配器
 * 所有继承"BaseExpandFragment"的类所用"ExpandableListView"适配器基类
 * 该类约束"ExpandableListView"的每个"GroupView"仅有一个"childItem"，而且这个"childItem"就是一个"GridView"
 * 同时"GridView"每个"Item"样式都是一个图片加上一行文字
 * 对于具体所使用的图片以及文字资源，子类根据需要重写方法即可修改
 */
abstract public class BaseExpandFragmentAdapter extends BaseExpandableListAdapter{

    protected Context context;//环境
    protected Fragment fragment;//创建该"Adapter"的Fragment
    protected List<SongInfo> songInfoList;//数据

    /**
     * 构造方法
     * @param fragment 使用该适配器的"Fragment"
     * @param songInfoList 要显示的数据集合
     */
    public BaseExpandFragmentAdapter(Fragment fragment, List<SongInfo> songInfoList){
        this.context = fragment.getActivity();
        this.fragment = fragment;
        this.songInfoList = songInfoList;
    }

    @Override
    public int getGroupCount() {return songInfoList.size();}

    /**
     * 子"item"数量写死，只有一个，该"item"里面是个"GridView"
     * @param groupPosition
     * @return
     */
    @Override
    public final int getChildrenCount(int groupPosition) {return 1;}

    @Override
    public Object getGroup(int groupPosition) {return songInfoList.get(groupPosition);}

    @Override
    public Object getChild(int groupPosition, int childPosition) {return null;}

    @Override
    public long getGroupId(int groupPosition) {return groupPosition;}

    @Override
    public long getChildId(int groupPosition, int childPosition) {return childPosition;}

    //"ExpandableListView"每个"GroupItem"都只有一个"ChildView"内容就是个"GridView"
    private class ChildViewHolder {
        GridView gridView;
    }

    /**
     * expandableListView的子列表，每个"GroupView"只有一个子"Item"
     * 子"item"使用自定义的那个"GridView"填充
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public final View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expandable_fragment_child, null);
            childViewHolder.gridView = (GridView) convertView.findViewById(R.id.gv_item_expandable_fragment_child);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        //"GridView"的初始化
        SongInfo songInfo = songInfoList.get(groupPosition);
        childViewHolder.gridView.setOnItemClickListener(new ChildItemClickListener(songInfo, groupPosition));
        ExpandListChildItemAdapter childItemAdapter = new ExpandListChildItemAdapter(context, getChildItemText(songInfo), getChildItemImageId(songInfo));
        childViewHolder.gridView.setAdapter(childItemAdapter);
        return convertView;
    }

    @Override
    public final boolean hasStableIds() {
        return false;
    }

    @Override
    public final boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * 设置适配器里面的数据
     * @param songInfoList 新的数据
     */
    public void setSongInfoList(List<SongInfo> songInfoList){
        this.songInfoList = songInfoList;
    }

    /**
     * "ExpandableListView"的子"View"是个"GridView"，该方法获取"GridView"每个"item"所显示的文字内容
     * @return "GridView"每个"item"上的文字
     */
    abstract protected String[] getChildItemText(SongInfo songInfo);

    /**
     * "ExpandableListView"的子"View"是个"GridView"，该方法获取"GridView"每个"item"所显示的图片资源id
     * @return "GridView"每个"item"图片资源
     */
    abstract protected int[] getChildItemImageId(SongInfo songInfo);

    /**
     * "ExpandableListView"子"Item"的"GridView"每个"Item"点击事件响应方法
     * @param songInfo 该"GridView"所在"GroupItem"所对应的歌曲信息
     * @param groupPosition 该"GridView"所在"GroupItem"序号
     * @param clickPosition "GridView"被点击的"item"序号
     */
    abstract protected void onGridViewItemClick(SongInfo songInfo, int groupPosition, int clickPosition);

    /*
    "ExpandableListView"的子"Item"里的"GridView"的"OnItemClickListener"的实现类
     */
    private class ChildItemClickListener implements AdapterView.OnItemClickListener{

        private SongInfo songInfo;//被操作的那首歌曲
        private int groupPosition;//被操作的歌曲在列表的位置

        ChildItemClickListener(SongInfo songInfo,int groupPosition){
            this.songInfo = songInfo;
            this.groupPosition = groupPosition;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //将点击事件传递给自定义的方法里面，子类如果需要自定义点击事件的操作，重写该自定义方法即可
            onGridViewItemClick(songInfo, groupPosition, position);
        }//onItemClick()
    }//ExpandableChildItemClick
}
