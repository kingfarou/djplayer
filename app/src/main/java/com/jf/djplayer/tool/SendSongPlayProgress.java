package com.jf.djplayer.tool;

import android.os.Handler;
import android.os.Message;

import com.jf.djplayer.interfaces.PlayInfoSubject;
import com.jf.djplayer.module.SongPlayInfo;

/**
 * Created by JF on 2016/2/9.
 * 该类工作于子线程里面
 * 专门用于不断读取当前播放进度
 * 然后发送播放进度到UI的线程里面
 */
public class SendSongPlayProgress implements Runnable {

    private PlayInfoSubject playInfoSubject;
    private Handler sendTheProgressHandler;
    public boolean run;//外部通过他来控制线程停止
    public static int updateProgress =1;
    private int intervalTime;
    private int lastPosition = -1;

    /**
     * 异步任务构造方法
     * @param playInfoSubject 这是音乐播放信息主题
     * @param uiHandler 这是主线程里面的Handler
     * @param intervalTime 这是更新播放进度间隔时间，有客户端根据需要指定
     */
    public SendSongPlayProgress(PlayInfoSubject playInfoSubject,Handler uiHandler,int intervalTime){
        this.playInfoSubject = playInfoSubject;
        this.sendTheProgressHandler = uiHandler;
        run = true;//设置运行的标记为可以运行
        this.intervalTime = intervalTime;
    }
    @Override
    public void run() {
//        刚启动线程就发送消息
//        Log.i("test","线程开始");
        Message playProgressMessage = new Message();
        playProgressMessage.what = updateProgress;//这是要更新的事件信息
        lastPosition = playInfoSubject.getSongPlayInfo().getProgress();
        playProgressMessage.arg1 = lastPosition;//这是要更新的进度
        sendTheProgressHandler.sendMessage(playProgressMessage);
        playProgressMessage = null;//消息发送出去之后变量置空
        while(run){
            //如果消息已经消费重新创建
            if(playProgressMessage==null){
                playProgressMessage = new Message();
                playProgressMessage.what = updateProgress;
            }
//            如果间隔时长大于客户端的指定时长
//            发送
            if(lastPosition+intervalTime<=playInfoSubject.getSongPlayInfo().getProgress()){
                lastPosition = playInfoSubject.getSongPlayInfo().getProgress();//刷新最后一次进度
                playProgressMessage.arg1 = playInfoSubject.getSongPlayInfo().getProgress();
                sendTheProgressHandler.sendMessage(playProgressMessage);
                playProgressMessage = null;
            }
//            try{Thread.sleep(100L);}catch (Exception e){}
        }//while
//        Log.i("test","线程停止");
    }
}
