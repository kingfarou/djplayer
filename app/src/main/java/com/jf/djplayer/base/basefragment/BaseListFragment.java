package com.jf.djplayer.base.basefragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.jf.djplayer.R;
import com.jf.djplayer.customview.ListViewPopupWindows;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by JF on 2016/1/23.
 * 所有使用"ListView"的"Fragment"的基类
 * 该类定义所有使用"ListView"显示歌曲信息的"Fragment"的显示样式
 * 该抽象类已经实现的功能有
 * 异步读取"ListView"显示所需要的数据（在“_getData()”方法里面）
 * 读取之前有进度条提示用户读取之后正常显示数据
 * 读取完成将数据设置到"ListView"里面
 * 子类根据需要重写方法即可
 */
public abstract class BaseListFragment extends BaseFragment
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    protected ListView listView;
    protected View loadingHintView;//在"ListVIew"获取到数据之前显示的视图
    protected View listViewEmptyView;//读取完数据之后"ListView"没有数据要显示时所显示的View

    protected BaseAdapter listViewAdapter;
    protected List dataList;//装填所获取的数据用的集合

    protected View rootView;//这是布局文件的根视图

    private ReadDataAsyncTask readDataAsyncTask;//内部类，用来读取异步任务，给"ListView"加载数据

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        获取"ListView"
        rootView = inflater.inflate(R.layout.fragment_list_view, container, false);
        listView = (ListView) rootView.findViewById(R.id.lv_fragment_list_view);

        //数据载入时的提示界面、没有数据可显示的提示界面的初始化
        loadingHintView = getLoadingHintView();
        listViewEmptyView = getListViewEmptyView();
        if(loadingHintView!=null){
            ((ViewGroup)rootView).addView(loadingHintView);
        }
        if(listViewEmptyView!=null){
            ((ViewGroup)rootView).addView(listViewEmptyView);
            listViewEmptyView.setVisibility(View.INVISIBLE);
        }

//        开始执行读数据的异步任务
        readDataAsyncTask = new ReadDataAsyncTask();
        readDataAsyncTask.execute();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        如果用户在异步任务完成前离开界面
//        则要取消异步任务
        readDataAsyncTask.cancel(true);
    }


    /**
     * 如果在"ListView"数据加载前需要相应的视图显示
     * 此类在此返回即可
     * @return "ListView"数据加载前的默认视图
     */
    protected View getLoadingHintView(){
        return null;
    }

    /**
     * 如果在数据读取完成后"ListView"没有数据可显示
     * 此时需要显示相关提示视图
     * 子类在此返回视图即可
     * @return "ListVIew"没有数据可显式时用的视图
     */
    protected View getListViewEmptyView(){
        return null;
    }

    /**
     * 如果子类想要为"ListVIew"添加"headerView"
     * 在此返回就可以了
     * @return headerView
     */
    protected View getListViewHeaderView(){
        return null;
    }

    /**
     * 如果子类想要为"ListView"添加footerView
     * 在此返回就可以了
     * @return footerView
     */
    protected View getListViewFooterView(){
        return null;
    }

    /**
     * 异步任务调用这个方法获得数据，子类在此返回自己所特有的数据集合
     * @return 这是读取到的数据集合
     */
    abstract protected List getData();

    /**
     * 获取子类的适配器
     * @param dataList getData()方法所读到的数据
     * @return 子类所特有数据适配器
     */
    abstract protected BaseAdapter getListViewAdapter(List dataList);

    /**
     * 当异步任务完成时将会回调这个方法
     */
    protected void readDataFinish(List dataList){
    }

    /**
     * "ListView"item点击事件回调方法
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    protected void doListViewOnItemClick(AdapterView<?> parent, View view, int position, long id){
    }

    /**
     * "ListView"长按时回调的方法
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    protected void doListViewOnItemLongClick(AdapterView<?> parent, View view, int position, long id){
    }

    /**
     * "LiseView"点击事件响应方法
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        doListViewOnItemClick(parent, view, position, id);
    }

    /**
     * "ListView"长按事件响应方法
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        doListViewOnItemLongClick(parent, view, position, id);
        return true;
    }

    /**
     * 异步刷新数据用的方法
     */
    protected final void refreshDataAsync(){
        readDataAsyncTask = new ReadDataAsyncTask();
        readDataAsyncTask.execute();
    }

    //    异步读取数据的内部类
    private class  ReadDataAsyncTask extends AsyncTask<Void, Void, List>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideListView();
            showLoadingHintView();
        }

        @Override
        protected List doInBackground(Void... params) {
            try {
                Thread.sleep(400L);
            }catch (Exception e){}
            return getData();
        }

        protected void onPostExecute(List dataList) {
            super.onPostExecute(dataList);
//            移除"loadingHintView"
            hideLoadingHintView();
            showListView();
            if(dataList==null){
            }else{
//                子类进行相关设置
                BaseListFragment.this.dataList = dataList;
                // listView做初始化
                listViewInit();
//                readDataFinish(dataList);//任务完成之后回调方法
                //            将数据给设置上去
                listViewAdapter = getListViewAdapter(dataList);
                readDataFinish(dataList);//任务完成之后回调方法
                listView.setAdapter(listViewAdapter);
            }
        }

        //隐藏"ListView"和其"EmptyView"
        private void hideListView(){
            //断开"ListView"对其"EmptyView"的控制
            listView.setEmptyView(null);
            if(listViewEmptyView!=null){
                listViewEmptyView.setVisibility(View.INVISIBLE);
            }
            listView.setVisibility(View.INVISIBLE);

        }

        private void showLoadingHintView(){
            //添加"ListView"加载前的默认视图
            if(loadingHintView!=null){
                loadingHintView.setVisibility(View.VISIBLE);
            }
        }

        //移除"showLoadingHintView()"方法所设置的视图
        private void hideLoadingHintView(){
            if(loadingHintView!=null){
                loadingHintView.setVisibility(View.INVISIBLE);
            }
        }

        //显示"ListView"以及添加"EmptyView"
        private void showListView(){
            listView.setVisibility(View.VISIBLE);
            listView.setEmptyView(listViewEmptyView);
        }//_showListView()

//        当读取到有数据时，"listView"的初始化
        private void listViewInit(){
//            设置相应点击事件
            listView.setOnItemClickListener(BaseListFragment.this);
            listView.setOnItemLongClickListener(BaseListFragment.this);
//            添加头View
            if(listView.getHeaderViewsCount()==0){
                View headerView = getListViewHeaderView();
                if(headerView!=null){
                    listView.addHeaderView(headerView);
                }
            }
//            添加尾View
            if(listView.getFooterViewsCount()==0){
                View footerView = getListViewFooterView();
                if(footerView!=null){
                    listView.addFooterView(footerView);
                }
            }
        }//listViewInit()
    }

}
