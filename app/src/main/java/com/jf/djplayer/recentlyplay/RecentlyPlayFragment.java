package com.jf.djplayer.recentlyplay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jf.djplayer.R;
import com.jf.djplayer.adapter.FragmentViewPagerAdapter;
import com.jf.djplayer.base.basefragment.BaseViewPagerFragment;
import com.jf.djplayer.interfaces.FragmentChanger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JF on 2016/4/25.
 * 最近播放-外层容器
 */
public class RecentlyPlayFragment extends BaseViewPagerFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initBeforeReturnView() {
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
        fragmentList.add(new RecentlyPlayExpandFragment());
        FragmentViewPagerAdapter fragmentViewPagerAdapter =
                new FragmentViewPagerAdapter(getChildFragmentManager(), fragmentList);
//        fragmentViewPagerAdapter.setFragments(fragmentList);
        return fragmentViewPagerAdapter;
    }

    @Override
    public void onTitleClick() {
        super.onTitleClick();
        ((FragmentChanger)getActivity()).popFragments();
    }
}
