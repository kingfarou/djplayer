package com.jf.djplayer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JF on 2016/2/23.
 * 管理用户信息用的数据库操作类
 * 当前数据库维护三张表
 * 保存多个用户账号以及密码的表
 * 保存所有用户信息的表
 * 保存当前已登录的用户账号密码的表
 */
public class UserInfoOpenHelper extends SQLiteOpenHelper{

    private static final String USER_INFO_DATABASE_NAME = "userInfoDatabase";//这是用户信息数据库的名字
    private static final String USERNAME_PASSWORD_TABLE_NAME = "userNamePassword";//存储用户名和密码对应信息的表
    private static final String USER_INFO_TABLE_NAME = "userInfo";//这是存储用户信息的表
    private static final String CURRENT_LOGIN_USER_TABLE_NAME = "currentLoginUser";//这是当前登录用户的表

    private static final String ID = "_id";//id
//    用户名和密码信息表所用的列名，也是当前登录账户表的列名
    private static final String USER_NAME = "userName";//账号
    private static final String PASSWORD = "password";//密码
//    用户信息表所用的列名
    private static final String CUSTOM_NAME = "customName";//这个表示用户自定义的昵称

    public UserInfoOpenHelper(Context context,int version){
        super(context, USER_INFO_DATABASE_NAME, null, version);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
//        创建账号以及密码建表语句
//        create table if not exists USERNAME_PASSWORD_TABLE_NAME (id integer primary key,userName text unique,password text);
        String userNamePasswordTable = "create table if not exists"+" "+USERNAME_PASSWORD_TABLE_NAME+"("
                                        +ID+" "+"INTEGER PRIMARY KEY"+","
                                        +USER_NAME+" "+"TEXT UNIQUE"+","
                                        +PASSWORD+" "+"TEXT"+");";
//        用户信息表的建表语句
//        create table if not exists USER_INFO_TABLE_NAME (id integer primary key,userName text unique,customName text);
        String userInfoTable = "create table if not exists"+" "+USER_INFO_TABLE_NAME+"("
                                +ID+" "+"INTEGER PRIMARY KEY"+","
                                +USER_NAME+" "+"TEXT UNIQUE"+","
                                +CUSTOM_NAME+" "+"TEXT"+");";
//        create table if not exists CURRENT_LOGIN_USER (id integer primary key,userName text, passWord text);
        String currentLoginUser = "create table if not exists"+" "+CURRENT_LOGIN_USER_TABLE_NAME+"("
                                +ID+" "+"INTEGER PRIMARY KEY"+","
                                +USER_NAME+" "+"TEXT"+","
                                +PASSWORD+" "+"TEXT"+");";
        db.execSQL(userNamePasswordTable);//创建用户名密码对应表
        db.execSQL(userInfoTable);
        db.execSQL(currentLoginUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * 查询当前用户名是否已存在
     * @param userName 用户名字
     * @return true:如果当前用户存在，false:不存在
     */
    public boolean hasUserNames(String userName) {
        boolean hasUserName = false;
        Cursor userNameCursor = getReadableDatabase().query(USERNAME_PASSWORD_TABLE_NAME, new String[]{ID},
                USER_NAME + "=?", new String[]{userName}, null, null, null);
//        如果存在此用户名则返回真
        if(userNameCursor.getCount()!=0) hasUserName = true;
        userNameCursor.close();
        return hasUserName;
    }

    /**
     * 添加新的用户进来
     * @param userName 用户账号
     * @param password 用户密码
     * @return
     */
    public boolean addNewUser(String userName,String password){
        boolean signUpSuccessful = false;//标志注册是否成功
        SQLiteDatabase userInfoDatabase = getWritableDatabase();
//        装填注册用户信息
        ContentValues userInfoValues = new ContentValues();
        userInfoValues.putNull(ID);
        userInfoValues.put(USER_NAME, userName);
        userInfoValues.put(PASSWORD, password);
//        如果添加成功修改标识
        if( userInfoDatabase.insert(USERNAME_PASSWORD_TABLE_NAME, null, userInfoValues)!=-1) {
            signUpSuccessful = true;
        }
        return signUpSuccessful;
    }

    public boolean userNameMachPassword(String userName, String password){
//        根据用户名来查询用户名和密码
        Cursor machUserInfoCursor = getReadableDatabase().query(USERNAME_PASSWORD_TABLE_NAME,new String[]{USER_NAME,PASSWORD},
                                                USER_NAME+"=?",new String[]{userName},null,null,null);
//        如果当前用户并不存在
        if(machUserInfoCursor.getCount()==0) {
            machUserInfoCursor.close();
            return false;
        }
//        匹配用户名和密码
        machUserInfoCursor.moveToFirst();
//        如果用户名和密码匹配上了
        if( machUserInfoCursor.getString(machUserInfoCursor.getColumnIndex(PASSWORD)).equals(password)){
            machUserInfoCursor.close();
            return true;
        }
        machUserInfoCursor.close();
        return false;//如果他们没匹配上返回这个
    }
}
