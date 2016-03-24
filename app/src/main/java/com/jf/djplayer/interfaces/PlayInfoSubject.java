package com.jf.djplayer.interfaces;

import com.jf.djplayer.SongInfo;

/**
 * Created by JF on 2016/1/28.
 * 这是观察者模式的接口
 * 这是歌曲信息主题
 * 持有歌曲播放数据的类实现该接口而成为主题
 */
public interface PlayInfoSubject {

    /**
     * 向主题里面添加观察者
     * @param playInfoObserver 实现了观察者接口的对象类
     */
    void registerObserver(PlayInfoObserver playInfoObserver);

    /**
     * 将观察者从主题里移除
     * @param playInfoObserver 实现了观察者接口的实现类
     */
    void removeObserver(PlayInfoObserver playInfoObserver);

    /**
     *
     */
    void notifyObservers();
//    以下三个方法是方便于观察者主动拉去状态的

    /**
     * 从主题里得到当前所播放的歌曲信息
     * @return 返回当前所播放的歌曲信息对象，如果用户没有选择任何歌曲，则返回空
     */
    SongInfo getCurrentSongInfo();

    /**
     * 返回当前是否有歌曲在播放
     * @return true:如果有歌曲在播放,false:歌曲暂停或者没有选择任何歌曲
     */
    boolean isPlaying();

    /**
     * 获取当前播放进度
     * @return 当前歌曲播放进度（毫秒）
     */
    int getCurrentPosition();
}
