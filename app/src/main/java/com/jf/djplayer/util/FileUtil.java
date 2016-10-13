package com.jf.djplayer.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by JF on 2016/2/4.
 * 该类专门用于管理应用里的
 * 各个文件和文件夹
 */
public class FileUtil {

    /**
     * App所用到的各路径，需要加上根目录的路径作为前缀，方可使用
     * 比如：new File(Environment.getExternalStoryDir(),APP_ROOT_DIR);
     */
    public static final String APP_ROOT_DIR = "djPlayer";//这是应用存储文件的根目录（相对路径）
    public static final String SINGER_INFO_DIR = APP_ROOT_DIR+"/artist";//存储歌手信息的文件夹
    public static final String SONG = APP_ROOT_DIR+"/song";//存放歌曲文件的文件夹
    public static final String LYRIC_DIR = APP_ROOT_DIR+"/lyric";//存放歌词文件的文件夹
    public static final String SINGER_PICTURE_DIR = APP_ROOT_DIR+"/artistPicture";//这是存放歌手图片路径

    private File sdDirFile;

    /**
     * 创建App所需的所有路径
     * @return true:创建成功，false:创建失败,或原路径已经存在
     */
    public boolean initAppDir(){
        if(!SdCardUtil.isSdCardEnable()) {
            LogUtil.i("SD卡不可读，应用路径创建失败");
            return false;
        }
        sdDirFile = Environment.getExternalStorageDirectory();
        File appRootDirFile = new File(sdDirFile,APP_ROOT_DIR);//拼接应用根目录的路径
        if(!appRootDirFile.mkdir()) return false;
        String[] appDirFileStrings = new String[]{SONG, SINGER_INFO_DIR, LYRIC_DIR, SINGER_PICTURE_DIR};//要创建的所有目录合集
        File appDirFile;
        for(String appDirFileString:appDirFileStrings){
            appDirFile = new File(sdDirFile,appDirFileString);
            if(!appDirFile.exists()) {
                if(!appDirFile.mkdir()) return false;
            }
        }
        return true;
    }

}
