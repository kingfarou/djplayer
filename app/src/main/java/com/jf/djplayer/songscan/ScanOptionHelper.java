package com.jf.djplayer.songscan;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jf.djplayer.base.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jf on 2016/8/4.
 * 这个数据库记录了，用户所选择的扫描音乐时的过滤条件，其中包括：
 * >需要扫描哪些路径。
 * >所扫描的歌曲文件必须大于多少(kb)。
 * >说扫描的歌曲时长必须大于多少(ms)。
 */
public class ScanOptionHelper extends SQLiteOpenHelper{

    private static final int version = 1;

    //数据库名字
    private static final String DATABASE_NAME = "SCAN_OPTION_DATABASE";

    //路径表名及字段名
    private static final String TABLE_NAME_PATH = "path";
    private static final String COLUMN_NAME_PATH = "path";

    //时长表明及字段名
    private static final String TABLE_NAME_DURATION = "duration";
    private static final String COLUMN_NAME_DURATION = "duration";
    //默认过滤小于一分钟的歌曲，该数值的单位为毫秒
    private static final int DEFAULT_DURATION = 60*1000;

    //文件大小表名及字段名
    private static final String TABLE_NAME_SIZE = "size";
    private static final String COLUMN_NAME_SIZE = "size";
    //默认过滤小于1M的歌曲，该数值的单位为kb
    private static final int DEFAULT_SIZE = 512;//默认过滤文件大小小于500k的歌

    //id
    private static final String id = "id";

    public ScanOptionHelper(){
        super(MyApplication.getContext(), DATABASE_NAME, null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //路径表的建表语句
        String createAbsolutePathTable = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME_PATH +"("+
                id+" INTEGER PRIMARY KEY NOT NULL,"+ COLUMN_NAME_PATH +" CHAR"+");";
        //时长表的建表语句
        String createDurationTable = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_DURATION+"("+
                id+" INTEGER PRIMARY KEY NOT NULL,"+COLUMN_NAME_DURATION+" INT"+");";
        //尺寸表的建表语句
        String createSizeTable = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_SIZE+"("+
                id+" INTEGER PRIMARY KEY NOT NULL,"+COLUMN_NAME_SIZE+" INT"+");";

        //建表
        sqLiteDatabase.execSQL(createAbsolutePathTable);
        sqLiteDatabase.execSQL(createDurationTable);
        sqLiteDatabase.execSQL(createSizeTable);

        //向表里面存入默认的值
        sqLiteDatabase.execSQL("INSERT INTO "+TABLE_NAME_DURATION+"("+COLUMN_NAME_DURATION+")"+" values("+DEFAULT_DURATION+");");
        sqLiteDatabase.execSQL("INSERT INTO "+TABLE_NAME_SIZE+"("+COLUMN_NAME_SIZE+")"+" values("+DEFAULT_SIZE+");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /*路径表的相关操作__start*/
    public void setPath(List<String> pathList){

    }

    public List<String> getPathList(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT "+COLUMN_NAME_PATH+" FROM "+TABLE_NAME_PATH, null);
        if(cursor == null || cursor.getCount() == 0){
            return null;
        }
        int size = cursor.getCount();
        List<String> path = new ArrayList<>(size);
        for(int i = 0; i<size; i++){
            path.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PATH)));
        }
        cursor.close();
        return path;
    }

    /*时长表的相关操作__start*/
    /**
     * 设置扫描选项里的时长
     * @param duration 时长，以毫秒为单位
     */
    public void setDuration(int duration){
        getWritableDatabase().execSQL("UPDATE "+TABLE_NAME_DURATION+" SET "+COLUMN_NAME_DURATION+"="+duration);
    }

    /**
     * 获取扫描选项里的时长
     * @return 时长，以毫秒作为单位
     */
    public int getDuration(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT "+COLUMN_NAME_DURATION+" FROM "+TABLE_NAME_DURATION, null);
        cursor.moveToFirst();
        int duration = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_DURATION));
        cursor.close();
        return duration;
    }
    /*时长表的相关操作__end*/

    /*尺寸表的相关操作__start*/
    /**
     * 设置扫描选项里的歌曲大小
     * @param size 大小，以kb作为单位
     */
    public void setSize(int size){
        getWritableDatabase().execSQL("UPDATE "+TABLE_NAME_SIZE+" SET "+COLUMN_NAME_SIZE+"="+size);
    }

    public int getSize(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT "+COLUMN_NAME_SIZE+" FROM "+TABLE_NAME_SIZE, null);
        cursor.moveToFirst();
        int size = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_SIZE));
        cursor.close();
        return size;
    }
    /*尺寸表的相关操作__end*/

}
