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

    /** 应用存储文件的根目录（相对路径），该目录在SD卡目录下*/
    public static final String APP_ROOT_DIR = "djPlayer";//这是应用存储文件的根目录（相对路径）

    /** 存放歌曲文件的文件夹*/
    public static final String SONG = APP_ROOT_DIR+"/song";

    /** 存储歌手信息的文件夹*/
    public static final String SINGER_INFO_DIR = APP_ROOT_DIR+"/artist";

    /** 存放歌词文件的文件夹*/
    public static final String LYRIC_DIR = APP_ROOT_DIR+"/lyric";

    /** 这是存放歌手图片路径*/
    public static final String SINGER_PICTURE_DIR = APP_ROOT_DIR+"/artistPicture";

    /**
     * 创建App所需的所有路径
     * @return 路径已存在（包括创建成功或者文件原来就已存在）：true
     *          外部存储卡不可读（路径可能已存在可能不存在，未知）：false
     */
    public boolean initAppDir(){
        if(!SdCardUtil.isSdCardEnable()) {
            LogUtil.i(getClass().getName()+"--SD卡不可读");
            return false;
        }
        File sdCardDir = Environment.getExternalStorageDirectory();
        File appRootDir = new File(sdCardDir,APP_ROOT_DIR);
        if(!appRootDir.exists()){
            appRootDir.mkdir();
        }
        String[] dirStrings = new String[]{SONG, SINGER_INFO_DIR, LYRIC_DIR, SINGER_PICTURE_DIR};//要创建的所有目录合集
        File dir;
        for(String dirString:dirStrings){
            dir = new File(sdCardDir,dirString);
            if(!dir.exists()) {
                dir.mkdir();
            }
        }
        return true;
    }

    /**
     * 获取存放歌词文件的路径
     * @return 获取成功：返回表示歌词文件路径的File对象，获取失败：null
     */
    public File getLyricDir(){
        if(!SdCardUtil.isSdCardEnable()) {
            LogUtil.i(getClass().getName()+"--SD卡不可读");
            return null;
        }
        File sdCardDir = Environment.getExternalStorageDirectory();
        File lyricDir = new File(sdCardDir,LYRIC_DIR);
        if(!lyricDir.exists()){
            lyricDir.mkdirs();
        }
        return lyricDir;
    }
}
