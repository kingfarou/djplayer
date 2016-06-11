package com.jf.djplayer.playertool;

import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.jf.djplayer.util.FileUtil;

import java.io.File;

/**
 * Created by JF on 2016/2/26.
 * 获取歌手图片所用的工具类
 */
public class SingerPictureTools {

    private String singerName;//这个表示歌手名字
    private File singerPictureFile;//这是歌手图片文件

    /**
     * 传入歌手名字构造歌手图片读取工具
     * @param singerName 歌手名字
     */
    public SingerPictureTools(String singerName){
        this.singerName = singerName;
    }

    /**
     * 是否有可现实的歌手的图片
     * 在调用该方法之前请先确保外存可读
     * @return true:有歌手图，false:没有图片
     */
    public boolean hasSingerPicture(){
        File singerPictureDir = new File(Environment.getExternalStorageDirectory(), FileUtil.SINGER_PICTURE_DIR);
        singerPictureFile = new File(singerPictureDir,singerName+".jpg");
//        Log.i("test",singerPictureFile.getAbsolutePath());
        return singerPictureFile.exists();
    }

    /**
     * 获取当前歌手图片
     * @return 当前歌手图片的Drawable，如果没有图片的话，则返回空
     */
    public Drawable getSingerPicture(){
        if(!hasSingerPicture()) {
            return null;
        }
        return Drawable.createFromPath(singerPictureFile.getAbsolutePath());

    }
}
