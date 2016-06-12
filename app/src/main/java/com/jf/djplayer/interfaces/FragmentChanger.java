package com.jf.djplayer.interfaces;

import android.support.v4.app.Fragment;
/**
 * Created by JF on 2016/1/29.
 * "Fragment"通过这个接口控制活动更替Fragment
 */
public interface FragmentChanger {

    /**
     * 启动新的Fragment更替当前这个
     * @param fragment 新的Fragment实例
     */
    public void replaceFragments(Fragment fragment);

    /**
     * 将当前"Fragment"出栈
     */
    public void popFragments();
}
