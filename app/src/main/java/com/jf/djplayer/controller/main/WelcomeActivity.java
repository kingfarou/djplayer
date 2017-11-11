package com.jf.djplayer.controller.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.jf.djplayer.R;
import com.jf.djplayer.backgroundplay.PlayerService;
import com.jf.djplayer.base.activity.BaseActivity;
import com.jf.djplayer.controller.main.MainActivity;
import com.jf.djplayer.util.DirManager;
import com.jf.djplayer.util.SdCardUtil;
import com.jf.djplayer.util.ToastUtil;


/**
 * Created by Kingfar on 2016/3/6.
 * 欢迎界面，该界面共需执行三个任务，执行完了才会进入到主界面：
 * 1、渐变动画；2、APP路径创建；3、读取数据库已有的歌曲总数量
 */
public class WelcomeActivity extends BaseActivity {

    /** Intent键，数据库歌曲总数量*/
    public static final String KEY_CODE_SONG_NUM = "songNum";

    // 透明度动画的持续时间
    private final int ANIMATION_DURATION = 2000;
    // 透明度动画透明的数值
    private final float FROM_ALPHA = 0.2f;
    private final float TO_ALPHA = 1.0f;

    private int songNum; // 数据库里的歌曲总数量
    private boolean isAnimationFinish = false;
    private boolean isInitFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initApp();
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
                if(isInitFinish){
                    startMainActivity();
                }else {
                    isAnimationFinish = true;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(alphaAnimation);
    }

    // APP各种初始化
    private void initApp(){
        initAppDir();
        // 欢迎界面就要启动服务，其他模块都是以bind方式启动服务，
        // 该界面以start方式启动，以避免Activity被销毁时服务被销毁
        Intent startService = new Intent(this, PlayerService.class);
        startService(startService);
        if(isAnimationFinish){
            startMainActivity();
        }else {
            isInitFinish = true;
        }
    }

    //创建应用在外存的相关目录
    private void initAppDir(){
        //检查并提醒用户SD卡是否可用
        if(!SdCardUtil.isSdCardEnable()){
            ToastUtil.showLongToast(this, "SD卡不可用，部分功能可能无法正常使用，请检查您的SD卡");
        } else {
            DirManager dirManager = new DirManager();
            dirManager.initAppDir();//创建应用的根目录
        }
    }

    //跳转到主界面去
    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(KEY_CODE_SONG_NUM, songNum);
        startActivity(intent);
        finish();//启动完成关闭当前活动
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //欢迎界面不许返回
        if(keyCode == KeyEvent.KEYCODE_BACK) return true;
        return super.onKeyDown(keyCode, event);
    }
}
