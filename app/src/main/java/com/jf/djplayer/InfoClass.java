package com.jf.djplayer;


import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2015/7/14.
 * 这个类是用来存放可能用到字符串的常量
 */
public final class InfoClass {
    //该类记录所有路径相关信息
    public static final class FileInfo {
        //这是手机外存的根目录
        public static final String  EXTERNAL_STORAGE_DIR =
                Environment.getExternalStorageDirectory().toString();
        //这是我的数据文件夹得根目录了
        public static final String  DJ_FILE_DIR = EXTERNAL_STORAGE_DIR+"/"+"djplayer/";
        //song目录所在
        public static final String SONG_FILE = DJ_FILE_DIR+"song/";
        //lyric的目录
        public static final String LYRIC_FILE = DJ_FILE_DIR+"lyric/";
        //播放设置文件名字
        public static final String OPTIONS_FILE = DJ_FILE_DIR+"options/";
        //歌手图片文件位置
        public static final String  SINGER_PICTURE_FILE = DJ_FILE_DIR+"singers/";
//        这是数据库文件的位置
        public static final String DATABASE_FILE = DJ_FILE_DIR+"dataBase/";
        //字符串形式表示的保存播放设置的文件的名字
        public static final String playerOptionsFileName = "playerOptions";
    }


    //这个类保存的是与用户设置相关信息
    public static final class Options {

        //播放模式所对应的中文
        public static final String[] PLAY_MODE = new String[]{"单曲循环","顺序播放","随机播放","列表循环"};

    }

//    记录所有自己软件所用到的action常量
    public static final class ActionString{

//        任何歌曲状态的改变发送他
        public static final String SONG_STATUS_CHANGE = "com.danjuan.www.djplayer.ActionString.songStatusChange";
    }

    public static final class CategoryString{
        public static final String SONG_STATUS_PLAY="com.danjuan.www.djplayer.categoryString.play";
        public static final String SONG_STATUS_PAUSE="com.danjuan.www.djplayer.categoryString.pause";
    }
}

