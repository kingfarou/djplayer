package com.jf.djplayer.myfavorite;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jf.djplayer.R;
import com.jf.djplayer.adapter.FragmentViewPagerAdapter;
import com.jf.djplayer.base.basefragment.BaseGroupFragment;
import com.jf.djplayer.interfaces.FragmentChanger;

import com.jf.djplayer.customview.FragmentTitleLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JF on 2016/2/21.
 * 我的最爱-外层容器
 */
public class MyFavoriteGroupFragment extends BaseGroupFragment{

    private View layoutView;
    private FragmentChanger fragmentChanger;
    private FragmentTitleLayout FragmentTitleLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected void initBeforeReturnView() {
//        setTitleImageResourceId(R.drawable.ic_return);
        setTitleText(getResources().getString(R.string.my_favorite));
        setTitleSearchVisibility(View.VISIBLE);
        setTitleMoreVisibility(View.VISIBLE);
    }

    @Override
    protected FragmentStatePagerAdapter getViewPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>(1);
        fragmentList.add(new MyFavoriteExpandFragment());
        FragmentViewPagerAdapter fragmentViewPagerAdapter =
                new FragmentViewPagerAdapter(getChildFragmentManager(), fragmentList);
//        fragmentViewPagerAdapter.setFragments(fragmentList);
        return fragmentViewPagerAdapter;
    }

    @Override
    protected String[] getTextViewTabsText() {
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentChanger = (FragmentChanger)activity;
    }

    @Override
    public void onTitleClick() {
        fragmentChanger.popFragments();
    }

    @Override
    public void onSearchIvOnclick() {

    }

    @Override
    public void onMoreIvOnclick() {

    }
}
