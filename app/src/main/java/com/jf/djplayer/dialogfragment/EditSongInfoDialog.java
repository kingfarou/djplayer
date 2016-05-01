package com.jf.djplayer.dialogfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jf.djplayer.other.SongInfo;
import com.jf.djplayer.R;
import com.jf.djplayer.broadcastreceiver.UpdateUiSongInfoReceiver;

import com.jf.djplayer.database.SongInfoOpenHelper;

/**
 * Created by Administrator on 2015/8/20.
 */
public class EditSongInfoDialog extends DialogFragment {

    private SongInfo songInfo = null;
    private EditText songNameEditText = null;
    private int position;
    private EditText artistNameEditText = null;
    private EditText albumEditText = null;
    private EditText styleEditText = null;

    private View view = null;


    public EditSongInfoDialog(SongInfo songInfo,int position){
        this.songInfo = songInfo;
        this.position = position;
    }

//    布局文件里的View做初始化
    private void initView(){
        songNameEditText = (EditText)view.findViewById(R.id.et_dialog_edit_songInfo_songName);
        artistNameEditText = (EditText)view.findViewById(R.id.et_dialog_edit_songInfo_artistName);
        albumEditText = (EditText)view.findViewById(R.id.et_dialog_edit_songInfo_album);
        styleEditText = (EditText)view.findViewById(R.id.et_dialog_edit_songInfo_style);
        songNameEditText.setText(songInfo.getSongName());
        artistNameEditText.setText(songInfo.getSingerName());
        albumEditText.setText(songInfo.getSongAlbum());
        styleEditText.setText("<未知>");
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
                        songInfo.setSongName(songNameEditText.getText().toString());
                        songInfo.setSingerName(artistNameEditText.getText().toString());
                        songInfo.setSongAlbum(albumEditText.getText().toString());
//                        调用工具类来更新数据库的歌曲信息
                        SongInfoOpenHelper updateOpenHelper = new SongInfoOpenHelper(getActivity());
                        updateOpenHelper.updateLocalMusicTables(songInfo);
//                        发送广播通知界面更新数据
//                        Intent updateSongInfoIntent = new Intent(UpdateUiSongInfoReceiver.UPDATE_SONG_INFO);
                        Intent updateSongInfoIntent = new Intent(UpdateUiSongInfoReceiver.ACTION_UPDATE_SONG_FILE_INFO);
                        updateSongInfoIntent.putExtra(UpdateUiSongInfoReceiver.position,position);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(updateSongInfoIntent);
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
