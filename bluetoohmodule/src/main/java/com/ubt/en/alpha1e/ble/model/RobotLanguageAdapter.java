package com.ubt.en.alpha1e.ble.model;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ubt.en.alpha1e.ble.R;
import com.vise.log.ViseLog;

import java.util.List;

/**
 * @author：liuhai
 * @date：2018/4/11 17:24
 * @modifier：ubt
 * @modify_date：2018/4/11 17:24
 * [A brief description]
 * version
 */

public class RobotLanguageAdapter extends BaseQuickAdapter<RobotLanguage, BaseViewHolder> {

    public RobotLanguageAdapter(@LayoutRes int layoutResId, @Nullable List<RobotLanguage> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RobotLanguage robotLanguage) {

        helper.setText(R.id.tv_robot_language, robotLanguage.getLanguageName());
        helper.setText(R.id.tv_robot_language_sub, robotLanguage.getLanguageSingleName());

        ViseLog.d("robotLanguage = " + robotLanguage.getLanguageName());

        RelativeLayout rlDownloadTip = helper.getView(R.id.rl_download_tip);
        rlDownloadTip.setVisibility(View.VISIBLE);

        TextView downloadTip = helper.getView(R.id.tv_language_update_tip);
        ProgressBar pbProgress = helper.getView(R.id.pb_progress);

        ImageView ivChoose = helper.getView(R.id.iv_choose);
        if(robotLanguage.isSelect()){
            ivChoose.setVisibility(View.VISIBLE);
        }else {
            ivChoose.setVisibility(View.INVISIBLE);
        }

    }
}

