package com.jf.djplayer.controller.playinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jf.djplayer.R;
import com.jf.djplayer.base.fragment.BaseFragment;

/**
 * Created by JF on 2016/2/6.
 * 这是滚动显示歌词页面
 */
public class ScrollLyricsFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layoutView = inflater.inflate(R.layout.fragment_scroll_lyric, container, false);
        return layoutView;
    }

}


