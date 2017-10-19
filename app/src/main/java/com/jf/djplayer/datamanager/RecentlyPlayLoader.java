package com.jf.djplayer.datamanager;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.database.SongInfoOpenHelper;

import java.util.List;

/**
 * Created by Kingfar on 2017/10/18.
 * “最近播放”音乐加载器
 * 通过子线程读取“最近播放”音乐，通过接口回传给调用者
 */
public class RecentlyPlayLoader {

    private static final int LOAD_SUCCESS = 1; // 读取成功的标志
    private static final int LOAD_FAILED = 0;  // 读取失败的标志

    private RecentlyPlayLoadListener recentlyPlayLoadListener;

    /**
     * 读取最近播放的歌曲数据
     * @param listener "RecentlyPlayLoadListener"接口
     * @param number 需要读取最近播放的几首歌曲
     */
    public void loadRecentlyPlaySong(RecentlyPlayLoadListener listener, final int number){
        recentlyPlayLoadListener = listener;
        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(400);
                }catch (InterruptedException e){

                }
                List<Song> songList =
                        new SongInfoOpenHelper(MyApplication.getContext()).getRecentlyPlaySong(number);
                // 遍历集合过滤所有从未播放过的歌曲（即"lastPlayTime == SongInfoOpenHelper.NEVER_PLAY"的歌）
                for(int i = songList.size()-1; i > -1; i--){
                    if(songList.get(i).getLastPlayTime() == SongInfoOpenHelper.NEVER_PLAY){
                        songList.remove(i);
                    }
                }
                Message resultMessage = new InnerHandler().obtainMessage(
                        LOAD_SUCCESS, new RecentlyPlayLoadResult(recentlyPlayLoadListener, songList));
                resultMessage.sendToTarget();
            }
        }.start();
    }

    /**
     * 本地音乐读取监听接口
     */
    public interface RecentlyPlayLoadListener {
        /**
         * 读取成功时的回调方法
         * @param songList 本地音乐信息集合
         */
        void onSuccess(List<Song> songList);

        /**
         * 读取失败时的回调方法
         * @param exception 异常信息
         */
        void onFailed(Exception exception);
    }

    // 接收子线程扫描结果的Handler
    private static class InnerHandler extends Handler{

        InnerHandler(){
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            RecentlyPlayLoadResult localMusicReaderResult = (RecentlyPlayLoadResult)msg.obj;
            if(msg.what == LOAD_SUCCESS){
                localMusicReaderResult.recentlyPlayLoadListener.onSuccess(localMusicReaderResult.songList);
            }else {
                // 暂时传空值
                localMusicReaderResult.recentlyPlayLoadListener.onFailed(null);
            }
        }
    }

    // 封装本地音乐读取结果的对象
    private class RecentlyPlayLoadResult {

        private RecentlyPlayLoadListener recentlyPlayLoadListener;
        private List<Song> songList;

        RecentlyPlayLoadResult(RecentlyPlayLoadListener listener, List<Song> songs){
            recentlyPlayLoadListener = listener;
            songList = songs;
        }
    }
}
