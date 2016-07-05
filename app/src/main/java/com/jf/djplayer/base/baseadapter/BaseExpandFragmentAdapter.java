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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.broadcastreceiver.UpdateUiSongInfoReceiver;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.dialogfragment.DeleteSongDialog;
import com.jf.djplayer.dialogfragment.SetToBellDialog;
import com.jf.djplayer.dialogfragment.SongInfoDialog;
import com.jf.djplayer.module.Song;

import java.util.List;

/**
 * Created by jf on 2016/5/20.
 * 适配器基类-"ExpandableListView"通用适配器
 */
public class BaseExpandFragmentAdapter extends BaseExpandableListAdapter{

    protected Context context;//环境
    protected Fragment fragment;//创建该"Adapter"的Fragment
    protected List<Song> songInfoList;//数据

    /**
     * 构造方法
     * @param fragment 使用该适配器的"Fragment"
     * @param songInfoList 要显示的数据集合
     */
    public BaseExpandFragmentAdapter(Fragment fragment, List<Song> songInfoList){
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

    @Override
    public final boolean hasStableIds() {
        return false;
    }

    @Override
    public final boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    //每个“组”的ViewHolder
    private class GroupViewHolder {
//        RelativeLayout rl;
        TextView position;//显示当前是第几首歌曲
        TextView songName;//歌曲名字
        TextView artistName;//歌手名字
        ImageView arrow;//箭头图标的ImageView
    }

    //"ExpandableListView"每个"GroupItem"都只有一个"ChildView"内容就是个"GridView"
    private class ChildViewHolder {
        GridView gridView;
    }

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
        //设置要显示的歌曲信息
        groupViewHolder.position.setText(groupPosition + 1 + "");//设置当前是第几首歌曲
        groupViewHolder.songName.setText(songInfoList.get(groupPosition).getSongName());//歌曲名字
        groupViewHolder.artistName.setText(songInfoList.get(groupPosition).getSingerName());//歌手名字
        //如果当前栏目是打开的绘制向上的箭头，否则绘制向下箭头
        if (isExpanded) {
            groupViewHolder.arrow.setImageResource(R.drawable.icon_drop);
        } else {
            groupViewHolder.arrow.setImageResource(R.drawable.icon_down);
        }
        //给箭头按钮设置监听器
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
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expandable_fragment_child, null);
            childViewHolder.gridView = (GridView) convertView.findViewById(R.id.gv_item_expandable_fragment_child);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        //"GridView"的初始化
        Song songInfo = songInfoList.get(groupPosition);
        childViewHolder.gridView.setOnItemClickListener(new ChildItemClickListener(songInfo, groupPosition));
        ExpandListChildItemAdapter childItemAdapter = new ExpandListChildItemAdapter(context, getChildItemText(songInfo), getChildItemImageId(songInfo));
        childViewHolder.gridView.setAdapter(childItemAdapter);
        return convertView;
    }

    /**
     * 设置适配器里面的数据
     * @param songInfoList 新的数据
     */
    public void setSongInfoList(List<Song> songInfoList){
        this.songInfoList = songInfoList;
    }

    /**
     * "ExpandableListView"的子"View"是个"GridView"，该方法获取"GridView"每个"item"所显示的图片资源id
     * @return "GridView"每个"item"图片资源
     */
    protected int[] getChildItemImageId(Song songInfo){
        int[] imageId = new int[]{R.drawable.ic_base_expand_fragment_adapter_no_collection, R.drawable.ic_base_expand_fragment_adapter_delete,
                R.drawable.ic_base_expand_fragment_adapter_add, R.drawable.ic_base_expand_fragment_adapter_bell,
                R.drawable.ic_base_expand_fragment_adapter_share, R.drawable.ic_base_expand_fragment_adapter_send,
                R.drawable.ic_base_expand_fragment_adapter_info};
        if(songInfo.getCollection() == Song.IS_COLLECTION){
            imageId[0] = R.drawable.ic_base_expand_fragment_adapter_collection;
        }
        return imageId;
    }

    /**
     * "ExpandableListView"的子"View"是个"GridView"，该方法获取"GridView"每个"item"所显示的文字内容
     * @return "GridView"每个"item"上的文字
     */
    protected String[] getChildItemText(Song songInfo){
        Resources resources = context.getResources();
        return new String[]{resources.getString(R.string.collection), resources.getString(R.string.delete),
                resources.getString(R.string.add), resources.getString(R.string.set_to_bell),
                resources.getString(R.string.share), resources.getString(R.string.send),
                resources.getString(R.string.detailed_information)};
    }

    /**
     * "ExpandableListView"子"Item"的"GridView"每个"Item"点击事件响应方法
     * @param songInfo 该"GridView"所在"GroupItem"所对应的歌曲信息
     * @param groupPosition 该"GridView"所在"GroupItem"序号
     * @param clickPosition "GridView"被点击的"item"序号
     */
    protected void onGridViewItemClick(Song songInfo, int groupPosition, int clickPosition){
        switch(clickPosition){
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
                SetToBellDialog setToBellDialog = new SetToBellDialog(context,songInfo);
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
    }

    /*
    "ExpandableListView"的子"Item"里的"GridView"的"OnItemClickListener"的实现类
     */
    private class ChildItemClickListener implements AdapterView.OnItemClickListener{

        private Song songInfo;//被操作的那首歌曲
        private int groupPosition;//被操作的歌曲在列表的位置

        ChildItemClickListener(Song songInfo,int groupPosition){
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
