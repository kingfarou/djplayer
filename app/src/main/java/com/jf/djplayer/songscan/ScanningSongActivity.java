package com.jf.djplayer.songscan;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.jf.djplayer.base.baseactivity.BaseActivity;
import com.jf.djplayer.module.Song;
import com.jf.djplayer.R;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.database.SystemMediaDatabaseUtils;


import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2015/8/17.
 * 音乐正在扫描中的窗体
 * 功能：
 * 逻辑方面制作一件事情：根据所获得的路径扫描音乐，如果得到路径为空，扫描所有路径下的音乐
 * 显示方面也做一件事情：将所扫描到的歌曲路径不断更新，最后显示所扫描到的歌曲数
 */
public class ScanningSongActivity extends BaseActivity {

    private List<File> fileList;
    private ImageView scanFinishIv = null;//这是扫描完成图标
    private ImageView scanningIv = null;//不断变更提示信息
    private List<String> pathList;//记录要扫描的文件路径
    private TextView scanInfoTv = null;//这个用来显示扫描信息
    private ProgressBar progressBar = null;
    private Button scanFinishButton = null;//这是扫描完成按钮
    private String currentAbsolute = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_scanning_song;
    }

    @Override
    protected void initBeforeView() {
    }

    @Override
    protected void initView() {
        scanningIv = (ImageView) findViewById(R.id.iv_activity_scanning_song_scanning);
        scanFinishIv = (ImageView) findViewById(R.id.iv_activity_scanning_song_scanFinish);
        scanInfoTv = (TextView) findViewById(R.id.tv_activity_scanning_song_scanInfo);
        progressBar = (ProgressBar)findViewById(R.id.pb_activity_scanning_song);
        scanFinishButton = (Button) findViewById(R.id.btn_activity_scanning_song_scanFinish);
        scanFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//点击扫描完成按钮即可结束窗体
            }
        });

//        根据用户所选择的路径扫描音乐文件
        pathList = getIntent().getStringArrayListExtra("scanFileList");//获取用户所选择的扫描路径
//        如果用户没有选择任何路径
        if (pathList==null) {
            new ScanSongAsyncTask().execute(null,null);//读取系统所有路径下的文件
        }
        else {
            new ScanSongAsyncTask().execute((String[]) (pathList.toArray()));//读取用户指定路径下的文件
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //如果用户在任务完成前退出
        //这里需要结束后台任务
    }


    private class ScanSongAsyncTask extends AsyncTask<String,String,Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            scanFinishIv.setVisibility(View.GONE);
            scanFinishButton.setVisibility(View.GONE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            String[] scanPath = null;
//            params[0]!=null说明用户对路径有选择，否则的话表示搜索所有路径
            if (params[0]!=null) {
                scanPath = params;
            }
            //读取系统数据库用的工具类
            SystemMediaDatabaseUtils systemMediaDatabaseUtils = new SystemMediaDatabaseUtils(ScanningSongActivity.this);
            //本地数据库帮助类
            SongInfoOpenHelper songInfoOpenHelper = new SongInfoOpenHelper(ScanningSongActivity.this);
//            根据所传入的路径读取路径下的所有歌曲
            List<Song> songInfoList = systemMediaDatabaseUtils.getSongInfoAccordingPath(scanPath);
//            如果没有任何歌曲直接返回
            if(songInfoList==null){
                return 0;
            }
//            循环遍历读取到的所有歌曲
            int insertSongNumber = 0;
            for(Song songInfo:songInfoList){
                songInfo.setLastPlayTime(0);//刚扫描的歌曲最后一次播放时间为零
                songInfo.setCollection(SongInfoOpenHelper.noCollection);//刚扫描的歌曲默认没有收藏
//                如果歌曲添加成功则要更新显示信息来的
                if(songInfoOpenHelper.insertInLocalMusicTable(songInfo)!=-1){
                    insertSongNumber++;//记录一共添加了几首歌
                    publishProgress(songInfo.getFileAbsolutePath());//显示所扫描的歌曲路径
//                    延迟一下才能看到扫描过程
                    try {
                        Thread.sleep(100L);
                    } catch (Exception e){

                    }
                }//if
            }//for
//            如果有添加新歌曲修改返回标记
            if(insertSongNumber!=0){
                setResult(RESULT_OK);
            }
            return insertSongNumber;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            scanInfoTv.setText(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            scanFinishIv.setVisibility(View.VISIBLE);
            scanInfoTv.setText("已扫描到"+integer+"首歌曲");
            progressBar.setVisibility(View.GONE);
            scanFinishButton.setVisibility(View.VISIBLE);
            scanningIv.setVisibility(View.GONE);
        }
    }
}