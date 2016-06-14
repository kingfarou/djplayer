package com.jf.djplayer.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.jf.djplayer.base.MyApplication;

/**
 * Created by JF on 2016/2/26.
 * 存储用户全局选项"SharedPreferences"
 */
public class UserOptionPreferences {

    private Context context;
    //这个"SharedPreferences"文件名字
    private static final String SHARED_PREFERENCES_FILE_NAME = "userOption";
    //"SharedPreferences"文件对象
    private final SharedPreferences sharedPreferences;

    //播放模式相关变量
    public static final String KEY_PLAY_MODE = "playMode";//"SharedPreferences"文件里面播放模式的键
    public static final int VALUES_PLAY_MODE_ORDER = 1;//顺序播放
    public static final int VALUES_PLAY_MODE_RANDOM = 1<<1;//随机播放
    public static final int VALUES_PLAY_MODE_LIST_CIRCULATE = 1<<2;//列表循环
    public static final int VALUES_PLAY_MODE_SINGLE_CIRCULATE = 1<<3;//单曲循环


//    public UserOptionPreferences(Context context){
//        this.context = context;
//        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
//    }

    /**
     * 创建一个"UserOptionPreferences"对象，该对象负责全局用户选项的保存
     */
    public UserOptionPreferences(){
        this.context = MyApplication.getContext();
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 向全局用户选项"SharedPreferences"文件里面存入一个整型数值
     * @param key 键
     * @param values 整型的值
     */
    public void putIntValues(String key, int values){
        sharedPreferences.edit().putInt(key, values).commit();
    }

    /**
     * 取出全局用户选项"SharedPreferences"某一个键对应的整型值
     * @param key 键
     * @param defaultValue 若文件里没有此“键”，返回这个为默认值
     * @return "key"对应的值，如果不存在这个"键"，返回"defaultValue"
     */
    public int getIntValues(String key, int defaultValue){
        return sharedPreferences.getInt(key, defaultValue);
    }

//    /**
//     * 设置用户播放模式
//     * @param playMode 要设置的播放模式
//     */
//    public void setPlayModes(int playMode){
//        SharedPreferences playModePreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME,Context.MODE_PRIVATE);
//        playModePreferences.edit().putInt(KEY_PLAY_MODE,playMode).commit();
//    }

//    /**
//     * 返回所设置的播放模式
//     * @return 整形所标示的播放模式，如果未曾设置用户播放模式，默认返回顺序播放
//     */
//    public int getPlayModes(){
//        SharedPreferences playModePreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME,Context.MODE_PRIVATE);
//        return playModePreferences.getInt(KEY_PLAY_MODE, VALUES_PLAY_MODE_ORDER);//默认返回顺序播放
//    }
}
