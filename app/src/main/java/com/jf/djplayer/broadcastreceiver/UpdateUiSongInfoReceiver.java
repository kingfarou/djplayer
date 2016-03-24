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

    public static final String COLLECTION_SONG = "collectionSong";//表示用户收藏歌曲
    public static final String CANCEL_COLLECTION_SONG = "cancelCollectionSong";//表示用户取消收藏某一曲目
    public static final String DELETE_SONG = "deleteSong";//表示用户删除掉了一首歌曲
    public static final String UPDATE_SONG_INFO = "updateSongInfo";//表示用户修改歌曲信息
    public static final String position = "position";//表示用户所操作的歌曲在列表里面的序号

    private SongInfoObserver songInfoObserver;
    public UpdateUiSongInfoReceiver(SongInfoObserver songInfoObserver){
        this.songInfoObserver = songInfoObserver;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
//        回调歌曲信息观察者里的方法通知界面更新UI
        songInfoObserver.updateSongInfo(intent.getAction(), intent.getIntExtra(position, -1));
    }
}
