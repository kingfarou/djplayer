package com.jf.djplayer.dialogfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jf.djplayer.bean.Song;
import com.jf.djplayer.R;

import com.jf.djplayer.database.SongInfoOpenHelper;

/**
 * Created by Administrator on 2015/8/20.
 */
public class EditSongInfoDialog extends DialogFragment {

    private Song song;
    private EditText songNameEt;//歌名
    private EditText singerEt;//歌手
    private EditText albumEt;//专辑
    private EditText styleEt;//风格
    private int position;//被点击的歌曲在原列表位置

    private View view;

//    public EditSongInfoDialog(){}

//    @SuppressLint("ValidFragment")
//    public EditSongInfoDialog(Song song,int position){
//        this.song = song;
//        this.position = position;
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments == null){
            return;
        }
        this.position = arguments.getInt(SongOperationDialog.KEY_POSITION, SongOperationDialog.VALUES_DEFAULT_POSITION);
        this.song = (Song)arguments.getSerializable(SongOperationDialog.KEY_SONG);
    }

    //    布局文件里的View做初始化
    private void initView(){
        songNameEt = (EditText)view.findViewById(R.id.et_dialog_edit_songInfo_songName);
        singerEt = (EditText)view.findViewById(R.id.et_dialog_edit_songInfo_artistName);
        albumEt = (EditText)view.findViewById(R.id.et_dialog_edit_songInfo_album);
        styleEt = (EditText)view.findViewById(R.id.et_dialog_edit_songInfo_style);
        songNameEt.setText(song.getSongName());
        singerEt.setText(song.getSingerName());
        albumEt.setText(song.getAlbum());
        styleEt.setText("<未知>");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_song_info,null);
        initView();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        将新数据给设置好
                        song.setSongName(songNameEt.getText().toString());
                        song.setSingerName(singerEt.getText().toString());
                        song.setAlbum(albumEt.getText().toString());
//                        调用工具类来更新数据库的歌曲信息
                        SongInfoOpenHelper updateOpenHelper = new SongInfoOpenHelper(getActivity());
                        updateOpenHelper.updateLocalMusic(song);
//                        发送广播通知界面更新数据
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, new Intent().putExtra(SongOperationDialog.KEY_POSITION, position));
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
}
