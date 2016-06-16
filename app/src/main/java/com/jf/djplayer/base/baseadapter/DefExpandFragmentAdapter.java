package com.jf.djplayer.base.baseadapter;

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
 * Created by JF on 2016/6/2.
 * 对"BaseExpandFragmentAdapter"的默认实现
 * 由于App里面有很多地方，对"BaseExpandFragmentAdapter"所需要的样式以及操作相同，所以提供默认实现
 * 样式及操作不同的地方，可以直接继承自"BaseExpandFragmentAdapter"重写相关方法即可
 */
public class DefExpandFragmentAdapter extends BaseExpandFragmentAdapter{

    /**
     * 构造方法
     *
     * @param fragment     使用该适配器的"Fragment"
     * @param songInfoList 要显示的数据集合
     */
    public DefExpandFragmentAdapter(Fragment fragment, List<Song> songInfoList) {
        super(fragment, songInfoList);
    }

    //用于优化"ExpandableListVIew"性能的内部类
    private class GroupViewHolder {
        RelativeLayout rl;
        TextView position;//显示当前是第几首歌曲
        TextView songName;//歌曲名字
        TextView artistName;//歌手名字
        ImageView arrow;//箭头图标的ImageView
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
//        设置要显示的歌曲信息
        groupViewHolder.position.setText(groupPosition + 1 + "");//设置当前是第几首歌曲
        groupViewHolder.songName.setText(songInfoList.get(groupPosition).getSongName());//歌曲名字
        groupViewHolder.artistName.setText(songInfoList.get(groupPosition).getSingerName());//歌手名字
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

    @Override
    protected int[] getChildItemImageId(Song songInfo) {
        int[] imageId = new int[]{R.drawable.ic_base_expand_fragment_adapter_no_collection, R.drawable.ic_base_expand_fragment_adapter_delete,
                R.drawable.ic_base_expand_fragment_adapter_add, R.drawable.ic_base_expand_fragment_adapter_bell,
                R.drawable.ic_base_expand_fragment_adapter_share, R.drawable.ic_base_expand_fragment_adapter_send,
                R.drawable.ic_base_expand_fragment_adapter_info};
        if(songInfo.getCollection() == Song.IS_COLLECTION){
            imageId[0] = R.drawable.ic_base_expand_fragment_adapter_collection;
        }
        return imageId;
    }

    @Override
    protected String[] getChildItemText(Song songInfo) {
        Resources resources = context.getResources();
        return new String[]{resources.getString(R.string.collection), resources.getString(R.string.delete),
                resources.getString(R.string.add), resources.getString(R.string.set_to_bell),
                resources.getString(R.string.share), resources.getString(R.string.send),
                resources.getString(R.string.detailed_information)};
    }

    @Override
    protected void onGridViewItemClick(Song songInfo, int groupPosition, int clickPosition) {
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
}
