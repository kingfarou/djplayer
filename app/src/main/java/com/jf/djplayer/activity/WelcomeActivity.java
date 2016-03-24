package com.jf.djplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.jf.djplayer.R;

/**
 * Created by JF on 2016/3/6.
 * 这是本软件的欢迎界面活动
 */
public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        new StartMainActivityThread().start();//使用一个异步任务完成到主活动的跳转
    }

    private class StartMainActivityThread extends Thread{
        @Override
        public void run() {
            super.run();
//            两秒钟后启动主活动
            try{
                Thread.sleep(2000L);
            }catch (InterruptedException e){
                Log.i("test","报错位置----"+WelcomeActivity.class);
            }
            startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
            finish();//启动完成关闭当前活动
        }
    }//StartMainActivityThread
}
