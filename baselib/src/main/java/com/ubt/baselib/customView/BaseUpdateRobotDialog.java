package com.ubt.baselib.customView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ubt.baselib.R;
import com.ubt.baselib.utils.ContextUtils;
import com.ubt.globaldialog.GlobalDialog;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/5/24 13:55
 * @描述: 全局的蓝牙断开对话框
 */

public class BaseUpdateRobotDialog {

    private static BaseUpdateRobotDialog instance;
    private Context mContext = ContextUtils.getContext();
    private GlobalDialog btDisDialog;

    private BaseUpdateRobotDialog() {    }


    public static BaseUpdateRobotDialog getInstance() {
        if (instance == null) {
            synchronized (BaseLowBattaryDialog.class) {
                if (instance == null) {
                    instance = new BaseUpdateRobotDialog();
                }
            }
        }

        return instance;
    }


    public void show(final IDialogClick listener) {
        if (btDisDialog != null) {
            btDisDialog.dismiss();
        }
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.base_dialog_bt_disconnected, null);
        btDisDialog = new GlobalDialog.Builder(mContext)
                .setView(contentView)
                .build();
        contentView.findViewById(R.id.btn_bt_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onConnect();
                }
            }
        });
        contentView.findViewById(R.id.btn_bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onCancel();
                }
            }
        });
        btDisDialog.show();
    }



    public void dismiss() {
        if (btDisDialog != null) {
            btDisDialog.dismiss();
            btDisDialog = null;
        }
    }

    public boolean isShowing(){
        return btDisDialog!= null;
    }

    public interface IDialogClick{
        void onConnect();
        void onCancel();
    }
}
