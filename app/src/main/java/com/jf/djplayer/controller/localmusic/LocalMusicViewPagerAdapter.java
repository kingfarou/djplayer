package com.jf.djplayer.controller.localmusic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * Created by Administrator on 2017/10/16.
 * 本地音乐界面的ViewPager适配器
 */

public class LocalMusicViewPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] fragments;
    private String[] fragmentTitles;

    public LocalMusicViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments =
                new Fragment[]{new SongListFragment(), new SingerListFragment(), new AlbumListFragment(), new FolderListFragment()};
        fragmentTitles = new String[]{"歌曲", "歌手", "专辑", "文件夹"};
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles[position];
    }
}
