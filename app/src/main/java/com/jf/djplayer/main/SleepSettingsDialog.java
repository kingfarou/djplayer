package com.jf.djplayer.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.MyApplication;
import com.jf.djplayer.base.activity.BaseActivity;

/**
 * Created by jf on 2016/6/12.
 * 侧滑菜单-睡眠设置
 */
public class SleepSettingsDialog extends DialogFragment implements AdapterView.OnItemClickListener{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //找到控件
        View layoutView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_sleep_settings, null);
        ListView listView = (ListView)layoutView.findViewById(R.id.lv_dialog_sleep_settings);
        //设置控件
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layoutView);
        initListView(listView);
        return builder.create();
    }

    private void initListView(ListView listView){
        Resources resources = getActivity().getResources();
        String[] listOption =
                new String[]{resources.getString(R.string.never), resources.getString(R.string.after_ten_minutes),
                resources.getString(R.string.after_twenty_minutes), resources.getString(R.string.after_thirty_minutes),
                resources.getString(R.string.after_forty_five_minutes), resources.getString(R.string.after_sixty_minutes),
                resources.getString(R.string.after_ninety_minutes)};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listOption);
        listView.setOnItemClickListener(this);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            default:break;
        }
        MyApplication.showToast((BaseActivity)getActivity(), "睡眠功能还未实现");
        dismiss();
    }
}
