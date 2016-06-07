package com.jf.djplayer.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.jf.djplayer.R;
import com.jf.djplayer.base.baseactivity.BaseActivity;
import com.jf.djplayer.base.baseactivity.BaseNoTitleActivity;
import com.jf.djplayer.other.MyApplication;
import com.jf.djplayer.tool.FileTool;

import java.lang.ref.WeakReference;


/**
 * Created by JF on 2016/3/6.
 * 欢迎界面-welcome page
 */
public class WelcomeActivity extends BaseActivity {

//    private final StartMainActivityThread startMainActivityThread = new StartMainActivityThread(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_welcome, null);
        setContentView(view);
        //窗口界面的出现添加动画的效果
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                reDirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(alphaAnimation);
        appDirInit();
    }

    @Override
    protected int getContentViewId() {
//        return R.layout.activity_welcome;
        return -1;
    }

    @Override
    protected void initExtrasBeforeView() {
//        使用异步任务来完成到"MainActivity"跳转
//        new StartMainActivityThread().start();
//        startMainActivityThread.start();
    }

    @Override
    protected void initView() {

    }

    //创建应用在外存的相关目录
    private void appDirInit(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            FileTool fileTool = new FileTool(this);
            fileTool.createAppRootDir();//创建应用的根目录
            fileTool.appDirInit();//创建应用所需要的各个路径
        }else{
            Toast.makeText(WelcomeActivity.this, "SD卡读取失败，请确定已正确插入", Toast.LENGTH_SHORT).show();
        }
    }

    //重定向到主界面去
    private void reDirectTo(){
        startActivity(new Intent(this, MainActivity.class));
        finish();//启动完成关闭当前活动
    }

//    private static class StartMainActivityThread extends Thread{
//
//        private final WeakReference<WelcomeActivity> activityWeakReference;
//
//        public StartMainActivityThread(WelcomeActivity welcomeActivity){
//            activityWeakReference = new WeakReference<>(welcomeActivity);
//        }
//
//        @Override
//        public void run() {
//            WelcomeActivity welcomeActivity = activityWeakReference.get();
//            if(welcomeActivity == null){
//                return;
//            }
//            //创建应用所需要的路径
//            welcomeActivity.appDirInit();
//            //两秒钟后启动主活动
//            try{
//                Thread.sleep(2000L);
//            }catch (InterruptedException e){
//                MyApplication.printLog("异常--位置--"+WelcomeActivity.class.getSimpleName());
//            }
//            welcomeActivity.startActivity(new Intent(welcomeActivity,MainActivity.class));
//            welcomeActivity.finish();//启动完成关闭当前活动
//        }
//    }//StartMainActivityThread
}
