package com.jf.djplayer.dialogfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jf.djplayer.base.fragment.SongListFragment;
import com.jf.djplayer.module.Song;
import com.jf.djplayer.R;

/**
 * Created by Administrator on 2015/8/16.
 * 展示歌曲信息用的Dialog
 */
public class SongInfoDialog extends DialogFragment {

    private View view;
    private int position;
    private Song song;

//    @SuppressLint("ValidFragment")
//    public SongInfoDialog(Song song,int position){
//        this.song = song;//获取传递来的歌曲信息
//        this.position = position;
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments == null){
            return;
        }
        position = arguments.getInt(SongListFragment.KEY_POSITION, SongListFragment.VALUES_DEFAULT_POSITION);
        song = (Song)arguments.getSerializable(SongListFragment.KEY_SONG);
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
                        EditSongInfoDialog editSongInfoDialog = new EditSongInfoDialog();
                        editSongInfoDialog.setArguments(getArguments());
                        editSongInfoDialog.setTargetFragment(getTargetFragment(), SongListFragment.REQUEST_CODE_EDIT_SONG_INFO);
                        editSongInfoDialog.show( getActivity().getSupportFragmentManager(),"EditSongInfoDialog");
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
        int minutes = song.getDuration()/1000/60;
        int second = song.getDuration()/1000%60;
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_song_name)).setText(song.getSongName());
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_singer)).setText(song.getSingerName());
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_album)).setText(song.getAlbum());
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_style)).setText("未知");
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_duration)).setText(minutes/10+minutes%10+":"+second);
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_size)).setText(song.getSize()+"");
        ((TextView)view.findViewById(R.id.tv_dialog_song_info_absolutePath)).setText(song.getFileAbsolutePath());

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


}
