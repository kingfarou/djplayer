package com.jf.djplayer.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jf.djplayer.R;
import com.jf.djplayer.adapter.ListViewFragmentAdapter;
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
 * 本地音乐里面三个"ListViewFragment"共有该类作为基类
 * “歌手Fragment”“专辑Fragment”“文件夹的Fragment”共用此类
 */
public abstract class BaseListViewFragment extends BaseFragment
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    private ListView listView;
    private View loadingHintView;//在"ListVIew"获取到数据之前显示的视图
    private View noDataHintView;//读取完数据之后"ListVIew"没有数据要显示时所显示的View
    protected String title = "title";
    protected String content = "content";
    protected ListViewFragmentAdapter listViewFragmentAdapter;
    protected BaseAdapter listViewAdapter;
    protected PopupWindow popupWindows;
    protected List<Map<String,String>> dataList;//装填所获取的数据用的集合
    private ReadDataAsyncTask readDataAsyncTask;
    protected View layoutView;//这是布局文件的根视图
    protected View footerView;//"ListVIew"的"FooterView"


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        获取"ListView"
        layoutView = inflater.inflate(R.layout.fragment_list_view,container,false);
        listView = (ListView)layoutView.findViewById(R.id.lv_fragment_list_view);
//        添加"ListView"加载前的默认视图
        loadingHintView = getLoadingHintView();
        if(loadingHintView!=null) {
            ((ViewGroup)listView.getParent()).addView(loadingHintView);

        }
//        子类做对应初始化
        initBeforeReturnView();
//        开始执行读数据的异步任务
        readDataAsyncTask = new ReadDataAsyncTask();
        readDataAsyncTask.execute();
        return layoutView;
    }

    /**
     * 在布局文件返回前回调方法
     * 子类可在此进行初始化
     */
    abstract protected void initBeforeReturnView();

    /**
     * 如果在"ListView"数据加载前需要相应的视图显示
     * 此类在此返回即可
     * @return "ListView"数据加载前的默认视图
     */
    abstract protected View getLoadingHintView();

    /**
     * 如果在数据读取完成后"ListView"没有数据可显示
     * 此时需要显示相关提示视图
     * 子类在此返回视图即可
     * @return "ListVIew"没有数据可显式时用的视图
     */
    abstract protected View getNoDataHintView();

    /**
     * 如果子类想要为"ListVIew"添加footerView
     * 在此返回就可以了
     * @return footerView
     */
    abstract protected View getListViewFooterView();

    /**
     * 如果子类想要为"ListVIew"添加"headerView"
     * 在此返回就可以了
     * @return headerView
     */
    abstract protected View getListViewHeaderView();

    /**
     * 异步任务调用这个方法获得数据
     * @return 这是读取到的数据集合
     */
    abstract protected List getData();

    abstract protected BaseAdapter getListViewAdapter(List dataList);

    abstract public ListViewPopupWindows getListViewPopupWindow();

    /**
     * 当异步任务完成时将会回调这个方法
     */
    abstract protected void readDataFinish(List dataList);

    /**
     * "ListView"item点击事件回调方法
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    abstract protected void doListViewOnItemClick(AdapterView<?> parent, View view, int position, long id);

    /**
     * "ListView"长按时回调的方法
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    abstract protected void doListViewOnItemLongClick(AdapterView<?> parent, View view, int position, long id);

    protected void sortAccordingTitle(){
        final Collator collator = Collator.getInstance(Locale.CHINA);
        Collections.sort(dataList, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> lhs, Map<String, String> rhs) {
                CollationKey key1 = collator
                        .getCollationKey(lhs.get(title));
                CollationKey key2 = collator
                        .getCollationKey(rhs.get(title));
                return key1.compareTo(key2);
            }
        });
    }
    protected void sortAccordingContent(){
        Collections.sort(dataList, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> lhs, Map<String, String> rhs) {
                return lhs.get(content).compareTo(rhs.get(content));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        如果用户在异步任务完成前离开界面
//        则要取消异步任务
        readDataAsyncTask.cancel(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        doListViewOnItemClick(parent, view, position, id);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        doListViewOnItemLongClick(parent, view, position, id);
        return true;
    }

    //    异步读取数据的内部类
    private class  ReadDataAsyncTask extends AsyncTask<Void, Void, List>{

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
            ((ViewGroup)layoutView).removeView(loadingHintView);
//            添加"listView"的"emptyView"
            setListViewEmptyView();
            if(dataList==null){
            }else{
//                如果读取到有数据，将数据给显示出来
//                子类进行相关设置
                readDataFinish(dataList);//任务完成之后回调方法
                // listView做初始化
                listViewInit();
//                将数据设置进适配器里
//                listViewFragmentAdapter = new ListViewFragmentAdapter(getActivity(), mapList);
                listViewAdapter = getListViewAdapter(dataList);
                listView.setAdapter(listViewAdapter);
                listView.setVisibility(View.VISIBLE);
            }
        }

//        给"ListView"添加没数据显示的默认视图
        private void setListViewEmptyView(){
            noDataHintView = getNoDataHintView();
            if(noDataHintView!=null) {
                ((ViewGroup)layoutView).addView(noDataHintView);
                listView.setEmptyView(noDataHintView);
            }
        }//setListViewEmptyView()

//        当读取到有数据时，"listView"的初始化
        private void listViewInit(){
//            设置相应点击事件
            listView.setOnItemClickListener(BaseListViewFragment.this);
            listView.setOnItemLongClickListener(BaseListViewFragment.this);
//            添加头尾View
            View listViewHeaderView = getListViewHeaderView();
            if(listViewHeaderView!=null){
                listView.addHeaderView(listViewHeaderView);
            }
            View listViewFooterView = getListViewFooterView();
            if(listViewFooterView!=null){
                listView.addFooterView(listViewFooterView);
            }
        }//listViewInit()
    }

}
