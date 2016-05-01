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
import com.jf.djplayer.basefragment.BaseGroupFragment;
import com.jf.djplayer.interfaces.ChangeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JF on 2016/4/25.
 */
public class RecentlyPlayFragment extends BaseGroupFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initBeforeReturnView() {
        setTitleImageResourceId(R.drawable.ic_return);
        setTitleText("最近播放");
        setTitleSearchVisibility(View.VISIBLE);
        setTitleMoreVisibility(View.VISIBLE);
    }


    @Override
    protected String[] getTextViewTabsText() {
        return null;
    }

    @Override
    protected FragmentStatePagerAdapter getViewPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new RecentlyPlayListFragment());
        FragmentViewPagerAdapter fragmentViewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager());
        fragmentViewPagerAdapter.setFragments(fragmentList);
        return fragmentViewPagerAdapter;
    }

    @Override
    public void onTitleClick() {
        super.onTitleClick();
        ((ChangeFragment)getActivity()).finishFragment();
    }
}
