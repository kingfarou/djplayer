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
public class ClassifySongLoader {

    private static final int LOAD_SUCCESS = 1; // 读取成功的标志
    private static final int LOAD_FAILED = 0;  // 读取失败的标志

    private ClassifySongLoadListener classifySongLoadListener;

    /**
     * 读取本地音乐数据
     * @param listener "LocalMusicReadListener"接口
     */
    public void loadSong(ClassifySongLoadListener listener, final String typeName, final String typeValue){
        classifySongLoadListener = listener;
        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(400);
                }catch (InterruptedException e){

                }
                List<Song> songList =
                        new SongInfoOpenHelper(MyApplication.getContext()).getClassifySongInfo(typeName, typeValue);
                Message resultMessage = new InnerHandler().obtainMessage(
                        LOAD_SUCCESS, new ClassifySongLoadResult(classifySongLoadListener, songList));
                resultMessage.sendToTarget();
            }
        }.start();
    }

    /**
     * 本地音乐读取监听接口
     */
    public interface ClassifySongLoadListener {
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
            ClassifySongLoadResult localMusicReaderResult = (ClassifySongLoadResult)msg.obj;
            if(msg.what == LOAD_SUCCESS){
                localMusicReaderResult.classifySongLoadListener.onSuccess(localMusicReaderResult.songList);
            }else {
                // 暂时传空值
                localMusicReaderResult.classifySongLoadListener.onFailed(null);
            }
        }
    }

    // 封装本地音乐读取结果的对象
    private class ClassifySongLoadResult {

        private ClassifySongLoadListener classifySongLoadListener;
        private List<Song> songList;

        ClassifySongLoadResult(ClassifySongLoadListener listener, List<Song> songs){
            classifySongLoadListener = listener;
            songList = songs;
        }
    }
}
