package com.jf.djplayer.base.baseadapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.broadcastreceiver.UpdateUiSongInfoReceiver;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.dialogfragment.DeleteSongDialog;
import com.jf.djplayer.dialogfragment.SetToBellDialog;
import com.jf.djplayer.dialogfragment.SongInfoDialog;
import com.jf.djplayer.module.SongInfo;
import com.jf.djplayer.other.MyApplication;

import java.util.List;

/**
 * Created by jf on 2016/5/20.
 * 适配器基类-"ExpandableListView"通用适配器
 * 所有继承"BaseExpandFragment"的类所用"ExpandableListView"适配器基类
 * 该类约束"ExpandableListView"的每个"GroupView"仅有一个"childItem"，而且这个"childItem"就是一个"GridView"
 * 同时"GridView"每个"Item"样式都是一个图片加上一行文字，这些资源该类都有默认实现，
 * 子类可以根据需要重写
 */
public class BaseExpandFragmentAdapter extends BaseExpandableListAdapter{

    protected Context context;//环境
    protected Fragment fragment;//创建该"Adapter"的Fragment
    protected List<SongInfo> dataList;//数据
    protected String[] childItemText;//"GridView"里的每个栏目文字
    protected int[] childItemImageId;//"GridView"里的每个栏目图片资源

    /**
     * 构造方法
     * @param fragment 使用该适配器的"Fragment"
     * @param dataList 要显示的数据集合
     */
    public BaseExpandFragmentAdapter(Fragment fragment, List<SongInfo> dataList){
        this.context = fragment.getActivity();
        this.fragment = fragment;
        this.dataList = dataList;
    }

    @Override
    public int getGroupCount() {return dataList.size();}

    /**
     * 子"item"数量写死，只有一个，该"item"里面是个"GridView"
     * @param groupPosition
     * @return
     */
    @Override
    public final int getChildrenCount(int groupPosition) {return 1;}

    @Override
    public Object getGroup(int groupPosition) {return dataList.get(groupPosition);}

    @Override
    public Object getChild(int groupPosition, int childPosition) {return null;}

    @Override
    public long getGroupId(int groupPosition) {return groupPosition;}

    @Override
    public long getChildId(int groupPosition, int childPosition) {return childPosition;}

    //用于优化"ExpandableListVIew"性能的内部类
    private class GroupViewHolder {
        RelativeLayout rl;
        TextView position;//显示当前是第几首歌曲
        TextView songName;//歌曲名字
        TextView artistName;//歌手名字
        ImageView arrow;//箭头图标的ImageView
    }

    //"ExpandableListView"每个"GroupItem"都只有一个"ChildView"内容就是个"GridView"
    private class ChildViewHolder {
        GridView gridView;
    }

    /**
     * 这是基类对"GroupView"默认实现，子类如果样式不同，可以根据需要重写
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            groupViewHolder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expandable_fragment_group, null);
            groupViewHolder.position = (TextView) convertView.findViewById(R.id.tv_item_expandable_fragment_group_position);
            groupViewHolder.songName = (TextView) convertView.findViewById(R.id.tv_item_expandable_fragment_group_songName);
            groupViewHolder.artistName = (TextView) convertView.findViewById(R.id.tv_item_expandable_fragment_group_artist);
            groupViewHolder.arrow = (ImageView) convertView.findViewById(R.id.iv_item_expandable_fragment_group_arrow);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
//        设置要显示的歌曲信息
        groupViewHolder.position.setText(groupPosition + 1 + "");//设置当前是第几首歌曲
        groupViewHolder.songName.setText(dataList.get(groupPosition).getSongName());//歌曲名字
        groupViewHolder.artistName.setText(dataList.get(groupPosition).getSingerName());//歌手名字
        //如果当前栏目是打开的绘制向上的箭头，否则绘制向下箭头
        if (isExpanded) {
            groupViewHolder.arrow.setImageResource(R.drawable.icon_drop);
        } else {
            groupViewHolder.arrow.setImageResource(R.drawable.icon_down);
        }
//        给箭头按钮设置监听器
        groupViewHolder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //箭头控制栏目打开以及收起
                if (((ExpandableListView) parent).isGroupExpanded(groupPosition)) {
                    ((ExpandableListView) parent).collapseGroup(groupPosition);
                } else {
                    ((ExpandableListView) parent).expandGroup(groupPosition);
                }
            }
        });
        return convertView;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expandable_fragment_chil, null);
            childViewHolder.gridView = (GridView) convertView.findViewById(R.id.gv_item_expandable_fragment_child);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        //"GridView"的初始化
        //获取"GridView"里的"Item"对应资源
        childItemText = getChildItemText();
        childItemImageId = getChildItemImageId();
        //让子类在完成前初始化
        initBeforeChildViewReturn(groupPosition, childPosition, isLastChild, convertView, parent);
        childViewHolder.gridView.setOnItemClickListener(getChildItemClickListener(groupPosition, childPosition, isLastChild, convertView, parent));
        ExpandListChildItemAdapter childItemAdapter = new ExpandListChildItemAdapter(context, childItemText, childItemImageId);
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
     * @param dataList 新的数据
     */
    public void setDataList(List<SongInfo> dataList){
        this.dataList = dataList;
    }

    /**
     * "ExpandableListView"的子"View"是个"GridView"，该方法获取"GridView"每个"item"所显示的文字内容
     * 子类如果想要返回别的文字，可以重写这个方法
     * @return "GridView"每个"item"上的文字
     */
    protected String[] getChildItemText(){
        Resources resources = context.getResources();
        return new String[]{resources.getString(R.string.collection), resources.getString(R.string.delete),
                resources.getString(R.string.add), resources.getString(R.string.set_to_bell),
                resources.getString(R.string.share), resources.getString(R.string.send),
                resources.getString(R.string.detailed_information)};
    }

    /**
     * "ExpandableListView"的子"View"是个"GridView"，该方法获取"GridView"每个"item"所显示的图片资源id
     * 子类如果想要返回别的图片，可以重写这个方法
     * @return "GridView"每个"item"图片资源
     */
    protected int[] getChildItemImageId(){
        return new int[]{R.drawable.ic_base_expand_fragment_adapter_no_collection, R.drawable.ic_base_expand_fragment_adapter_delete,
                R.drawable.ic_base_expand_fragment_adapter_add, R.drawable.ic_base_expand_fragment_adapter_bell,
                R.drawable.ic_base_expand_fragment_adapter_share, R.drawable.ic_base_expand_fragment_adapter_send,
                R.drawable.ic_base_expand_fragment_adapter_info};
    }

    /**
     * 获取"ExpandableListView"的子"View"里的"GridView"每个栏目点击事件的监听器
     * 子类如果想要进行别的操作，可以重写这个方法
     * @return
     */
    protected OnItemClickListener getChildItemClickListener(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
        return new ChildItemClickListener(dataList.get(groupPosition), groupPosition);
    }

    protected void initBeforeChildViewReturn(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent){

    }

    /*
    该类定义"ExpandableListView"里的子列表里面的"GridView"里的item点击事件，
    这是一个默认实现，子类如果想要自己实现点击事件，需要自己写一个类
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
            switch(position){
                //用户收藏或者取消收藏这首歌曲
                case 0:
                    SongInfoOpenHelper collectionOpenHelper = new SongInfoOpenHelper(context);
                    Intent intent = new Intent();
                    //根据歌曲原来收藏状态，设置新的收藏状态
                    if(songInfo.getCollection() == 1){
                        //将数据库里面歌曲收藏状态改为没有收藏
                        collectionOpenHelper.updateCollection(songInfo, 0);
                        songInfo.setCollection(0);
                        intent.setAction(UpdateUiSongInfoReceiver.ACTION_CANCEL_COLLECTION_SONG);
                    }else{
                        collectionOpenHelper.updateCollection(songInfo, 1);
                        songInfo.setCollection(1);
                        intent.setAction(UpdateUiSongInfoReceiver.ACTION_COLLECTION_SONG);
                    }
                    //发送广播通知界面更新UI
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
                    intent.putExtra(UpdateUiSongInfoReceiver.position, groupPosition);//传递所操作的序号
                    localBroadcastManager.sendBroadcast(intent);
                    break;
                case 1://如果点击删除歌曲，打开删除的提示框
                    DeleteSongDialog deleteSongDialogFragment = new DeleteSongDialog(context,songInfo,groupPosition);
//                    deleteSongDialogFragment.setTargetFragment(fragment, SongFragment.REQUEST_CODE_DELETE_SONG);
                    deleteSongDialogFragment.show( ((FragmentActivity)context).getSupportFragmentManager(),"DeleteSongDialogFragment");
                    break;
                case 2:
                    break;
                case 3://歌曲设置为某一个铃声（还未实现）
                    SetToBellDialog setToBellDialog = new SetToBellDialog(context,this.songInfo);
                    setToBellDialog.show(((FragmentActivity)context).getSupportFragmentManager(),"setToBellDialog");
                    break;
                case 4://这个表示分享功能（还没有申请到对应平台分享权限）
                    break;
                case 6://这个表示用户编辑歌曲信息
                    SongInfoDialog songInfoDialog = new SongInfoDialog(songInfo,groupPosition);
                    songInfoDialog.show(((FragmentActivity)context).getSupportFragmentManager(), "songOperationDialog");
                    break;
                default:break;
            }//switch(position)
        }//onItemClick()
    }//ExpandableChildItemClick
}
