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
import com.ubt.baselib.skin.SkinManager;
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

        ImageView ivChoose = helper.getView(R.id.iv_choose);
        ivChoose.setImageResource(R.drawable.img_choose);
        if(robotLanguage.isSelect()){
            ivChoose.setVisibility(View.VISIBLE);
        }else {
            ivChoose.setVisibility(View.INVISIBLE);
        }

        RelativeLayout rlDownloadTip = helper.getView(R.id.rl_download_tip);
        if(robotLanguage.getResult() == -1){
            rlDownloadTip.setVisibility(View.GONE);
        }else {
            rlDownloadTip.setVisibility(View.VISIBLE);
            TextView downloadTip = helper.getView(R.id.tv_language_update_tip);
            TextView tvProgress = helper.getView(R.id.tv_progress);
            ProgressBar pbProgress = helper.getView(R.id.pb_progress);

            if(robotLanguage.getResult() == 0){
                downloadTip.setText(SkinManager.getInstance().getTextById(R.string.about_robot_language_package_downloading));
                pbProgress.setProgress(robotLanguage.getProgess());
                tvProgress.setText(robotLanguage.getProgess() + "%");

                downloadTip.setVisibility(View.VISIBLE);
                downloadTip.setTextColor(mContext.getResources().getColor(R.color.base_blue));
                pbProgress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.shape_progressbar_mini));
                if(robotLanguage.getProgess() == 100){
                    downloadTip.setVisibility(View.GONE);
                }
            }else {
                downloadTip.setText(SkinManager.getInstance().getTextById(R.string.about_robot_language_package_downloading));
                pbProgress.setProgress(robotLanguage.getProgess());
                tvProgress.setText(robotLanguage.getProgess() + "%");

                downloadTip.setVisibility(View.VISIBLE);
                downloadTip.setTextColor(mContext.getResources().getColor(R.color.base_color_red));
                pbProgress.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.shape_progressbar_mini_red));
                ivChoose.setImageResource(R.drawable.img_wrong);
                ivChoose.setVisibility(View.VISIBLE);
            }
        }

    }
}

