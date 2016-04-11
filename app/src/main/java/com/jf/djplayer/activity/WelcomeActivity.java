package com.jf.djplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jf.djplayer.R;

/**
 * Created by JF on 2016/3/6.
 * 这是本软件的欢迎界面活动
 * 由原来的直接继承"Activity"变成继承"BaseNoTitleActivity"
 */
public class WelcomeActivity extends BaseNoTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        以下操作全部放到基类响应方法里面
//        另外"requestWindowFeature(Window.FEATURE_NO_TITLE)"已在几类里面调用，不再重复
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_welcome);
//        new StartMainActivityThread().start();//使用一个异步任务完成到主活动的跳转
    }

    @Override
    protected void doSetContentView() {
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void extrasInit() {
//        使用异步任务来完成到"MainActivity"跳转
        new StartMainActivityThread().start();
    }

    @Override
    protected void viewInit() {

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
