package com.jf.djplayer.database;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.jf.djplayer.other.SongInfo;

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
    public List<SongInfo> getSongInfoAccordingPath(String[] pathString) {
        List<SongInfo> songInfoList = null;
        SongInfo songInfo;
        StringBuilder selection = new StringBuilder();//查询数据库时候的selection
        String[] selectionArgs = null;//查询数据库时候的selectionArgs

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
//        条件查询指定路径下的歌曲
        Cursor songInfoCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA}, selection.toString(), selectionArgs, null);
//        如果数据库里没有歌曲则返回空
        if(songInfoCursor == null||songInfoCursor.getCount()==0) {
            return null;
        }
        songInfoList = new ArrayList<>(songInfoCursor.getCount());
//                歌曲信息全部都以SongInfo对象形式存入集合
        while (songInfoCursor.moveToNext()) {
            songInfo = new SongInfo(
                    songInfoCursor.getString(songInfoCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                    songInfoCursor.getString(songInfoCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                    songInfoCursor.getString(songInfoCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                    songInfoCursor.getInt(songInfoCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)),
                    songInfoCursor.getInt(songInfoCursor.getColumnIndex(MediaStore.Audio.Media.SIZE)),
                    songInfoCursor.getString(songInfoCursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
//            Log.i("test",songInfoCursor.getString(songInfoCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            songInfoList.add(songInfo);
        }
        songInfoCursor.close();
        return songInfoList;

    }
}
