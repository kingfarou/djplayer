package com.jf.djplayer.tool.playertool;

import android.os.Environment;
import android.util.Log;

import com.jf.djplayer.LyricLine;
import com.jf.djplayer.tool.FileTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JF on 2016/2/6.
 * 专门用于读取歌词信息的类
 */
public class LyricTool {

    private String lyricFileNames;//这是歌词文件名字
    private int currentPosition;//表示当前歌词文件在第几句歌词
    private List<LyricLine> lyricLineList;//装填歌词信息用的集合
    private File lyricFile;

    /**
     * 每个歌词操作工具对象都必须要指定歌曲文件名字
     * @param songFileName 你想要取得歌词的歌曲文件名字（带扩展名：歌曲名字.mp3）
     */
    public LyricTool(String songFileName){
//        由于歌曲文件名字总是：文件名.mp3
//        所以需要进行截取，变成：文件名.lrc
        String lyricFileStrings = songFileName.substring(0,songFileName.length()-4);
        this.lyricFileNames = lyricFileStrings+".lrc";

    }

    /**
     * 获取当前歌曲是否有对应的歌词文件
     * @return true:有对应的歌词文件，false：没有对应歌词文件或者无法读取歌词文件
     */
    public boolean hasSongLyric(){
//        链接对应歌词文件
//        标记是否存在歌词文件
        if(lyricFile==null){
            lyricFile = new File(Environment.getExternalStorageDirectory(), FileTool.LYRIC_DIR +"/"+ lyricFileNames);
//            Log.i("test",lyricFile.getAbsolutePath());
        }
        return lyricFile.exists();
    }

    /**
     * 获取歌曲文件所对应的歌词信息
     * @return 装有歌词信息的List的集合，如果没有对应歌词则返回空
     */
    public List<LyricLine> getLyricLine(){
        return lyricLineList;
    }

    /**
     * 将文件的歌词载入到集合里
     * 如果不调用该方法
     * 所有获取歌词的方法都将返回空
     */
    public void loadLyric(){
//        如果没有歌词文件直接返回
        if(!hasSongLyric()) {
            return;
        }
//        Log.i("test","开始读取");
        String startContent = "\\[[0-9]{2}:[0-9]{2}.[0-9]{2}\\][\\s\\S]*";//读到这个正则表达式所表达的内容开始才有用
        String lyricContent;//代表歌词文件里的每行内容
        String[] lyricStrings;//装填一句歌词信息的字符串数组
        lyricLineList = new ArrayList<>(40);//初始化装填歌词的集合
        BufferedReader lyricReaders = null;//读取歌词文件的流对象

//        歌词文件读取的过程是
//        每次读取一行歌词信息
//        将一行歌词分开成两段
//        调用函数将第一段解析变成毫秒
//        最后将第一段和第二段存到对象以及集合里面
        try{
            lyricReaders = new BufferedReader(new InputStreamReader(new FileInputStream(lyricFile),"utf-8"));
    //            读到歌词开始时间为止
            while( (lyricContent = lyricReaders.readLine())!=null){
                if(lyricContent.matches(startContent)){
                    lyricStrings = splitLyricString(lyricContent);//将读到的歌词分成两段
                    lyricLineList.add(new LyricLine(parseTime(lyricStrings[0]), lyricStrings[1]));
                    break;
                }
            }//while
    //            从开始时间一直读直到整个文件读完
            while( (lyricContent = lyricReaders.readLine())!=null){
                lyricStrings = splitLyricString(lyricContent);
                lyricLineList.add(new LyricLine(parseTime(lyricStrings[0]), lyricStrings[1]));
            }
        }catch (FileNotFoundException e){
            Log.i("test","位置--"+LyricTool.class+"--未能成功找到歌词文件");
            return;
        } catch (IOException e){
            Log.i("test","位置--"+LyricTool.class+"io错误");
        }
    //        finally语句用来关流的
        finally {
            if(lyricReaders!=null){
                try{
                    lyricReaders.close();
                }catch (IOException e){
                    Log.i("test","关流失败--异常位置--"+LyricTool.class);
                }
            }
        }//finally
        currentPosition = lyricLineList.size()/2;//将当前的位置定到中间平衡外界对歌词的查找效率
    }

//    将一行歌词文件如：[分钟:秒钟.毫秒]歌词内容
//    分割成时间和内容的字符串
    private String[] splitLyricString(String lyricLine){
        String[] lyricLineString = new String[]{"",""};
        lyricLineString[0] = lyricLine.substring(1,9);//表示时间的字符串（毫秒）
        lyricLineString[1] = lyricLine.substring(10);//这个表示歌词内容
        return lyricLineString;
    }

    //    将指定的时间格式："分钟:秒钟.毫秒"转换成对应的毫秒（所传入的字符串内无双引号）
    private int parseTime(String time){
        int asc2 = 48;//数字字符所对应的Asc2编码差值
        char[] charTime = time.toCharArray();
        int minutes = (charTime[0]-asc2)*10+(charTime[1]-asc2);
        int second = (charTime[3]-asc2)*10+(charTime[4]-asc2);
        int milliSeconds = (charTime[6]-asc2)*100+(charTime[7]-asc2)*10;
        return minutes*60*1000+second*1000+milliSeconds;
    }

//    根据所传入的时间设定当前歌词在集合里是第几句
    private void setCurrentPosition(int msec){
//        如果没有歌词文件或者传入时间不对
        if(lyricLineList==null||msec<0){
            return;
        }
//            倘若所传入的时间在当前位置所对应歌词时间后边
        if(msec>=lyricLineList.get(currentPosition).getStartTime()){
            while( (currentPosition+1<lyricLineList.size()) && (lyricLineList.get(currentPosition+1).getStartTime()<=msec)){
                currentPosition = currentPosition+1;
            }
        }else{
            while( (currentPosition>0) && (lyricLineList.get(currentPosition).getStartTime()>msec)){
                currentPosition = currentPosition-1;
            }
        }
    }
    /**
     * 获取当前时间所对应的歌词对象
     * @param msec 毫秒时间
     * @return 封装有当前时间下歌词信息的对象，如果所传入的时间有误，返回一个没有歌词的歌词行对象
     */
    public LyricLine getLyricLine(int msec){
//        边界
        if(lyricLineList==null||msec<0){
            return new LyricLine(-1,"");//-1表示当前时间没有对应歌词
        }
        setCurrentPosition(msec);
        return lyricLineList.get(currentPosition);
    }

    /**
     * 获取当前时间点的下一句词
     * @param msec 当前时间（毫秒单位）
     * @return 一个歌词行对象，对象封装有当前时间所对应的下一句歌词信息，
     *          如果所传入的时间下面没有歌词，返回一个歌词为空的歌词行对象
     */
    public LyricLine getNextLyricLine(int msec){
//        如果没有对应歌词文件或者传入时间不对
        if(lyricLineList==null||msec<0){
            return new LyricLine(-1,"");//-1表示当前时间没有对应歌词
        }
        setCurrentPosition(msec);
//        如果没有下一句歌词了
        if(currentPosition+1>=lyricLineList.size()){
            return new LyricLine(-1,"");//-1表示当前时间没有对应歌词
        }
        return lyricLineList.get(currentPosition+1);
    }

    /**
     * 获取当前时间点前一句歌词
     * @param msec 想要获取歌词的时间点
     * @return 所传入的时间点前一句歌词，如果没有歌词文件，或者传入错误时间，或者所传入时间点没前一句歌词，返回一个没歌词歌词行对象
     */
    public LyricLine getPreviousLyricLine(int msec){
//        如果没有歌词文件或者传入时间不对
        if(lyricLineList==null||msec<0){
            return new LyricLine(-1,"");
        }
        setCurrentPosition(msec);
//        如果当前时间点没有前一句歌词
        if(currentPosition-1<0){
            return new LyricLine(-1,"");
        }
        return lyricLineList.get(currentPosition-1);
    }

//    获取当前歌词是歌词文件的第几句词
    public int getCurrentPosition(int msec){
        if(lyricLineList==null||msec<0){
            return -1;
        }
        setCurrentPosition(msec);
        return currentPosition;
    }
}
