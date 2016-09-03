package com.jf.djplayer.recentlyplay;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.jf.djplayer.R;
import com.jf.djplayer.adapter.FragmentViewPagerAdapter;
import com.jf.djplayer.base.fragment.BaseViewPagerFragment;
import com.jf.djplayer.interfaces.FragmentChanger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JF on 2016/4/25.
 * 最近播放-外层容器
 */
public class RecentlyPlayFragment extends BaseViewPagerFragment {

    @Override
    protected void initView(View layoutView) {
        super.initView(layoutView);
        setTitleImageResourceId(R.drawable.ic_return);
        setTitleText(getResources().getString(R.string.recently_play));
        setTitleSearchVisibility(View.VISIBLE);
        setTitleMoreVisibility(View.VISIBLE);
    }

    //返回空表示该容器只有一个页卡，所以就不需要显示页卡
    @Override
    protected String[] getTextViewTabsText() {
        return null;
    }

    @Override
    protected FragmentStatePagerAdapter getViewPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new RecentlyPlayListFragment());
        return new FragmentViewPagerAdapter(getChildFragmentManager(), fragmentList);
    }

    @Override
    public void onTitleClick() {
        super.onTitleClick();
        ((FragmentChanger)getActivity()).popFragments();
    }
}
