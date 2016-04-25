package com.jf.djplayer.interfaces;

import android.content.Intent;

/**
 * Created by JF on 2016/2/22.
 * 歌曲信息的观察者
 * 如果哪个类要监听歌曲信息的变动的
 * 需要实现这个接口
 * UpdateUiSongInfoReceiver
 * 接收器接收到歌曲信息的变动后
 * 将会回调接口里的对应方法
 */
public interface SongInfoObserver {

    /**
     * 当歌曲的信息被修改了，歌曲信息的观察者通过这个方法收到修改通知
     * @param updateIntent 标识做了什么修改
     * @param position 表示被修改的歌曲在原来的列表里的位置
     */
    public void updateSongInfo(Intent updateIntent, int position);
}
