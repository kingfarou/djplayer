package com.jf.djplayer.interfaces;

import com.jf.djplayer.bean.PlayInfo;

/**
 * Created by JF on 2016/1/28.
 * 这是观察者模式的接口
 * 这是歌曲信息主题
 * 持有歌曲播放数据的类实现该接口而成为主题
 */
public interface PlayInfoSubject {

    /**
     * 向"PlayInfoSubject"主题里添加观察者
     * @param playInfoObserver 要添加的观察者对象
     */
    void registerObserver(PlayInfoObserver playInfoObserver);

    /**
     * 将观察者从"PlayInfoSubject"主题里移除
     * @param playInfoObserver 要移除的观察者对象
     */
    void removeObserver(PlayInfoObserver playInfoObserver);

    /**
     * 发送新消息给观察者
     */
    void notifyObservers();

    /**
     * PlayInfoObserver"对象主动拉取当前正播放的歌曲的信息和播放信息
     * @return 返回当前正播放的歌曲的信息和播放信息
     */
    PlayInfo getPlayInfo();
}
