package com.jf.djplayer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;

import com.jf.djplayer.bean.Album;
import com.jf.djplayer.bean.Folder;
import com.jf.djplayer.bean.Singer;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kingfar on 2016/1/5.
 * 该类负责管理自己音乐文件的数据库，负责数据库表管理，并对外提供操作方法，有表如下：
 * >本地音乐歌曲表，包含歌名、歌手、专辑、时长、文件大小、绝对路径、文件夹路径。
 * >歌手表，包含歌手、歌手对应歌曲数量
 * >专辑表，包含专辑、专辑对应歌曲数量
 * >文件夹表，含有歌曲文件的文件夹名、文件夹对应歌曲数量
 * 注：各表均含有主键(integer primary key)
 */
public class SongInfoOpenHelper extends SQLiteOpenHelper {

    /** 类型名-歌手*/
    public static final int TYPE_NAME_SINGER = 1;
    /** 类型名-专辑*/
    public static final int TYPE_NAME_ALBUM = 2;
    /** 类型名-文件夹*/
    public static final int TYPE_NAME_FOLDER = 3;
    /** 当一首歌最后一次播放时间等于这个常量，表示这首歌从未播放过*/
    public static final int NEVER_PLAY = 0;

    private static final String SONG_INFO_DATABASE_NAME = "SONG_INFO_DATABASE";//这是数据库的名字

    // 本地音乐-歌曲表
    private static class LocalMusicSongTable{
        private static final String TABLE_NAME = "LOCAL_MUSIC_SONG_TABLE"; // 表名
        // 各个列名
        private static final String ID = "_id"; // 主键
        private static final String TITLE = MediaStore.Audio.Media.TITLE;         // 歌名
        private static final String ARTIST = MediaStore.Audio.Media.ARTIST;       // 歌手
        private static final String ALBUM = MediaStore.Audio.Media.ALBUM;         // 专辑
        private static final String DURATION = MediaStore.Audio.Media.DURATION;   // 时长
        private static final String SIZE = MediaStore.Audio.Media.SIZE;           // 大小
        private static final String ABSOLUTE_PATH = MediaStore.Audio.Media.DATA;  // 歌曲文件绝对路径
        private static final String FOLDER = "folder_path";                       // 歌曲文件所在的文件夹路径
        private static final String collection = "_collection";                   // 标记用户是否收藏音乐
        private static final String lastPlayTime = "last_play_time";              // 标记歌曲最后一次播放时间
        private static final String isDownload = "is_download";                   // 标记歌曲是否是从网络下载
    }

    // 本地音乐-歌手表
    private static class LocalMusicSingerTable{
        private static final String TABLE_NAME = "LOCAL_MUSIC_SINGER_TABLE";                      // 表名
        private static final String ID = "_id";                                                   // 主键
        private static final String ARTIST = MediaStore.Audio.Artists.ARTIST;                     // 歌手名字
        private static final String NUMBER_OF_SONGS_FOR_ARTIST = "number_of_songs_for_artist";    // 歌手歌曲数量
    }

    // 本地音乐-专辑表
    private static class LocalMusicAlbumTable{
        private static final String ID = "_id";                                              // 主键
        private static final String TABLE_NAME = "LOCAL_MUSIC_ALBUM_TABLE";                  // 表名
        private static final String ALBUM = MediaStore.Audio.Albums.ALBUM;                   // 专辑名字
        private static final String NUMBER_OF_SONGS_FOR_ALBUM = "number_of_songs_for_album"; // 专辑歌曲数量
    }

    // 本地音乐-文件夹表
    private static class LocalMusicFolderTable{
        private static final String ID = "_id";                                                 // 主键
        private static final String TABLE_NAME = "LOCAL_MUSIC_FOLDER_TABLE";                    // 表名
        private static final String FOLDER = "folder";                                          // 专辑名字
        private static final String NUMBER_OF_SONGS_FOR_FOLDER = "number_of_songs_for_folder";  // 专辑歌曲数量
    }

    /**
     * 创建本地音乐的数据库操作工具
     * @param context 环境
     */
    public SongInfoOpenHelper(Context context){
        super(context, SONG_INFO_DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建各个表
        createLocalMusicSongTable(db);
        createLocalMusicArtistTable(db);
        createLocalMusicAlbumTable(db);
        createLocalMusicFolderTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // 创建本地音乐信息表
    private void createLocalMusicSongTable(SQLiteDatabase db){
        StringBuilder createTableBuilder = new StringBuilder(26);
        createTableBuilder.append("CREATE TABLE IF NOT EXISTS ").append(LocalMusicSongTable.TABLE_NAME).append("(")
                .append(LocalMusicSongTable.ID).append(" INTEGER PRIMARY KEY,").append(LocalMusicSongTable.TITLE).append(" TEXT,")
                .append(LocalMusicSongTable.ARTIST).append(" TEXT,").append(LocalMusicSongTable.ALBUM).append(" TEXT,")
                .append(LocalMusicSongTable.DURATION).append(" INTEGER,").append(LocalMusicSongTable.SIZE).append(" INTEGER,")
                .append(LocalMusicSongTable.ABSOLUTE_PATH).append(" TEXT UNIQUE,").append(LocalMusicSongTable.FOLDER).append(" INTEGER,")
                .append(LocalMusicSongTable.collection).append(" INTEGER,").append(LocalMusicSongTable.lastPlayTime).append(" INTEGER,")
                .append(LocalMusicSongTable.isDownload).append(" INTEGER")
                .append(");");
        db.execSQL(createTableBuilder.toString());
    }

    // 创建本地音乐歌手表
    private void createLocalMusicArtistTable(SQLiteDatabase db){
        StringBuilder createTableBuilder = new StringBuilder(10);
        createTableBuilder.append("CREATE TABLE IF NOT EXISTS ").append(LocalMusicSingerTable.TABLE_NAME).append("(")
                .append(LocalMusicSingerTable.ID).append(" INTEGER PRIMARY KEY,").append(LocalMusicSingerTable.ARTIST).append(" TEXT UNIQUE,")
                .append(LocalMusicSingerTable.NUMBER_OF_SONGS_FOR_ARTIST).append(" INTEGER")
                .append(");");
        db.execSQL(createTableBuilder.toString());
    }

    // 创建本地音乐专辑表
    private void createLocalMusicAlbumTable(SQLiteDatabase db){
        StringBuilder createTableBuilder = new StringBuilder(10);
        createTableBuilder.append("CREATE TABLE IF NOT EXISTS ").append(LocalMusicAlbumTable.TABLE_NAME).append("(")
                .append(LocalMusicAlbumTable.ID).append(" INTEGER PRIMARY KEY,").append(LocalMusicAlbumTable.ALBUM).append(" TEXT UNIQUE,")
                .append(LocalMusicAlbumTable.NUMBER_OF_SONGS_FOR_ALBUM).append(" INTEGER")
                .append(");");
        db.execSQL(createTableBuilder.toString());
    }

    // 创建本地音乐文件夹表
    private void createLocalMusicFolderTable(SQLiteDatabase db){
        StringBuilder createTableBuilder = new StringBuilder(10);
        createTableBuilder.append("CREATE TABLE IF NOT EXISTS ").append(LocalMusicFolderTable.TABLE_NAME).append("(")
                .append(LocalMusicFolderTable.ID).append(" INTEGER PRIMARY KEY,").append(LocalMusicFolderTable.FOLDER).append(" TEXT UNIQUE,")
                .append(LocalMusicFolderTable.NUMBER_OF_SONGS_FOR_FOLDER).append(" INTEGER")
                .append(");");
        db.execSQL(createTableBuilder.toString());
    }

    /****************本地音乐歌曲表：增、删、改、查****************/
    /**
     * 插入一首本地歌曲
     * @param song 需插入的歌曲对象
     * @return 返回新插入的行数ID，如果插入发生错误，返回-1
     */
    public long insertLocalMusic(Song song){
        String notKnow = "未知";
        if(song == null) {
            throw new IllegalArgumentException(getClass().getSimpleName()+"--insertLocalMusic()收到的参数异常");
        }
        SQLiteDatabase songInfoDatabase = getWritableDatabase();
        ContentValues songValues = new ContentValues();
        // 判断字符串是不是空对象，如果是就给一个默认值，让自己的数据库不要有空的对象
        if(song.getSongName() == null || song.getSongName().equals("")) song.setSongName(notKnow);
        if(song.getSingerName() == null || song.getSongName().equals("")) song.setSingerName(notKnow);
        if(song.getAlbum() == null || song.getAlbum().equals("")) song.setAlbum(notKnow);
        // 插入
        songValues.putNull(LocalMusicSongTable.ID); // id由系统去进行自增
        songValues.put(LocalMusicSongTable.TITLE, song.getSongName());
        songValues.put(LocalMusicSongTable.ARTIST,song.getSingerName());
        songValues.put(LocalMusicSongTable.ALBUM,song.getAlbum());
        songValues.put(LocalMusicSongTable.DURATION,song.getDuration());
        songValues.put(LocalMusicSongTable.SIZE,song.getSize());
        songValues.put(LocalMusicSongTable.FOLDER, (song.getFileAbsolutePath() == null || song.getFileAbsolutePath().equals("")) ?
                notKnow : new File(song.getFileAbsolutePath()).getParent());
        songValues.put(LocalMusicSongTable.ABSOLUTE_PATH, (song.getFileAbsolutePath() == null || song.getFileAbsolutePath().equals("")) ?
                notKnow : song.getFileAbsolutePath());
        songValues.put(LocalMusicSongTable.collection, song.getCollection());
        songValues.put(LocalMusicSongTable.lastPlayTime, NEVER_PLAY);
        long insertId = songInfoDatabase.insert(LocalMusicSongTable.TABLE_NAME, null, songValues);
        // 数据库插入失败
        if(insertId == -1){
            songInfoDatabase.close();
            return -1;
        } else {
            // 歌曲表插入成功，则该歌曲对应歌手、专辑、文件夹表歌曲数量加一
            numberOfSongAddOne(LocalMusicSingerTable.TABLE_NAME, song.getSingerName());
            numberOfSongAddOne(LocalMusicAlbumTable.TABLE_NAME, song.getAlbum());
            numberOfSongAddOne(LocalMusicFolderTable.TABLE_NAME, new File(song.getFileAbsolutePath()).getParent());
            songInfoDatabase.close();
            return insertId;
        }
    }

    /**
     * 删除Song对象里的绝对路径所标示的歌曲文件
     * @param song 歌曲对象
     */
    public void deleteFromLocalMusic(Song song) {
        StringBuilder deleteBuilder = new StringBuilder(8);
        deleteBuilder.append("delete from ").append(LocalMusicSongTable.TABLE_NAME)
                .append(" where ").append(LocalMusicSongTable.ABSOLUTE_PATH).append("='").append(song.getFileAbsolutePath()).append("';");
        getWritableDatabase().execSQL(deleteBuilder.toString());
        // 从歌曲表删除一首歌曲，则该歌曲对应歌手、专辑、文件夹表歌曲数量减一
        numberOfSongMinusOne(LocalMusicSingerTable.TABLE_NAME, song.getSingerName());
        numberOfSongMinusOne(LocalMusicAlbumTable.TABLE_NAME, song.getAlbum());
        numberOfSongMinusOne(LocalMusicFolderTable.TABLE_NAME, new File(song.getFileAbsolutePath()).getParent());
    }


    /**
     * 更新由song对象里面的绝对路径所指定的那条歌曲记录
     * @param song 待更新的歌曲对象
     */
    public void updateLocalMusic(Song song) {
        if(song == null) return;
        // 更新歌曲信息，需要考虑如果歌曲的歌手、专辑信息被用户改动过，需要同步歌手表、专辑表信息
        // 获取原有歌手名字、专辑名字
        StringBuilder selectSingerBuilder = new StringBuilder(14);
        selectSingerBuilder.append("SELECT ").append(LocalMusicSongTable.ARTIST).append(",").append(LocalMusicSongTable.ALBUM)
                .append(" from ").append(LocalMusicSongTable.TABLE_NAME)
                .append(" where ").append(LocalMusicSongTable.ABSOLUTE_PATH).append("=").append("'").append(song.getFileAbsolutePath()).append("';");
        Cursor singerAndAlbumCursor = getReadableDatabase().rawQuery(selectSingerBuilder.toString(), null);
        singerAndAlbumCursor.moveToNext();
        String oldSinger = singerAndAlbumCursor.getString(singerAndAlbumCursor.getColumnIndex(LocalMusicSongTable.ARTIST));
        // 如果用户修改过歌手名字，同步变更歌手表信息
        if( !oldSinger.equals(song.getSingerName()) ){
            numberOfSongMinusOne(LocalMusicSingerTable.TABLE_NAME, oldSinger);
            numberOfSongAddOne(LocalMusicSingerTable.TABLE_NAME, song.getSingerName());
        }
        // 对专辑表进行和歌手表相同的操作
        String oldAlbum = singerAndAlbumCursor.getString(singerAndAlbumCursor.getColumnIndex(LocalMusicSongTable.ALBUM));
        if( !oldAlbum.equals(song.getAlbum()) ){
            numberOfSongMinusOne(LocalMusicAlbumTable.TABLE_NAME, oldAlbum);
            numberOfSongAddOne(LocalMusicAlbumTable.TABLE_NAME, song.getAlbum());
        }
        // 更新本地音乐歌曲表信息
        SQLiteDatabase songInfoDatabase = getWritableDatabase();
        StringBuilder updateBuilder = new StringBuilder(22);
        updateBuilder.append("update ").append(LocalMusicSongTable.TABLE_NAME).append(" set ")
                .append(LocalMusicSongTable.TITLE).append("='").append(song.getSongName()).append("',")
                .append(LocalMusicSongTable.ARTIST).append("='").append(song.getSingerName()).append("',")
                .append(LocalMusicSongTable.ALBUM).append("='").append(song.getAlbum()).append("'")
                .append(" where ").append(LocalMusicSongTable.ABSOLUTE_PATH).append("='").append(song.getFileAbsolutePath()).append("';");
        songInfoDatabase.execSQL(updateBuilder.toString());
        songInfoDatabase.close();
    }

    /**
     * 获取本地音乐所有歌曲信息
     * @return 一个装着本地数据库所有歌曲的集合，如果数据库没有歌曲，则返回null
     */
    public List<Song> getLocalMusicSongList(){
        // 读取所有歌曲数据
        Cursor songCursor = getReadableDatabase().query(LocalMusicSongTable.TABLE_NAME,null,null,null,null,null,null);
        //如果没有歌曲数据直接返回
        if (songCursor.getCount()==0){
            songCursor.close();
            return null;
        }
        List<Song> songList = cursorToSongList(songCursor);
        songCursor.close();
        return songList;
    }

    /**
     * 获取本地音乐歌曲数量
     * @return 数据库的歌曲数量
     */
    public int getLocalMusicNumber(){
        Cursor songNumberCursor = getReadableDatabase().query(LocalMusicSongTable.TABLE_NAME, new String[]{LocalMusicSongTable.ID}, null, null, null, null, null);
        int songNumber = songNumberCursor.getCount();
        songNumberCursor.close();
        return songNumber;
    }
    /****************本地音乐歌曲表：增、删、改、查****************/

    /****************用户所收藏的歌曲：改、查****************/
    /**
     * 改变歌曲被收藏的状态
     * @param song 要收藏的（或者要取消收藏的）歌曲对象
     */
    public void updateCollection(Song song,int collectionOrNot){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String collectionString = "update"+" "+ LocalMusicSongTable.TABLE_NAME +" "+"set"+" "+
                LocalMusicSongTable.collection+"="+collectionOrNot+" "+"where"+" "+ LocalMusicSongTable.ABSOLUTE_PATH +"="+"'"+song.getFileAbsolutePath()+"'"+";";
        sqLiteDatabase.execSQL(collectionString);
    }

    /**
     * 获取用户所收藏的所有歌曲
     * @return 一个装填这用户收藏的所有歌曲的集合
     */
    public List<Song> getCollectionSongInfo(){
        Cursor collectionCursor = getReadableDatabase().query(LocalMusicSongTable.TABLE_NAME, null, LocalMusicSongTable.collection + "=?", new String[]{"1"}, null, null, null, null);
        // 如果没有收藏歌曲直接返回元素个数零的集合
        if(collectionCursor.getCount()==0) {
            collectionCursor.close();
            return null;
        }
        List<Song> songInfoList;
        songInfoList = cursorToSongList(collectionCursor);
        collectionCursor.close();
        return songInfoList;
    }
    /****************用户所收藏的歌曲：改查****************/

    /****************用户最近播放歌曲：改、查****************/
    /**
     * 更新由"song"所指定的绝对路径最近一次播放时间
     * @param song 要更新的"_song"对象
     * @param lastPlayTime 最近一次播放时间
     */
    public void setLastPlayTime(Song song, long lastPlayTime){
        String updateString = "update"+" "+ LocalMusicSongTable.TABLE_NAME +" "+"set"+" "+
                SongInfoOpenHelper.LocalMusicSongTable.lastPlayTime+"="+lastPlayTime+" "+
                "where"+" "+ LocalMusicSongTable.ABSOLUTE_PATH +"="+"'"+song.getFileAbsolutePath()+"'"+";";
        getWritableDatabase().execSQL(updateString);
    }

    /**
     * 获取本地音乐列表里面指定数量歌曲，并且按照最后一次播放时间，从最近得到最远的开始排列
     * @param songNumber 要获取的歌曲数量
     * @return 按照最后一次播放时间，从近到远，有序排列的歌曲集合，集合的第一个元素是最近播放的一首，如果数据库没歌曲，则返回空
     */
    public List<Song> getRecentlyPlaySong(int songNumber){
        String selectString = "SELECT * FROM"+" "+ LocalMusicSongTable.TABLE_NAME +" "+"ORDER BY"+" "+LocalMusicSongTable.lastPlayTime+" "+"DESC"+" "+"LIMIT"+" "+songNumber;
        Cursor recentlyPlayerCursor = getReadableDatabase().rawQuery(selectString,null);
        if(recentlyPlayerCursor.getCount()==0){
            recentlyPlayerCursor.close();
            return null;
        }
        List<Song> recentlyPlayList = cursorToSongList(recentlyPlayerCursor);
        recentlyPlayerCursor.close();
        return recentlyPlayList;
    }

    /**
     * 获取某类型下某个值所对应的歌曲信息
     * @param typeName 类型名
     * @param typeValue 该类某个值
     * @return 歌曲列表，如果查询结果为空，返回null
     * 示例：如想获取“歌手”类值为“张三”歌手的所有歌曲，则typeName传入歌手，typeValue传入“张三”
     */
    public List<Song> getClassifySongInfo(int typeName, String typeValue){
        String columnName = null;
        if(typeName == TYPE_NAME_SINGER){
            columnName = LocalMusicSongTable.ARTIST;
        } else if(typeName == TYPE_NAME_ALBUM){
            columnName = LocalMusicSongTable.ALBUM;
        } else if(typeName == TYPE_NAME_FOLDER){
            columnName = LocalMusicSongTable.FOLDER;
        }
        StringBuilder selectBuilder = new StringBuilder(8);
        selectBuilder.append("SELECT * FROM ").append(LocalMusicSongTable.TABLE_NAME)
                .append(" where ").append(columnName).append("=").append("'").append(typeValue).append("'");
        Cursor cursor = getReadableDatabase().rawQuery(selectBuilder.toString(),null);
        if(cursor.getCount() == 0){
            return null;
        }
        return cursorToSongList(cursor);
    }

    // 将结果集里的音乐信息，变成"List<_SongInfo>"对象
    private List<Song> cursorToSongList(Cursor cursor){
        List<Song> songInfoList = new ArrayList<>(cursor.getCount());
        Song songInfo;
        // 装填所有歌曲数据
        while (cursor.moveToNext()){
            songInfo = new Song();
            songInfo.setSongName(cursor.getString(cursor.getColumnIndex(LocalMusicSongTable.TITLE)));
            songInfo.setSingerName(cursor.getString(cursor.getColumnIndex(LocalMusicSongTable.ARTIST)));
            songInfo.setAlbum(cursor.getString(cursor.getColumnIndex(LocalMusicSongTable.ALBUM)));
            songInfo.setDuration(cursor.getInt(cursor.getColumnIndex(LocalMusicSongTable.DURATION)));
            songInfo.setSize(cursor.getInt(cursor.getColumnIndex(LocalMusicSongTable.SIZE)));
            songInfo.setFileAbsolutePath(cursor.getString(cursor.getColumnIndex(LocalMusicSongTable.ABSOLUTE_PATH)));
            songInfo.setCollection(cursor.getInt(cursor.getColumnIndex(LocalMusicSongTable.collection)));
            songInfo.setLastPlayTime(cursor.getInt(cursor.getColumnIndex(LocalMusicSongTable.lastPlayTime)));
            songInfoList.add(songInfo);
        }
        return songInfoList;
    }

    /**
     * 获取全部歌手列表
     * @return 歌手列表
     */
    public List<Singer> getSingerList(){
        StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append("SELECT ").append(LocalMusicSingerTable.ARTIST).append(",").append(LocalMusicSingerTable.NUMBER_OF_SONGS_FOR_ARTIST)
                .append(" from ").append(LocalMusicSingerTable.TABLE_NAME).append(";");
        Cursor singsCursor = getReadableDatabase().rawQuery(selectBuilder.toString(), null);
        List<Singer> singerList = new ArrayList<>(singsCursor.getCount());
        Singer singer = null;
        int singerNameIndex = singsCursor.getColumnIndex(LocalMusicSingerTable.ARTIST);
        int singerSongNumberIndex = singsCursor.getColumnIndex(LocalMusicSingerTable.NUMBER_OF_SONGS_FOR_ARTIST);
        while (singsCursor.moveToNext()){
            singer = new Singer();
            singer.setImgUrl("");
            singer.setName(singsCursor.getString(singerNameIndex));
            singer.setSongNumber(singsCursor.getInt(singerSongNumberIndex));
            singerList.add(singer);
        }
        singsCursor.close();
        return singerList;
    }

    /**
     * 获取专辑列表
     * @return 专辑列表
     */
    public List<Album> getAlbumList(){
        StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append("SELECT ").append(LocalMusicAlbumTable.ALBUM).append(",").append(LocalMusicAlbumTable.NUMBER_OF_SONGS_FOR_ALBUM)
                .append(" from ").append(LocalMusicAlbumTable.TABLE_NAME).append(";");
        Cursor alubmsCursor = getReadableDatabase().rawQuery(selectBuilder.toString(), null);
        List<Album> albumList = new ArrayList<>(alubmsCursor.getCount());
        Album album = null;
        int albumNameIndex = alubmsCursor.getColumnIndex(LocalMusicAlbumTable.ALBUM);
        int albumSongNumberIndex = alubmsCursor.getColumnIndex(LocalMusicAlbumTable.NUMBER_OF_SONGS_FOR_ALBUM);
        while (alubmsCursor.moveToNext()){
            album = new Album();
            album.setImgUrl("");
            album.setName(alubmsCursor.getString(albumNameIndex));
            album.setSongNumber(alubmsCursor.getInt(albumSongNumberIndex));
            albumList.add(album);
        }
        alubmsCursor.close();
        return albumList;
    }

    /**
     * 获取全部路径
     * @return 路径列表
     */
    public List<Folder> getFolderList(){
        StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append("SELECT ").append(LocalMusicFolderTable.FOLDER).append(",").append(LocalMusicFolderTable.NUMBER_OF_SONGS_FOR_FOLDER)
                .append(" from ").append(LocalMusicFolderTable.TABLE_NAME).append(";");
        Cursor foldersCursor = getReadableDatabase().rawQuery(selectBuilder.toString(), null);
        List<Folder> albumList = new ArrayList<>(foldersCursor.getCount());
        Folder folder = null;
        int albumNameIndex = foldersCursor.getColumnIndex(LocalMusicFolderTable.FOLDER);
        int albumSongNumberIndex = foldersCursor.getColumnIndex(LocalMusicFolderTable.NUMBER_OF_SONGS_FOR_FOLDER);
        while (foldersCursor.moveToNext()){
            folder = new Folder();
            folder.setName(foldersCursor.getString(albumNameIndex));
            folder.setSongNumber(foldersCursor.getInt(albumSongNumberIndex));
            albumList.add(folder);
        }
        foldersCursor.close();
        return albumList;
    }

    /**
     * 歌手表或专辑表或文件夹表某条记录歌曲数量加一
     * @param witchTable 哪一张表，如歌手表、专辑表、或文件夹表
     * @param columnValue 名字
     * 示例：如想将歌手表里歌手“张三”歌曲数量加一，则witchTable传入歌手表，columnValue传入“张三”
     */
    private void numberOfSongAddOne(String witchTable, String columnValue){
        if(columnValue == null || columnValue.equals("")){
            columnValue = "未知";
        }
        String tableName = null;              // 待查询表名
        String columnName = null;             // 待查询列名
        String numberOfSongColumnName = null; // “歌曲数量”的列名
        if(witchTable.equals(LocalMusicSingerTable.TABLE_NAME)){
            tableName = LocalMusicSingerTable.TABLE_NAME;
            columnName = LocalMusicSingerTable.ARTIST;
            numberOfSongColumnName = LocalMusicSingerTable.NUMBER_OF_SONGS_FOR_ARTIST;
        } else if(witchTable.equals(LocalMusicAlbumTable.TABLE_NAME)){
            tableName = LocalMusicAlbumTable.TABLE_NAME;
            columnName = LocalMusicAlbumTable.ALBUM;
            numberOfSongColumnName = LocalMusicAlbumTable.NUMBER_OF_SONGS_FOR_ALBUM;
        } else if(witchTable.equals(LocalMusicFolderTable.TABLE_NAME)){
            tableName = LocalMusicFolderTable.TABLE_NAME;
            columnName = LocalMusicFolderTable.FOLDER;
            numberOfSongColumnName = LocalMusicFolderTable.NUMBER_OF_SONGS_FOR_FOLDER;
        }else{
            throw new IllegalArgumentException("--numberOfSongAddOne()“witchTable”参数异常");
        }
        // 查找该歌手（专辑、文件夹）及其对应歌曲数量
        StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append("SELECT * FROM ").append(tableName)
                .append(" where ").append(columnName).append("='").append(columnValue).append("'");
        Cursor cursor = getReadableDatabase().rawQuery(selectBuilder.toString(),null);
        if(cursor.getCount() == 0){
            // 对应表原来没有该（歌手、专辑、或文件夹），新增一行并设置歌曲数量为一
            StringBuilder insertBuilder = new StringBuilder(14);
            insertBuilder.append("INSERT INTO ").append(tableName).append("(")
                    .append(columnName).append(",").append(numberOfSongColumnName).append(")")
                    .append("VALUES(").append("'").append(columnValue).append("',").append(1).append(");");
            getWritableDatabase().execSQL(insertBuilder.toString());
        }else{
            // 对应表有该（歌手、专辑、或文件夹），将歌曲数量加一
            cursor.moveToNext();
            int songNumber = cursor.getInt(cursor.getColumnIndex(numberOfSongColumnName));
            songNumber+=1;
            StringBuilder updateBuilder = new StringBuilder();
            updateBuilder.append("update ").append(tableName).append(" set ")
                    .append(numberOfSongColumnName).append("=").append(songNumber)
                    .append(" where ").append(columnName).append("='").append(columnValue).append("';");
            getWritableDatabase().execSQL(updateBuilder.toString());
        }
        cursor.close();
    }

    /**
     * 歌手表或专辑表或文件夹表某条记录歌曲数量减一
     * @param witchTable 哪一张表，如歌手表、专辑表、或文件夹表
     * @param columnValue 名字
     * 示例：如想将歌手表里歌手“张三”歌曲数量加一，则witchTable传入歌手表，columnValue传入“张三”
     */
    private void numberOfSongMinusOne(String witchTable, String columnValue){
        if(columnValue == null || columnValue.equals("")){
            columnValue = "未知";
        }
        String tableName = null;              // 待查询表名（如歌手表、专辑表、文件夹表）
        String columnName = null;             // 待查询列名（如歌手“张三”、专辑“笑傲江湖”）
        String numberOfSongColumnName = null; // “歌曲数量”的列名（歌曲表里歌手数量列，专辑表里歌手数量列）
        if(witchTable.equals(LocalMusicSingerTable.TABLE_NAME)){
            tableName = LocalMusicSingerTable.TABLE_NAME;
            columnName = LocalMusicSingerTable.ARTIST;
            numberOfSongColumnName = LocalMusicSingerTable.NUMBER_OF_SONGS_FOR_ARTIST;
        } else if(witchTable.equals(LocalMusicAlbumTable.TABLE_NAME)){
            tableName = LocalMusicAlbumTable.TABLE_NAME;
            columnName = LocalMusicAlbumTable.ALBUM;
            numberOfSongColumnName = LocalMusicAlbumTable.NUMBER_OF_SONGS_FOR_ALBUM;
        } else if(witchTable.equals(LocalMusicFolderTable.TABLE_NAME)){
            tableName = LocalMusicFolderTable.TABLE_NAME;
            columnName = LocalMusicFolderTable.FOLDER;
            numberOfSongColumnName = LocalMusicFolderTable.NUMBER_OF_SONGS_FOR_FOLDER;
        }else{
            throw new IllegalArgumentException("--numberOfSongAddOne()“witchTable”参数异常");
        }
        // 查找该歌手（专辑、文件夹）及其对应歌曲数量
        StringBuilder selectBuilder = new StringBuilder(8);
        selectBuilder.append("SELECT * FROM ").append(tableName)
                .append(" where ").append(columnName).append("='").append(columnValue).append("'");
        Cursor cursor = getReadableDatabase().rawQuery(selectBuilder.toString(),null);
        int count = cursor.getCount();
        if(count == 0){
            /* 对应表原来没有该（歌手、专辑、或文件夹），理论上这是不可能出现的情况，
             * 只有在歌曲表新增歌曲的时候漏了增加（歌手、专辑、文件夹表）歌曲数量时，才会出现该情况
             */
            LogUtil.i(getClass().getName(), "--numberOfSongMinusOne()--执行删除操作时，查找不到对应数据");
        } else {
            // 对应表有该（歌手、专辑、或文件夹）
            cursor.moveToNext();
            int songNumber = cursor.getInt(cursor.getColumnIndex(numberOfSongColumnName));
            if(songNumber == 1){
                // 原来只剩一首歌曲，直接删除该记录即可
                StringBuilder deleteBuilder = new StringBuilder(8);
                deleteBuilder.append("delete from ").append(tableName)
                        .append(" where ").append(columnName).append("='").append(columnValue).append("';");
                getWritableDatabase().execSQL(deleteBuilder.toString());
                LogUtil.i(getClass().getName(), "原来只剩一首歌曲，直接删除该记录即可");
            }else{
                // 原来剩余超过一首歌曲，将歌曲数量减一
                songNumber-=1;
                StringBuilder updateBuilder = new StringBuilder(8);
                updateBuilder.append("update ").append(tableName).append(" set ")
                        .append(numberOfSongColumnName).append("=").append(songNumber)
                        .append(" where ").append(columnName).append("='").append(columnValue).append("';");
                getWritableDatabase().execSQL(updateBuilder.toString());
                LogUtil.i(getClass().getName(), "对应表有该（歌手、专辑、或文件夹），将歌曲数量减一");
            }
        }
        cursor.close();
    }
}
