package com.jf.djplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by JF on 2016/2/6.
 * _SongPlayInfoActivity
 * 里ViewPager的适配器
 */
public class SongPlayInfoAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;

    public SongPlayInfoAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
