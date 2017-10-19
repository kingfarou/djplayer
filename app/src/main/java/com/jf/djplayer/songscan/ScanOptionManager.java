package com.jf.djplayer.songscan;

import com.jf.djplayer.util.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Administrator on 2017/1/3.
 * 扫描选项管理器，
 * 提供存储、获取扫描选项的方法
 */

public class ScanOptionManager {

    private File scanOptionFile;    // 保存扫描选项的文件

    public ScanOptionManager(){
        // 保存扫描选项的文件的名字
        String scanOptionFileName = "scanOptionFile";
        scanOptionFile = new File("/"+scanOptionFileName);
    }
    /**
     * 获取歌曲扫描选项
     * @return 歌曲扫描选项对象：ScanOption
     */
    public ScanOption getScanOption() {
        if(!scanOptionFile.exists()){
            ScanOption scanOption = getDefaultScanOption();
            saveDefaultScanOption(scanOption);
            return scanOption;
        }else{
            // 读取已存在的扫描选项
            return readScanOption(scanOptionFile);
        }
    }

    public void updateScanOption(ScanOption scanOption){

    }

    // 获取默认扫描选项
    private ScanOption getDefaultScanOption(){
        int defaultDuration = 512;
        int defaultSize = 60000;
        ScanOption scanOption = new ScanOption();
        scanOption.setDuration(defaultDuration);
        scanOption.setSize(defaultSize);
        scanOption.setPathList(null);
        return scanOption;
    }

    // 保存默认扫描选项到本地
    private void saveDefaultScanOption(ScanOption scanOption) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            scanOptionFile.createNewFile();
            fileOutputStream = new FileOutputStream(scanOptionFile);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(scanOption);
        } catch (IOException e) {
            LogUtil.i(getClass().getName() + e.toString());
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    // 什么事情都不用做
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    // 什么事情都不用做
                }
            }
        }
    }

    private ScanOption readScanOption(File scanOptionFile){
        ScanOption scanOption = null;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try{
            fileInputStream = new FileInputStream(scanOptionFile);
            objectInputStream = new ObjectInputStream(fileInputStream);
            scanOption = (ScanOption)objectInputStream.readObject();
        }catch (IOException e){

        }catch (ClassNotFoundException e){

        }finally {
            if(objectInputStream != null){
                try{
                    objectInputStream.close();
                }catch (IOException e){
                    // 什么事情都不用做
                }
            }
            if(fileInputStream != null){
                try{
                    fileInputStream.close();
                }catch (IOException e){
                    // 什么事情都不用做
                }
            }
        }
        return scanOption;
    }
}
