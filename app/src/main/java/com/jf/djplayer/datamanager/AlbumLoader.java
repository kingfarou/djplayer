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
 * Created by Kingfar on 2017/10/17.
 * 专辑列表加载器
 */

public class AlbumLoader {

    private static final int LOAD_SUCCESS = 1; // 读取成功的标志
    private static final int LOAD_FAILED = 0;  // 读取失败的标志

    private InnerHandler innerHandler;

    public void setLoadListener(LoadListener listener){
        if(listener == null) return;
        innerHandler = new InnerHandler(new WeakReference<>(listener));
    }

    /** 读取歌手*/
    public void load(){
        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(400);
                }catch (InterruptedException e){

                }
                List<Album> albumList = new SongInfoOpenHelper(MyApplication.getContext()).getAlbumList();
                Message resultMessage = innerHandler.obtainMessage(LOAD_SUCCESS, albumList);
                resultMessage.sendToTarget();
            }
        }.start();
    }

    /** 歌手加载监听接口*/
    public interface LoadListener {

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
                listener.onSuccess((List<Album>)msg.obj);
            }else {
                Exception e = new Exception("专辑集合加载失败");
                listener.onFailed(e);
            }
        }
    }

}
