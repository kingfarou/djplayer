package com.jf.djplayer.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.jf.djplayer.R;
import com.jf.djplayer.base.baseactivity.BaseNoTitleActivity;
import com.jf.djplayer.tool.FileTool;


/**
 * Created by JF on 2016/3/6.
 * 欢迎界面-welcome page
 */
public class WelcomeActivity extends BaseNoTitleActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initExtrasBeforeView() {
//        使用异步任务来完成到"MainActivity"跳转
        new StartMainActivityThread().start();
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


    private class StartMainActivityThread extends Thread{
        @Override
        public void run() {
            super.run();
            //创建应用在外存的相关目录
            appDirInit();
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
