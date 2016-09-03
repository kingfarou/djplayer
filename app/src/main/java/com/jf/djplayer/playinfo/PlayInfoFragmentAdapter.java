package com.jf.djplayer.playinfo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jf.djplayer.base.adapter.MyFragmentStatePagerAdapter;
import com.jf.djplayer.base.fragment.BaseFragment;

import java.util.List;

/**
 * Created by JF on 2016/2/6.
 * "PlayInfoActivity"里"ViewPager"的适配器
 */
public class PlayInfoFragmentAdapter extends MyFragmentStatePagerAdapter {

    public PlayInfoFragmentAdapter(FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm);
        super.fragmentList = fragmentList;
    }
}
