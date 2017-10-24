package com.jf.djplayer.songscan;

import android.os.Handler;
import android.os.Message;

import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.util.LogUtil;

import java.util.List;

/**
 * Created by jf on 2016/8/4.
 * 扫描音乐的子线程，该线程做如下事情：
 * >使用工具读取系统音乐信息，读取操作具体写在工具里面，不在该线程里实现。
 * >将所读取到的信息，一条一条插进自己数据库里，插入操作写在数据库工具里，不在该线程里实现。
 * >每插一条数据需要发送消息通知界面更新数据。
 */
public class ScanThread extends Thread{

    //在扫描过程中接收扫描信息的线程的"Handler"
    private Handler handler;

    /**
     * 创建一个扫描音乐用的线程
     * @param handler 在扫描过程中接收扫描信息的线程的"Handler"
     */
    public ScanThread(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        //发送扫描开始信息
//        sendScanStartInfo();
        //扫描过程更新歌曲信息
        updateScanInfo();
        //发送扫描结束信息
        sendScanFinishInfo();
    }

    //向"handler"所在线程发送歌曲扫描开始信息
    private void sendScanStartInfo(){
        handler.sendEmptyMessage(ScanningSongActivity.HANDLER_WHAT_START_SCAN);
    }

    //向"handler"所在线程发送扫描更新信息
    private void updateScanInfo(){
        //获取音乐扫描工具并且进行扫描
        ScanUtil scanUtil = new ScanUtil();
        List<Song> songList = scanUtil.scanSong();
        //如果没有任何歌曲
        if(songList == null || songList.size() == 0){
            //发送消息告诉界面总歌曲数为零
            handler.obtainMessage(ScanningSongActivity.HANDLER_WHAT_UPDATE_SCAN_INFO, new ScanInfo(0, 0, "")).sendToTarget();
            //发送扫描结束标记
            sendScanFinishInfo();
            return;
        }
        //获取自己数据库的管理工具，用于逐条插入歌曲
        SongInfoOpenHelper songInfoOpenHelper = new SongInfoOpenHelper(MyApplication.getContext());
        Message updateMessage;//发送信息用的变量
        int sum = songList.size();
        //逐条发送每条歌曲信息
        for(int i = 0; i<songList.size(); i++){
            songInfoOpenHelper.insertLocalMusic(songList.get(i));
            //发送歌曲信息到主线程
            updateMessage = handler.obtainMessage(ScanningSongActivity.HANDLER_WHAT_UPDATE_SCAN_INFO,
                    new ScanInfo(sum, i+1, songList.get(i).getFileAbsolutePath()));
            updateMessage.sendToTarget();
            //线程休眠，方便查看效果以及调试
            try{
                sleep(200);
            }catch (InterruptedException e){
                LogUtil.i("扫描线程中断异常--"+e.toString());
            }
        }
    }//updateScanInfo()

    //向"handler"所在线程发送扫描结束信息
    private void sendScanFinishInfo(){
        handler.sendEmptyMessage(ScanningSongActivity.HANDLER_WHAT_FINISH_SCAN);
    }

}
