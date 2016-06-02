package com.jf.djplayer.myfavorite;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.baseadapter.BaseExpandFragmentAdapter;
import com.jf.djplayer.broadcastreceiver.UpdateUiSongInfoReceiver;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.dialogfragment.DeleteSongDialog;
import com.jf.djplayer.dialogfragment.SetToBellDialog;
import com.jf.djplayer.dialogfragment.SongInfoDialog;
import com.jf.djplayer.module.SongInfo;

import java.util.List;

/**
 * Created by jf on 2016/5/20.
 * 我的最爱-"MyFavoriteExpandFragment"列表所用适配器
 */
public class MyFavoriteFragmentAdapter extends BaseExpandFragmentAdapter {

    public MyFavoriteFragmentAdapter(Fragment fragment, List<SongInfo> myFavoriteList) {
        super(fragment, myFavoriteList);
    }

    private class GroupViewHolder{
        TextView tv_song_name;//歌曲名字
        TextView tv_singer_name;//歌手名字
        ImageView iv_arrow;//箭头
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if(convertView == null){
            groupViewHolder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_favorite_expandable_list_view_group, null);
            groupViewHolder.tv_song_name = (TextView) convertView.findViewById(R.id.tv_item_my_favorite_expandable_list_view_group_song_name);
            groupViewHolder.tv_singer_name = (TextView) convertView.findViewById(R.id.tv_item_my_favorite_expandable_list_view_group_singer_name);
            groupViewHolder.iv_arrow = (ImageView) convertView.findViewById(R.id.iv_item_my_favorite_expandable_list_view_group_arrow);
            convertView.setTag(groupViewHolder);
        }else{
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tv_song_name.setText(songInfoList.get(groupPosition).getSongName());
        groupViewHolder.tv_singer_name.setText(songInfoList.get(groupPosition).getSingerName());
        if(isExpanded){
            groupViewHolder.iv_arrow.setImageResource(R.drawable.icon_drop);
        }else{
            groupViewHolder.iv_arrow.setImageResource(R.drawable.icon_down);
        }
        groupViewHolder.iv_arrow.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected int[] getChildItemImageId(SongInfo songInfo) {
        return new int[]{R.drawable.ic_base_expand_fragment_adapter_collection, R.drawable.ic_base_expand_fragment_adapter_delete, R.drawable.ic_base_expand_fragment_adapter_add, R.drawable.ic_base_expand_fragment_adapter_bell,
                R.drawable.ic_base_expand_fragment_adapter_share, R.drawable.ic_base_expand_fragment_adapter_send, R.drawable.ic_base_expand_fragment_adapter_info};
    }

    @Override
    protected String[] getChildItemText(SongInfo songInfo) {
        Resources resources = context.getResources();
        return new String[]{resources.getString(R.string.collection), resources.getString(R.string.delete),
                resources.getString(R.string.add), resources.getString(R.string.set_to_bell),
                resources.getString(R.string.share), resources.getString(R.string.send),
                resources.getString(R.string.detailed_information)};
    }

    @Override
    protected void onGridViewItemClick(SongInfo songInfo, int groupPosition, int clickPosition) {
        switch(clickPosition){
            //用户取消收藏某一首歌（在“我的最爱”列表里，只有取消收藏功能，没有添加收藏功能）
            case 0:
                //将数据库里面歌曲收藏状态改为没有收藏
                SongInfoOpenHelper collectionOpenHelper = new SongInfoOpenHelper(context);
                collectionOpenHelper.updateCollection(songInfo, 0);
                songInfo.setCollection(0);
                //从集合里删掉这首歌曲
                songInfoList.remove(groupPosition);
                notifyDataSetChanged();
                //发送广播通知界面更新UI
                Intent updateCollectionIntent;
                updateCollectionIntent = new Intent(UpdateUiSongInfoReceiver.ACTION_CANCEL_COLLECTION_SONG);
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
                updateCollectionIntent.putExtra(UpdateUiSongInfoReceiver.position, groupPosition);//传递所操作的序号
                localBroadcastManager.sendBroadcast(updateCollectionIntent);
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

}
