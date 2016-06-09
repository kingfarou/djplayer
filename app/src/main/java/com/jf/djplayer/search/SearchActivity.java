package com.jf.djplayer.search;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.jf.djplayer.R;
import com.jf.djplayer.base.baseactivity.BaseNoActionBarActivity;
import com.jf.djplayer.customview.CustomTitles;

import java.util.List;

/**
 * Created by JF on 2016/5/8.
 * 搜索与查找
 * 分类搜索歌曲信息用的"Activity"
 * 负责接受用户输入搜索内容，
 * 搜索结果的显示由其包含的"Fragment"显示
 * 搜索列表样式只有两类，分别对应"ExpandableListView"列表以及"ListView"列表
 */
public class SearchActivity extends BaseNoActionBarActivity
        implements CustomTitles.FragmentTitleListener, TextWatcher,
        SearchedDataProvider {

    private CustomTitles customTitles;//标题
    private EditText searchKeywordEt;//用户输入搜索内容
    private SearcherFragment searcherFragment;//搜索用的"Fragment"

    //跟搜索有关的信息常量
    /*要显示的"Fragment"类型*/
    public static final String FRAGMENT_TYPE = "fragmentType";//这是键
    public static final String EXPANDABLE_LIST_VIEW = "EXPANDABLE_LIST_VIEW";//表示需要展示的是"BaseExpandListFragment"或其子类
    public static final String LIST_VIEW = "LIST_VIEW";//表示需要展示的是"BaseListViewFragment"或其子类
    /*要显示的"Fragment"类型*/

    public static final String EDIT_TEXT_HINT = "editTextHint";//用户没有输入数据时要显示的提示内容
    public static final String SEARCH_LIST = "searchList";//这是待搜索的数据集合

    @Override
    protected int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        //对标题栏显示部分做初始化
        fragmentTitleInit();
        editInit();
        //对所添加的"Fragment"做初始化
        fragmentInit();
    }

    @Override
    protected void initExtrasBeforeView() {

    }

    //对标题栏做初始化
    private void fragmentTitleInit(){
        customTitles = (CustomTitles)findViewById(R.id.fragment_title_layout_activity_search);
        customTitles.setTitleIcon(R.drawable.ic_return);
        customTitles.setSearchVisibility(View.GONE);//设置标题栏“搜索”按钮不可见
        customTitles.setMenuVisibility(View.GONE);//设置标题栏“菜单”按钮不可见
        customTitles.setTitleText("查找");
        //给标题栏里的按钮设置监听
        customTitles.setTitleClickListener(this);
    }

    //用户输入框初始化
    private void editInit(){
        searchKeywordEt = (EditText) findViewById(R.id.et_activity_search_key_word);
        searchKeywordEt.addTextChangedListener(this);
        //设置要显示的提示内容
        String editTextHint = getIntent().getStringExtra(EDIT_TEXT_HINT);
        if(editTextHint == null){
            return;
        }
        searchKeywordEt.setHint(editTextHint);
    }

    //对所加载的"Fragment"做初始化
    private void fragmentInit(){
        String fragmentType = getIntent().getStringExtra(FRAGMENT_TYPE);
        if(fragmentType == null){
            return;
        }
        //根据标记判断加载哪类"Fragment"
        switch(fragmentType){
            case EXPANDABLE_LIST_VIEW:
                searcherFragment = new ExpandListSearchFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fl_activity_search_fragment_group, (Fragment) searcherFragment).commit();
                break;
            case LIST_VIEW:
                searcherFragment = new ListViewSearchFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fl_activity_search_fragment_group, (Fragment) searcherFragment).commit();
                break;
            default:break;
        }
    }


    /*"TextWatch"方法覆盖--start*/
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //一旦用户有所输入，立即搜索
        String keyword = s.toString();
        searcherFragment.search(keyword);
    }
    /*"TextWatch"方法覆盖--end*/

    //"SearchDataInterface"方法覆盖
    @Override
    public List returnSearchedDataList() {
        //将待搜索的数据返回给"Fragment"
        return (List)getIntent().getSerializableExtra(SEARCH_LIST);
    }

    @Override
    public void onTitleClick() {
        finish();
    }

    @Override
    public void onSearchIvOnclick() {
        //搜索按钮没有显示，所以这里不用做任何事
    }

    @Override
    public void onMoreIvOnclick() {
        //“更多”按钮没有显示所以这里不用做任何事
    }
}
