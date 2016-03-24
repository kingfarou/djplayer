package com.jf.djplayer.Listener;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

/**
 * Created by Administrator on 2015/7/7.
 */
public class MTabListener implements ActionBar.TabListener {

    private ViewPager mViewPager = null;
    public MTabListener(ViewPager viewPager){
        this.mViewPager = viewPager;
    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        //actionTab一旦更换了，修改ViewPager的页卡
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
