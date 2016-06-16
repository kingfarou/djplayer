package com.jf.djplayer.dialogfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jf.djplayer.R;
import com.jf.djplayer.module.Song;

/**
 * Created by JF on 2015/11/28.
 */
public class SetToBellDialog extends DialogFragment {

    private Context context;
    private Song songInfo;
    private ListView setToBellListView;
    private Fragment fragment;
    private View view;
    public SetToBellDialog(){}

    public SetToBellDialog(Context context, Song songInfo){
        this.context = context;
        this.songInfo = songInfo;
    }

    public SetToBellDialog(Fragment containerFragment, Song songInfo){
        this.fragment = containerFragment;
        this.songInfo = songInfo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        viewInit();
        AlertDialog.Builder setToBellBuilder = new AlertDialog.Builder(getActivity());
        setToBellBuilder.setView(view).setTitle("设为铃声").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SetToBellDialog.this.getDialog().cancel();
            }
        });

        return setToBellBuilder.create();
    }
    /*
    绘制控件
     */
    private void viewInit(){
        view = LayoutInflater.from(context).inflate(R.layout.dialog_fragment_set_to_bell,null);
        setToBellListView = (ListView)(view.findViewById(R.id.lv_dialog_fragment_set_to_bell));
        //ListView添加数据
        //设置相关点击事件
        String[] title = new String[]{"设为来电铃声","设为通知铃声","设为所有铃声"};
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,title);
        setToBellListView.setAdapter(arrayAdapter);
        setToBellListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里进行铃声设置
                dismiss();
            }
        });
    }

}
