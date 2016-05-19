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

    private SongInfo songInfo;
    private EditText et_song_name;//歌名
    private EditText et_artist_name;//歌手
    private EditText et_album;//专辑
    private EditText et_style;//风格
    private int position;

    private View view;


    public EditSongInfoDialog(SongInfo songInfo,int position){
        this.songInfo = songInfo;
        this.position = position;
    }

//    布局文件里的View做初始化
    private void initView(){
        et_song_name = (EditText)view.findViewById(R.id.et_dialog_edit_songInfo_songName);
        et_artist_name = (EditText)view.findViewById(R.id.et_dialog_edit_songInfo_artistName);
        et_album = (EditText)view.findViewById(R.id.et_dialog_edit_songInfo_album);
        et_style = (EditText)view.findViewById(R.id.et_dialog_edit_songInfo_style);
        et_song_name.setText(songInfo.getSongName());
        et_artist_name.setText(songInfo.getSingerName());
        et_album.setText(songInfo.getSongAlbum());
        et_style.setText("<未知>");
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
                        songInfo.setSongName(et_song_name.getText().toString());
                        songInfo.setSingerName(et_artist_name.getText().toString());
                        songInfo.setSongAlbum(et_album.getText().toString());
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
