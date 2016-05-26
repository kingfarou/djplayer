package com.jf.djplayer.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.jf.djplayer.R;
import com.jf.djplayer.other.MyApplication;

/**
 * Created by JF on 2016/5/22.
 * 主页面-退出弹框
 */
public class ExitDialog extends DialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.exit).setMessage("是否退出？")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentActivity fragmentActivity = getActivity();
                        if (!(fragmentActivity instanceof ExitDialogListener)) {
                            MyApplication.printLog("\"ExitDialog\"宿主应实现\"ExitDialogListener\"接口");
                            return;
                        }
                        ((ExitDialogListener)fragmentActivity).exitApp();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    /**
     * "ExitDialog"对宿主的回调接口
     */
    public interface ExitDialogListener{
        /**
         * 退出当前的应用
         */
        public void exitApp();
    }
}
