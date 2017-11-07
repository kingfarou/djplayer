package com.jf.djplayer.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.fragment.BaseFragment;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.bean.PlayInfo;
import com.jf.djplayer.controller.playinfo.PlayInfoActivity;
import com.jf.djplayer.interfaces.PlayInfoObserver;
import com.jf.djplayer.backgroundplay.PlayerOperator;
import com.jf.djplayer.interfaces.PlayController;
import com.jf.djplayer.interfaces.PlayInfoSubject;
import com.jf.djplayer.util.ToastUtil;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2015/8/22.
 * 播放控制底边栏
 * 该Fragment的宿主Activity需要实现PlayController接口
 */
public class BottomFragment extends BaseFragment implements PlayInfoObserver,View.OnClickListener{

    // view成员变量
    private ImageView singerPicture;        // 这个表示歌手图片
    private TextView songNameTv;            // 歌名
    private TextView songArtistTv;          // 这个表示歌手名字
    private LinearLayout linearLayout;      // 整个界面线性布局
    private ImageButton playButton;         // 播放
    private ImageButton nextButton;         // 下首
    private ImageButton menuButton;         // 菜单
    private ProgressBar progressBar;

    //功能类的成员变量
    private Song lastSongInfo;//这个保存主题上次传进来的歌曲信息
    private PlayController playController;//音乐播放的控制者（其实就是当前活动）
    private PlayInfoSubject playInfoSubject;//所观察的那个主题
    private final UpdateProgressHandler updateProgressHandler = new UpdateProgressHandler(this);

    private final int FLAG_UPDATE_PROGRESS = 0x0004;//"Handler"里更新播放进度标记
    private final int UPDATE_TIME = 200;//"Handler"更新播放进度间隔时间
    private boolean continueUpdateUI;//"Handler"通过这个标记识别是否更新UI

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layoutView = inflater.inflate(R.layout.fragment_bottom, container, false);
        initView(layoutView);
        return layoutView;
    }

    private void initView(View layoutView) {
        //找到布局文件里的控件
        linearLayout = (LinearLayout)layoutView.findViewById(R.id.ll_fragment_bottom);
        singerPicture = (ImageView) layoutView.findViewById(R.id.iv_fragment_bottom_singer_picture);
        progressBar = (ProgressBar)layoutView.findViewById(R.id.pb_fragment_bottom_control);
        songNameTv = (TextView) layoutView.findViewById(R.id.tv_fragment_bottom_control_songName);
        songArtistTv = (TextView) layoutView.findViewById(R.id.tv_fragment_bottom_control_artistName);
        playButton = (ImageButton) layoutView.findViewById(R.id.ib_fragment_bottom_control_play);
        nextButton = (ImageButton) layoutView.findViewById(R.id.ib_fragment_bottom_control_next);
        menuButton = (ImageButton) layoutView.findViewById(R.id.ib_fragment_bottom_control_menu);
        //设置监听以及数据
        layoutView.findViewById(R.id.ll_fragment_bottom).setOnClickListener(this);
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        menuButton.setOnClickListener(this);
        linearLayout.setOnClickListener(this);

        //添加观察者到主题里面，注意这个操作要在控件都初始化以后
        playInfoSubject = PlayerOperator.getInstance();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        playController = (PlayController)activity;
    }

    /*不论Fragment新启动的
    还是从回退栈里面重新启动
    都会回调这个方法*/
    @Override
    public void onStart() {
        super.onStart();
        playInfoSubject.registerObserver(this);
        continueUpdateUI = true;
    }

    @Override
    public void onStop() {
        super.onStop();
//        当用户不再看到界面了执行以下操作
        playInfoSubject.removeObserver(this);//将自己从主题里面移除出去
        continueUpdateUI = false;
    }

    //点击事件回调方法
    @Override
    public void onClick(View v) {
        int id = v.getId();
        //如果点击整个底边的栏
        if(id == R.id.ll_fragment_bottom) {
            startActivity(new Intent(getActivity(), PlayInfoActivity.class));
            return;
        }
        //获取当前歌曲信息
        PlayInfo playInfo = playInfoSubject.getPlayInfo();
        //如果点击播放或者暂停按钮
        if(id == R.id.ib_fragment_bottom_control_play){
            playInfo = playInfoSubject.getPlayInfo();
            if(playInfo == null){
//                MyApplication.showToast((BaseActivity) getActivity(), "还没选中任何一首歌曲，快去本地音乐列表里选取吧");
                ToastUtil.showShortToast(getActivity(), "还没选中任何一首歌曲，快去本地音乐列表里选取吧");
                return;
            }
            if(playInfo.isPlaying()){
                playController.pause();
            }else {
                playController.play();
            }
            return;
        }
        //如果点击下一曲的按钮
        if(id == R.id.ib_fragment_bottom_control_next){
            if(playInfo == null){
//                MyApplication.showToast((BaseActivity) getActivity(), "还没选中任何一首歌曲，快去本地音乐列表里选取吧");
                ToastUtil.showShortToast(getActivity(), "还没选中任何一首歌曲，快去本地音乐列表里选取吧");
                return;
            }
            playController.nextSong();
        }
    }

    /*覆盖作为观察者的方法__开始*/
    @Override
    public void updatePlayInfo(PlayInfo playInfo) {
        // 如果没有任何歌曲选中直接返回
        if (playInfo == null || playInfo.getSongInfo() == null) {
            return;
        }
        //如果原来没有播放任何歌曲，或者原来所播放的歌曲和现在的不同
        if(lastSongInfo ==null || !playInfo.getSongInfo().getFileAbsolutePath().equals(lastSongInfo.getFileAbsolutePath())){
            setNewPlayInfo(playInfo.getSongInfo());
        }
        if(playInfo.isPlaying()) {
            playSettings();
        }
        else {
            pauseSettings();
        }//if(isPlaying)
        progressBar.setProgress(playInfo.getProgress());//设置当前播放进度
        lastSongInfo = playInfo.getSongInfo();//更新当前播放的那首歌
    }

    //    更新歌曲名字歌手名字和进度条的最大值
    private void setNewPlayInfo(Song currentSongInfo){
        songNameTv.setText(currentSongInfo.getSongName());
        songArtistTv.setText(currentSongInfo.getSingerName());
        progressBar.setMax(currentSongInfo.getDuration());
    }

    //当收到了观察者的通知，如果歌曲正在播放，调此方法
    private void playSettings(){
        //设置按钮的图案为暂停图案
        playButton.setImageResource(R.drawable.fragment_bottom_pause);
        //设置发送消息标记 == true
        continueUpdateUI = true;
        //发送一个延迟消息
        updateProgressHandler.sendEmptyMessageDelayed(FLAG_UPDATE_PROGRESS, UPDATE_TIME);
    }

    //当接收到观察者的消息，如果歌曲已经暂停，调此方法
    private void pauseSettings(){
        //设置图案为播放的图案
        playButton.setImageResource(R.drawable.fragment_bottom_play);
        //修改标记让"Handler"停止继续更新消息
        continueUpdateUI = false;
    }

    //用来更新进度条用的内部类
    private static class UpdateProgressHandler extends Handler{
        private final WeakReference<BottomFragment> fragmentWeakReference;

        public UpdateProgressHandler(BottomFragment bottomFragment){
            fragmentWeakReference = new WeakReference<>(bottomFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            BottomFragment bottomFragment = fragmentWeakReference.get();
            //如果界面为空指针，或者界面不需要去更新UI
            if( bottomFragment == null || !bottomFragment.continueUpdateUI ){
                super.handleMessage(msg);
                return;
            }
            //更新当前界面进度
            if(msg.what == 0x0004){
                bottomFragment.progressBar.setProgress(bottomFragment.playInfoSubject.getPlayInfo().getProgress());
                sendEmptyMessageDelayed(bottomFragment.FLAG_UPDATE_PROGRESS, bottomFragment.UPDATE_TIME);
            }
            super.handleMessage(msg);
        }//handleMessage
    }
}

