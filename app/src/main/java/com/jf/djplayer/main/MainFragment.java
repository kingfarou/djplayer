package com.jf.djplayer.main;

import android.view.View;
import android.widget.TextView;

import com.jf.djplayer.base.fragment.BaseFragment;
import com.jf.djplayer.view.TitleBar;
import com.jf.djplayer.myfavorite.MyFavoriteFragment;
import com.jf.djplayer.interfaces.FragmentChanger;
import com.jf.djplayer.database.SongInfoOpenHelper;
import com.jf.djplayer.R;

import com.jf.djplayer.localmusic.LocalMusicFragment;
import com.jf.djplayer.recentlyplay.RecentlyPlayFragment;
import com.jf.djplayer.util.ToastUtil;

/**
 * 主界面-MainFragment
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View layoutView) {
        //对标题栏的初始化
        TitleBar titleBar = (TitleBar) layoutView.findViewById(R.id.fragmentTitleLinearLayout_fragment_mine);
        titleBar.setSearchVisibility(View.GONE);
        titleBar.setMenuVisibility(View.GONE);

        //各个按钮点击事件，分别是，本地音乐、我的最爱、我的下载、我的歌单、最近播放、随机播放
        layoutView.findViewById(R.id.ll_fragment_mine_local_music).setOnClickListener(this);
        layoutView.findViewById(R.id.btn_fragment_main_my_favorite).setOnClickListener(this);
        layoutView.findViewById(R.id.btn_fragment_main_my_down).setOnClickListener(this);
        layoutView.findViewById(R.id.btn_fragment_mine_song_menu).setOnClickListener(this);
        layoutView.findViewById(R.id.btn_fragment_mine_recently_play).setOnClickListener(this);
        layoutView.findViewById(R.id.imgBtn_fragment_mine_dice).setOnClickListener(this);

        //初始化歌曲数量显示的控件
        TextView songNumberTv = (TextView) layoutView.findViewById(R.id.tv_fragment_main_song_num);
        songNumberTv.setText(new SongInfoOpenHelper(getActivity()).getLocalMusicNumber() + "首歌曲");
    }

    //点击事件响应函数
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.ll_fragment_mine_local_music){
            //本地音乐按钮
            ((FragmentChanger) getActivity()).replaceFragments(new LocalMusicFragment());
        }else if(id == R.id.btn_fragment_main_my_favorite){
            //我的最爱按钮
            ((FragmentChanger) getActivity()).replaceFragments(new MyFavoriteFragment());
        }else if(id == R.id.btn_fragment_main_my_down){
            //我的下载按钮
            ToastUtil.showShortToast(getActivity(), "该功能还未实现");
        }else if(id == R.id.btn_fragment_mine_song_menu){
            //我的歌单按钮
            ToastUtil.showShortToast(getActivity(), "该功能还未实现");
        }else if(id == R.id.btn_fragment_mine_recently_play){
            //最近播放按钮
            ((FragmentChanger) getActivity()).replaceFragments(new RecentlyPlayFragment());
        }else if(id == R.id.imgBtn_fragment_mine_dice){
            //随机播放按钮，界面上的那个色子
            ToastUtil.showShortToast(getActivity(), "该功能还未实现");
        }
    }//onClick()

}


