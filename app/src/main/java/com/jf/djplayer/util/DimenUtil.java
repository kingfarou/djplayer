package com.jf.djplayer.util;


import android.util.DisplayMetrics;

import com.jf.djplayer.base.MyApplication;

/**
 * Created by JF on 2016/11/8.
 * Tool that used to change px,dp,sp
 */
public final class DimenUtil {

    private static final float SCALE;
    private static final float FONT_SCALE;

    static {
        DisplayMetrics displayMetrics = MyApplication.getContext().getResources().getDisplayMetrics();
        SCALE = displayMetrics.density;
        FONT_SCALE = displayMetrics.scaledDensity;
    }

    /**
     * 将dp转变成px
     * @param dpValue 想要转变的dp
     * @return 转变后的px值
     */
    public static float dpToPx(float dpValue){
        return dpValue * SCALE + 0.5f;
    }

    /**
     * 将px转变成dp
     * @param pxValue 想要转变的px
     * @return 转变后的dp值
     */
    public static float pxToDp(float pxValue){
        return (int) (pxValue / SCALE + 0.5f);
    }

    /**
     * 将sp转变成px
     * @param spValue 想要转变的sp
     * @return 转变后的px值
     */
    public static float spToPx(float spValue){
        return spValue * FONT_SCALE + 0.5f;
    }

    /**
     * 将px转变成sp
     * @param pxValue 想要转变的px
     * @return 转变后的sp值
     */
    public static float pxToSp(float pxValue){
        return pxValue / FONT_SCALE + 0.5f;
    }
}
