package com.jf.djplayer.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jf.djplayer.basefragment.BaseFragment;
import com.jf.djplayer.customview.FragmentTitleLayout;
import com.jf.djplayer.myfavorite.MyFavoriteFragment;
import com.jf.djplayer.interfaces.ChangeFragment;
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

    private View layoutView;//这个指向当前fragment布局文件
    private TextView songNumberTv = null;//这是显示歌曲数量用的
    private ImageButton dice = null;//中间布局那个色子
    private FragmentTitleLayout FragmentTitleLayout;
//    新版取消登录功能
//    private final int SIGN_IN_REQUEST_CODE = 1;//这是启动登录的请求码
    private ImageButton menu = null;//中间布局右下角的那个菜单
    private PopupWindow menuWindow = null;
//    private int windowsWidth = 0;
//    private int windowsHeigth = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutView = inflater.inflate(R.layout.fragment_main, container, false);
        viewInit();
        //initSongDatabase();
        return layoutView;
    }

//    view的初始化
    private void viewInit() {
//        各个控件设置点击事件
        layoutView.findViewById(R.id.ll_fragment_mine_local_music).setOnClickListener(this);
        layoutView.findViewById(R.id.btn_fragment_main_my_favorite).setOnClickListener(this);
        layoutView.findViewById(R.id.btn_fragment_main_my_down).setOnClickListener(this);
        layoutView.findViewById(R.id.btn_fragment_mine_song_menu).setOnClickListener(this);
        layoutView.findViewById(R.id.btn_fragment_mine_recently_play).setOnClickListener(this);
        FragmentTitleLayout = (FragmentTitleLayout)layoutView.findViewById(R.id.fragmentTitleLinearLayout_fragment_mine);
        FragmentTitleLayout.setTitleClickListener(this);

        //这是显示歌曲数目那个TextView
        songNumberTv = (TextView) layoutView.findViewById(R.id.tv_mine_fragment_song_num);
        layoutView.findViewById(R.id.imgBtn_fragment_mine_dice).setOnClickListener(this);//设置
        layoutView.findViewById(R.id.ib_fragment_mine_menu).setOnClickListener(this);//菜单
        menuWindow = new PopupWindow();
//      读取当前歌曲数量
        songNumberTv.setText(new SongInfoOpenHelper(getActivity()).getLocalMusicNumber()+"首歌曲");

    }


//    点击事件响应函数
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_fragment_mine_local_music://如果点击本地音乐
//                更替Fragment
                ((ChangeFragment)getActivity()).replaceFragments(new LocalMusicFragment());
                break;
            case R.id.btn_fragment_main_my_favorite://如果点击我的最爱
                ((ChangeFragment)getActivity()).replaceFragments(new MyFavoriteFragment());
                break;
            case R.id.btn_fragment_main_my_down://如果点击我的下载
                break;
            case R.id.btn_fragment_mine_song_menu://如果点击我的歌单
                break;
            case R.id.btn_fragment_mine_recently_play://如果点击最近播放
                ((ChangeFragment)getActivity()).replaceFragments(new RecentlyPlayFragment());
                break;
            case R.id.imgBtn_fragment_mine_dice://如果点击随机播放
                break;
            case R.id.ib_fragment_mine_menu://如果点击了菜单键
                break;
            case R.id.tv_fragment_mine_menu_scan_music://如果点击了菜单布局的扫描音乐
                break;
            case R.id.tv_fragment_mine_menu_recently_add://如果点击了菜单布局的最近添加
                break;
            case R.id.tv_fragment_mine_menu_custom_mainActivity://如果点击了定制首页的按钮
                break;
        }
    }

//   FragmentTitleLayout
//    三个方法下面覆盖
    @Override
    public void onTitleClick() {
//        新版取消登录功能
//        用户点击登录或者注册
//        startActivityForResult(new Intent(getActivity(), UserInfoActivity.class),SIGN_IN_REQUEST_CODE);
    }

//    当前页卡并不支持搜索按钮
    @Override
    public void onSearchIvOnclick() {}


//    更多按钮按下时的回调方法
//    当前页卡并不支持“更多”按钮
    @Override
    public void onMoreIvOnclick() {}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        如果请求已经成功
        if(resultCode==MainActivity.RESULT_OK){
//            新版取消登录功能
//            如果是登录请求码
//            if(requestCode == SIGN_IN_REQUEST_CODE){
//                UserInfo userInfo = (UserInfo)data.getSerializableExtra("userInfo");
//                FragmentTitleLayout.setTitleText(userInfo.getUserName());
//                //这里填写登录成功后的操作
//                Toast.makeText(getActivity(),"登陆成功",Toast.LENGTH_SHORT).show();
//            }
        }
    }//onActivityResult()
}

