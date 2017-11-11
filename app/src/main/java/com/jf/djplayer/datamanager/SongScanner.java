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
        new Thread(new ScanRunnable()).start();
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

    // 封装扫描任务过程的Runnable
    private class ScanRunnable implements Runnable{

        @Override
        public void run() {
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
            // 插入过程用到的变量
            Song song; // 正在处理的歌曲
            int batchNumber = 10; // 批量插入时，每次插入几首歌曲
            List<Song> songList = new ArrayList<>(batchNumber); // 批量插入用的集合
            int sum = songCursor.getCount(); // 扫描到的歌曲总数
            int current = 0; // 正在处理的歌曲序号
            SongInfoOpenHelper songInfoOpenHelper = new SongInfoOpenHelper(MyApplication.getContext());
            // 遍历结果集并将结果插入到APP的数据库里（批量插入）
            while (songCursor.moveToNext()) {
                current+=1;
                song = new Song(songCursor.getString(titleIndex), songCursor.getString(artistIndex),
                        songCursor.getString(albumIndex), songCursor.getInt(durationIndex),
                        songCursor.getInt(sizeIndex), songCursor.getString(dataIndex));
                // 发送正在处理的歌曲信息到UI线程给用户看
                innerHandler.obtainMessage(UPDATE_PROGRESS, new ScanInfo(sum, current, song.getFileAbsolutePath())).sendToTarget();
                songList.add(song);
                // 批量插入
                if(songList.size() == batchNumber){
                    songInfoOpenHelper.insertLocalMusic(songList);
                    songList.clear();
                }
                // 线程休眠，方便查看效果以及调试
                try{
                    Thread.sleep(200);
                }catch (InterruptedException e){
                    LogUtil.i("扫描线程中断异常--"+e.toString());
                }
            }
            // 如果扫描到的歌曲总数和批处理的个数不成整数倍关系，
            // 则剩余未处理的歌曲在这里补充处理
            if(songList.size() != 0){
                songInfoOpenHelper.insertLocalMusic(songList);
            }
            // 发送扫描成功信息
            innerHandler.obtainMessage(SCAN_SUCCESS, new ScanInfo(sum, sum, null)).sendToTarget();
        }
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
