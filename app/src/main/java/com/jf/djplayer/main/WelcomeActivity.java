package com.jf.djplayer.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.jf.djplayer.R;
import com.jf.djplayer.base.activity.BaseActivity;
import com.jf.djplayer.util.FileUtil;
import com.jf.djplayer.util.SdCardUtil;
import com.jf.djplayer.util.ToastUtil;


/**
 * Created by JF on 2016/3/6.
 * 主模块，欢迎界面
 */
public class WelcomeActivity extends BaseActivity {

    // 透明度动画的持续时间
    private static final int ANIMATION_DURATION = 2000;

    // 透明度动画透明的数值
    private static final float FROM_ALPHA = 0.2f;
    private static final float TO_ALPHA = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    private void initView() {
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
                startMainActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(alphaAnimation);
        initAppDir();
    }

    //创建应用在外存的相关目录
    private void initAppDir(){
        //检查并提醒用户SD卡是否可用
        if(!SdCardUtil.isSdCardEnable()){
            ToastUtil.showLongToast(this, "SD卡不可用，无法创建所需路径，部分功能无法正常使用");
            return;
        }
        FileUtil fileTool = new FileUtil();
        fileTool.initAppDir();//创建应用的根目录
    }

    //跳转到主界面去
    private void startMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish();//启动完成关闭当前活动
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //欢迎界面不许返回
        if(keyCode == KeyEvent.KEYCODE_BACK) return true;
        return super.onKeyDown(keyCode, event);
    }
}
