package com.jf.djplayer.tool.database;

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

    private static final String SONG_INFO_DATABASE_NAME = "JfPlayerDatabase";//这是数据库的名字
    private static final String LOCAL_MUSIC_TABLENAME = "LOCAL_MUSIC";//本地音乐歌曲信息表名
//    该表记录所有下载的歌——有些歌曲是下载的有些歌曲是本地的
    private static final String DOWNLOAD_SONG_TABLE_NAME = "DOWN_LOAD_SONG";//这是下载歌曲表名
    private static final String RECENTLY_PLAY_TABLE_NAME = "RECENTLY_PLAY";//这个表里存着最近播放过的十首歌曲
//    这是“LOCAL_MUSIC”表的所有列的名字
    public static final String id = "_ID";//主键
    public static final String title = "title";//歌名
    public static final String artist = "artist";//歌手
    public static final String album = "album";//专辑
    public static final String duration = "duration";//时长
    public static final String size = "_size";//大小
    public static final String absolutionPath = "absolutePath";//音频文件绝对路径
    public static final String folderPath = "folderPath";//歌曲文件所在的文件夹路径
    public static final String collection = "collection";//标记用户是否收藏音乐

//    "RECENTLY_PLAY"表关键字
    private static final String LAST_PLAY_TIME = "lastPlayTime";//最后一次播放时间
    private static final int RECENTLY_PLAY_MAX_NUM = 10;//最近播放列表所容纳的最大歌曲数量

    /**
     * 创建本地音乐的数据库操作工具
     * @param context 环境
     * @param version 版本
     */
    public SongInfoOpenHelper(Context context,int version){
        super(context,SONG_INFO_DATABASE_NAME,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        拼接一个建表语句
//        内容包括：主键ID、歌曲名字、
//        歌手名字、专辑名字、
//        播放时长、歌曲大小、
//        绝对路径，文件位置，
//        音频文件绝对路径
        String createLocalMusicTables = "CREATE TABLE IF NOT EXISTS" + " " + LOCAL_MUSIC_TABLENAME +"(" +
                id + " " + "INTEGER PRIMARY KEY," + title + " " + "TEXT," +
                artist + " " + "TEXT,"+ album + " " + "TEXT," +
                duration + " " + "INTEGER," +size + " " + "INTEGER," +
                absolutionPath + " " + "TEXT UNIQUE,"+folderPath+" "+"TEXT,"+
                collection + " " + "INTEGER" +
                ");";

//        拼接一个建表语句
//        表里存着所有经由本软件所下载的歌
        String createDownLoadTables = "CREATE TABLE IF NOT EXISTS"+" "+ DOWNLOAD_SONG_TABLE_NAME +"("+
                id+" "+"INTEGER PRIMARY KEY,"+absolutionPath+" "+"TEXT UNIQUE"+");";

//        拼接一个建表语句
//        里面存着用户最近所播放的十首歌曲
        String createRecentlyPlayTables = "CREATE TABLE IF NOT EXISTS"+" "+ RECENTLY_PLAY_TABLE_NAME +"("+
                id+" "+"INTEGER PRIMARY KEY,"+absolutionPath+" "+"TEXT UNIQUE,"+LAST_PLAY_TIME+" "+"INTEGER"+");";

        db.execSQL(createLocalMusicTables);//创建本地音乐的表
        db.execSQL(createDownLoadTables);//创建所下载的歌曲的表
        db.execSQL(createRecentlyPlayTables);//创建最近播放歌曲的表
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
        Cursor songInfoCursor = getReadableDatabase().query(LOCAL_MUSIC_TABLENAME,null,null,null,null,null,null);
        //如果没有歌曲数据直接返回
        if (songInfoCursor.getCount()==0){
            songInfoCursor.close();
            return null;
        }
        List<SongInfo> songInfoList = new ArrayList<SongInfo>(songInfoCursor.getCount());
        SongInfo songInfo;
//        装填所有歌曲数据
        while (songInfoCursor.moveToNext()){
            songInfo = new SongInfo();
            songInfo.setSongName(songInfoCursor.getString(songInfoCursor.getColumnIndex(title)));
            songInfo.setSongSinger(songInfoCursor.getString(songInfoCursor.getColumnIndex(artist)));
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
     *
     * 向本地音乐表里面插入一首歌曲信息
     * @param songInfo 需插入的歌曲对象
     * @return 返回新插入的行数ID，如果插入发生错误，返回-1
     */
    public long insertInLocalMusicTable(SongInfo songInfo){
        if(songInfo==null) return -1;//对传入的参数进行检查
        SQLiteDatabase songInfoDatabase = getWritableDatabase();
        ContentValues songInfoValues = new ContentValues();
        songInfoValues.putNull(id);//id由系统去进行自增
        songInfoValues.put(title, songInfo.getSongName());
        songInfoValues.put(artist,songInfo.getSongSinger());
        songInfoValues.put(album,songInfo.getSongAlbum());
        songInfoValues.put(duration,songInfo.getSongDuration());
        songInfoValues.put(size,songInfo.getSongSize());
        songInfoValues.put(folderPath,new File(songInfo.getSongAbsolutePath()).getParent());
        songInfoValues.put(absolutionPath, songInfo.getSongAbsolutePath());
        songInfoValues.put(collection, songInfo.getCollection());
        long id =  songInfoDatabase.insert(LOCAL_MUSIC_TABLENAME, null, songInfoValues);
        songInfoDatabase.close();
        return id;
    }

    /**
     * 更新由songInfo对象里面的绝对路径
     * 所指定的那条歌曲记录
     * @param songInfo 待更新的歌曲对象
     */
    public void updateLocalMusicTables(SongInfo songInfo) {
        if(songInfo==null) return;
//        拼接需要更新的字符串
        SQLiteDatabase songInfoDatabase = getWritableDatabase();
        StringBuilder updateBuilder = new StringBuilder(34);
//        update LOCAL_MUSIC_TABLENAME set title = 'songInfo.getSongName()',artist = 'songInfo.getSingerName()'...
//        拼接SQLite语句
        updateBuilder.append("update").append(" ").append(LOCAL_MUSIC_TABLENAME).append(" ")
                .append("set").append(" ").append(title).append("=")
                .append("'").append(songInfo.getSongName()).append("'").append(",")
                .append(artist).append("=").append("'").append(songInfo.getSongSinger())
                .append("'").append(",").append(album).append("=")
                .append("'").append(songInfo.getSongAlbum()).append("'").append(" ")
                .append("where").append(" ").append(absolutionPath).append("=")
                .append("'").append(songInfo.getSongAbsolutePath()).append("'").append(";");
        songInfoDatabase.execSQL(updateBuilder.toString());
        songInfoDatabase.close();
    }

    /**
     * 删除SongInfo对象里的绝对路径
     * 所标示的歌曲文件
     * @param songInfo
     */
    public void deleteFromLocalMusicTable(SongInfo songInfo) {
        String deleteSql = "delete from "+ LOCAL_MUSIC_TABLENAME +" where "+absolutionPath+"="+"'"+songInfo.getSongAbsolutePath()+"';";
        getWritableDatabase().execSQL(deleteSql);
    }

    /**
     * 获取所有用户收藏的歌
     */
    public List<SongInfo> getCollectionSongInfo(){
        List<SongInfo> songInfoList;
        SongInfo collectionSongInfo;
        Cursor collectionCursor = getReadableDatabase().query(LOCAL_MUSIC_TABLENAME,null,collection+"=?",new String[]{"1"},null,null,null,null);
//        如果没有收藏歌曲直接返回元素个数零的集合
        if(collectionCursor.getCount()==0) {
            collectionCursor.close();
            return null;
        }
        songInfoList = new ArrayList<>(collectionCursor.getCount());
        while(collectionCursor.moveToNext()){
            collectionSongInfo = new SongInfo();
            collectionSongInfo.setSongName(collectionCursor.getString(collectionCursor.getColumnIndex(title)));
            collectionSongInfo.setSongSinger(collectionCursor.getString(collectionCursor.getColumnIndex(artist)));
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
        Cursor collectionCursor = getReadableDatabase().query(LOCAL_MUSIC_TABLENAME,new String[]{id},collection+"=?",new String[]{"1"},null,null,null,null);
        int collectionSongNumber = collectionCursor.getCount();
        collectionCursor.close();
        return collectionSongNumber;
    }

    /**
     * 获取本地音乐歌曲数量
     * @return 数据库的歌曲数量
     */
    public int getLocalMusicNumber(){
        Cursor songNumberCursor = getReadableDatabase().query(LOCAL_MUSIC_TABLENAME, new String[]{id}, null, null, null, null, null);
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
        StringBuilder stringBuilder = new StringBuilder(20);
//        update LOCAL_MUSIC_TABLENAME set collection = collectionOrNot where absolutionPath='songInfo.getSongAbsolution()';
        stringBuilder.append("update").append(" ").append(LOCAL_MUSIC_TABLENAME).append(" ")
                .append("set").append(" ").append(collection).append("=")
                .append(collectionOrNot).append(" ").append("where").append(" ")
                .append(absolutionPath).append("=").append("'").append(songInfo.getSongAbsolutePath())
                .append("'").append(";");
        sqLiteDatabase.execSQL(stringBuilder.toString());
    }

    /*
     * 获取"LOCAL_MUSIC_TABLES"表里指定列下面的所有的值（不重复的）
     * 例如获取所有歌手名字（不重复的），或者获取所有专辑名字（不重复的）
     * @param columnName 想要获取的那个列
     * @return 如果没有任何数据则返回空
     */
    private List<String> getDistinctValue(String columnName) {
        List<String> dataList;
        Cursor dataCursor = getReadableDatabase().query(true, LOCAL_MUSIC_TABLENAME, new String[]{columnName}, null, null, null, null, null, null);
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
            songNumberCursor = songNumberDatabase.query(LOCAL_MUSIC_TABLENAME, new String[]{collection}, columnName+"=?", new String[]{columnValues}, null, null, null);
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

    /**
     * 插入一手最近播放歌曲
     * @param songInfo 要插入的那首歌曲
     */
    public void insertInRecentlyPlay(SongInfo songInfo){
//        1.先看原来表里面是否有这首歌曲，如果有就直接更新最近一次播放时间
//        2，如果没有，查看表里面的歌曲数量是否超过容许最大数量
//        3，如果超过删除最老那首歌曲，否则什么都不用删
//        4，最后插入新的那首播放歌曲
        SQLiteDatabase jfPlayerDatabase = getWritableDatabase();
//        首先查询表里面是否有这首歌曲
        Cursor recentlyPlayCursor = jfPlayerDatabase.query(RECENTLY_PLAY_TABLE_NAME,new String[]{id},absolutionPath,new String[]{songInfo.getSongAbsolutePath()},null,null,null);
//        如果原来有这首歌只要更新最近一次播放时间即可
        if(recentlyPlayCursor.getCount()!=0){
            recentlyPlayCursor.close();
            StringBuilder updateLastPlayTime = new StringBuilder(20);
            updateLastPlayTime.append("update").append(" ").append(RECENTLY_PLAY_TABLE_NAME).append(" ")
                    .append("set").append(" ").append(LAST_PLAY_TIME).append("=")
                    .append(System.currentTimeMillis()).append(" ").append("where").append(" ")
                    .append(absolutionPath).append("=").append("'").append(songInfo.getSongAbsolutePath())
                    .append("'").append(";");
            jfPlayerDatabase.execSQL(updateLastPlayTime.toString());
            jfPlayerDatabase.close();
            return;
        }

        recentlyPlayCursor = jfPlayerDatabase.query(RECENTLY_PLAY_TABLE_NAME,new String[]{id},null,null,null,null,null);
//        如果表里面的歌曲已达所容许的最大数量
        if(recentlyPlayCursor.getCount()>RECENTLY_PLAY_MAX_NUM){
            StringBuilder deleteSongInfo = new StringBuilder(28);
//            删除最老那首歌曲
//            delete from recentlyPlay where lastPlayTime IN (select lastPlayTime from recentlyPlay ORDER BY lastPlayTime ASC limit 1);
            deleteSongInfo.append("delete from").append(" ").append(RECENTLY_PLAY_TABLE_NAME).append(" ")
                    .append("where").append(" ").append(LAST_PLAY_TIME).append(" ")
                    .append("IN").append(" ").append("(").append("select")
                    .append(" ").append(LAST_PLAY_TIME).append(" ").append("from")
                    .append(" ").append(RECENTLY_PLAY_TABLE_NAME).append(" ").append("order by")
                    .append(" ").append(LAST_PLAY_TIME).append(" ").append("asc")
                    .append("limit 1").append(")");
            jfPlayerDatabase.execSQL(deleteSongInfo.toString());
        }
//        插入最新那首歌曲
        StringBuilder insertSongInfo = new StringBuilder(20);
//        insert into recentlyPlay (absolutePath,lastPlayTime) values(songInfo.getSongAbsolutePath(),System.currentTimeMillis());
        insertSongInfo.append("insert into").append(" ").append(RECENTLY_PLAY_TABLE_NAME).append("(")
                .append(absolutionPath).append(",").append(LAST_PLAY_TIME).append(")")
                .append(" ").append("values").append("(").append("'")
                .append(songInfo.getSongAbsolutePath()).append("'").append(",")
                .append(System.currentTimeMillis()).append(");");
        jfPlayerDatabase.execSQL(insertSongInfo.toString());
        jfPlayerDatabase.close();
        return;
    }//insertInRecentlyPlay

}