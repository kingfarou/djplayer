package com.jf.djplayer.localmusicfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jf.djplayer.R;
import com.jf.djplayer.basefragment.BaseGroupFragment;
import com.jf.djplayer.basefragment.BaseListFragment;
import com.jf.djplayer.customview.ListViewPopupWindows;

import com.jf.djplayer.adapter.FragmentViewPagerAdapter;
import com.jf.djplayer.interfaces.ChangeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JF on 2016/1/19.
 */
public class LocalMusicFragment extends BaseGroupFragment {

    private int windowWidths;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        windowWidths = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        return super.onCreateView(inflater, container, savedInstanceState);
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
    protected void initBeforeReturnView() {
        //进行子类对View初始化
        setTitleImageResourceId(R.drawable.ic_return);
        setTitleText("本地音乐");
        setTitleSearchVisibility(View.VISIBLE);
        setTitleMoreVisibility(View.VISIBLE);
    }

    @Override
    protected FragmentStatePagerAdapter getViewPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>(4);
//   如果数据库里面有歌曲
//        fragmentList.add(new SongFragment());
        fragmentList.add(new SongFragment());
        fragmentList.add(new SingerFragment());
        fragmentList.add(new AlbumFragment());
        fragmentList.add(new FolderFragment());
        FragmentViewPagerAdapter fragmentViewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager());

        fragmentViewPagerAdapter.setFragments(fragmentList);
        return fragmentViewPagerAdapter;
    }

    @Override
    protected String[] getTextViewTabsText() {
        return new String[]{"歌曲", "歌手", "专辑", "文件夹"};
    }


    @Override
    public void onTitleClick() {
        ((ChangeFragment) getActivity()).finishFragment();
    }


    //    标题栏的"更多按钮"被按下了
    @Override
    public void onMoreIvOnclick() {
        ListViewPopupWindows listViewPopupWindows;
        Fragment fragment = mFragmentStatePagerAdapter.getItem(getCurrentItem());
        switch (getCurrentItem()) {
            case 0:
                listViewPopupWindows = ((SongFragment) fragment).getListViewPopupWindow();
                listViewPopupWindows.showAsDropDown(mFragmentTitleLayout, windowWidths - listViewPopupWindows.getWidth(), 0);
                break;
            case 1:
            case 2:
            case 3:
                listViewPopupWindows = ((BaseListFragment) fragment).getListViewPopupWindow();
                listViewPopupWindows.showAsDropDown(mFragmentTitleLayout, windowWidths - listViewPopupWindows.getWidth(), 0);
                break;
            default:
                break;
        }
    }

}
