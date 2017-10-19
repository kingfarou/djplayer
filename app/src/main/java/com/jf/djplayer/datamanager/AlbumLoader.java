package com.jf.djplayer.datamanager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.bean.Album;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.util.LogUtil;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 *
 */

public class AlbumLoader {

    private static final int LOAD_SUCCESS = 1; // 读取成功的标志
    private static final int LOAD_FAILED = 0;  // 读取失败的标志

    private AlbumLoadListener albumLoadListener;

    /**
     * 读取歌手
     * @param listener "LocalMusicReadListener"接口
     */
    public void loadAlbum(AlbumLoadListener listener){
        albumLoadListener = listener;
        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(400);
                }catch (InterruptedException e){

                }
                List<Album> albumList = new SongInfoOpenHelper(MyApplication.getContext()).getAlbumList();
                Message resultMessage = new InnerHandler().obtainMessage(
                        LOAD_SUCCESS, new AlbumLoaderResult(albumLoadListener, albumList));
                resultMessage.sendToTarget();
            }
        }.start();
    }

    /**
     * 歌手加载监听接口
     */
    public interface AlbumLoadListener{
        /**
         * 读取成功时的回调方法
         * @param singerList 歌手信息集合
         */
        void onSuccess(List<Album> singerList);

        /**
         * 读取失败时的回调方法
         * @param exception 异常信息
         */
        void onFailed(Exception exception);
    }

    // 接收子线程扫描结果的Handler
    private static class InnerHandler extends Handler {

        InnerHandler(){
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            AlbumLoaderResult albumLoaderResult = (AlbumLoaderResult)msg.obj;
            if(msg.what == LOAD_SUCCESS){
                    albumLoaderResult.albumLoadListener.onSuccess(albumLoaderResult.albumList);
            }else {
                // 暂时传空值
                albumLoaderResult.albumLoadListener.onFailed(null);
            }
        }
    }

    // 封装本地音乐读取结果的对象
    private class AlbumLoaderResult{

        private AlbumLoadListener albumLoadListener;
        private List<Album> albumList;

        AlbumLoaderResult(AlbumLoadListener listener, List<Album> albums){
            albumLoadListener = listener;
            albumList = albums;
        }
    }
}
