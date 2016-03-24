package com.jf.djplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jf.djplayer.adapter.SelectedFileAdapter;
import com.jf.djplayer.R;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/12.
 * 让用户选择扫描路径的窗体
 * 数据有适配器进行适配
 * 同时通过适配器的公有方法获取用户所选择的数据
 */
public class SelectedFileActivity extends Activity implements View.OnClickListener {

    private LinearLayout returnLinearLayout = null;//最顶上的返回上级目录按钮
    private ListView fileDirectoryListView = null;//用来显示指定路径下的所有文件
    private com.jf.djplayer.adapter.SelectedFileAdapter SelectedFileAdapter = null;//适配器
    private List<File> displayDir = null;//集合里面装着适配器要显示的数据
    private TextView currentDirectoryTv = null;//显示当前目录名字
    private Button startScanButton = null;//最底下的开始扫描按钮
    //获取存储卡根节点路径
    final private File rootFile = Environment.getExternalStorageDirectory();
    private File currentDir = null;//这个变量永远指向当前所显示的路径
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_file);
        getActionBar().setDisplayHomeAsUpEnabled(true);//低版本下窗体的回退键
        initDir();
        initView();

    }

    /*
    * 对存储卡路径文件作初始化
    * 只有“路径”以及mp3文件可以装进去
    * */
    private void initDir(){
        currentDir = rootFile;//初始化是当前路径就是根节点的路径
        displayDir = new ArrayList<File>();//集合用来装填要展示的路径
        //只有路径或者音乐能装进去
        for (File file : rootFile.listFiles()){
            if (file.isDirectory()) this.displayDir.add(file);
            else if (file.toString().endsWith(".mp3")) this.displayDir.add(file);
        }
    }


    /**
     * 这里寻找布局控件
    */
    private void initView(){
        //寻找及设置监听器
        returnLinearLayout = (LinearLayout)findViewById(R.id.ll_selected_file_return_direction);
        currentDirectoryTv =  (TextView)findViewById(R.id.tv_selected_file_current_dir);
        fileDirectoryListView = (ListView)findViewById(R.id.lv_selected_file_file_directory);
        startScanButton = (Button)findViewById(R.id.btn_selected_file_start_scan);
        returnLinearLayout.setOnClickListener(this);
        startScanButton.setOnClickListener(this);
//        设置显示列表上的数据
        SelectedFileAdapter = new SelectedFileAdapter(this,displayDir);
        fileDirectoryListView.setAdapter(SelectedFileAdapter);
        currentDirectoryTv.setText(currentDir.getAbsolutePath());
        fileDirectoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取所点击的文件名字
                TextView fileNameTv = (TextView) view.findViewById(R.id.tv_item_selected_file_filename);
                String fileNameString = fileNameTv.getText().toString();
                //如果选择的是一个路径
                if (new File(currentDir,fileNameString).isDirectory()){
                    //更新新的路径全名显示
                    currentDirectoryTv.setText(new File(currentDir, fileNameString).getAbsolutePath());
                    //同时修改新的当前路径全名
                    currentDir = new File(currentDir,fileNameString);
                    displayDir.clear();//将原有集合的数据全部清空
                    //只有路径或者音乐能装进去
                    for (File file : currentDir.listFiles()) {
                        if (file.isDirectory()) displayDir.add(file);
                        else if (file.toString().endsWith(".mp3")) displayDir.add(file);
                    }
                    //刷新适配器的数据
                    SelectedFileAdapter.updateData(displayDir);
                    fileDirectoryListView.setAdapter(SelectedFileAdapter);
                }else{
                    Log.i("test","点击一个音乐文件");
                }
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //如果点击窗体顶部的上一级按钮
            case R.id.ll_selected_file_return_direction://如果点击返回上级目录
                if (currentDir.getParent()==null){//如果已经到根节点
                    finish();//直接结束窗体即可
                }else {
                    //设置当前路径显示为上一级目录
                    currentDirectoryTv.setText(currentDir.getParentFile().getAbsolutePath());
                    currentDir = currentDir.getParentFile();//更新当前文件路径
                    displayDir.clear();//将原有集合的数据全部清空
                    //只有路径或者音乐能装进去
                    for (File file : currentDir.listFiles()){
                        if (file.isDirectory()) displayDir.add(file);
                        else if (file.toString().endsWith(".mp3")) displayDir.add(file);
                    }
                    //刷新数据适配器的数据
                    SelectedFileAdapter.updateData(displayDir);
                    fileDirectoryListView.setAdapter(SelectedFileAdapter);
                    break;
                }
                //点击开始扫描按钮
            case R.id.btn_selected_file_start_scan:
                List<File> selectedFileList = SelectedFileAdapter.getSelectedFileList();//获取用户所选择的所有文件
                //如果用户已选择了路径
                if (selectedFileList.size()!=0){

                    //将用户所选择路径返回给上一级
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("selectedFileList",(Serializable)selectedFileList);
                    setResult(RESULT_OK,resultIntent);
                    finish();//关窗
                }
                //如果用户没有选择任何路径
                else{
                    Toast.makeText(this,"您未选择任何文件",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //如果按下了回退键
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (currentDir.getParent()==null){//如果已经到根节点
                finish();//直接结束窗体即可
            }else {
                currentDirectoryTv.setText(currentDir.getParentFile().getAbsolutePath());
                currentDir = currentDir.getParentFile();//更新当前文件路径
                displayDir.clear();//将原有集合的数据全部清空
                //只有路径或者音乐能装进去
                for (File file : currentDir.listFiles()){
                    if (file.isDirectory()) displayDir.add(file);
                    else if (file.toString().endsWith(".mp3")) displayDir.add(file);
                }
                SelectedFileAdapter.updateData(displayDir);
                fileDirectoryListView.setAdapter(SelectedFileAdapter);

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            //回到上一个窗体的按钮
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

}