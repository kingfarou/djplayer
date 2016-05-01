package com.jf.djplayer.dialogfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


import com.jf.djplayer.other.SongInfo;
import com.jf.djplayer.broadcastreceiver.UpdateUiSongInfoReceiver;
import com.jf.djplayer.tool.FileTool;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.R;

import java.io.File;

/**
 * Created by Administrator on 2015/8/16.
 */
public class DeleteSongDialogFragment extends DialogFragment implements CompoundButton.OnCheckedChangeListener{


    private SongInfo songInfo = null;//要操作的歌曲信息
    private boolean isDeleteSongFile = false;//是否删除歌曲文件
    private boolean isDeleteSingerPicture = false;//是否删除歌手图片
    private boolean isDeleteLyricFile = true;//是否删除歌词文件
    private boolean isDeleteSoundFile = true;//是否删除音效文件
    private boolean[] selected = new boolean[]{false,false,true,true};//数组保存用户做的选择
    private int groupPosition;
    private View view = null;

    public DeleteSongDialogFragment(){}
    public DeleteSongDialogFragment(Context context,SongInfo songInfo,int groupPosition) {
        this.songInfo = songInfo;
        this.groupPosition = groupPosition;
    }

    public DeleteSongDialogFragment(SongInfo songInfo,int position){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        viewInit();
        //获取到builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                如果存储卡不可读
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(getActivity(), "SD卡读取失败，请您检查SD卡是否已接好", Toast.LENGTH_SHORT).show();
                    return;
                }
                File sdCardFile = Environment.getExternalStorageDirectory();//获取SD卡的路径
//                如果删除歌曲文件
                if (isDeleteSongFile) {
                    File songFile = new File(songInfo.getSongAbsolutePath());
                    if(!songFile.delete()){
                        Toast.makeText(getActivity(),"原音频文件未删除",Toast.LENGTH_SHORT).show();
                    }
                }
//                如果删除歌手图片
                if (isDeleteSingerPicture) {
//                    File artistPictureDir = new File(Environment.getExternalStorageDirectory(),FileTool.SINGER_PICTURE_DIR);
//                    File artistPictureFile = new File(artistPictureDir,songInfo.getSingerName()+".jpg");
//                    artistPictureFile.delete();
                }
                if (isDeleteSoundFile) {
                }
                if (isDeleteLyricFile) {
                    File lyricDir = new File(sdCardFile, FileTool.LYRIC_DIR);//连接歌词文件路径
                    File songFile = new File(songInfo.getSongAbsolutePath());//连接歌曲那个文件
//                    截取歌曲文件名字（去拓展名）
                    String songFileName = songFile.getName().substring(0,songFile.getName().length()-4);
//                    Log.i("test",songFileName);
                    File lyricFile = new File(lyricDir,songFileName+".lrc");//拼接要删除的歌词文件名字
                    lyricFile.delete();
                }
//                清除数据库里面的歌曲记录
                SongInfoOpenHelper deleteOpenHelper = new SongInfoOpenHelper(getActivity());
                deleteOpenHelper.deleteFromLocalMusicTable(songInfo);
//                发送广播通知界面更新UI
                Intent deleteSongInfo = new Intent(UpdateUiSongInfoReceiver.ACTION_DELETE_SONG_FILE);
                deleteSongInfo.putExtra(UpdateUiSongInfoReceiver.position,groupPosition);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(deleteSongInfo);
            }
        })//setPositionButton()
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteSongDialogFragment.this.getDialog().cancel();
            }
        });//setNegativeButton()


        return builder.create();
    }

    private void viewInit(){
        view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete_song,null);
        ((TextView)view.findViewById(R.id.tv_dialog_delete_song_songName)).setText("删除"+"\""+songInfo.getSongName()+"\""+"?");
        ((CheckBox)view.findViewById(R.id.cb_dialog_delete_song_songFile)).setOnCheckedChangeListener(this);
        ((CheckBox)view.findViewById(R.id.cb_dialog_delete_song_singerPicture)).setOnCheckedChangeListener(this);
        ((CheckBox)view.findViewById(R.id.cb_dialog_delete_song_soundFile)).setOnCheckedChangeListener(this);
        ((CheckBox)view.findViewById(R.id.cb_dialog_delete_song_lyric)).setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cb_dialog_delete_song_songFile:
                isDeleteSongFile = isChecked;
                break;
            case R.id.cb_dialog_delete_song_singerPicture:
                isDeleteSingerPicture = isChecked;
                break;
            case R.id.cb_dialog_delete_song_soundFile:
                isDeleteSoundFile = isChecked;
                break;
            case R.id.cb_dialog_delete_song_lyric:
                isDeleteLyricFile = isChecked;
                break;
            default:break;
        }
    }//onCheckedChanged
}
