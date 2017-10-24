package com.jf.djplayer.datamanager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.database.SongInfoOpenHelper;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 * 本地音乐加载器，加载App数据库里面全部的音乐。
 * 通过子线程读取本地音乐，通过接口回传给调用者
 */
public class LocalMusicLoader {

    private static final int LOAD_SUCCESS = 1; // 读取成功的标志
    private static final int LOAD_FAILED = 0;  // 读取失败的标志

    private LocalMusicLoadListener localMusicLoadListener;

    /**
     * 读取本地音乐数据
     * @param listener "LocalMusicReadListener"接口
     */
    public void loadLocalMusic(LocalMusicLoadListener listener){
        localMusicLoadListener = listener;
        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(400);
                }catch (InterruptedException e){

                }
                List<Song> songList = new SongInfoOpenHelper(MyApplication.getContext()).getLocalMusicSongList();
                Message resultMessage = new InnerHandler().obtainMessage(
                        LOAD_SUCCESS, new LocalMusicLoadResult(localMusicLoadListener, songList));
                resultMessage.sendToTarget();
            }
        }.start();
    }

    /**
     * 本地音乐读取监听接口
     */
    public interface LocalMusicLoadListener{
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
            LocalMusicLoadResult localMusicReaderResult = (LocalMusicLoadResult)msg.obj;
            if(msg.what == LOAD_SUCCESS){
                localMusicReaderResult.localMusicLoadListener.onSuccess(localMusicReaderResult.songList);
            }else {
                // 暂时传空值
                localMusicReaderResult.localMusicLoadListener.onFailed(null);
            }
        }
    }

    // 封装本地音乐读取结果的对象
    private class LocalMusicLoadResult {

        private LocalMusicLoadListener localMusicLoadListener;
        private List<Song> songList;

        LocalMusicLoadResult(LocalMusicLoadListener listener, List<Song> songs){
            localMusicLoadListener = listener;
            songList = songs;
        }
    }
}
