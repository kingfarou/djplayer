package com.jf.djplayer.controller.localmusic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jf.djplayer.R;
import com.jf.djplayer.base.activity.BackgroundPlayActivity;
import com.jf.djplayer.widget.TitleBar;
import com.viewpagerindicator.TabPageIndicator;


/**
 * 本地音乐
 */
public class LocalMusicActivity extends BackgroundPlayActivity implements TitleBar.OnTitleClickListener,
        TitleBar.OnSearchClickListener, TitleBar.OnMenuClickListener{

    private ViewPager viewPager;
    private TabPageIndicator tabPageIndicator;
    protected LocalMusicViewPagerAdapter localMusicViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
        initTitleBar();
        initViewPager();
    }

    // 初始化标题栏
    private void initTitleBar(){
        TitleBar titleBar = (TitleBar)findViewById(R.id.title_bar_activity_local_music);
        titleBar.setTitleText("本地音乐");
        titleBar.setOnTitleClickListener(this);
        titleBar.setOnSearchClickListener(this);
        titleBar.setOnMenuClickListener(this);
    }

    // 初始化ViewPager
    private void initViewPager(){
        //"ViewPager"初始化
        viewPager = (ViewPager)findViewById(R.id.vp_activity_local_music);
        localMusicViewPagerAdapter =
                new LocalMusicViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(localMusicViewPagerAdapter);

        // 初始化TabPageIndicator
        tabPageIndicator = (TabPageIndicator)findViewById(R.id.tab_page_indicator_activity_local_music);
        tabPageIndicator.setViewPager(viewPager);
    }

    /****************TitleBar接口方法实现****************/
    @Override
    public void onMenuClick(View menuView) {
        int currentItem = viewPager.getCurrentItem();
        Fragment fragment = localMusicViewPagerAdapter.getItem(currentItem);
        if(currentItem == 0){
            ((SongListFragment)fragment).getListViewPopupWindow().showAsDropDown(menuView, 0, 0);
        }else if(currentItem == 1){
//            ((SingerListFragment)fragment).getListViewPopupWindow();
        }else if(currentItem == 2){
//            ((AlbumListFragment)fragment).getListViewPopupWindow();
        }else{
//            ((FolderListFragment)fragment).getListViewPopupWindow();
        }
    }

    @Override
    public void onSearchClick(View SearchView) {

    }

    @Override
    public void onTitleClick(View titleView) {
        finish();
    }
    /****************TitleBar接口方法实现****************/

}
