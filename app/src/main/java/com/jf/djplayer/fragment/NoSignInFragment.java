package com.jf.djplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jf.djplayer.R;

/**
 * Created by JF on 2016/2/27.
 * 如果用户还未登陆将会启用这个界面
 */
public class NoSignInFragment extends Fragment{

    private View layoutView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_no_sign_in,container,false);
        return layoutView;
    }

    private void viewInit(){}
}
