package com.jf.djplayer.fragment;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jf.djplayer.R;
import com.jf.djplayer.interfaces.ChangeFragment;
import com.jf.djplayer.tool.database.SongInfoOpenHelper;

import com.jf.djplayer.customview.FragmentTitleLayout;

/**
 * Created by JF on 2016/2/21.
 */
public class MyFavoriteFragment extends Fragment implements FragmentTitleLayout.FragmentTitleListener{

    private View layoutView;
    private ChangeFragment changeFragment;
    private FragmentTitleLayout FragmentTitleLayout;
    private LinearLayout contentLinearLayout;

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
        contentLinearLayout = (LinearLayout)layoutView.findViewById(R.id.ll_fragment_my_favorite);
//        到数据库根据收藏与否加载不同布局
        SongInfoOpenHelper collectionOpenHelper = new SongInfoOpenHelper(getActivity(),1);
//        如果用户有收藏有歌曲，加载“HasFavoriteSongFragment”类
//        Log.i("test",collectionOpenHelper.getCollectionNum()+"");
        if(collectionOpenHelper.getCollectionNum()!=0){
            getChildFragmentManager().beginTransaction().add(R.id.ll_fragment_my_favorite,new HasFavoriteSongFragment()).commit();
        }else{
            View noCollectionSong = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_no_favourites,null);
            contentLinearLayout.addView(noCollectionSong,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//            Log.i("test","没有收藏任何歌曲");
        }
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
