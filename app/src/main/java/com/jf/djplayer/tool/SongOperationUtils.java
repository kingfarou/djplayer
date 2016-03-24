package com.jf.djplayer.tool;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.jf.djplayer.InfoClass;
import com.jf.djplayer.MyApplication;
import com.jf.djplayer.SongInfo;
import com.jf.djplayer.tool.database.SongInfoOpenHelper;

import java.io.File;

/**
 * Created by JF on 2015/10/30.
 * 该类封装了对一首歌曲做的所有操作用的方法
 * 包括：
 * 收藏以及取消收藏、删除、添加、设为铃声、分享、发送、歌曲信息
 */
public class SongOperationUtils {
    private SongInfo songInfo;
    public static final String CALLER_RING = "callerRing";//这个表示来电铃声
    public static final String INFORM_RING = "informRing";//这个表示通知铃声
    public static final String ALLRING = "allRING";//这个表示所有铃声
    public SongOperationUtils(SongInfo songInfo){
        this.songInfo = songInfo;
    }

    /**
     * 蒋某首歌设置为被收藏
     * @return true if the operation is successful，false if the operation is fail
     */
    public boolean collect(){
//        判断SD卡是否可读取
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) return false;
//        修改
    //            SQLiteDatabase mySongInfoDatabase = SQLiteDatabase.openOrCreateDatabase(new File(InfoClass.DatabaseInfo.DATABASE_FILE_DIR, "SongInfo.db"), null);
        SQLiteDatabase mySongInfoDatabase = new SongInfoOpenHelper(MyApplication.getContext(),1).getWritableDatabase();
    //            歌曲设置为已收藏
        mySongInfoDatabase.execSQL("update" + " " + InfoClass.DatabaseInfo.TABLE_NAME + " " +
                "set" + " " + InfoClass.DatabaseInfo.COLLECTION + "=1" + " " +
                "where" + " " + InfoClass.DatabaseInfo.ABSOLUTE_PATH + "=" + "'" + songInfo.getSongAbsolutePath() + "'");
        mySongInfoDatabase.close();
        return true;
    }


    /**
     * collect the song that appointed by the songAbsolutePath
     * @return true if the operation is successful，false if the operation is fail
     */
    public boolean cancelCollect() {
//            修改
//            SQLiteDatabase mySongInfoDatabase = SQLiteDatabase.openOrCreateDatabase(new File(InfoClass.DatabaseInfo.DATABASE_FILE_DIR, "SongInfo.db"), null);
        SQLiteDatabase mySongInfoDatabase = new SongInfoOpenHelper(MyApplication.getContext(), 1).getWritableDatabase();
//            设置歌曲没有收藏
        mySongInfoDatabase.execSQL("update" + " " + InfoClass.DatabaseInfo.TABLE_NAME + " " +
                "set" + " " + InfoClass.DatabaseInfo.COLLECTION + "=0" + " " +
                "where" + " " + InfoClass.DatabaseInfo.ABSOLUTE_PATH + "=" + "'" + songInfo.getSongAbsolutePath() + "'");
        mySongInfoDatabase.close();
        return true;
    }
    /**
     * delete the song and the information of the song
     * @param songFile delete the songFile or not
     * @param artistPicture delete the artistPicture or not
     * @param soundFile delete the soundFile or not
     * @param lyric delete the lyric or not
     * @return true if the operation is successful,false if the operation is false
     */
    public boolean deleteSong(boolean songFile,boolean artistPicture,boolean soundFile,boolean lyric){
        //判断SD卡是否可读了
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //如果用户勾选删除歌曲文件
            if (songFile){
                new File(songInfo.getSongAbsolutePath()).delete();
            }
//            如果用户勾选删除歌手图片
            if (artistPicture)
                new File(InfoClass.FileInfo.SINGER_PICTURE_FILE, songInfo.getSongSinger() + ".jpg").delete();
//            如果用户勾选删除音效文件
            if (soundFile)
                ;
//            如果用户勾选删除歌词文件
            if (lyric)
                ;
//            从我数据库里删除歌曲信息
            String deleteString = "delete from" + " " + InfoClass.DatabaseInfo.TABLE_NAME + " " + "where" + " "
                    + InfoClass.DatabaseInfo.ABSOLUTE_PATH + "=" + "'" + songInfo.getSongAbsolutePath() + "'";
//            修改
//            SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(new File(InfoClass.DatabaseInfo.DATABASE_FILE_DIR, "songInfo.db"), null);
            SQLiteDatabase sqLiteDatabase = new SongInfoOpenHelper(MyApplication.getContext(),1).getWritableDatabase();
            sqLiteDatabase.execSQL(deleteString);
            sqLiteDatabase.close();
            return true;
        }
        return false;
    }
    public boolean add(){
        return false;
    }

    /**
     * set the ring become caller ring 、inform ring or every
     * @param ringType what the ring type do you set the ring to
     * @return
     */
    public boolean setToBell(String ringType){
        switch (ringType){
//            将该歌曲设置成来电的铃声
            case CALLER_RING:
                return true;
//            将该歌曲设置成提示音
            case INFORM_RING:
                return true;
//            将该歌曲设置成所有的铃声
            case ALLRING:
                return true;
        }
        return false;
    }
    public boolean sharedSongFile(){
        Intent sharedFileIntent = new Intent();
        return false;
    }

    public boolean send(){
        return false;
    }

    /**
     * 用来修改数据库的歌曲信息
     * @return true if the operation is successful,false if not.
     */
    public boolean updateSongFileInfo(){
        SQLiteOpenHelper sqLiteOpenHelper = new SongInfoOpenHelper(MyApplication.getContext(),1);
        ((SongInfoOpenHelper)sqLiteOpenHelper).updateLocalMusicTables(songInfo);
        return true;
    }
}
