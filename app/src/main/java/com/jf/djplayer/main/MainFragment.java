package com.jf.djplayer.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jf.djplayer.base.basefragment.BaseFragment;
import com.jf.djplayer.customview.FragmentTitleLayout;
import com.jf.djplayer.myfavorite.MyFavoriteViewPagerFragment;
import com.jf.djplayer.interfaces.FragmentChanger;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.R;

import com.jf.djplayer.localmusic.LocalMusicFragment;
import com.jf.djplayer.recentlyplay.RecentlyPlayViewPagerFragment;

/**
 * 主界面-MainFragment
 */
public class MainFragment extends BaseFragment implements View.OnClickListener, FragmentTitleLayout.FragmentTitleListener {

    private View rootView;//"fragment"布局文件
    private TextView tv_song_num;//歌词数量
    private FragmentTitleLayout fragmentTitleLayout;//标题
    private PopupWindow menuWindow = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        //initSongDatabase();
        return rootView;
    }

//    view的初始化
    private void initView() {
        //对标题栏的初始化
        fragmentTitleLayout = (FragmentTitleLayout) rootView.findViewById(R.id.fragmentTitleLinearLayout_fragment_mine);
        fragmentTitleLayout.setSearchVisibility(View.GONE);
        fragmentTitleLayout.setMenuVisibility(View.GONE);
        fragmentTitleLayout.setTitleClickListener(this);

        //各个按钮点击事件
        rootView.findViewById(R.id.ll_fragment_mine_local_music).setOnClickListener(this);//本地音乐
        rootView.findViewById(R.id.btn_fragment_main_my_favorite).setOnClickListener(this);//我的最爱
        rootView.findViewById(R.id.btn_fragment_main_my_down).setOnClickListener(this);//我的下载
        rootView.findViewById(R.id.btn_fragment_mine_song_menu).setOnClickListener(this);//我的歌单
        rootView.findViewById(R.id.btn_fragment_mine_recently_play).setOnClickListener(this);//最近播放
        rootView.findViewById(R.id.imgBtn_fragment_mine_dice).setOnClickListener(this);//随机播放

        //初始化歌曲数量显示的控件
        tv_song_num = (TextView) rootView.findViewById(R.id.tv_fragment_main_song_num);
        tv_song_num.setText(new SongInfoOpenHelper(getActivity()).getLocalMusicNumber() + "首歌曲");

        menuWindow = new PopupWindow();
    }


//    点击事件响应函数
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //如果点击本地音乐
            case R.id.ll_fragment_mine_local_music:
                ((FragmentChanger)getActivity()).replaceFragments(new LocalMusicFragment());
                break;
            //如果点击我的最爱
            case R.id.btn_fragment_main_my_favorite:
                ((FragmentChanger)getActivity()).replaceFragments(new MyFavoriteViewPagerFragment());
                break;
            //如果点击我的下载
            case R.id.btn_fragment_main_my_down:
                break;
            //如果点击我的歌单
            case R.id.btn_fragment_mine_song_menu:
                break;
            //如果点击最近播放
            case R.id.btn_fragment_mine_recently_play:
                ((FragmentChanger)getActivity()).replaceFragments(new RecentlyPlayViewPagerFragment());
                break;
            //如果点击随机播放
            case R.id.imgBtn_fragment_mine_dice:
                break;
        }
    }

//    三个方法下面覆盖
    @Override
    public void onTitleClick() {
        //标题点击没有事件
    }

    @Override
    public void onSearchIvOnclick() {
        //    当前页卡并不支持搜索按钮
    }

    @Override
    public void onMoreIvOnclick() {
        //当前界面暂不支持菜单按钮
    }

}


