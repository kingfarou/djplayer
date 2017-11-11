package com.jf.djplayer.datamanager;

import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;

import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.bean.ScanInfo;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.util.ScanUtil;
import com.jf.djplayer.util.LogUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kingfar on 2017/11/5.
 * 本地音乐扫描器，
 * 通过子线程扫描音乐，通过接口返回扫描信息
 */

public class SongScanner {

    // Handler用的what标志
    private static final int UPDATE_PROGRESS = 1; // 更新扫描进度信息
    private static final int SCAN_SUCCESS = 2;    // 扫描完成
    private static final int SCAN_FAILED = 3;     // 扫描失败

    private InnerHandler innerHandler;

    public void setSongScanListener(ScanListener listener){
        WeakReference<ScanListener> weakReference = new WeakReference<ScanListener>(listener);
        innerHandler = new InnerHandler(weakReference);
    }

    /** 开始扫描*/
    public void startScan(){
        new Thread(){
            @Override
            public void run() {
                LogUtil.i(SongScanner.class.getSimpleName()+"--startScan()--子线程开始工作");
                // 获取歌曲扫描工具，执行扫描并获取扫描结果
                ScanUtil scanUtil = new ScanUtil();
                Cursor songCursor = scanUtil.scanSong();
                // 获取Cursor各个列的索引
                int titleIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int artistIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int albumIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int durationIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                int sizeIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
                int dataIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                // 装填歌曲集合
                List<Song> songList = new ArrayList<>(songCursor.getCount());
                Song songInfo;
                while (songCursor.moveToNext()) {
                    songInfo = new Song(
                            songCursor.getString(titleIndex),
                            songCursor.getString(artistIndex),
                            songCursor.getString(albumIndex),
                            songCursor.getInt(durationIndex),
                            songCursor.getInt(sizeIndex),
                            songCursor.getString(dataIndex)
                    );
                    songList.add(songInfo);
                }
                songCursor.close();
                SongInfoOpenHelper songInfoOpenHelper = new SongInfoOpenHelper(MyApplication.getContext());
                int sum = songList.size();
                // 逐条发送每条歌曲信息
                for(int i = 0; i < sum; i++){
                    songInfoOpenHelper.insertLocalMusic(songList.get(i));
                    //发送歌曲信息到主线程
                    innerHandler.obtainMessage(UPDATE_PROGRESS, new ScanInfo(sum, i+1, songList.get(i).getFileAbsolutePath()))
                            .sendToTarget();
                    // 线程休眠，方便查看效果以及调试
                    try{
                        sleep(200);
                    }catch (InterruptedException e){
                        LogUtil.i("扫描线程中断异常--"+e.toString());
                    }
                }
                // 发送扫描成功信息
                innerHandler.obtainMessage(SCAN_SUCCESS, new ScanInfo(sum, sum, null)).sendToTarget();
            }
        }.start();
    }

    /** 歌曲扫描监听器*/
    public interface ScanListener {

        /**
         * 扫描进行时回调该方法更新扫描信息
         * @param scanInfo 扫描信息
         */
        void onProgressUpdate(ScanInfo scanInfo);

        /**
         * 扫描成功时回调的方法
         * @param scanInfo 扫描信息
         */
        void onSuccess(ScanInfo scanInfo);

        /**
         * 扫描失败时回调的方法
         * @param e 异常信息
         */
        void onFailed(Exception e);
    }

    // 子线程发送消息到UI线程用的Handler
    private static class InnerHandler extends Handler{

        private WeakReference<ScanListener> listenerWeakReference;

        public InnerHandler(WeakReference<ScanListener> listenerWeakReference){
            super(Looper.getMainLooper());
            this.listenerWeakReference = listenerWeakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            ScanListener listener = listenerWeakReference.get();
            if(listener == null){
                LogUtil.i(SongScanner.class.getSimpleName()+"--InnerHandler--handleMessage()--弱引用已经失效");
                return;
            }
            int what = msg.what;
            if (what == UPDATE_PROGRESS) {
                listener.onProgressUpdate((ScanInfo)msg.obj);
            }else if(what == SCAN_SUCCESS){
                listener.onSuccess((ScanInfo)msg.obj);
            }else if(what == SCAN_FAILED){
                Exception e = new Exception("歌曲扫描失败");
                listener.onFailed(e);
            }
        }
    }

}
