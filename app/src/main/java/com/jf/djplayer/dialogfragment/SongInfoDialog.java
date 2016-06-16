package com.jf.djplayer.dialogfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jf.djplayer.module.Song;
import com.jf.djplayer.R;

/**
 * Created by Administrator on 2015/8/16.
 * 展示歌曲信息用的Dialog
 */
public class SongInfoDialog extends DialogFragment {

    private View view = null;
    private int position;
    private Song songInfo = null;
    public SongInfoDialog(){}


    public SongInfoDialog(Song songInfo,int position){
        this.songInfo = songInfo;//获取传递来的歌曲信息
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_song_info,null);
        initView();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton("编辑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        启动编辑歌曲信息那个Dialog
                        EditSongInfoDialog editSongInfoDialog = new EditSongInfoDialog(songInfo,position);
                        editSongInfoDialog.show( ((FragmentActivity)getActivity()).getSupportFragmentManager(),"editSongInfoDialog");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
            //no thing to do
        }
    });
    return builder.create();
}
    private void initView(){
        int minutes = songInfo.getDuration()/1000/60;
        int second = songInfo.getDuration()/1000%60;
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_song_name)).setText(songInfo.getSongName());
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_singer)).setText(songInfo.getSingerName());
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_album)).setText(songInfo.getAlbum());
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_style)).setText("未知");
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_duration)).setText(minutes/10+minutes%10+":"+second);
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_size)).setText(songInfo.getSize()+"");
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_absolutePath)).setText(songInfo.getFileAbsolutePath());

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


}
