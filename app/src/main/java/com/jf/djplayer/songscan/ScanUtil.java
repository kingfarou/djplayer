package com.jf.djplayer.songscan;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.util.ScanOptionUtil;
import com.jf.djplayer.util.SdCardUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jf on 2016/8/4.
 * 扫描音乐用的工具
 * 该类要做的工作是：
 * >读取用户所设置的扫描过滤选项，根据这些选项得出“SQLite”条件查询的语句
 * >根据这些语句查询系统的媒体库，并将查询的"Cursor"结果集，转成"List"对象，最终返回
 */
public class ScanUtil{

    /**
     * 扫描手机里的音乐，扫描条件由用户在"ScanSettingActivity"界面里面的设置所决定
     * @return 返回根据扫描条件所得到的歌曲信息集合对象，如果没有读到任何数据，则返回null
     */
    public List<Song> scanSong(){
        List<Song> songList = new ArrayList<>();    //待返回的查询结果
        String scanSentence = getScanSelection();   //从数据库获取用户所设置的查询条件

        //扫描并添加内存里的歌
        List<Song> internalSongList = scanSong(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, scanSentence);
        if(internalSongList != null) songList.addAll(internalSongList);

        //如果SD卡可读，扫描并添加SD卡里面的歌
        if(SdCardUtil.isSdCardEnable()){
            List<Song> externalSongList = scanSong(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, scanSentence);
            if(externalSongList != null) songList.addAll(externalSongList);
        }
        if(songList.size()<1) return null;
        return songList;
    }

    //根据用户所设置的过滤条件，生成"SQLite"条件查询用的语句
    private String getScanSelection(){
        // 获取所设置的过滤条件
        ScanOptionUtil scanOptionUtil = new ScanOptionUtil();

        //装填每个过滤条件所对应的查询语句
        List<String> filterSentence = new ArrayList<>();

        //根据路径获取扫描路径过滤语句
        List<String> path = scanOptionUtil.getPathList();
        if(path != null && path.size() != 0){
            filterSentence.add(getPathQuerySentence(path));
        }

        //根据时间获取歌曲时长过滤语句
        int duration = scanOptionUtil.getDuration();
        filterSentence.add(getDurationQuerySentence(duration));

        //根据大小获取歌曲文件大小过滤语句
        int songSize = scanOptionUtil.getSize();
        filterSentence.add(getSizeQuerySentence(songSize));

        //将各语句拼接成最终的语句
        StringBuilder stringBuilder = new StringBuilder(filterSentence.size()*2);//每个语句"append()"两次
        int size = filterSentence.size();
        for(int i = 0; i<size-1; i++){
            stringBuilder.append(filterSentence.get(i)).append(" and ");
        }
        stringBuilder.append(filterSentence.get(size-1));
        return stringBuilder.toString();
    }

    //扫描指定的表下面（其实只有内存表、外存表），指定扫描条件下的歌曲，返回歌曲集合对象
    private List<Song> scanSong(Uri tableName, String scanSentence){
        Cursor cursor = MyApplication.getContext().getContentResolver().query(tableName,
                new String[]{MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA},
                scanSentence, null, null);
        return cursorToSongList(cursor);
    }

    //将读到的"Cursor"歌曲信息数据集合转成"List<Song>"集合
    private List<Song> cursorToSongList(Cursor songInfoCursor){
        if(songInfoCursor == null || songInfoCursor.getCount() == 0){
            return null;
        }
        //获取各个列的索引
        int titleIndex = songInfoCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int artistIndex = songInfoCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int albumIndex = songInfoCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        int durationIndex = songInfoCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int sizeIndex = songInfoCursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
        int dataIndex = songInfoCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        //读取数据
        List<Song> songInfoList = new ArrayList<>(songInfoCursor.getCount());
        Song songInfo;
        while (songInfoCursor.moveToNext()) {
            songInfo = new Song(
                    songInfoCursor.getString(titleIndex),
                    songInfoCursor.getString(artistIndex),
                    songInfoCursor.getString(albumIndex),
                    songInfoCursor.getInt(durationIndex),
                    songInfoCursor.getInt(sizeIndex),
                    songInfoCursor.getString(dataIndex)
            );
            songInfoList.add(songInfo);
        }
        songInfoCursor.close();
        return songInfoList;
    }

    //根据所传入的路径集合，生成路径过滤语句
    private String getPathQuerySentence(List<String> path){
        if(path == null || path.size() == 0){
            return null;
        }
        int size = path.size();
        StringBuilder selection = new StringBuilder(size*4);//每个"path"对象需要拼接四次"append"语句
        String absolutePath = MediaStore.Audio.Media.DATA;
        for(int i = 0; i<size-1; i++){
            selection.append(absolutePath).append(" like ").append(path.get(i)).append("& or ");
        }
        selection.append(absolutePath).append(" like ").append(path.get(size-1)).append("&;");
        return "("+selection.toString()+")";
    }

    //根据所传入的文件尺寸，生成尺寸过滤语句
    private String getSizeQuerySentence(int size){
        return "("+MediaStore.Audio.Media.SIZE+">="+size+")";
    }

    //根据所传入的时长，生成时长过滤语句
    private String getDurationQuerySentence(int duration){
        return "("+MediaStore.Audio.Media.DURATION+">="+duration+")";
    }

}
