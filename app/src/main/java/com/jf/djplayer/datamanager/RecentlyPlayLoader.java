package com.jf.djplayer.datamanager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.database.SongInfoOpenHelper;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Kingfar on 2017/10/18.
 * “最近播放”音乐加载器
 */
public class RecentlyPlayLoader {

    private static final int LOAD_SUCCESS = 1; // 读取成功的标志
    private static final int LOAD_FAILED = 0;  // 读取失败的标志

    private InnerHandler innerHandler;

    public void setRecentlyLoadListener(LoadListener listener){
        if(listener == null) return;
        innerHandler = new InnerHandler(new WeakReference<>(listener));
    }

    /**
     * 读取最近播放的歌曲数据
     * @param number 需要读取最近播放的几首歌曲
     */
    public void load(final int number){
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
                Message resultMessage = innerHandler.obtainMessage(LOAD_SUCCESS, songList);
                resultMessage.sendToTarget();
            }
        }.start();
    }

    /** 最近播放读取监听接口*/
    public interface LoadListener {

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

        private WeakReference<LoadListener> listenerWeakReference;

        InnerHandler(WeakReference<LoadListener> listenerWeakReference){
            super(Looper.getMainLooper());
            this.listenerWeakReference = listenerWeakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            LoadListener listener = listenerWeakReference.get();
            if(listener == null){
                return;
            }
            if(msg.what == LOAD_SUCCESS){
                listener.onSuccess((List<Song>)msg.obj);
            } else {
                Exception e = new Exception("最近播放的歌曲加载失败");
                listener.onFailed(e);
            }
        }
    }

}
