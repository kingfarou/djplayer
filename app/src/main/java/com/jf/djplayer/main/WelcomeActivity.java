package com.jf.djplayer.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.jf.djplayer.R;
import com.jf.djplayer.base.baseactivity.BaseActivity;
import com.jf.djplayer.util.FileUtil;


/**
 * Created by JF on 2016/3/6.
 * 欢迎界面-welcome page
 */
public class WelcomeActivity extends BaseActivity {

    //透明度动画的持续时间
    private static final int ANIMATION_DURATION = 2000;
    //透明度动画透明的数值
    private static final float FROM_ALPHA = 0.2f;
    private static final float TO_ALPHA = 1.0f;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initExtra() {
    }


    @Override
    protected void initView() {
        View view = findViewById(R.id.fl_activity_welcome);
        //窗口界面的出现添加动画的效果
        AlphaAnimation alphaAnimation = new AlphaAnimation(FROM_ALPHA, TO_ALPHA);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(ANIMATION_DURATION);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束后的跳转
                reDirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(alphaAnimation);
        appDirInit();
    }

    //创建应用在外存的相关目录
    private void appDirInit(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            FileUtil fileTool = new FileUtil(this);
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
}
