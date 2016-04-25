package com.jf.djplayer.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;

import com.jf.djplayer.R;

import java.util.List;

/**
 * Created by Administrator on 2015/9/14.
 * 该类定义所有使用"ExpandableListView"显示歌曲信息的"Fragment"的显示样式
 * 该抽象类已经实现的功能有
 * 异步读取Expandable需显示的数据（在“readData()”方法里面）
 * 读取之前有进度条提示用户读取之后正常显示数据
 * 读取完成将数据设置到"ExpandableListView"里面
 * expandable任何一个栏目展开之后
 * 自动收起其他栏目
 * 子类根据需要重写方法即可
 */

abstract public class BaseExpandableListFragment extends BaseFragment
        implements ExpandableListView.OnGroupExpandListener, ExpandableListView.OnGroupClickListener,
                AdapterView.OnItemLongClickListener {

    private ExpandableListView expandableListView;//expandableListView
    protected BaseExpandableListAdapter expandableFragmentAdapter;//expandableListView适配器

    private View loadingHintView;//"ExpandableListView"读到数据前显示的提示视图

    private LoadingAsyncTask loadingAsyncTask;//异步读取数据的内部类
    private int lastExpand = -1;//记录上次"expandableListView"所展开的那个位置
    private View layoutView;//跟布局
    protected PopupWindow popupWindows;//点击选项菜单那时弹出的PopupWindow

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        对界面进行初始化
        layoutView = inflater.inflate(R.layout.fragment_base_expandable_list_view, container, false);
        expandableListView = (ExpandableListView) layoutView.findViewById(R.id.el_fragment_expandable_list_view);
        //子类在此做View初始化
        initBeforeReturnView();
//        开始执行异步任务工作
        loadingAsyncTask = new LoadingAsyncTask();
        loadingAsyncTask.execute();
        return layoutView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loadingAsyncTask.cancel(true);
    }

    /**
     * 在"onCreateView()"返回View之前调用，子类在此作对应初始化
     */
    protected abstract void initBeforeReturnView();

    /**
     * 返回"ExpandableListView"读到数据前显示的视图
     * @return 这是要显示的视图
     */
    protected abstract View getExpandableLoadingView();

    /**
     * 如果子类想在"ExpandableListView"没数据时显示一些提示，在这设置
     * @return "ExpandableListView"要显示的提示视图
     */
    protected abstract View getExpandableNoDataView();

    /**
     * 数据的具体来源有子类进行实现
     * @return 返回读取到的歌曲信息集合，如果没有读到信息真返回空
     */
    abstract protected List readData();

    /**
     * 异步任务读取数据完成之后回调这个方法
     * 具体时间：数据读取完成之后，expandableListView.setAdapter();方法被调用之前
     */
    abstract protected void asyncReadDataFinished();

    /**
     * 获取"ExpandableListView"的适配器
     *
     * @return "ExpandableListView"的适配器
     */
    abstract protected BaseExpandableListAdapter getExpandableAdapter();

    /**
     * 当"ExpandableListView"item被点击时将会执行这个方法
     * @param parent
     * @param v
     * @param groupPosition 被点位置
     * @param id
     */
    abstract protected boolean doOnGroupClick(ExpandableListView parent, View v, int groupPosition, long id);

    /**
     * 当"ExpandableListView"item被长按时执行这个方法
     *
     * @param parent
     * @param view
     * @param position 按下位置
     * @param id
     */
    abstract protected boolean doExpandableItemLongClick(AdapterView<?> parent, View view, int position, long id);

    /**
     * 如果要为"ExpandableListView"添加"headerView"在次返回
     *
     * @return headerView
     */
    abstract protected View getExpandableHeaderView();

    /**
     * 如果要为"ExpandableListView"添加"footerView"在此返回
     * @return footerView
     */
    abstract protected View getExpandableFooterView();


    @Override
    public void onGroupExpand(int groupPosition) {
//        以下共有三类情况
//        1，如果这次所展开的栏目和上次的同个位置，什么操作都不用做
//        2，如果两次所展开的不是同个栏目，(1)那么有可能当前是首次展开，(2)也有可能当前不是首次展开
        if (lastExpand==groupPosition) {//如果当前所展开的和上次是一个栏目
            return;
        }
        //如果当前不是首次展开
        if (lastExpand != -1) {
            expandableListView.collapseGroup(lastExpand);
        }
        lastExpand = groupPosition;//不论当前是否首次展开都要保存新的栏目位置
    }


    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//        被调用的方法在子类里实现
        return doOnGroupClick(parent, v, groupPosition, id);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        被调用的方法在子类里实现
        return doExpandableItemLongClick(parent, view, position, id);
    }

    protected final void expanGroups(int groupPos){
        expandableListView.expandGroup(groupPos);
    }

    protected final void collapseGroup(int groupPos){
        expandableListView.collapseGroup(groupPos);
    }

    protected final void loadingDataAsync(){
        new LoadingAsyncTask().execute();
    }

    //    异步读取数据用的任务
    private class LoadingAsyncTask extends AsyncTask<Void, Void, List> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            expandableListView.setVisibility(View.GONE);
            addLoadingHintView();
        }

        @Override
        protected List doInBackground(Void... params) {
            try {
                Thread.sleep(400L);
            } catch (Exception e) {
                Log.i("test", this.getClass().getName() + "--" + e.toString());
            }
            return readData();
        }

        @Override
        protected void onPostExecute(List dataList) {
            super.onPostExecute(dataList);
//            移除"loadingHintView"
            if(loadingHintView!=null){
                ((ViewGroup) layoutView).removeView(loadingHintView);
            }
//            添加"emptyView"
            setExpandableEmptyView();
//            如果没读取到数据
            if (dataList == null) {
            } else {
                asyncReadDataFinished();//通知子类数据已经读取完成
                expandableListViewInit();
                expandableListView.setVisibility(View.VISIBLE);
            }
        }

        private void addLoadingHintView() {
            if(loadingHintView==null){
                //显示读取数据前的提示
                loadingHintView = getExpandableLoadingView();
            }
            if (loadingHintView != null) {
                ((ViewGroup) layoutView).addView(loadingHintView);
            }
        }

        //给"ExpandableListView"添加"EmptyView"
        private void setExpandableEmptyView() {
            if(expandableListView.getEmptyView()==null){
                View emptyView = getExpandableNoDataView();
                if(emptyView!=null){
                    ((ViewGroup) layoutView).addView(emptyView);
                    expandableListView.setEmptyView(emptyView);
                }
            }
        }


        //"ExpandableListView"初始化的相关设置
        private void expandableListViewInit() {
            expandableListView.setOnGroupExpandListener(BaseExpandableListFragment.this);
            expandableListView.setGroupIndicator(null);
//            点击事件相关设置
            expandableListView.setOnGroupClickListener(BaseExpandableListFragment.this);
            expandableListView.setOnItemLongClickListener(BaseExpandableListFragment.this);
//            添加头尾view
            if (expandableListView.getHeaderViewsCount()==0){
                View headerView = getExpandableHeaderView();
                if (headerView != null) {
                    expandableListView.addHeaderView(headerView);
                }
            }
            if(expandableListView.getFooterViewsCount()==0){
                View footerView = getExpandableFooterView();
                if (footerView != null) {
                    expandableListView.addFooterView(footerView);
                }
            }
            //"ExpandableListView"添加数据
            expandableFragmentAdapter = getExpandableAdapter();
            expandableListView.setAdapter(expandableFragmentAdapter);
        }//expandableListViewInit()
    }


}
