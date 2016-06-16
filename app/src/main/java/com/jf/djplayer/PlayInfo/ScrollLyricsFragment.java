package com.jf.djplayer.playinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.jf.djplayer.R;

/**
 * Created by JF on 2016/2/6.
 * 这是滚动显示歌词页面
 */
public class ScrollLyricsFragment extends Fragment{

    private View layoutView;
    private ScrollView lyricScrollView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_scroll_lyric,container,false);
        viewInit();
        return layoutView;
    }

    private void viewInit(){
        lyricScrollView = (ScrollView)layoutView.findViewById(R.id.sv_fragment_scroll_lyric);


    }
}


