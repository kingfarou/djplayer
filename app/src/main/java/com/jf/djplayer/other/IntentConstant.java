package com.jf.djplayer.other;

/**
 * Created by JF on 2016/5/7.
 * 该项目所有自定义用于Intent的常量
 */
public class IntentConstant {

    /*音乐播放类的常量*/
    /**播放音乐*/
    public final static String ACTION_PLAY_SONG = "com.jf.djplayer.intent.action.PLAY";
    /**播放下一曲的音乐*/
    public final static String ACTION_PLAY_NEXT_SONG = "com.jf.djplayer.intent.action.PLAY_NEXT";
    /**播放前面一首音乐*/
    public final static String ACTION_PLAY_PREVIOUS_SONG = "com.jf.djplayer.intent.action.PLAY_PREVIOUS";

    /*修改歌曲信息且要通知UI用的常量*/
    /**表示用户修改歌曲信息*/
    public static final String ACTION_UPDATE_SONG_FILE_INFO = "com.jf.djplayer.intent.action.UPDATE_SONG_FILE_INFO";
    /**表示用户添加收藏*/
    public static final String ACTION_COLLECTION_SONG = "com.jf.djplayer.intent.action.COLLECTION_SONG_FILE";
    /**表示用户取消收藏*/
    public static final String ACTION_CANCEL_COLLECTION_SONG = "com.jf.djplayer.action.CANCEL_COLLECTION_SONG_FILE";
    /**表示用户删除歌曲*/
    public static final String ACTION_DELETE_SONG_FILE = "com.jf.djplayer.action.DELETE_SONG_FILE";

}
