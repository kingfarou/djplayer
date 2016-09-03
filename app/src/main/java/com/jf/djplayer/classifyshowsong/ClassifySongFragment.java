package com.jf.djplayer.classifyshowsong;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.jf.djplayer.adapter.FragmentViewPagerAdapter;
import com.jf.djplayer.base.fragment.BaseViewPagerFragment;
import com.jf.djplayer.interfaces.FragmentChanger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JF on 2016/5/1.
 * 分类显示--外层容器
 * 在“歌手”、“文件夹”、“专辑”页面里面点击栏目，将会跳到这个页面
 */
public class ClassifySongFragment extends BaseViewPagerFragment {

    public static final String COLUMN_NAME = "columnName";
    public static final String COLUMN_VALUES = "columnValues";

    @Override
    protected void initView(View layoutView) {
        super.initView(layoutView);
        //搜索按钮菜单按钮设置可见
        mCustomTitles.setSearchVisibility(View.VISIBLE);
        mCustomTitles.setMenuVisibility(View.VISIBLE);
        mCustomTitles.setTitleText(getArguments().getString((COLUMN_VALUES), "分类显示"));
    }

    @Override
    protected String[] getTextViewTabsText() {
        return null;
    }

    @Override
    protected FragmentStatePagerAdapter getViewPagerAdapter() {
        Bundle bundle = new Bundle();
        bundle.putString(COLUMN_NAME, getArguments().getString(COLUMN_NAME));
        bundle.putString(COLUMN_VALUES, getArguments().getString(COLUMN_VALUES));
        //创建"ClassifySongListFragment"
        ClassifySongListFragment fragment = new ClassifySongListFragment();
        fragment.setArguments(bundle);
        //初始化集合以及适配器
        List<Fragment> fragmentList = new ArrayList<>(1);
        fragmentList.add(fragment);
        mFragmentStatePagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), fragmentList);
        return mFragmentStatePagerAdapter;
    }

    @Override
    public void onTitleClick() {
        ((FragmentChanger)getActivity()).popFragments();
    }
}
