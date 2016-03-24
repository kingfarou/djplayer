package com.jf.djplayer.Listener;

import android.app.Activity;
import android.support.v4.view.ViewPager;

/**
 * Created by Administrator on 2015/7/7.
 * ViewPager change listener
 */
public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

    private Activity activity = null;
    private int currentPosition = 0;
    public MyOnPageChangeListener(Activity activity){
        this.activity = activity;
    }

    public int getCurrentPosition(){
        return currentPosition;
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //"ViewPager"的位置一旦变动，随即修改actionTab的位置
        activity.getActionBar().setSelectedNavigationItem(position);
        currentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
