package com.jf.djplayer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jf.djplayer.SongInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JF on 2016/1/5.
 * 该类专门管理自己音乐文件的数据库
 * 该类所提供的方法
 * 添加一条歌曲记录
 * 修改指定绝对路径下的歌曲信息
 * 查询数据库中歌曲数量
 * 查询用户所喜爱的歌曲数量
 */
public class SongInfoOpenHelper extends SQLiteOpenHelper {

    private static final String SONG_INFO_DATABASE_NAME = "SONG_INFO_DATABASE";//这是数据库的名字
    private static final String LOCAL_MUSIC_TABLE_NAME = "LOCAL_MUSIC_TABLE";//本地音乐歌曲信息表名

//    这是“SONG_INFO_TABLE”表的所有列的名字
    public static final String id = "_ID";//主键
    public static final String title = "_title";//歌名
    public static final String artist = "_artist";//歌手
    public static final String album = "_album";//专辑
    public static final String duration = "_duration";//时长
    public static final String size = "_size";//大小
    public static final String absolutionPath = "absolute_path";//音频文件绝对路径
    public static final String folderPath = "folder_path";//歌曲文件所在的文件夹路径
    public static final String collection = "_collection";//标记用户是否收藏音乐
    private static final String lastPlayTime = "last_play_time";//标记歌曲最后一次播放时间
    public static final String isDownload = "is_download";//标记歌曲是否是从网络下载

    /**
     * 创建本地音乐的数据库操作工具
     * @param context 环境
     */
    public SongInfoOpenHelper(Context context){
        super(context,SONG_INFO_DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createLocalMusicTables = "CREATE TABLE IF NOT EXISTS" + " " + LOCAL_MUSIC_TABLE_NAME +"(" +
                id + " " + "INTEGER PRIMARY KEY," + title + " " + "TEXT," +
                artist + " " + "TEXT,"+ album + " " + "TEXT," +
                duration + " " + "INTEGER," +size + " " + "INTEGER," +
                absolutionPath + " " + "TEXT UNIQUE,"+folderPath+" "+"TEXT,"+
                collection + " " + "INTEGER," + lastPlayTime+" "+"INTEGER,"+
                isDownload+" "+"INTEGER"+");";

        db.execSQL(createLocalMusicTables);//创建本地音乐的表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 获取本地音乐所有歌曲信息
     * @return 一个装着本地数据库所有歌曲的集合
     */
    public List<SongInfo> getLocalMusicSongInfo(){
//        读取所有歌曲数据
        Cursor songInfoCursor = getReadableDatabase().query(LOCAL_MUSIC_TABLE_NAME,null,null,null,null,null,null);
        //如果没有歌曲数据直接返回
        if (songInfoCursor.getCount()==0){
            songInfoCursor.close();
            return null;
        }
        List<SongInfo> songInfoList = new ArrayList<>(songInfoCursor.getCount());
        SongInfo songInfo;
//        装填所有歌曲数据
        while (songInfoCursor.moveToNext()){
            songInfo = new SongInfo();
            songInfo.setSongName(songInfoCursor.getString(songInfoCursor.getColumnIndex(title)));
            songInfo.setSingerName(songInfoCursor.getString(songInfoCursor.getColumnIndex(artist)));
            songInfo.setSongAlbum(songInfoCursor.getString(songInfoCursor.getColumnIndex(album)));
            songInfo.setSongDuration(songInfoCursor.getInt(songInfoCursor.getColumnIndex(duration)));
            songInfo.setSongSize(songInfoCursor.getInt(songInfoCursor.getColumnIndex(size)));
            songInfo.setSongAbsolutePath(songInfoCursor.getString(songInfoCursor.getColumnIndex(absolutionPath)));
            songInfo.setCollection(songInfoCursor.getInt(songInfoCursor.getColumnIndex(collection)));
            songInfoList.add(songInfo);
        }
        songInfoCursor.close();
        return songInfoList;
    }

    /**
     * 向本地音乐表里面插入一首歌曲信息
     * @param songInfo 需插入的歌曲对象
     * @return 返回新插入的行数ID，如果插入发生错误，返回-1
     */
    public long insertInLocalMusicTable(SongInfo songInfo){
        if(songInfo==null) {
            return -1;//对传入的参数进行检查
        }
        SQLiteDatabase songInfoDatabase = getWritableDatabase();
        ContentValues songInfoValues = new ContentValues();
        songInfoValues.putNull(id);//id由系统去进行自增
        songInfoValues.put(title, songInfo.getSongName());
        songInfoValues.put(artist,songInfo.getSingerName());
        songInfoValues.put(album,songInfo.getSongAlbum());
        songInfoValues.put(duration,songInfo.getSongDuration());
        songInfoValues.put(size,songInfo.getSongSize());
        songInfoValues.put(folderPath,new File(songInfo.getSongAbsolutePath()).getParent());
        songInfoValues.put(absolutionPath, songInfo.getSongAbsolutePath());
        songInfoValues.put(collection, songInfo.getCollection());
        long id =  songInfoDatabase.insert(LOCAL_MUSIC_TABLE_NAME, null, songInfoValues);
        songInfoDatabase.close();
        return id;
    }

    /**
     * 更新由songInfo对象里面的绝对路径
     * 所指定的那条歌曲记录
     * @param songInfo 待更新的歌曲对象
     */
    public void updateLocalMusicTables(SongInfo songInfo) {
        if(songInfo==null) {
            return;
        }
//        拼接需要更新的字符串
        SQLiteDatabase songInfoDatabase = getWritableDatabase();
        String updateString = "update"+" "+LOCAL_MUSIC_TABLE_NAME+" "+"set"+" "+
                title+"="+"'"+songInfo.getSongName()+"'"+","+
                artist+"="+"'"+songInfo.getSingerName()+"'"+","+
                album+"="+"'"+songInfo.getSongAlbum()+"'"+" "+
                "where"+" "+absolutionPath+"="+"'"+songInfo.getSongAbsolutePath()+"'"+";";
        songInfoDatabase.execSQL(updateString);

        songInfoDatabase.close();
    }

    /**
     * 更新由"songInfo"所制定的绝对路径最近一次播放时间
     * @param songInfo 要更新的"SongInfo"对象
     * @param lastPlayTime 最近一次播放时间
     */
    public void updateSongRecentlyPlay(SongInfo songInfo, long lastPlayTime){
        String updateString = "update"+" "+LOCAL_MUSIC_TABLE_NAME+" "+"set"+" "+
                SongInfoOpenHelper.lastPlayTime+"="+lastPlayTime+" "+
                "where"+" "+absolutionPath+"="+"'"+songInfo.getSongAbsolutePath()+"'"+";";
    }

    /**
     * 删除SongInfo对象里的绝对路径
     * 所标示的歌曲文件
     * @param songInfo
     */
    public void deleteFromLocalMusicTable(SongInfo songInfo) {
        String deleteSql = "delete from "+ LOCAL_MUSIC_TABLE_NAME +" where "+absolutionPath+"="+"'"+songInfo.getSongAbsolutePath()+"';";
        getWritableDatabase().execSQL(deleteSql);
    }

    /**
     * 获取所有用户收藏的歌
     */
    public List<SongInfo> getCollectionSongInfo(){
        List<SongInfo> songInfoList;
        SongInfo collectionSongInfo;
        Cursor collectionCursor = getReadableDatabase().query(LOCAL_MUSIC_TABLE_NAME,null,collection+"=?",new String[]{"1"},null,null,null,null);
//        如果没有收藏歌曲直接返回元素个数零的集合
        if(collectionCursor.getCount()==0) {
            collectionCursor.close();
            return null;
        }
        songInfoList = new ArrayList<>(collectionCursor.getCount());
        while(collectionCursor.moveToNext()){
            collectionSongInfo = new SongInfo();
            collectionSongInfo.setSongName(collectionCursor.getString(collectionCursor.getColumnIndex(title)));
            collectionSongInfo.setSingerName(collectionCursor.getString(collectionCursor.getColumnIndex(artist)));
            collectionSongInfo.setSongAlbum(collectionCursor.getString(collectionCursor.getColumnIndex(album)));
            collectionSongInfo.setSongDuration(collectionCursor.getInt(collectionCursor.getColumnIndex(duration)));
            collectionSongInfo.setSongSize(collectionCursor.getInt(collectionCursor.getColumnIndex(size)));
            collectionSongInfo.setSongAbsolutePath(collectionCursor.getString(collectionCursor.getColumnIndex(absolutionPath)));
            collectionSongInfo.setCollection(1);
            songInfoList.add(collectionSongInfo);
        }
        collectionCursor.close();
        return songInfoList;
    }

    /**
     * 获取用户所收藏的歌曲数量
     * @return 返回用户所收藏的歌曲数量
     */
    public int getCollectionNum(){
        Cursor collectionCursor = getReadableDatabase().query(LOCAL_MUSIC_TABLE_NAME,new String[]{id},collection+"=?",new String[]{"1"},null,null,null,null);
        int collectionSongNumber = collectionCursor.getCount();
        collectionCursor.close();
        return collectionSongNumber;
    }

    /**
     * 获取本地音乐歌曲数量
     * @return 数据库的歌曲数量
     */
    public int getLocalMusicNumber(){
        Cursor songNumberCursor = getReadableDatabase().query(LOCAL_MUSIC_TABLE_NAME, new String[]{id}, null, null, null, null, null);
        int songNumber = songNumberCursor.getCount();
        songNumberCursor.close();
        return songNumber;
    }

    /**
     * 蒋某一首歌设置为收藏或者取消收藏
     * @param songInfo 要收藏的（或者要取消收藏的）歌曲对象
     */
    public void updateCollection(SongInfo songInfo,int collectionOrNot){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String collectionString = "update"+" "+LOCAL_MUSIC_TABLE_NAME+" "+"set"+" "+
                collection+"="+collectionOrNot+" "+"where"+" "+absolutionPath+"="+"'"+songInfo.getSongAbsolutePath()+"'"+";";
        sqLiteDatabase.execSQL(collectionString);
    }

    /*
     * 获取"LOCAL_MUSIC_TABLES"表里指定列下面的所有的值（不重复的）
     * 例如获取所有歌手名字（不重复的），或者获取所有专辑名字（不重复的）
     * @param columnName 想要获取的那个列
     * @return 如果没有任何数据则返回空
     */
    private List<String> getDistinctValue(String columnName) {
        List<String> dataList;
        Cursor dataCursor = getReadableDatabase().query(true, LOCAL_MUSIC_TABLE_NAME, new String[]{columnName}, null, null, null, null, null, null);
        if (dataCursor.getCount() == 0) return null;
        dataList = new ArrayList<>(dataCursor.getCount());

        while (dataCursor.moveToNext()) {
            dataList.add(dataCursor.getString(dataCursor.getColumnIndex(columnName)));
        }
        dataCursor.close();
        return dataList;
    }

    /**
     * 获取指定列下面所有的值所对应的歌曲的数量
     * 比如获取所有歌手所对应的歌曲数量
     * 或者获取所有专辑所对应的歌曲数量
     * @param columnName 想要获取的那个列
     * @return
     */
    public List<Map<String,String>> getValueSongNumber(String columnName){
        List<String> dataList = getDistinctValue(columnName);
        if (dataList==null||dataList.size()==0) return null;//如果为空直接返回
        SQLiteDatabase songNumberDatabase= getReadableDatabase();
        Cursor songNumberCursor;
        List<Map<String,String>> mapList = new ArrayList<Map<String,String>>(dataList.size());
        Map<String,String> itemMap;
//        读取“dataList”里面每个数据所对应的歌曲树木
        for (String columnValues:dataList) {
            songNumberCursor = songNumberDatabase.query(LOCAL_MUSIC_TABLE_NAME, new String[]{collection}, columnName+"=?", new String[]{columnValues}, null, null, null);
            itemMap = new HashMap<>(2);
//            装填值及所对应的歌曲数目
            itemMap.put("title", columnValues);
            itemMap.put("content", songNumberCursor.getCount() + "");
            mapList.add(itemMap);
            songNumberCursor.close();
        }
        songNumberDatabase.close();
        return mapList;
    }

}
