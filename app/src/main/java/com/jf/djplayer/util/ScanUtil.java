package com.jf.djplayer.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.MergeCursor;
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
     * 扫描歌曲，根据用户所选择的扫描条件，扫描内部存储和外部存储符合条件的歌曲
     * @return 内部存储和外部存储符合条件的歌曲的结果集
     */
    public Cursor scanSong(){
        String scanSentence = getScanSelection();
        Cursor[] cursors = new Cursor[2];
        // 查询内部存储
        ContentResolver contentResolver = MyApplication.getContext().getContentResolver();
        cursors[0] = contentResolver.query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA},
                scanSentence, null, null);
        // 查询外部存储
        cursors[1] = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA},
                scanSentence, null, null);
        return new MergeCursor(cursors);
    }

    // 根据用户所设置的过滤条件，生成"SQLite"条件查询用的语句
    private String getScanSelection(){
        // 获取所设置的过滤条件
        ScanOptionUtil scanOptionUtil = new ScanOptionUtil();

        // 装填每个过滤条件所对应的查询语句
        List<String> filterSentence = new ArrayList<>();

        // 根据路径获取扫描路径过滤语句
        List<String> path = scanOptionUtil.getPathList();
        if(path != null && path.size() != 0){
            filterSentence.add(getPathQuerySentence(path));
        }

        // 根据时间获取歌曲时长过滤语句
        int duration = scanOptionUtil.getDuration();
        filterSentence.add(getDurationQuerySentence(duration));

        // 根据大小获取歌曲文件大小过滤语句
        int songSize = scanOptionUtil.getSize();
        filterSentence.add(getSizeQuerySentence(songSize));

        // 将各语句拼接成最终的语句
        StringBuilder stringBuilder = new StringBuilder(filterSentence.size()*2);//每个语句"append()"两次
        int size = filterSentence.size();
        for(int i = 0; i<size-1; i++){
            stringBuilder.append(filterSentence.get(i)).append(" and ");
        }
        stringBuilder.append(filterSentence.get(size-1));
        return stringBuilder.toString();
    }

    // 根据所传入的路径集合，生成路径过滤语句
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

    // 根据所传入的文件尺寸，生成尺寸过滤语句
    private String getSizeQuerySentence(int size){
        return "("+MediaStore.Audio.Media.SIZE+">="+size+")";
    }

    // 根据所传入的时长，生成时长过滤语句
    private String getDurationQuerySentence(int duration){
        return "("+MediaStore.Audio.Media.DURATION+">="+duration+")";
    }

}
