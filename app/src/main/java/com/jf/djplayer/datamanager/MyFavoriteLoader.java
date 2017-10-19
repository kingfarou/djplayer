package com.jf.djplayer.datamanager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.database.SongInfoOpenHelper;

import java.util.List;

/**
 * Created by Kingfar on 2017/10/18.
 * “我的最爱”音乐加载器
 * 通过子线程读取“我的最爱”音乐，通过接口回传给调用者
 */
public class MyFavoriteLoader {

    private static final int LOAD_SUCCESS = 1; // 读取成功的标志
    private static final int LOAD_FAILED = 0;  // 读取失败的标志

    private MyFavoriteLoadListener myFavoriteLoadListener;

    /**
     * 读取本地音乐数据
     * @param listener "LocalMusicReadListener"接口
     */
    public void loadMyFavoriteSong(MyFavoriteLoadListener listener){
        myFavoriteLoadListener = listener;
        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(400);
                }catch (InterruptedException e){

                }
                List<Song> songList = new SongInfoOpenHelper(MyApplication.getContext()).getCollectionSongInfo();
                Message resultMessage = new InnerHandler().obtainMessage(
                        LOAD_SUCCESS, new MyFavoriteLoadResult(myFavoriteLoadListener, songList));
                resultMessage.sendToTarget();
            }
        }.start();
    }

    /**
     * 本地音乐读取监听接口
     */
    public interface MyFavoriteLoadListener {
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
            MyFavoriteLoadResult localMusicReaderResult = (MyFavoriteLoadResult)msg.obj;
            if(msg.what == LOAD_SUCCESS){
                localMusicReaderResult.myFavoriteLoadListener.onSuccess(localMusicReaderResult.songList);
            }else {
                // 暂时传空值
                localMusicReaderResult.myFavoriteLoadListener.onFailed(null);
            }
        }
    }

    // 封装本地音乐读取结果的对象
    private class MyFavoriteLoadResult {

        private MyFavoriteLoadListener myFavoriteLoadListener;
        private List<Song> songList;

        MyFavoriteLoadResult(MyFavoriteLoadListener listener, List<Song> songs){
            myFavoriteLoadListener = listener;
            songList = songs;
        }
    }
}
