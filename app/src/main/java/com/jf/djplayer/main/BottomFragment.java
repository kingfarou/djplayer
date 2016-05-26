package com.jf.djplayer.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.other.SongInfo;
import com.jf.djplayer.songplayinfo.SongPlayInfoActivity;
import com.jf.djplayer.interfaces.PlayInfoObserver;
import com.jf.djplayer.tool.SendSongPlayProgress;
import com.jf.djplayer.playertool.PlayerOperator;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.interfaces.PlayInfoSubject;
import com.jf.djplayer.tool.RemindUiUpdateThread;

/**
 * Created by Administrator on 2015/8/22.
 * 播放控制底边栏
 */
public class BottomFragment extends Fragment implements PlayInfoObserver,View.OnClickListener{

//    view成员变量
    private View viewLayout;//这是当前Fragment布局文件
    private ImageView singerPicture;//这个表示歌手图片
    private TextView songNameTv;//歌名
    private TextView songArtistTv;//这个表示歌手名字
    private LinearLayout linearLayout;//整个界面线性布局
    private ImageButton playButton;//播放
    private ImageButton nextButton;//下首
    private ImageButton menuButton;//菜单
    private ProgressBar progressBar;

//    功能类的成员变量
    private SongInfo lastSongInfo;//这个保存主题上次传进来的歌曲信息
    private RemindUiUpdateThread remindUiUpdateThread;
    private Handler updateProgressHandler;
    private PlayController playController;//音乐播放的控制者（其实就是当前活动）
    private PlayInfoSubject playInfoSubject;//所观察的那个主题



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.viewLayout = inflater.inflate(R.layout.fragment_bottom, container, false);
        viewInit();//界面信息的初始化
        handlerInits();//处理进度的Handler的初始化
//        添加观察者到主题里面
        playInfoSubject = PlayerOperator.getInstance();

        return viewLayout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        playController = (PlayController)activity;
    }

//    不论Fragment新启动的
//    还是从回退栈里面重新启动
//    都会回调这个方法
    @Override
    public void onStart() {
        super.onStart();
        playInfoSubject.registerObserver(this);
    }

    @Override
    public void onStop() {
        super.onStop();
//        当用户不再看到界面了执行以下操作
        playInfoSubject.removeObserver(this);//将自己从主题里面移除出去
        if(remindUiUpdateThread !=null) {
            remindUiUpdateThread.run = false;
            remindUiUpdateThread = null;
        }
//        Log.i("test", "stop");
    }

    //    找到控件以及设置相关的监听器
    private void viewInit() {
//        找到布局文件里的控件
        linearLayout = (LinearLayout)viewLayout.findViewById(R.id.ll_fragment_bottom);
        singerPicture = (ImageView) viewLayout.findViewById(R.id.iv_fragment_bottom_singer_picture);
        progressBar = (ProgressBar)viewLayout.findViewById(R.id.pb_fragment_bottom_control);
        songNameTv = (TextView) viewLayout.findViewById(R.id.tv_fragment_bottom_control_songName);
        songArtistTv = (TextView) viewLayout.findViewById(R.id.tv_fragment_bottom_control_artistName);
        playButton = (ImageButton) viewLayout.findViewById(R.id.ib_fragment_bottom_control_play);
        nextButton = (ImageButton) viewLayout.findViewById(R.id.ib_fragment_bottom_control_next);
        menuButton = (ImageButton) viewLayout.findViewById(R.id.ib_fragment_bottom_control_menu);
//        设置监听以及数据
        viewLayout.findViewById(R.id.ll_fragment_bottom).setOnClickListener(this);
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        menuButton.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
    }

    private void handlerInits(){
        updateProgressHandler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                if(msg.what == SendSongPlayProgress.updateProgress){
                    progressBar.setProgress(playInfoSubject.getCurrentPosition());
                }
                super.handleMessage(msg);
            }//handleMessage
        };
    }//handlerInits

//    点击事件回调方法
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ll_fragment_bottom://如果点击整个底边的控制栏
                startActivity(new Intent(getActivity(), SongPlayInfoActivity.class));
                break;
            case R.id.ib_fragment_bottom_control_play://如果点击播放或者暂停按钮
                if(playController.isPlaying()) playController.pause();
                else playController.play();
                break;
            case R.id.ib_fragment_bottom_control_next://如果点击下一曲的按钮
                playController.nextSong();
                break;
            default:break;
        }
    }

    //    覆盖作为观察者的方法
    @Override
    public void updatePlayInfo(final SongInfo currentPlaySongInfo, final boolean isPlaying, final int progress) {
//        如果没有任何歌曲选中直接返回
        if (currentPlaySongInfo==null) {
            return;
        }
//        如果原来没有播放任何歌曲
//        或者原来所播放的歌曲和现在的不同
        if(lastSongInfo==null||!lastSongInfo.getSongAbsolutePath().equals(currentPlaySongInfo.getSongAbsolutePath())){
            setNewPlayInfo(currentPlaySongInfo);
        }
        if(isPlaying) {
            playSettings();
        }
        else {
            pauseSettings();
        }//if(isPlaying)
        progressBar.setProgress(progress);//设置当前播放进度
        lastSongInfo = currentPlaySongInfo;//更新当前播放的那首歌
    }

//    更新歌曲名字歌手名字和进度条的最大值
    private void setNewPlayInfo(SongInfo currentSongInfo){
        songNameTv.setText(currentSongInfo.getSongName());
        songArtistTv.setText(currentSongInfo.getSingerName());
        progressBar.setMax(currentSongInfo.getSongDuration());
    }

//    如果歌曲正在播放调此方法
    private void playSettings(){
        playButton.setImageResource(R.drawable.fragment_bottom_pause);
//                开启一个新的线程刷新的进度条
        if(remindUiUpdateThread ==null){
            remindUiUpdateThread = new RemindUiUpdateThread(updateProgressHandler,100);
            remindUiUpdateThread.start();
        }
    }

//    如果歌曲没有播放调此方法
    private void pauseSettings(){
        playButton.setImageResource(R.drawable.fragment_bottom_play);
        if(remindUiUpdateThread !=null){
            remindUiUpdateThread.run = false;
            remindUiUpdateThread = null;
        }
    }
}

