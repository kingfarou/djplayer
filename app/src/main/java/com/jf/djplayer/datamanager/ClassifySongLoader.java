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
 * Created by Administrator on 2017/10/17.
 * 分类歌曲加载器
 */
public class ClassifySongLoader {

    private static final int LOAD_SUCCESS = 1; // 读取成功的标志
    private static final int LOAD_FAILED = 0;  // 读取失败的标志

    private InnerHandler innerHandler;

    public void setLoadListener(LoadListener listener){
        if(listener == null) return;
        innerHandler = new InnerHandler(new WeakReference<>(listener));
    }

    /**
     *
     * @param listener
     * @param typeName
     * @param typeValue
     */
    public void load(final int typeName, final String typeValue){
        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(400);
                }catch (InterruptedException e){

                }
                List<Song> songList =
                        new SongInfoOpenHelper(MyApplication.getContext()).getClassifySongInfo(typeName, typeValue);
                Message resultMessage = innerHandler.obtainMessage(LOAD_SUCCESS, songList);
                resultMessage.sendToTarget();
            }
        }.start();
    }

    /**
     * 本地音乐读取监听接口
     */
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
            }else {
                Exception e = new Exception("分类歌曲加载失败");
                listener.onFailed(e);
            }
        }
    }

}
