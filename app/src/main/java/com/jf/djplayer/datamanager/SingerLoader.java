package com.jf.djplayer.datamanager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.bean.Singer;
import com.jf.djplayer.database.SongInfoOpenHelper;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 * 歌手加载器，加载App数据库里的歌曲对应的歌手。
 * 通过子线程读取歌手，通过接口回传给调用者
 */
public class SingerLoader {

    private static final int LOAD_SUCCESS = 1; // 读取成功的标志
    private static final int LOAD_FAILED = 0;  // 读取失败的标志

    private SingerLoadListener singerLoadListener;

    /**
     * 读取歌手
     * @param listener "SingerLoadListener"接口
     */
    public void loadSinger(SingerLoadListener listener){
        singerLoadListener = listener;
        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(400);
                }catch (InterruptedException e){

                }
                List<Singer> singerList = new SongInfoOpenHelper(MyApplication.getContext()).getSingerList();
                Message resultMessage = new InnerHandler().obtainMessage(
                        LOAD_SUCCESS, new SingerLoadResult(singerLoadListener, singerList));
                resultMessage.sendToTarget();
            }
        }.start();
    }

    /**
     * 歌手加载监听接口
     */
    public interface SingerLoadListener{
        /**
         * 读取成功时的回调方法
         * @param singerList 歌手信息集合
         */
        void onSuccess(List<Singer> singerList);

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
            SingerLoadResult singerLoadResult = (SingerLoadResult)msg.obj;
            if(msg.what == LOAD_SUCCESS){
                singerLoadResult.singerLoadListener.onSuccess(singerLoadResult.singerList);
            }else {
                // 暂时传空值
                singerLoadResult.singerLoadListener.onFailed(null);
            }
        }
    }

    // 封装本地音乐读取结果的对象
    private class SingerLoadResult {

        private SingerLoadListener singerLoadListener;
        private List<Singer> singerList;

        SingerLoadResult(SingerLoadListener listener, List<Singer> singers){
            singerLoadListener = listener;
            singerList = singers;
        }
    }
}
