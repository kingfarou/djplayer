package com.jf.djplayer.songplayinfo;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.module.SongInfo;
import com.jf.djplayer.interfaces.PlayInfoObserver;
import com.jf.djplayer.interfaces.PlayInfoSubject;
import com.jf.djplayer.module.SongPlayInfo;
import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.playertool.LyricTool;
import com.jf.djplayer.playertool.PlayerOperator;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by JF on 2016/2/6.
 * 播放信息-两行歌词显示界面
 */
public class TwoLineLyricFragment extends Fragment implements PlayInfoObserver{

    private View layoutView;//当前布局的根视图
    private SongInfo lastSongInfo;//保存最新歌曲信息

    private TextView topLineTv;//这是第一行的歌词
    private LyricTool lyricTool;//只是读取歌词用的工具
    private TextView bottomLineTv;//这个显示第二行的歌词

    private PlayInfoSubject playInfoSubject;//这是歌曲信息主题
    private Handler updateUIHandler;//更新UI的"Handler"
    private boolean continueUpdateLyric;//"Handler"通过他来识别是否继续更新歌词
    private final int WHAT_UPDATE_LYRIC = 0x0004;//"Handler"事件标记，表示需要更新歌词
    private final int LYRIC_UPDATE_TIME = 100;//"Handler"间隔多久更新一次歌词

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_two_line_lyric, container, false);
//        获取所需要的主体对象
        playInfoSubject = PlayerOperator.getInstance();
        viewInit();//对视图进行初始化
        initHandler();//对Handler做初始化

        return layoutView;
    }

    @Override
    public void onStart() {
        super.onStart();
        playInfoSubject.registerObserver(this);//界面显示是将自己注册成为观察者
    }

//    一旦用户不再看到界面将观察者从主题里移除
    @Override
    public void onStop(){
        super.onStop();
        playInfoSubject.removeObserver(this);
        continueUpdateLyric = false;
    }


//    view初始化的
    private void viewInit(){
        topLineTv = (TextView)layoutView.findViewById(R.id.tv_fragment_two_line_lyric_topLine);
        bottomLineTv = (TextView)layoutView.findViewById(R.id.tv_fragment_two_line_lyric_bottomLine);
//        初始化时界面所显示的文字
        topLineTv.setText(getResources().getString(R.string.good_tone));
        topLineTv.setGravity(Gravity.CENTER_HORIZONTAL);
        bottomLineTv.setText(getResources().getString(R.string.app_name));
        bottomLineTv.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    private void initHandler(){
        updateUIHandler = new UpdateUiHandler(this);
    }

//    根据时间在界面上显示两行歌词
    private void showTwoLyricLine(int msec){
        int currentPosition = lyricTool.getCurrentPosition(msec);
        if(currentPosition%2==0){
            topLineTv.setText(lyricTool.getLyricLine(msec).getLyricContent());
            bottomLineTv.setText(lyricTool.getNextLyricLine(msec).getLyricContent());
        }else{
            topLineTv.setText(lyricTool.getPreviousLyricLine(msec).getLyricContent());
            bottomLineTv.setText(lyricTool.getLyricLine(msec).getLyricContent());
        }
    }

    @Override
    public void updatePlayInfo(SongPlayInfo songPlayInfo) {
//        如果当前没有任何歌曲被选中了
        if (songPlayInfo == null || songPlayInfo.getSongInfo() == null) {
            return;
        }
        SongInfo songInfo = songPlayInfo.getSongInfo();
        //满足以下条件表示需要更新歌曲信息
        if (lastSongInfo == null || !lastSongInfo.getSongAbsolutePath().equals(songInfo.getSongAbsolutePath())) {
            setNewSongInfo(songInfo);
            lastSongInfo = songInfo;//保存新播放的歌曲信息
        }//if
        //根据播放状态不同设置不同
        if (songPlayInfo.isPlaying()) {
            //如果歌词读取工具已经读到歌词
            if (lyricTool != null && lyricTool.hasSongLyric()) {
                continueUpdateLyric = true;
                updateUIHandler.sendEmptyMessageDelayed(WHAT_UPDATE_LYRIC, LYRIC_UPDATE_TIME);
            }
        } else {
            //让"Handler"停止继续更新歌词
            continueUpdateLyric = true;
        }//if(isPlaying)
        showTwoLyricLine(songPlayInfo.getProgress());

    }

    private void setNewSongInfo(SongInfo theNewSongInfo){
        //如果外存可读的话读取歌词
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//            根据音乐文件读取歌词：文件名字.mp3
            String songFileName = new File(theNewSongInfo.getSongAbsolutePath()).getName();
            lyricTool = new LyricTool(songFileName);
            //如果当前应用没有存有歌词直接返回
            if(!lyricTool.hasSongLyric()){
                MyApplication.showLog("没有对应歌词文件");
                return;
            }
            lyricTool.loadLyric();//调用方法载入歌词
            topLineTv.setGravity(Gravity.LEFT);//一行歌词将显示在左边
            bottomLineTv.setGravity(Gravity.RIGHT);//一行歌词将显示在右边
        }//如果外存可读的话读取歌词
    }

    private static class UpdateUiHandler extends Handler{

        private final WeakReference<TwoLineLyricFragment> weakReference;

        public UpdateUiHandler(TwoLineLyricFragment twoLineLyricFragment){
            weakReference = new WeakReference<>(twoLineLyricFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            TwoLineLyricFragment twoLineLyricFragment = weakReference.get();
            if( twoLineLyricFragment == null || !twoLineLyricFragment.continueUpdateLyric){
                super.handleMessage(msg);
                return;
            }
            if(msg.what == twoLineLyricFragment.WHAT_UPDATE_LYRIC){
                //更新当前进度所对应的歌词
                twoLineLyricFragment.showTwoLyricLine(twoLineLyricFragment.playInfoSubject.getPlayInfo().getProgress());
                //继续发送延迟消息
                sendEmptyMessageDelayed(twoLineLyricFragment.WHAT_UPDATE_LYRIC, twoLineLyricFragment.LYRIC_UPDATE_TIME);
            }
            super.handleMessage(msg);
        }
    }
}
