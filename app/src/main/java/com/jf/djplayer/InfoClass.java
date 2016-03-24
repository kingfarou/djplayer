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

    //这个类保存的是与数据库相关的信息
    public static final class DatabaseInfo{
        //这是我的数据库的路径
//        public static final File DATABASE_FILE_DIR = FileInfo.DJ_FILE_DIR;
        public static final File DATABASE_FILE_DIR = new File(FileInfo.DATABASE_FILE);
        //除了"ABSOLUTE_PATH","collection"以外
        //其余列名与数据库列名相同
        public static final String TABLE_NAME="JF_SONG_INFO";//表名
        public static final String _ID = "_ID";//主键
        public static final String TITLE = "title";//歌名
        public static final String ARTIST = "artist";//歌手
        public static final String ALBUM = "album";//专辑
        public static final String DURATION = "duration";//时长
        public static final String SIZE = "_size";//大小
        public static final String ABSOLUTE_PATH = "absolutePath";//音频文件绝对路径
        public static final String COLLECTION = "collection";//标记用户是否收藏音乐
    }

    //这个类保存的是与用户设置相关信息
    public static final class Options {

        //播放模式所对应的中文
        public static final String[] PLAY_MODE = new String[]{"单曲循环","顺序播放","随机播放","列表循环"};

    }

//    记录所有自己软件所用到的action常量
    public static final class ActionString{
//        任何对数据库的操作发送他
        public static final String UPDATE_SONG_FILE_INFO_ACTION = "com.danjuan.www.djplayer.ActionString.updateDatabase";
//        任何歌曲状态的改变发送他
        public static final String SONG_STATUS_CHANGE = "com.danjuan.www.djplayer.ActionString.songStatusChange";
    }

    public static final class CategoryString{
        public static final String SONG_STATUS_PLAY="com.danjuan.www.djplayer.categoryString.play";
        public static final String SONG_STATUS_PAUSE="com.danjuan.www.djplayer.categoryString.pause";
        public static final String DELETE_SONG_FILE_CATEGORY = "com.danjuan.www.djplayer.categoryString.deleteSongFile";
        public static final String UPDATE_COLLECTION_CATEGORY = "com.danjuan.www.djplayer.categoryString.updateCollection";
        public static final String UPDATE_SONG_FILE_INFO_CATEGORY = "com.danjuan.www.djplayer.categoryString.updateSongFileInfo";
    }
}

