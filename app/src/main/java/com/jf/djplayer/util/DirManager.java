package com.jf.djplayer.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by Kingfar on 2016/2/4.
 * 路径管理器，负责管理应用里的各个文件夹
 */
public class DirManager {

    // 应用存储文件的根目录（相对路径），该目录在SD卡目录下
    private static final String APP_ROOT_DIR = "djPlayer";//这是应用存储文件的根目录（相对路径）
    // 存放歌曲文件的文件夹
    private static final String SONG_DIR = APP_ROOT_DIR+"/song";
    // 存储歌手信息的文件夹
    private static final String SINGER_DIR = APP_ROOT_DIR+"/artist";
    // 存放歌词文件的文件夹
    private static final String LYRIC_DIR = APP_ROOT_DIR+"/lyric";
    // 存放歌手图片的路径
    private static final String SINGER_PICTURE_DIR = APP_ROOT_DIR+"/artistPicture";
    // sd卡路径
    private File sdCardDir;

    /** 创建FileUtil对象之前，请确保SD卡可用*/
    public DirManager(){
        sdCardDir = Environment.getExternalStorageDirectory();
    }

    /** 创建App所需的所有路径，调用该方法前请确认SD卡可读*/
    public void initAppDir(){
        createAppRootDir();
        createSongDir();
        createSingerDir();
        createLyricDir();
        createSingerPictureDir();
    }

    /**
     * 获取存放歌曲文件的路径
     * @return 存放歌曲文件的路径
     */
    public File getSongDir(){
        return getDir(SONG_DIR);
    }

    /**
     * 获取存放歌手文件的路径
     * @return 存放歌手文件的路径
     */
    public File getSingerDir(){
        return getDir(SINGER_DIR);
    }

    /**
     * 获取存放歌词文件的路径
     * @return 存放歌词文件的路径
     */
    public File getLyricDir(){
        return getDir(LYRIC_DIR);
    }

    /**
     * 获取存放歌手图片文件的路径
     * @return 存放歌手图片文件的路径
     */
    public File getSingerPictureDir(){
        return getDir(SINGER_PICTURE_DIR);
    }

    // 根据路径名字获取路径
    private File getDir(String dirName){
        File dir = new File(sdCardDir, dirName);
        if(dir.exists()) {
            return dir;
        } else {
            dir.mkdir();
            return dir;
        }
    }

    // 创建APP根路径，调用该方法前请确认SD卡可读
    private void createAppRootDir(){
        new File(sdCardDir,APP_ROOT_DIR).mkdir();
    }

    // 创建存放歌曲文件的路径，调用该方法前请确认SD卡可读
    private void createSongDir(){
        new File(sdCardDir, SONG_DIR).mkdir();
    }
    
    // 创建存放歌手信息文件的路径，调用该方法前请确认SD卡可读
    private void createSingerDir(){
        new File(sdCardDir, SINGER_DIR).mkdir();
    }
    
    // 创建存放歌词文件的路径，调用该方法前请确认SD卡可读
    private void createLyricDir(){
        new File(sdCardDir, LYRIC_DIR).mkdir();
    }
    
    // 创建存放歌手图片文件的路径，调用该方法前请确认SD卡可读
    private void createSingerPictureDir(){
        new File(sdCardDir, SINGER_PICTURE_DIR).mkdir();
    }
    
}
