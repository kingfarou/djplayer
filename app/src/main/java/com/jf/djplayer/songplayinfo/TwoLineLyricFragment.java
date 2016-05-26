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
import com.jf.djplayer.tool.RemindUiUpdateThread;
import com.jf.djplayer.tool.SendSongPlayProgress;
import com.jf.djplayer.playertool.LyricTool;
import com.jf.djplayer.playertool.PlayerOperator;

import java.io.File;

/**
 * Created by JF on 2016/2/6.
 * 播放信息-两行歌词显示界面
 */
public class TwoLineLyricFragment extends Fragment implements PlayInfoObserver{

    private View layoutView;//当前布局的根视图
//    private TextView singerNameTv;//这个表示歌手名字
    private TextView topLineTv;//这个现实第一行的歌词
    private SongInfo lastSongInfo;
    private LyricTool lyricTool;//只是读取歌词用的工具
    private TextView bottomLineTv;//这个显示第二行的歌词
    private PlayInfoSubject playInfoSubject;//这是歌曲信息主题
    private RemindUiUpdateThread remindUiUpdateThread;
    private Handler updateUIHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_two_line_lyric, container, false);
//        获取所需要的主体对象
        playInfoSubject = PlayerOperator.getInstance();
        viewInit();//对视图进行初始化
        handlerInits();//对Handler做初始化

        return layoutView;
    }


    @Override
    public void onStart() {
        super.onStart();
//        将自己注册成为观察者
        playInfoSubject.registerObserver(this);
    }

//    一旦用户不再看到界面将观察者从主题里移除
    @Override
    public void onStop(){
        super.onStop();
        playInfoSubject.removeObserver(this);
//        由于不需要再跟新UI关子线程
        if(remindUiUpdateThread !=null){
            remindUiUpdateThread.run = false;
            remindUiUpdateThread = null;
        }
    }


//    view初始化的
    private void viewInit(){
//        singerNameTv = (TextView)layoutView.findViewById(R.id.tv_fragment_two_line_lyric_singerName);
        topLineTv = (TextView)layoutView.findViewById(R.id.tv_fragment_two_line_lyric_topLine);
        bottomLineTv = (TextView)layoutView.findViewById(R.id.tv_fragment_two_line_lyric_bottomLine);
//        初始化时界面所显示的文字
        topLineTv.setText(getResources().getString(R.string.good_tone));
        topLineTv.setGravity(Gravity.CENTER_HORIZONTAL);
        bottomLineTv.setText(getResources().getString(R.string.app_name));
        bottomLineTv.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    private void handlerInits(){
        updateUIHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
//                如果这是子线程发来的更新进度提示
                if(msg.what == SendSongPlayProgress.updateProgress){
//                    更新当前进度所对应的歌词
                    showTwoLyricLine(playInfoSubject.getCurrentPosition());
                }
//
                super.handleMessage(msg);
            }
        };
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

//    覆盖观察者的方法
//    对当前的歌曲信息进行更新
    @Override
    public void updatePlayInfo(SongInfo currentPlaySongInfo, boolean isPlaying, int progress) {
//        如果当前没有任何歌曲被选中了
        if(currentPlaySongInfo==null){
            return;
        }

//        满足以下条件表示需要更新歌曲信息
        if(lastSongInfo==null||!lastSongInfo.getSongAbsolutePath().equals(currentPlaySongInfo.getSongAbsolutePath())) {
            setNewSongInfo(currentPlaySongInfo);
            lastSongInfo = currentPlaySongInfo;//保存新播放的歌曲信息
        }//if
//        根据播放状态不同设置不同
        if (isPlaying) {
//            如果歌词读取工具已初始化且有歌词，而且异步任务还未启动
            if(lyricTool!=null&&lyricTool.hasSongLyric()&& remindUiUpdateThread == null) {
                remindUiUpdateThread = new RemindUiUpdateThread(updateUIHandler, 100);
                remindUiUpdateThread.start();
            }
        } else {
//            如果线程曾启动过设置标志让其停止
            if (remindUiUpdateThread != null) {
                remindUiUpdateThread.run = false;
                remindUiUpdateThread = null;
            }
//            bottomLineTv.setText("暂停");
        }//if(isPlaying)
        showTwoLyricLine(progress);

    }

    private void setNewSongInfo(SongInfo theNewSongInfo){
        //            这里更新歌手名字
//        singerNameTv.setText(theNewSongInfo.getSingerName());
//            如果外存可读的话读取歌词
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//            根据音乐文件读取歌词：文件名字.mp3
            String songFileName = new File(theNewSongInfo.getSongAbsolutePath()).getName();
            lyricTool = new LyricTool(songFileName);
//                如果当前应用没有存有歌词直接返回
            if(!lyricTool.hasSongLyric()) {
//                Log.i("test","没有歌词");
                return;
            }
            lyricTool.loadLyric();//调用方法载入歌词
            topLineTv.setGravity(Gravity.LEFT);//一行歌词将显示在左边
            bottomLineTv.setGravity(Gravity.RIGHT);//一行歌词将显示在右边
        }//如果外存可读的话读取歌词
    }
}
