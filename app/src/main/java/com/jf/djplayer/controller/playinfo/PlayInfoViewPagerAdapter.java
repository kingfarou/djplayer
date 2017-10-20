package com.jf.djplayer.controller.playinfo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jf.djplayer.base.fragment.BaseFragment;

import java.util.List;

/**
 * Created by JF on 2016/2/6.
 * "PlayInfoActivity"里"ViewPager"的适配器
 */
public class PlayInfoViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> fragmentList;

    public PlayInfoViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }
}
