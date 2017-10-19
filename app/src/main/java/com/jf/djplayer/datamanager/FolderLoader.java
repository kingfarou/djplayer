package com.jf.djplayer.datamanager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.bean.Folder;
import com.jf.djplayer.database.SongInfoOpenHelper;

import java.util.List;

/**
 * Created by Kingfar on 2017/10/17.
 * 文件夹加载器，加载App数据库里的文件夹对应的歌手。
 * 通过子线程读取文件夹，通过接口回传给调用者
 */
public class FolderLoader {

    private static final int LOAD_SUCCESS = 1; // 读取成功的标志
    private static final int LOAD_FAILED = 0;  // 读取失败的标志

    private FolderLoadListener loadListener;

    /**
     * 读取歌手
     * @param listener "SingerLoadListener"接口
     */
    public void loadFolder(FolderLoadListener listener){
        loadListener = listener;
        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(400);
                }catch (InterruptedException e){

                }
                List<Folder> folderList = new SongInfoOpenHelper(MyApplication.getContext()).getFolderList();
                Message resultMessage = new InnerHandler().obtainMessage(
                        LOAD_SUCCESS, new FolderLoadResult(loadListener, folderList));
                resultMessage.sendToTarget();
            }
        }.start();
    }

    /**
     * 歌手加载监听接口
     */
    public interface FolderLoadListener{
        /**
         * 读取成功时的回调方法
         * @param folderList 文件夹列表集合
         */
        void onSuccess(List<Folder> folderList);

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
            FolderLoadResult folderLoadResult = (FolderLoadResult)msg.obj;
            if(msg.what == LOAD_SUCCESS){
                folderLoadResult.folderLoadListener.onSuccess(folderLoadResult.folderList);
            }else {
                // 暂时传空值
                folderLoadResult.folderLoadListener.onFailed(null);
            }
        }
    }

    // 封装本地音乐读取结果的对象
    private class FolderLoadResult {

        private FolderLoadListener folderLoadListener;
        private List<Folder> folderList;

        FolderLoadResult(FolderLoadListener listener, List<Folder> folderList){
            folderLoadListener = listener;
            this.folderList = folderList;
        }
    }
}
