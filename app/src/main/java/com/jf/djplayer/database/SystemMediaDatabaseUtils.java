package com.jf.djplayer.database;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.jf.djplayer.module.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by JF on 2016/1/22.
 * 专门读取系统歌曲数据库的自定义类
 * 该类提供相关方法读取系统存储歌曲的那张表
 */
public class SystemMediaDatabaseUtils{

    private Context context;
    private StringBuilder selection;//查询数据库时候的selection
    private String[] selectionArgs;//查询数据库时候的selectionArgs
    public SystemMediaDatabaseUtils(Context context){
        this.context = context;
    }

    /**
     * 获取系统里面所有存放有歌曲的路径
     * @return 返回存放有歌曲的路径集合
     */
    public List<String> getPathHasSong(){
        List<String> pathList = null;//这还是要返回的集合
        Set<String> pathSet = null;//通过Set集合来过滤重复路径
        File pathFile;
//        MediaStore.Audio.Media.DATA表示歌曲绝对路径
        Cursor songInfoCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DATA}, null, null, null);
//        如果系统没有歌曲直接返回
        if(songInfoCursor==null||songInfoCursor.getCount()==0) {
            return null;
        }
//        集合过滤歌曲路径
        pathSet = new TreeSet<>();
        while(songInfoCursor.moveToNext()){
            pathFile = new File(songInfoCursor.getString(songInfoCursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            pathSet.add(pathFile.getParent());
        }
        songInfoCursor.close();
//        过滤后的路径装到List集合里去
        pathList = new ArrayList<>(pathSet.size());
        for(String path:pathSet){
            pathList.add(path);
        }
        return pathList;
    }
    /**
     * 读取指定路径下的所有歌曲
     * @param pathString 通过字符串数组来指定扫描路径，如果为空表示读取所有路径下的歌曲
     * @return 返回装填这歌曲信息的集合
     */
    public List<Song> getSongInfoAccordingPath(String[] pathString) {
        List<Song> songInfoList = null;
        Song songInfo;
        //每次调用前都要将两个成员变量重置
        selection = new StringBuilder();//查询数据库时候的selection
        selectionArgs = null;//查询数据库时候的selectionArgs

//        如果所传入的路径不是空的
        if (pathString != null) {
            selectionArgs = new String[pathString.length];
            //        拼接处selection语句
            for (int i = 0; i < selectionArgs.length - 1; i++) {
                selection.append(MediaStore.Audio.Media.DATA + " like ? or ");
            }
            selection.append(MediaStore.Audio.Media.DATA + " like ?");
            //        拼接出selectionArgs
            for (int i = 0; i < selectionArgs.length; i++) {
                selectionArgs[i] = pathString[i] + "%";
            }
        }
        //扫描SD卡的歌曲
        Cursor sdCardCursor = getSystemSongInfo(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
//        Cursor sdCardCursor = getSystemSongInfo(MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
        if(sdCardCursor!=null) {
            songInfoList = cursorToSongInfo(sdCardCursor);
            sdCardCursor.close();
        }
        return songInfoList;

    }

    private Cursor getSystemSongInfo(Uri tableName){
        return context.getContentResolver().query(tableName,
                new String[]{MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA}, selection.toString(), selectionArgs, null);
    }

    //将读到的"Cursor"歌曲信息数据集合转成"List<_SongInfo>"集合
    private List<Song> cursorToSongInfo(Cursor songInfoCursor){
        int titleIndex = songInfoCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int artistIndex = songInfoCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int albumIndex = songInfoCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        int durationIndex = songInfoCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int sizeIndex = songInfoCursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
        int dataIndex = songInfoCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        List<Song> songInfoList = new ArrayList<>(songInfoCursor.getCount());
        Song songInfo;
        while (songInfoCursor.moveToNext()) {
            songInfo = new Song(
                    songInfoCursor.getString(titleIndex),
                    songInfoCursor.getString(artistIndex),
                    songInfoCursor.getString(albumIndex),
                    songInfoCursor.getInt(durationIndex),
                    songInfoCursor.getInt(sizeIndex),
                    songInfoCursor.getString(dataIndex)
            );
            songInfoList.add(songInfo);
        }
        return songInfoList;
    }
}
