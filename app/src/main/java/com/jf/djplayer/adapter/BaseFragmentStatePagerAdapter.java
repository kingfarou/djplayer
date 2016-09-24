package com.jf.djplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by JF on 2016/1/20.
 * 基本"FragmentStatePagerAdapter"类，
 * 如果没有特殊需求，项目里面通用此类作为"ViewPager"的适配器
 */
public class BaseFragmentStatePagerAdapter extends FragmentStatePagerAdapter{

    private List<Fragment> fragmentList;

    public BaseFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public BaseFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragmentList){
        super(fm);
        this.fragmentList = fragmentList;
    }

    public List<Fragment> getFragmentList() {
        return fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList != null ? fragmentList.size() : 0;
    }
}
