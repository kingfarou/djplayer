package com.jf.djplayer.datamanager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.database.SongInfoOpenHelper;

import java.lang.ref.WeakReference;

/**
 * Created by Kingfar on 2017/11/10.
 * 歌曲数量加载器，用来加载本地音乐歌曲总数
 */

public class SongNumberLoader {

    private static final int SUCCESS = 1;  // 加载成功
    private static final int FAILED = 0;   // 加载失败
    private InnerHandler innerHandler;     // 内部Handler

    public void setLoadListener(loadListener listener){
        if(listener == null) return;
        innerHandler = new InnerHandler(new WeakReference<>(listener));
    }

    public void load(){
        new Thread(){
            @Override
            public void run() {
                int songNum = new SongInfoOpenHelper(MyApplication.getContext()).getLocalMusicNumber();
                innerHandler.obtainMessage(SUCCESS, songNum).sendToTarget();
            }
        }.start();
    }

    /** 歌曲数量加载监听器*/
    public interface loadListener {

        /**
         * 加载成功时的回调
         * @param songNumber 歌曲数量
         */
        public void onSuccess(int songNumber);

        /**
         * 加载失败时的回调
         * @param e 异常对象
         */
        public void onFailed(Exception e);
    }

    // 发送消息给监听器用的handler
    private static class InnerHandler extends Handler{

        private WeakReference<loadListener> songNumberLoadListenerWeak;

        InnerHandler(WeakReference<loadListener> weakReference){
            super(Looper.getMainLooper());
            this.songNumberLoadListenerWeak = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadListener listener = (loadListener)songNumberLoadListenerWeak.get();
            if(listener == null){
                return;
            }
            if(msg.what == SUCCESS){
                listener.onSuccess((int)msg.obj);
            }else{
                Exception e = new Exception("歌手数量加载失败");
                listener.onFailed(e);
            }
        }
    }
}
