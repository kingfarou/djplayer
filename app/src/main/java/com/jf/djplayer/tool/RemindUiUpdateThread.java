package com.jf.djplayer.tool;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by JF on 2016/2/15.
 * 这个线程用来提醒UI进行更新
 */
public class RemindUiUpdateThread extends Thread {

    private long intervalTime;//需要多长时间提醒一次

    /**
     * 客户端通过该变量控制线程停止
     */
    public boolean run;

    public static int UPDATE_PROGRESS = 1;
    private Handler uiHandler;

    /**
     * 该类唯一构造方法
     * @param uiHandler UI线程的Handler
     * @param intervalTime 想要间隔多长时间被子线程提醒一次
     */
    public RemindUiUpdateThread(Handler uiHandler, int intervalTime){
        this.uiHandler = uiHandler;
        this.intervalTime = intervalTime;
        this.run = true;
    }

    @Override
    public void run() {
        Message updateUiMessage;
        while(this.run){
//            每隔intervalTime，提醒UI更新一次
            updateUiMessage = new Message();
            updateUiMessage.what = UPDATE_PROGRESS;
            uiHandler.sendMessage(updateUiMessage);
            updateUiMessage = null;
            try{
                Thread.sleep(intervalTime);
            }catch (InterruptedException e){
                Log.i("test","线程休眠操作失败--位置"+ RemindUiUpdateThread.class);
            }
        }//while
//        Log.i("test","县城结束");
    }
}
