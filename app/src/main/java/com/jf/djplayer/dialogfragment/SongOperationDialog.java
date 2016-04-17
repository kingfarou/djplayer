package com.jf.djplayer.dialogfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jf.djplayer.InfoClass;
import com.jf.djplayer.SongInfo;
import com.jf.djplayer.tool.SongOperationUtils;
import com.jf.djplayer.R;

/**
 * Created by Administrator on 2015/8/16.
 */
public class SongOperationDialog extends DialogFragment implements View.OnClickListener {

    private Activity activity;
    private SongInfo songInfo;//记录传递来的歌曲信息对象
    private ImageView collectionIv;
    private SongOperationUtils songOperationTools;//操作歌曲的工具类
    private int groupPosition;
    private LayoutInflater layoutInflater = null;
    private View view = null;

    public SongOperationDialog(){}

    public SongOperationDialog(Activity activity,SongInfo songInfo,int groupPosition){
        this.activity = activity;
        this.songInfo = songInfo;
        this.layoutInflater = LayoutInflater.from(activity);
        this.groupPosition = groupPosition;
        this.view = layoutInflater.inflate(R.layout.dialog_song_operation, null);
        this.songOperationTools = new SongOperationUtils(songInfo);
        viewInit();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        TextView songName = (TextView)view.findViewById(R.id.tv_dialog_song_operation_songName);
        songName.setText(songInfo.getSongName());//设置所点击的歌曲名字
        //所有栏目设置点击事件
        builder.setView(this.view)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("test", "点击取消");
                    }
                });
        return builder.create();
    }

    private void viewInit(){
        view.findViewById(R.id.ll_dialog_song_operation_collect).setOnClickListener(this);
        view.findViewById(R.id.ll_dialog_song_operation_delete).setOnClickListener(this);
        view.findViewById(R.id.ll_dialog_song_operation_plus).setOnClickListener(this);
        view.findViewById(R.id.ll_dialog_song_operation_bell).setOnClickListener(this);
        view.findViewById(R.id.ll_dialog_song_operation_share).setOnClickListener(this);
        view.findViewById(R.id.ll_dialog_song_operation_send).setOnClickListener(this);
        view.findViewById(R.id.ll_dialog_song_operation_info).setOnClickListener(this);
        collectionIv = (ImageView)view.findViewById(R.id.iv_dialog_song_operation_collection);
        if (songInfo.getCollection()==0)//如果用户没有收藏这首歌曲
            collectionIv.setImageResource(R.drawable.song_operation_dialog_no_collection);
        else
            collectionIv.setImageResource(R.drawable.fragment_song_collection);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_dialog_song_operation_collect://如果用户点击收藏
//                如果歌曲原来未被收藏
                if (songInfo.getCollection()==0){
                    if(songOperationTools.collect()){
//                        设置Intent发送广播
                        Intent sendToUpdateCollection = new Intent(InfoClass.ActionString.ACTION_UPDATE_SONG_FILE_INFO);
                        sendToUpdateCollection.addCategory(InfoClass.CategoryString.UPDATE_COLLECTION_CATEGORY).putExtra("groupPosition",groupPosition);
                        activity.sendBroadcast(sendToUpdateCollection);
                        Toast.makeText(activity, "收藏成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(activity,"当前SD卡不可读，请您检查SD卡是否已插好",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    if (songOperationTools.cancelCollect()){
                        Intent sendToUpdateCollection = new Intent(InfoClass.ActionString.ACTION_UPDATE_SONG_FILE_INFO);
                        sendToUpdateCollection.addCategory(InfoClass.CategoryString.UPDATE_COLLECTION_CATEGORY).putExtra("groupPosition",groupPosition);
                        activity.sendBroadcast(sendToUpdateCollection);
                        Toast.makeText(activity,"取消收藏",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(activity,"当前SD卡不可读，请您检查SD卡是否已插好",Toast.LENGTH_LONG).show();
                    }
                }
                dismiss();
                break;
            case R.id.ll_dialog_song_operation_delete://删除
//                打开删除的提示框
                DeleteSongDialogFragment deleteSongDialogFragment = new DeleteSongDialogFragment(this.activity,songInfo,groupPosition);
                deleteSongDialogFragment.show(getActivity().getFragmentManager(),"DeleteSongDialogFragment");
                this.dismiss();
                break;
            case R.id.ll_dialog_song_operation_plus://添加
                songOperationTools.add();
                break;
            case R.id.ll_dialog_song_operation_bell://铃声
//                songOperationTools.setToBell();
                break;
            case R.id.ll_dialog_song_operation_share://分享
                songOperationTools.sharedSongFile();
                break;
            case R.id.ll_dialog_song_operation_send://发送
                songOperationTools.send();
                break;
            case R.id.ll_dialog_song_operation_info://查看歌曲详细信
//                Log.i("test", "歌曲信息");
                SongInfoDialog songInfoDialog = new SongInfoDialog(this.songInfo,groupPosition);
                songInfoDialog.show(activity.getFragmentManager(), "songOperationDialog");
                this.dismiss();
                break;
        }
    }
}
