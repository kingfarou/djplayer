package com.jf.djplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jf.djplayer.R;
import com.jf.djplayer.activity.ScanningSongActivity;

/**
 * Created by Administrator on 2015/8/2.
 * 如果我的音乐里面没有信息
 * 那么"歌曲"选项卡将加载这个Fragment
 */
public class NoSongFragment extends Fragment implements OnClickListener{

    private Button scanMusicBtn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.local_music_no_song, container, false);
        scanMusicBtn = (Button)view.findViewById(R.id.btn_localmusic_nosong_keyscan);
        scanMusicBtn.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v){
        Intent scanIntent = new Intent(getActivity(),ScanningSongActivity.class);
        getActivity().startActivity(scanIntent);
    }
}
