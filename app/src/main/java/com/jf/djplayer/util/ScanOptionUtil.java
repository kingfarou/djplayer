package com.jf.djplayer.util;

import android.content.Context;

import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.bean.ScanOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by Administrator on 2017/10/26.
 * 扫描选项读写工具，提供方法读取或写入用户选择的扫描条件。
 * 通过文件保存序列化对象实现扫描选项的持久化存储
 */

public class ScanOptionUtil {

    private ScanOptions scanOptions;
    private static final String SCAN_OPTION_FILE_NAME = "scanOption"; // 保存歌曲扫描选项的文件名
    private static final int DEFAULT_DURATION = 60*1000;              // 默认时长，60秒
    private static final int DEFAULT_SIZE = 512;                      // 默认尺寸，512kb

    public ScanOptionUtil(){
        // ScanOptionUtil对象一旦被创建，即读取用户已存储的扫描选项，
        // 如果之前还没有存储过用户扫描选项，则创建一个ScanOptions对象，并用默认值初始化
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try{
            fis = MyApplication.getContext().openFileInput(SCAN_OPTION_FILE_NAME);
            try{
                ois = new ObjectInputStream(fis);
                try{
                    scanOptions = (ScanOptions)ois.readObject();
                }catch (ClassNotFoundException e){
                    LogUtil.i(getClass().getSimpleName() + "构造方法异常--信息："+e.getMessage());
                }finally {
                    try{ois.close();}catch (IOException e){}
                }
            }catch (IOException e){
                LogUtil.i(getClass().getSimpleName() + "构造方法异常--信息："+e.getMessage());
            }finally {
                try{fis.close();}catch (IOException e){}
            }
        }catch (FileNotFoundException e) {
            LogUtil.i(getClass().getSimpleName() + "构造方法异常--信息：" + e.getMessage());
        }finally {
            // 如果在文件读取过程中任意过程失败了，则scanOptions为空，
            // 此时创建一个ScanOptions对象兵传入默认参数
            if(scanOptions == null){
                scanOptions = new ScanOptions();
                scanOptions.setDuration(DEFAULT_DURATION);
                scanOptions.setSize(DEFAULT_SIZE);
            }
        }
    }

    /**
     * 设置歌曲时长
     * @param duration 时长，以毫秒为单位
     */
    public void setDuration(int duration){
        if(duration < 0){
            throw new IllegalArgumentException(getClass().getSimpleName()+"--setDuration(int)不能输入小于0的参数");
        }
        scanOptions.setDuration(duration);
    }

    /**
     * 获取歌曲时长，在调用了该方法后，需要调用saveAllOptions()方法，所设置的时长才会真的得到持久化存储。
     * @return 时长，以毫秒作为单位，如果用户还没有设置过时长，返回默认时长
     */
    public int getDuration(){
        return scanOptions.getDuration();
    }

    /**
     * 设置歌曲大小，在调用了该方法后，需要调用saveAllOptions()方法，所设置的大小才会真的得到持久化存储。
     * @param size 大小，以kb作为单位
     */
    public void setSize(int size){
        if(size < 0){
            throw new IllegalArgumentException(getClass().getSimpleName()+"--setSize(int)不能输入小于0的参数");
        }
        scanOptions.setSize(size);
    }

    /**
     * 获取歌曲大小
     * @return 大小，已kb作为单位，如果用户还没有设置过大小，则返回默认大小
     */
    public int getSize(){
        return scanOptions.getSize();
    }

    /**
     * 获取路径集合，该功能暂时还未实现，先返回null
     * @return 路径集合
     */
    public List<String> getPathList(){
        return null;
    }

    /**
     * 保存所有扫描选项。
     * 在调用了setDuration()方法和setSize()方法之后，
     * 需要调用该方法，所设置的时长和尺寸才会真的得到持久化存储。
     */
    public void saveAllOptions(){
        FileOutputStream fos = null;
        ObjectOutputStream ous = null;
        try{
            fos = MyApplication.getContext().openFileOutput(SCAN_OPTION_FILE_NAME, Context.MODE_PRIVATE);
            try{
                ous = new ObjectOutputStream(fos);
                try{
                    ous.writeObject(scanOptions);
                }catch (IOException e){
                    LogUtil.i(getClass().getSimpleName()+"saveAllOptions()异常--信息：" + e.getMessage());
                }finally {
                    try{ous.close();}catch (IOException e){}
                }
            }catch (IOException e){
                LogUtil.i(getClass().getSimpleName(), "saveAllOptions()-ObjectOutputStream对象创建失败");
            }finally {
                try{fos.close();}catch (IOException e){}
            } // finally
        }catch (FileNotFoundException e){
            LogUtil.i(getClass().getSimpleName() + "saveAllOptions()异常--信息：" + e.getMessage());
        }
    }

}
