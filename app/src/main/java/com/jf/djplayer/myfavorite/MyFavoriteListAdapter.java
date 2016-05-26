package com.jf.djplayer.myfavorite;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

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
 * 我的最爱-"MyFavoriteListFragment"列表所用适配器
 */
public class MyFavoriteListAdapter extends BaseExpandFragmentAdapter {

    public MyFavoriteListAdapter(Fragment fragment, List<SongInfo> myFavoriteList){
        super(fragment, myFavoriteList);
    }

    @Override
    protected int[] getChildItemImageId() {
        return new int[]{R.drawable.ic_fragment_mine_my_favorite, R.drawable.ic_base_expand_fragment_adapter_delete, R.drawable.ic_base_expand_fragment_adapter_add, R.drawable.ic_base_expand_fragment_adapter_bell,
                R.drawable.ic_base_expand_fragment_adapter_share, R.drawable.ic_base_expand_fragment_adapter_send, R.drawable.ic_base_expand_fragment_adapter_info};
    }

    //该页面的点击事件与默认的基类不完全相符，自行重写
    @Override
    protected AdapterView.OnItemClickListener getChildItemClickListener(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return  new ChildItemClickListener(dataList.get(groupPosition), groupPosition);
    }

    /*
    该类定义"ExpandableListView"里的子列表里面的"GridView"里的item点击事件
     */
    private class ChildItemClickListener implements AdapterView.OnItemClickListener{

        private SongInfo songInfo;//被点击的歌曲信息对象
        private int groupPosition;//被点击得歌曲在列表里位置

        ChildItemClickListener(SongInfo songInfo,int groupPosition){
            this.songInfo = songInfo;
            this.groupPosition = groupPosition;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch(position){
                //用户取消收藏某一首歌（在“我的最爱”列表里，只有取消收藏功能，没有添加收藏功能）
                case 0:
                    //将数据库里面歌曲收藏状态改为没有收藏
                    SongInfoOpenHelper collectionOpenHelper = new SongInfoOpenHelper(context);
                    collectionOpenHelper.updateCollection(songInfo, 0);
                    songInfo.setCollection(0);
                    //从集合里删掉这首歌曲
                    dataList.remove(position);
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
