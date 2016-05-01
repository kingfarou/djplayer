package com.jf.djplayer.dialogfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jf.djplayer.other.SongInfo;
import com.jf.djplayer.R;

/**
 * Created by Administrator on 2015/8/16.
 * 展示歌曲信息用的Dialog
 */
public class SongInfoDialog extends DialogFragment {

    private View view = null;
    private int position;
    private SongInfo songInfo = null;
    public SongInfoDialog(){}


    public SongInfoDialog(SongInfo songInfo,int position){
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
                        editSongInfoDialog.show(getActivity().getFragmentManager(),"editSongInfoDialog");
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
        int minutes = songInfo.getSongDuration()/1000/60;
        int second = songInfo.getSongDuration()/1000%60;
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_songName)).setText("歌曲："+songInfo.getSongName());
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_singer)).setText("歌手："+songInfo.getSingerName());
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_albumn)).setText("专辑："+songInfo.getSongAlbum());
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_style)).setText("风格；未知");
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_duration)).setText("时长："+minutes/10+minutes%10+":"+second);
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_size)).setText("大小："+songInfo.getSongSize());
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_absolutePath)).setText("存储位置："+songInfo.getSongAbsolutePath());

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


}
