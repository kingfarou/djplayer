package com.jf.djplayer.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jf.djplayer.base.fragment.BaseFragment;

import java.util.List;

/**
 * Created by jf on 2016/8/29.
 * 项目所有使用到“FragmentStatePagerAdapter”适配器基类
 */
public abstract class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter{

    protected List<BaseFragment> fragmentList;

    public MyFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
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
