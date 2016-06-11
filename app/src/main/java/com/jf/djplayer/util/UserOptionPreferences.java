package com.jf.djplayer.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JF on 2016/2/26.
 * 存储用户选项用的"SharedPreferences"
 */
public class UserOptionPreferences {

    private Context context;
    private static final String SHARED_PREFERENCES_FILE_NAME = "userOption";//这是保存用户配置用的文件名字

//    播放模式相关变量设置
     private static final String PLAY_MODE_KEY = "playMode";//"SharedPreferences"文件里面播放模式的键
     public static final int PLAY_MODE_ORDER = 1<<0;//这个表示顺序播放
     public static final int PLAY_MODE_RANDOM = 1<<1;//这个表示随机播放
     public static final int PLAY_MODE_CIRCULATE = 1<<2;//这个表示列表循环
     public static final int PLAY_MODE_SINGLE_CIRCLUATE = 1<<3;//这个表示单曲循环


    public UserOptionPreferences(Context context){
        this.context = context;
    }

    /**
     * 设置用户播放模式
     * @param playMode 要设置的播放模式
     */
    public void setPlayModes(int playMode){
        SharedPreferences playModePreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME,Context.MODE_PRIVATE);
        playModePreferences.edit().putInt(PLAY_MODE_KEY,playMode).commit();
    }

    /**
     * 返回所设置的播放模式
     * @return 整形所标示的播放模式，如果未曾设置用户播放模式，默认返回顺序播放
     */
    public int getPlayModes(){
        SharedPreferences playModePreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME,Context.MODE_PRIVATE);
        return playModePreferences.getInt(PLAY_MODE_KEY,PLAY_MODE_ORDER);//默认返回顺序播放
    }
}
