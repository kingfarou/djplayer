package com.jf.djplayer.myfavorite;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.jf.djplayer.R;
import com.jf.djplayer.adapter.BaseFragmentStatePagerAdapter;
import com.jf.djplayer.base.fragment.BaseViewPagerFragment;
import com.jf.djplayer.interfaces.FragmentChanger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JF on 2016/2/21.
 * 我的最爱-外层容器
 */
public class MyFavoriteFragment extends BaseViewPagerFragment {

    private FragmentChanger fragmentChanger;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }


    @Override
    protected void initView(View layoutView) {
        super.initView(layoutView);
        setTitleText(getResources().getString(R.string.my_favorite));
        setTitleSearchVisibility(View.VISIBLE);
        setTitleMoreVisibility(View.VISIBLE);
    }

    @Override
    protected FragmentStatePagerAdapter getViewPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>(1);
        fragmentList.add(new MyFavoriteListFragment());
        return new BaseFragmentStatePagerAdapter(getChildFragmentManager(), fragmentList);
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

    //标题栏点击事件回调的方法
    @Override
    public void onTitleClick(View titleView) {
        fragmentChanger.popFragments();
    }

}
