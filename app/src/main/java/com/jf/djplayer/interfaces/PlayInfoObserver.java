package com.jf.djplayer.interfaces;

import com.jf.djplayer.module.SongInfo;
import com.jf.djplayer.module.SongPlayInfo;;

/**
 * Created by JF on 2016/1/28.
 * 这是歌曲信息的观察着接口
 * 谁想获取歌曲播放实时信息
 * 就要实现这个接口
 */
public interface PlayInfoObserver {

    /**
     * 用来通知观察者的方法
     * @param songPlayInfo 该对象包含正播放歌曲的歌曲信息、以及歌曲播放信息
     */
    public void updatePlayInfo(SongPlayInfo songPlayInfo);
}
