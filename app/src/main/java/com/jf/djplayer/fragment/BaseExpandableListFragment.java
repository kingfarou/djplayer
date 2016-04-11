package com.jf.djplayer.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jf.djplayer.InfoClass;
import com.jf.djplayer.MyApplication;
import com.jf.djplayer.SongInfo;
import com.jf.djplayer.adapter.ExpandableFragmentAdapter;
import com.jf.djplayer.customview.ListViewPopupWindows;
import com.jf.djplayer.interfaces.SongInfoObserver;
import com.jf.djplayer.R;
import com.jf.djplayer.broadcastreceiver.UpdateUiSongInfoReceiver;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/9/14.
 * 该类定义所有使用"ExpandableListView"显示歌曲信息的"Fragment"的显示样式
 * 该抽象类已经实现的功能有
 * 异步读取Expandable需显示的数据（在“getSongInfoList()”方法里面）
 * 读取之前有进度条提示用户读取之后正常显示数据
 * 读取完成将数据设置到"ExpandableListView"里面
 * expandable任何一个栏目展开之后
 * 自动收起其他栏目
 * 子类根据需要重写方法即可
 */

abstract public class BaseExpandableListFragment extends BaseFragment
        implements ExpandableListView.OnGroupExpandListener,SongInfoObserver,
                ExpandableListView.OnGroupClickListener,AdapterView.OnItemLongClickListener{

    protected ExpandableListView expandableListView;//expandableListView
    private ProgressBar loadingProgressBar;//正在载入提示图标
    private TextView loadingTv;//正在载入提示文字
    private UpdateUiSongInfoReceiver updateUiSongInfoReceiver;
    private ReadSongInfoAsyncTask readSongInfoAsyncTask;
//    protected View footerView;//expandableListView底部
    protected ExpandableFragmentAdapter expandableFragmentAdapter;//expandableListView适配器
    protected List<SongInfo> songInfoList;
    private int lastExpand = -1;//记录上次"expandableListView"所展开的那个位置
    protected View layoutView;//跟布局
    protected PopupWindow popupWindows;//点击选项菜单那时弹出的PopupWindow

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        对界面进行初始化
        layoutView = inflater.inflate(R.layout.fragment_expandalbe, container, false);
        loadingProgressBar = (ProgressBar) layoutView.findViewById(R.id.pb_fragment_expandable);
        loadingTv = (TextView) layoutView.findViewById(R.id.tv_fragment_expandable_loading);

//        开始执行异步任务工作
        readSongInfoAsyncTask = new ReadSongInfoAsyncTask();
        readSongInfoAsyncTask.execute();
        return layoutView;
    }

    @Override
    public void onStart() {
        super.onStart();
//        动态注册广播接收
        IntentFilter updateUiFilter = new IntentFilter();
        updateUiFilter.addAction(UpdateUiSongInfoReceiver.COLLECTION_SONG);
        updateUiFilter.addAction(UpdateUiSongInfoReceiver.CANCEL_COLLECTION_SONG);
        updateUiFilter.addAction(UpdateUiSongInfoReceiver.DELETE_SONG);
        updateUiSongInfoReceiver = new UpdateUiSongInfoReceiver(this);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateUiSongInfoReceiver,updateUiFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateUiSongInfoReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        readSongInfoAsyncTask.cancel(true);
    }

    @Override
    public void onGroupExpand(int groupPosition) {
//        以下共有三类情况
//        1，如果这次所展开的栏目和上次的同个位置，什么操作都不用做
//        2，如果两次所展开的不是同个栏目，(1)那么有可能当前是首次展开，(2)也有可能当前不是首次展开
        if (lastExpand==groupPosition) {//如果当前所展开的和上次是一个栏目
            return;
        }
        //如果当前不是首次展开
        if(lastExpand!=-1) {
            expandableListView.collapseGroup(lastExpand);
        }
        lastExpand = groupPosition;//不论当前是否首次展开都要保存新的栏目位置
    }


    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//        被调用的方法在子类里实现
        doExpandableOnItemClick(parent, v, groupPosition, id);
        return true;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        被调用的方法在子类里实现
        doExpandableItemLongClick(parent, view, position, id);
        return true;
    }

    /**
     * 数据的具体来源有子类进行实现
     * @return 返回读取到的歌曲信息集合，如果没有读到信息真返回空
     */
    abstract protected List<SongInfo> getSongInfoList();

    /**
     * 异步任务读取数据完成之后回调这个方法
     * 具体时间：数据读取完成之后，expandableListView.setAdapter();方法被调用之前
     */
    abstract protected void asyncReadDataFinished();

    /**
     * 获取一个自定义的"ListViewPopupWindows"对象
     * 该对象是popupWindow，不过使用ListView显示数据
     * @return ListViewPopupWindow
     */
    abstract public ListViewPopupWindows getListViewPopupWindow();

    /**
     * 当"ExpandableListView"item被点击时将会执行这个方法
     * @param parent
     * @param v
     * @param groupPosition 被点位置
     * @param id
     */
    abstract protected void doExpandableOnItemClick(ExpandableListView parent, View v, int groupPosition, long id);

    /**
     * 当"ExpandableListView"item被长按时执行这个方法
     * @param parent
     * @param view
     * @param position 按下位置
     * @param id
     */
    abstract protected void doExpandableItemLongClick(AdapterView<?> parent, View view, int position, long id);

    /**
     * 如果子类想在"ExpandableListView"没数据时显示一些提示，在这设置
     * @return
     */
    abstract protected View getExpandableEmptyView();

    //    异步读取数据库信息的任务
    private class ReadSongInfoAsyncTask extends AsyncTask<Void, Void, List<SongInfo>> {

        @Override
        protected List<SongInfo> doInBackground(Void... params) {
            try {
                Thread.sleep(400L);
            }catch (Exception e){}
            return getSongInfoList();
        }

        @Override
        protected void onPostExecute(List<SongInfo> songInfos) {
            super.onPostExecute(songInfos);
//            如果读取到了数据
            if(songInfos!=null){
//                如果有数据就要对"ExpandableListView"进行设置
                expandableListView = (ExpandableListView) layoutView.findViewById(R.id.el_fragment_expandable_list_view);
                expandableListView.setOnGroupExpandListener(BaseExpandableListFragment.this);
                expandableListView.setGroupIndicator(null);
                expandableListView.setOnGroupClickListener(BaseExpandableListFragment.this);
                expandableListView.setOnItemLongClickListener(BaseExpandableListFragment.this);
//                将数据存到成员变量里
                songInfoList = songInfos;
//                将数据加载到适配器里
                expandableFragmentAdapter = new ExpandableFragmentAdapter(getActivity(),expandableListView,songInfoList);
                asyncReadDataFinished();//通知子类数据已经读取完成
//                显示
                expandableListView.setAdapter(expandableFragmentAdapter);
            }else{
                expandableListView.setEmptyView(getExpandableEmptyView());
            }
//            将用于提示“正读取”信息用的控件隐藏
            loadingProgressBar.setVisibility(View.GONE);
            loadingTv.setVisibility(View.GONE);
//            显示"ExpandableListView"
            expandableListView.setVisibility(View.VISIBLE);
        }
    }



    protected class FileOperationReceiver extends BroadcastReceiver {
        //            收到这个广播说明数据库的内容已被修改
        @Override
        public void onReceive(Context context, Intent intent) {
//            如果用户修改音乐文件信息
            if (intent.getAction().equals(InfoClass.ActionString.UPDATE_SONG_FILE_INFO_ACTION)) {
                Set category = intent.getCategories();
//                用户收藏或者取消收藏某一首歌
                if (category.contains(InfoClass.CategoryString.UPDATE_COLLECTION_CATEGORY)) {
//                    歌曲信息被修改后，修改集合里面该歌曲得信息，可以保持信息里的一致
                    int collection = songInfoList.get(intent.getIntExtra("groupPosition", -1)).getCollection();
                    songInfoList.get(intent.getIntExtra("groupPosition", -1)).setCollection((collection ^ 1));
//                    收起该列，视觉效果，忽悠用户
                    expandableListView.collapseGroup(intent.getIntExtra("groupPosition", -1));

                }
//                如果用户曾经删除文件
                else if (category.contains(InfoClass.CategoryString.DELETE_SONG_FILE_CATEGORY)) {
//                    列表修改原有数据状况
                    songInfoList.remove(intent.getIntExtra("groupPosition", -1));
                    expandableFragmentAdapter.notifyDataSetChanged();
//                    修改尾巴上显示的歌曲数量
                } else if (category.contains(InfoClass.CategoryString.UPDATE_SONG_FILE_INFO_CATEGORY)) {

                }
            }
        }//onReceive
    }
}
