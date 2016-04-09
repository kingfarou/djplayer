package com.jf.djplayer.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.widget.Toast;

import com.jf.djplayer.R;
import com.jf.djplayer.SongInfo;
import com.jf.djplayer.baseactivity.BaseNoTitleActivity;
import com.jf.djplayer.fragment.MainFragment;
import com.jf.djplayer.interfaces.ChangeFragment;
import com.jf.djplayer.interfaces.PlayControls;
import com.jf.djplayer.tool.FileTool;
import com.jf.djplayer.service.PlayerService;


import java.util.List;

/**
 * MainFragment
 * 做几件事
 * 动态加载MineFragment
 * 实现几个回调接口
 *
 */
public class MainActivity extends BaseNoTitleActivity implements ChangeFragment, PlayControls, ServiceConnection{

    private FragmentManager fragmentManager;
    private PlayerService playerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        由于继承"BaseNoTitleActivity"部分方法需要修改或者迁移
        requestWindowFeature(Window.FEATURE_NO_TITLE)方法已在几类里面调用，不再重复。
        绑定服务转移到了"extraInit()"方法里面。
        动态添加"Fragment"转移到了"widgetsInit()"方法里面
         */
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//删除掉ActionBar
//        setContentView(R.layout.activity_main);
////        通过两个方式启动服务确保解绑之后服务不会关闭
//        Intent startService = new Intent(this,PlayerService.class);
//        startService(startService);
//        bindService(startService,this,BIND_AUTO_CREATE);
////        动态加载MineFragment
//        fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().add(R.id.ll_activity_main,new MainFragment()).commit();
////        创建应用在外存的相关目录
//        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//            appDirInit();
//        }else{
//            Toast.makeText(this, "SD卡读取失败，请确定已正确插入", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void doSetContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void widgetsInit() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.ll_activity_main,new MainFragment()).commit();
    }

    @Override
    protected void extrasInit() {
//        通过两个方式启动服务确保解绑之后服务不会关闭
        Intent startService = new Intent(this,PlayerService.class);
        startService(startService);
        bindService(startService,this,BIND_AUTO_CREATE);
//        创建应用在外存的相关目录
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            appDirInit();
        }else{
            Toast.makeText(this, "SD卡读取失败，请确定已正确插入", Toast.LENGTH_SHORT).show();
        }
    }

    //    创建应用在外村的相关目录
    private void appDirInit(){
        FileTool fileTool = new FileTool(this);
        fileTool.createAppRootDir();//创建应用的根目录
        fileTool.appDirInit();//创建应用所需要的各个路径
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);//解绑
    }


//    活动里的Fragment通过他来通知活动
//    更换新的Fragment
    @Override
    public void replaceFragments(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.ll_activity_main,fragment).addToBackStack(null).commit();
    }


//    活动里的Fragment通过他来通知活动
//    关闭当前Fragment
    @Override
    public void finishFragment() {
        fragmentManager.popBackStack();
    }


//    覆盖下面这些方法
//    Fragment通过这些方法控制服务
//    从而进行播放控制
    @Override
    public void play(List<SongInfo> songInfoList,int position) {
        playerService.play(songInfoList, position);
    }

    @Override
    public void play() {
        playerService.play();
    }

    @Override
    public boolean isPlaying() {
        return playerService.isPlaying();
    }

    @Override
    public void pause() {
        playerService.pause();
    }

    @Override
    public void nextSong() {
        playerService.nextSong();
    }

    @Override
    public void previousSong() {
        playerService.previousSong();
    }

    @Override
    public void stop() {

    }

//    两个用于绑定服务用的方法
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        playerService = ((PlayerService.PlayerServiceBinder)service).getPlayerService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}