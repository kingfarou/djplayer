package com.jf.djplayer.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jf.djplayer.base.basefragment.BaseFragment;
import com.jf.djplayer.customview.FragmentTitleLayout;
import com.jf.djplayer.myfavorite.MyFavoriteFragment;
import com.jf.djplayer.interfaces.FragmentChanger;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.R;

import com.jf.djplayer.localmusicfragment.LocalMusicFragment;
import com.jf.djplayer.recentlyplay.RecentlyPlayFragment;

/**
 * 主界面窗体的“我的”页卡
 * 这个Fragment仅做基本显示以及响应用户操作
 *
 */
public class MainFragment extends BaseFragment implements View.OnClickListener, FragmentTitleLayout.FragmentTitleListener {

    private View rootView;//这个指向当前fragment布局文件
    private TextView songNumberTv;//歌词数量
    private FragmentTitleLayout fragmentTitleLayout;//标题
    private PopupWindow menuWindow = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        viewInit();
        //initSongDatabase();
        return rootView;
    }

//    view的初始化
    private void viewInit() {
        //对标题栏的初始化
        fragmentTitleLayout = (FragmentTitleLayout) rootView.findViewById(R.id.fragmentTitleLinearLayout_fragment_mine);
        fragmentTitleLayout.setSearchIvVisibility(View.GONE);
        fragmentTitleLayout.setMoreIvVisivility(View.GONE);
        fragmentTitleLayout.setTitleClickListener(this);

        //各个按钮点击事件
        rootView.findViewById(R.id.ll_fragment_mine_local_music).setOnClickListener(this);//本地音乐
        rootView.findViewById(R.id.btn_fragment_main_my_favorite).setOnClickListener(this);//我的最爱
        rootView.findViewById(R.id.btn_fragment_main_my_down).setOnClickListener(this);//我的下载
        rootView.findViewById(R.id.btn_fragment_mine_song_menu).setOnClickListener(this);//我的歌单
        rootView.findViewById(R.id.btn_fragment_mine_recently_play).setOnClickListener(this);//最近播放
        rootView.findViewById(R.id.imgBtn_fragment_mine_dice).setOnClickListener(this);//随机播放
//        rootView.findViewById(R.id.ib_fragment_mine_menu).setOnClickListener(this);//菜单

        //这是显示歌曲数目那个TextView
        songNumberTv = (TextView) rootView.findViewById(R.id.tv_mine_fragment_song_num);

        menuWindow = new PopupWindow();
//      读取当前歌曲数量
        songNumberTv.setText(new SongInfoOpenHelper(getActivity()).getLocalMusicNumber()+"首歌曲");

    }


//    点击事件响应函数
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fragment_mine_local_music://如果点击本地音乐
                ((FragmentChanger)getActivity()).replaceFragments(new LocalMusicFragment());
                break;
            case R.id.btn_fragment_main_my_favorite://如果点击我的最爱
                ((FragmentChanger)getActivity()).replaceFragments(new MyFavoriteFragment());
                break;
            case R.id.btn_fragment_main_my_down://如果点击我的下载
                break;
            case R.id.btn_fragment_mine_song_menu://如果点击我的歌单
                break;
            case R.id.btn_fragment_mine_recently_play://如果点击最近播放
                ((FragmentChanger)getActivity()).replaceFragments(new RecentlyPlayFragment());
                break;
            case R.id.imgBtn_fragment_mine_dice://如果点击随机播放
                break;
//            case R.id.ib_fragment_mine_menu://如果点击了菜单键
//                break;
//            case R.id.tv_fragment_mine_menu_scan_music://如果点击了菜单布局的扫描音乐
//                break;
//            case R.id.tv_fragment_mine_menu_recently_add://如果点击了菜单布局的最近添加
//                break;
//            case R.id.tv_fragment_mine_menu_custom_mainActivity://如果点击了定制首页的按钮
//                break;
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


