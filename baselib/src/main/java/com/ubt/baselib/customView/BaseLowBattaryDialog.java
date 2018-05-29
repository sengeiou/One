package com.ubt.baselib.customView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.ubt.baselib.R;
import com.ubt.baselib.utils.ContextUtils;
import com.ubt.globaldialog.GlobalDialog;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/5/22 20:01
 * @描述: 低电量弹出对话框
 */

public class BaseLowBattaryDialog {
    private static BaseLowBattaryDialog instance;
    private Button btnLowpowerOk;
    private Context mContext = ContextUtils.getContext();
    private GlobalDialog low5Dialog;
    private GlobalDialog low5ActionDialog;

    private BaseLowBattaryDialog() {    }


    public static BaseLowBattaryDialog getInstance() {
        if (instance == null) {
            synchronized (BaseLowBattaryDialog.class) {
                if (instance == null) {
                    instance = new BaseLowBattaryDialog();
                }
            }
        }

        return instance;
    }


    /**
     * 显示电量低于5%的对话框
     */
    public void showLow5Dialog(final IDialog5Click okListener) {
        if (low5Dialog != null) {
            low5Dialog.dismiss();
        }
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.base_dialog_lowbattary_5, null);
        low5Dialog = new GlobalDialog.Builder(mContext)
                .setView(contentView)
                .build();
        btnLowpowerOk = contentView.findViewById(R.id.btn_lowpower_ok);
        btnLowpowerOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissLow5Dialog();
                if(okListener != null){
                    okListener.onOK();
                }
            }
        });
        low5Dialog.show();
    }

    public void dissLow5Dialog() {
        if (low5Dialog != null) {
            low5Dialog.dismiss();
            low5Dialog = null;
        }
    }

    /**
     * 显示电量低于5%但界面在编程页面时的对话框
     */
    public void showLow5ActionDialog(final IDialog5ActionClick saveListener) {
        if (low5ActionDialog != null) {
            low5ActionDialog.dismiss();
        }

        View viewTwoBtn = LayoutInflater.from(mContext).inflate(R.layout.base_dialog_lowbattary_5_action, null);
        low5ActionDialog = new GlobalDialog.Builder(mContext)
                .setView(viewTwoBtn)
                .build();
        viewTwoBtn.findViewById(R.id.btn_lowpower_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissLow5ActionDialog();
                if(saveListener != null){
                    saveListener.onCancel();
                }
            }
        });
        viewTwoBtn.findViewById(R.id.btn_lowpower_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dissLow5ActionDialog();
                if(saveListener != null){
                    saveListener.onSave();
                }
            }
        });
        low5ActionDialog.show();
    }

    public void dissLow5ActionDialog() {
        if (low5ActionDialog != null) {
            low5ActionDialog.dismiss();
            low5ActionDialog = null;
        }
    }



    public interface IDialog5ActionClick{
        void onCancel();
        void onSave();
    }

    public interface IDialog5Click{
        void onOK();
    }
}
