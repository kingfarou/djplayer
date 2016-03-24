package com.jf.djplayer.tool;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by JF on 2016/2/4.
 * 该类专门用于管理应用里的
 * 各个文件和文件夹
 */
public class FileTool {

    /**
     * 在应用的根目录前加上SD卡的路径，方可使用
     * 比如：new File(Environment.getExternalStoryDir(),APP_ROOT_DIR);
     */
    public static final String APP_ROOT_DIR = "jfPlayer";//这是应用存储文件的根目录（相对路径）
    public static final String SINGER_INFO_DIR = APP_ROOT_DIR+"/artist";//存储歌手信息的文件夹
    public static final String SONG = APP_ROOT_DIR+"/song";//存放歌曲文件的文件夹
    public static final String LYRIC_DIR = APP_ROOT_DIR+"/lyric";//存放歌词文件的文件夹
    public static final String SINGER_PICTURE_DIR = APP_ROOT_DIR+"/artistPicture";//这是存放歌手图片路径

    private File sdDirFile;
    public FileTool(Context context){
        this.sdDirFile = Environment.getExternalStorageDirectory();
    }

    /**
     * 创建应用存储文件的根目录
     * @return true:创建成功，false:创建失败,或原路径已经存在
     */
    public boolean createAppRootDir(){
        File appRootDirFile = new File(sdDirFile,APP_ROOT_DIR);//拼接应用根目录的路径
        return appRootDirFile.mkdir();
    }

    /**
     * 初始化应用所需的各个路径
     * @return true:所有路径创建成功，false:任一路径创建失败
     */
    public boolean appDirInit(){
//        如果可读开始创建各个路径
        String[] appDirFileStrings = new String[]{SONG, SINGER_INFO_DIR, LYRIC_DIR, SINGER_PICTURE_DIR};//要创建的所有目录合集
        File appDirFile;
        for(String appDirFileString:appDirFileStrings){
            appDirFile = new File(sdDirFile,appDirFileString);
            if(!appDirFile.exists()) appDirFile.mkdir();
        }
        return true;
    }

    /**
     * 在应用的根目录下创建指定名字的文件夹
     * @param dirName 要创建的文件夹名
     * @return true:创建成功，false:创建失败，或原路径已经存在，或者外存卡不可读
     */
    public boolean createDirInAppRootDir(String dirName){
//        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//            Toast.makeText(context,"SD卡读取失败，请确定已正确插入",Toast.LENGTH_SHORT).show();
//            return false;
//        }
        File appRootDir = new File(sdDirFile,APP_ROOT_DIR);//获取应用的根目录
        File beCreateFile = new File(appRootDir,dirName);
//        Log.i("test",beCreateFile.getAbsolutePath());
        return beCreateFile.mkdir();
    }

    

}
