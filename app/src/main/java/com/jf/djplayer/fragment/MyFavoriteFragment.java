package com.jf.djplayer.fragment;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jf.djplayer.R;
import com.jf.djplayer.interfaces.ChangeFragment;

import com.jf.djplayer.customview.FragmentTitleLayout;

/**
 * Created by JF on 2016/2/21.
 */
public class MyFavoriteFragment extends Fragment implements FragmentTitleLayout.FragmentTitleListener{

    private View layoutView;
    private ChangeFragment changeFragment;
    private FragmentTitleLayout FragmentTitleLayout;
    private ViewGroup contentLinearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_my_favorite,container,false);
        viewInit();
        return layoutView;
    }

    private void viewInit(){
        FragmentTitleLayout = (FragmentTitleLayout)layoutView.findViewById(R.id.fragmentTitleLinearLayout_fragment_my_favorite);
        FragmentTitleLayout.setItemClickListener(this);
        contentLinearLayout = (FrameLayout)layoutView.findViewById(R.id.ll_fragment_my_favorite);
        getChildFragmentManager().beginTransaction().add(R.id.ll_fragment_my_favorite,new MyFavoriteListFragment()).commit();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        changeFragment = (ChangeFragment)activity;
    }

    @Override
    public void onTitleClick() {
        changeFragment.finishFragment();
    }

    @Override
    public void onSearchIvOnclick() {

    }

    @Override
    public void onMoreIvOnclick() {

    }
}
