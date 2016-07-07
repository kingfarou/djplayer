package com.jf.djplayer.localmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jf.djplayer.R;
import com.jf.djplayer.adapter.FragmentViewPagerAdapter;
import com.jf.djplayer.base.basefragment.BaseFragment;
import com.jf.djplayer.base.basefragment.BaseViewPagerFragment;
import com.jf.djplayer.customview.ListViewPopupWindows;
import com.jf.djplayer.interfaces.FragmentChanger;
import com.jf.djplayer.search.SearchActivity;
import com.jf.djplayer.search.SearchedDataProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JF on 2016/1/19.
 * 本地音乐-外层容器
 */
public class LocalMusicFragment extends BaseViewPagerFragment {

    private int windowWidths;

    @Override
    protected void initView(View layoutView) {
        super.initView(layoutView);
        windowWidths = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        //进行子类对View初始化
        setTitleImageResourceId(R.drawable.ic_return);
        setTitleText(getResources().getString(R.string.local_music));
        setTitleSearchVisibility(View.VISIBLE);
        setTitleMoreVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : ((FragmentViewPagerAdapter)mFragmentStatePagerAdapter).getFragmentList()) {
            //可见的"Fragment"才去调用"onActivityResult()"不可见的调用了会出错
            if(fragment.isVisible()){
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected ViewPager.PageTransformer getViewPagerTransformer() {
        return new LocalMusicPageTransformer();
    }

    @Override
    protected FragmentStatePagerAdapter getViewPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>(4);
//        fragmentList.add(new SongFragment());
        fragmentList.add(new SongFragment());
        fragmentList.add(new SingerFragment());
        fragmentList.add(new AlbumFragment());
        fragmentList.add(new FolderFragment());
        return new FragmentViewPagerAdapter(getChildFragmentManager(), fragmentList);
    }

    //返回"tabs"个个页卡所对应的标题名字
    @Override
    protected String[] getTextViewTabsText() {
        return new String[]{getResources().getString(R.string.song),
                getResources().getString(R.string.singer),
                getResources().getString(R.string.album),
                getResources().getString(R.string.folder)};
    }

    /*"FragmentTitleListener"方法覆盖_start*/
    //如果标题栏的标题按钮被按下了
    @Override
    public void onTitleClick() {
        ((FragmentChanger) getActivity()).popFragments();
    }

    //如果标题栏的搜索按钮被按下了
    @Override
    public void onSearchIvOnclick() {
        //待搜索的数据集合
        Fragment fragment = getViewPagerCurrentPage();
//        Fragment fragment = (Fragment) mFragmentStatePagerAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
        List searchedList = ((SearchedDataProvider)fragment).returnSearchedDataList();
        String fragmentType = SearchActivity.LIST_VIEW;//表示需要使用哪类"Fragment"展示数据
        String keyHint = null;//用户没有输入搜索关键字时所显示的提示文字
//        switch(getViewPagerCurrentItem()){
        switch (mViewPager.getCurrentItem()){
            case 0://获取歌曲列表数据
                fragmentType = SearchActivity.EXPANDABLE_LIST_VIEW;
                keyHint = "输入歌曲名字搜索";
                break;
            case 1://获取歌手列表数据
                 keyHint = "搜索歌手";
//                fragmentType = "ListView";
                break;
            case 2://获取专辑列表数据
                keyHint = "搜索专辑";
//                fragmentType = "ListView";
                break;
            case 3://获取文件夹列表的数据
                keyHint = "搜索文件夹";
//                fragmentType = "ListView";
                break;
            default:
                break;
        }
        Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
        //装填待搜索的数据列表
        searchIntent.putExtra(SearchActivity.SEARCH_LIST, (Serializable)searchedList);
        //装填展示数据所需要的"Fragment"类型
        searchIntent.putExtra(SearchActivity.FRAGMENT_TYPE, fragmentType);
        //装填用户没有输入是的提示信息
        searchIntent.putExtra(SearchActivity.EDIT_TEXT_HINT, keyHint);
        startActivity(searchIntent);
    }

    //标题栏的"更多按钮"被按下了
    @Override
    public void onMoreIvOnclick() {
        ListViewPopupWindows listViewPopupWindows = null;
        BaseFragment fragment = (BaseFragment)mFragmentStatePagerAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
        switch (mViewPager.getCurrentItem()) {
            case 0:
                listViewPopupWindows = ((SongFragment)fragment).getListViewPopupWindow();
                break;
            case 1:
                listViewPopupWindows = ((SingerFragment)fragment).getListViewPopupWindow();
                break;
            case 2:
                listViewPopupWindows = ((AlbumFragment)fragment).getListViewPopupWindow();
                break;
            case 3:
                listViewPopupWindows = ((FolderFragment)fragment).getListViewPopupWindow();
                break;
            default:
                break;
        }
        listViewPopupWindows.showAsDropDown(mCustomTitles, windowWidths - listViewPopupWindows.getWidth(), 0);
    }
    /*"FragmentTitleListener"方法覆盖_end*/
}
