package com.ubt.baselib.customView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ubt.baselib.R;
import com.ubt.baselib.utils.ContextUtils;
import com.ubt.globaldialog.GlobalDialog;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/5/22 20:01
 * @描述: 低电量弹出对话框
 */

public class UpgradeProgressDialog {
    private static UpgradeProgressDialog instance;
    private TextView tvProgress;
    private ProgressBar pbProgress;
    private Context mContext = ContextUtils.getContext();

    private GlobalDialog upgradeLanguageProgress = null;

    private UpgradeProgressDialog() {    }

    public static UpgradeProgressDialog getInstance() {
        if (instance == null) {
            synchronized (UpgradeProgressDialog.class) {
                if (instance == null) {
                    instance = new UpgradeProgressDialog();
                }
            }
        }

        return instance;
    }


    /**
     * 显示升级语言进度框
     */
    public void showUpgradeLanguageProgressDialog() {
        if (upgradeLanguageProgress != null) {
            upgradeLanguageProgress.dismiss();
        }
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.base_upgrade_progress_dialog, null);

        upgradeLanguageProgress = new GlobalDialog.Builder(mContext)
                                .setView(contentView)
                                .build();



        tvProgress = contentView.findViewById(R.id.tv_progress);
        pbProgress = contentView.findViewById(R.id.pb_progress);

        upgradeLanguageProgress.show();
    }

    public void updateUpgradeProgress(int progress){
        tvProgress.setText(progress + "%");
        pbProgress.setProgress(progress);
    }

    public void dismissUpgradeLanguageProgressDialog() {
        if (upgradeLanguageProgress != null) {
            upgradeLanguageProgress.dismiss();
            upgradeLanguageProgress = null;
        }
    }

}
