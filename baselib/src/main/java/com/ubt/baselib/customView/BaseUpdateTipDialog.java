package com.ubt.baselib.customView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ubt.baselib.R;
import com.ubt.baselib.btCmd1E.cmd.BTCmdStartUpgradeSoft;
import com.ubt.baselib.utils.ContextUtils;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.globaldialog.GlobalDialog;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/5/24 13:55
 * @描述: 提示机器人开始OTA升级
 */

public class BaseUpdateTipDialog {

    private static BaseUpdateTipDialog instance;
    private Context mContext = ContextUtils.getContext();
    private GlobalDialog updateDialog;

    private BaseUpdateTipDialog() {    }


    public static BaseUpdateTipDialog getInstance() {
        if (instance == null) {
            synchronized (BaseLowBattaryDialog.class) {
                if (instance == null) {
                    instance = new BaseUpdateTipDialog();
                }
            }
        }

        return instance;
    }


    public void show() {
        if (updateDialog != null) {
            updateDialog.dismiss();
        }
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.base_dialog_robot_upgrade, null);
        updateDialog = new GlobalDialog.Builder(mContext)
                .setView(contentView)
                .build();
        contentView.findViewById(R.id.btn_not_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        contentView.findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlueClientUtil.getInstance().sendData(new BTCmdStartUpgradeSoft(BTCmdStartUpgradeSoft.START_UPDATE).toByteArray());
                dismiss();
            }
        });
        updateDialog.show();
    }



    public void dismiss() {
        if (updateDialog != null) {
            updateDialog.dismiss();
            updateDialog = null;
        }
    }

    public boolean isShowing(){
        return updateDialog!= null;
    }

}
