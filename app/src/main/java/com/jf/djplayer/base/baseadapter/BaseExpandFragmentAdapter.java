package com.jf.djplayer.base.baseadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
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

import com.jf.djplayer.adapter.ExpandableChildItemAdapter;
import com.jf.djplayer.localmusic.SongFragment;
import com.jf.djplayer.other.SongInfo;
import com.jf.djplayer.dialogfragment.SetToBellDialog;
import com.jf.djplayer.R;
import com.jf.djplayer.broadcastreceiver.UpdateUiSongInfoReceiver;
import com.jf.djplayer.dialogfragment.DeleteSongDialogFragment;
import com.jf.djplayer.dialogfragment.SongInfoDialog;
import com.jf.djplayer.database.SongInfoOpenHelper;

import java.util.List;

/**
 *
 * Created by Administrator on 2015/9/14.
 * 适配器基类-"ExpandableListView"通用适配器
 * 所有继承"BaseExpandFragment"
 * 的类所用"ExpandableListView"的适配器
 */
public class BaseExpandFragmentAdapter extends BaseExpandableListAdapter {

    private List<SongInfo> songInfoList;
    private Fragment fragment;//创建该适配器的"Fragment"
    private Context context;

    public BaseExpandFragmentAdapter(Activity context, List<SongInfo> songInfoList){
        this.context = context;
        this.songInfoList = songInfoList;
    }

    /**
     * 创建一个当前应用通用"BaseExpandableListAdapter"适配器
     * @param containerFragment 创建该适配器的"Fragment"
     * @param songInfoList 所需要的数据集合
     */
    public BaseExpandFragmentAdapter(Fragment containerFragment, List<SongInfo> songInfoList){
        this.fragment = containerFragment;
        this.context = fragment.getActivity();
        this.songInfoList = songInfoList;
    }

    @Override
    public int getGroupCount() {
        return songInfoList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return songInfoList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

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

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView==null) {
            groupViewHolder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expandable_fragment_group, null);
            groupViewHolder.position = (TextView) convertView.findViewById(R.id.tv_item_expandable_fragment_group_position);
            groupViewHolder.songName = (TextView)convertView.findViewById(R.id.tv_item_expandable_fragment_group_songName);
            groupViewHolder.artistName = (TextView)convertView.findViewById(R.id.tv_item_expandable_fragment_group_artist);
            groupViewHolder.arrow = (ImageView)convertView.findViewById(R.id.iv_item_expandable_fragment_group_arrow);
            convertView.setTag(groupViewHolder);
        }else{
            groupViewHolder = (GroupViewHolder)convertView.getTag();
        }
//        设置要显示的歌曲信息
        groupViewHolder.position.setText(groupPosition+1+"");//设置当前是第几首歌曲
        groupViewHolder.songName.setText(songInfoList.get(groupPosition).getSongName());//歌曲名字
        groupViewHolder.artistName.setText(songInfoList.get(groupPosition).getSingerName());//歌手名字
        //如果当前栏目是打开的绘制向上的箭头，否则绘制向下箭头
        if (isExpanded) {
            groupViewHolder.arrow.setImageResource(R.drawable.icon_drop);
        }else {
            groupViewHolder.arrow.setImageResource(R.drawable.icon_down);
        }
//        给箭头按钮设置监听器
        groupViewHolder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //箭头控制栏目打开以及收起
                if (((ExpandableListView)parent).isGroupExpanded(groupPosition)) {
                    ((ExpandableListView)parent).collapseGroup(groupPosition);
                }
                else {
                    ((ExpandableListView)parent).expandGroup(groupPosition);
                }
            }
        });
        return convertView;
    }//getGroupView()

    /*
    expandableListView的子列表，
    使用自定义的那个GridView填充
     */
    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expandable_fragment_chil, null);
            childViewHolder.gridView = (GridView) convertView.findViewById(R.id.gv_item_expandable_fragment_child);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
//        准备GridView要显示的文字以及图片
        String[] text = new String[]{"收藏", "删除", "添加", "设为铃声", "分享", "发送", "歌曲信息"};
        int[] icon = new int[]{R.drawable.fragment_song_no_collection, R.drawable.icon_del, R.drawable.icon_plus, R.drawable.icon_bell,
                R.drawable.expandable_fragment_adapter_childview_share, R.drawable.icon_send, R.drawable.icon_info};

//        如果歌曲已被收藏需要更改一下图片
        if (songInfoList.get(groupPosition).getCollection() == 1) {
            icon[0] = R.drawable.fragment_song_collection;
        }

        ExpandableChildItemAdapter childItemAdapter = new ExpandableChildItemAdapter(context, text, icon);
//        设置GridView每一项的点击事件
        childViewHolder.gridView.setOnItemClickListener(new ExpandableChildItemClick(songInfoList.get(groupPosition),groupPosition));
        childViewHolder.gridView.setAdapter(childItemAdapter);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setData(List<SongInfo> dataList){
        this.songInfoList = dataList;
    }

    /*
    该类定义"ExpandableListView"里的子列表里面的"GridView"里的item点击事件
     */
    private class ExpandableChildItemClick implements AdapterView.OnItemClickListener{
        private SongInfo songInfo;
        private int groupPosition;

        ExpandableChildItemClick(SongInfo songInfo,int groupPosition){
            this.songInfo = songInfo;
            this.groupPosition = groupPosition;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch(position){
//            如果用户收藏或者取消收藏某一首歌
                case 0:
                    SongInfoOpenHelper collectionOpenHelper = new SongInfoOpenHelper(context);
                    Intent updateCollectionIntent;
                    //根据歌曲原来收藏状态设置歌曲新的收藏状态
                    if (songInfo.getCollection()==0) {
                        //如果歌曲原先没有被收藏的
                        collectionOpenHelper.updateCollection(songInfo, 1);//1表示歌曲需收藏
                        songInfo.setCollection(1);
                        updateCollectionIntent = new Intent(UpdateUiSongInfoReceiver.ACTION_COLLECTION_SONG);
                    }else{
                        collectionOpenHelper.updateCollection(songInfo, 0);
                        songInfo.setCollection(0);
                        updateCollectionIntent = new Intent(UpdateUiSongInfoReceiver.ACTION_CANCEL_COLLECTION_SONG);
                    }
//                发送广播通知界面更新UI
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
                    updateCollectionIntent.putExtra(UpdateUiSongInfoReceiver.position, groupPosition);//传递所操作的序号
                    localBroadcastManager.sendBroadcast(updateCollectionIntent);
                    break;
                case 1://如果点击删除歌曲，打开删除的提示框
                    DeleteSongDialogFragment deleteSongDialogFragment = new DeleteSongDialogFragment(context,songInfo,groupPosition);
                    deleteSongDialogFragment.show(((Activity)context).getFragmentManager(),"DeleteSongDialogFragment");
                    break;
                case 2:
                    break;
                case 3://歌曲设置为某一个铃声（还未实现）
                    SetToBellDialog setToBellDialog = new SetToBellDialog(context,this.songInfo);
                    setToBellDialog.show(((Activity)context).getFragmentManager(),"setToBellDialog");
                    break;
                case 4://这个表示分享功能（还没有申请到对应平台分享权限）
                    break;
                case 6://这个表示用户编辑歌曲信息
                    SongInfoDialog songInfoDialog = new SongInfoDialog(songInfo,groupPosition);
                    songInfoDialog.show(((Activity)context).getFragmentManager(), "songOperationDialog");
                    break;
                default:break;
            }//switch(position)
        }//onItemClick()
    }//ExpandableChildItemClick

}
