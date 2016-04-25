package com.jf.djplayer.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jf.djplayer.interfaces.SongInfoObserver;

/**
 * Created by JF on 2016/2/22.
 * 所有对歌曲进行的收藏/取消收藏、删除、信息修改，都要发送对应广播、
 * 这个接收器将接收这些广播
 * 对这些广播有兴趣的类，实现SongInfoObserver接口，就可监听歌曲信息变化
 */
public class UpdateUiSongInfoReceiver extends BroadcastReceiver {
    public static final String position = "position";//表示用户所操作的歌曲在列表里面的序号

    /**表示用户修改歌曲信息*/
    public static final String ACTION_UPDATE_SONG_FILE_INFO = "com.jf.djplayer.intent.action.UPDATE_SONG_FILE_INFO";
    /**表示用户添加收藏*/
    public static final String ACTION_COLLECTION_SONG = "com.jf.djplayer.intent.action.COLLECTION_SONG_FILE";
    /**表示用户取消收藏*/
    public static final String ACTION_CANCEL_COLLECTION_SONG = "com.jf.djplayer.action.CANCEL_COLLECTION_SONG_FILE";
    /**表示用户删除歌曲*/
    public static final String ACTION_DELETE_SONG_FILE = "com.jf.djplayer.action.DELETE_SONG_FILE";

    private SongInfoObserver songInfoObserver;

    public UpdateUiSongInfoReceiver(SongInfoObserver songInfoObserver){
        this.songInfoObserver = songInfoObserver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        回调歌曲信息观察者里的方法通知界面更新UI
        songInfoObserver.updateSongInfo(intent, intent.getIntExtra(position, -1));
    }
}
