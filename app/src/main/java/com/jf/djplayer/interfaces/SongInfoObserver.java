package com.jf.djplayer.interfaces;

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
     * 当歌曲的信息被修改了（在非界面的地方做修改）这个方法将被回调
     * @param updateAction 对歌曲所做的修改类型
     * @param position 被修改的歌曲在列表的位置
     */
    public void updateSongInfo(String updateAction,int position);
}
