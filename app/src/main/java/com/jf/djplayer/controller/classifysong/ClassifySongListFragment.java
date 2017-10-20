package com.jf.djplayer.controller.classifysong;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.fragment.BaseFragment;
import com.jf.djplayer.bean.Song;
import com.jf.djplayer.datamanager.ClassifySongLoader;
import com.jf.djplayer.interfaces.PlayController;

import java.util.List;

/**
 * Created by Kingfar on 2017/10/18.
 * 我的最爱-歌曲列表
 */

public class ClassifySongListFragment extends BaseFragment
        implements ClassifySongLoader.ClassifySongLoadListener, AdapterView.OnItemClickListener{

    private ListView listView;     // 歌曲列表
    private ClassifySongListAdapter classifySongListAdapter;
    private View loadingHintView;  // ListView加载提示
    private View emptyView;        // ListView没数据时的提示
    private View footerView; // ListView的footView

    private List<Song> songList;  // 歌曲列表
    private PlayController playController;      // 当点击了歌曲列表，通过该变量来控制音乐播放

    private String typeName = "";  // 要加载的分类名字
    private String typeValue = ""; // 要加载的分类的值

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layoutView = inflater.inflate(R.layout.fragment_classify_song_list, container, false);
        initView(layoutView);
        return layoutView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // 宿主Activity转为音乐播放控制器
        playController = (PlayController)activity;
        // 获取要查询的分类名字和值
        ClassifySongListCallback callback = (ClassifySongListCallback)activity;
        typeName = callback.getTypeName();
        typeValue = callback.getTypeValue();
    }

    // 初始化界面
    private void initView(View layoutView){
        // find view
        listView = (ListView)layoutView.findViewById(R.id.lv_fragment_classify_song_list);
        loadingHintView = layoutView.findViewById(R.id.ll_fragment_classify_song_list_loading_view);
        emptyView = layoutView.findViewById(R.id.ll_fragment_classify_song_list_empty_view);
        // 扫描音乐执行按钮
        loadSong();
    }

    // 读取本地音乐
    private void loadSong(){
        // 隐藏除了进度条以外的其它View
        listView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        // 加载音乐
        ClassifySongLoader classifySongLoader = new ClassifySongLoader();
        classifySongLoader.loadSong(this, typeName, typeValue);
    }

    /****************本地音乐读取器回调接口****************/
    @Override
    public void onSuccess(List<Song> songList) {
        if( songList == null || songList.size() == 0 ){
            this.songList = songList;
            // 数据库没有歌曲
            emptyView.setVisibility(View.VISIBLE);
            loadingHintView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }else{
            this.songList = songList;
            emptyView.setVisibility(View.INVISIBLE);
            loadingHintView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            // 设置ListView
            footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view, null);
            ((TextView) footerView.findViewById(R.id.tv_list_footer_view)).setText(songList.size() + "首歌");
            listView.addFooterView(footerView);
            listView.setOnItemClickListener(this);
            classifySongListAdapter = new ClassifySongListAdapter(this, songList);
            listView.setAdapter(classifySongListAdapter);
        }
    }

    @Override
    public void onFailed(Exception exception) {

    }
    /****************本地音乐读取器回调接口****************/

    /****************ListView的Item点击事件****************/
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        //传入当前播放列表以及用户所点击的位置
        playController.play(getClass().getSimpleName(), songList, position);
    }
    /****************ListView的Item点击事件****************/

    /**
     * 和宿主Activity通信的接口
     */
    interface ClassifySongListCallback{
        /**
         * 获取要查询的类型名字
         * @return 以下三者里的一个：
         * ClassifySongActivity.TYPE_NAME_SINGER
         * ClassifySongActivity.TYPE_NAME_ALBUM
         * ClassifySongActivity.TYPE_NAME_FOLDER
         */
        String getTypeName();

        /**
         * 获取要查询的类型的值
         * @return 类型的值
         */
        String getTypeValue();
    }
}
