package com.ubt.en.alpha1e.ble.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.orhanobut.dialogplus.DialogPlus;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseDialog;
import com.ubt.baselib.customView.BaseUpdateRobotDialog;
import com.ubt.baselib.customView.BaseUpdateTipDialog;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.en.alpha1e.ble.Contact.RobotStatuContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.R2;
import com.ubt.en.alpha1e.ble.model.SystemRobotInfo;
import com.ubt.en.alpha1e.ble.model.UpgradeProgressInfo;
import com.ubt.en.alpha1e.ble.presenter.RobotStatuPrenster;
import com.vise.log.ViseLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


@Route(path = ModuleUtils.Bluetooh_BleStatuActivity)
public class RobotStatuActivity extends MVPBaseActivity<RobotStatuContact.View, RobotStatuPrenster> implements RobotStatuContact.View {

    private static final int UPDATE_AUTO_UPGRADE = 1; //更新自动升级状态
    private static final int UPDATE_UPGRADE_PROGRESS = 2; //更新升级进度
    private static final int UPDATE_ROBOT_LANGUAGE = 3; //更新机器人语言


    Unbinder mUnbinder;
    @BindView(R2.id.iv_robot_back)
    ImageView mIvRobotBack;
    @BindView(R2.id.tv_robot_update)
    TextView mTvRobotUpdate;
    @BindView(R2.id.ckb_auto_upgrade)
    CheckBox ckbAutoUpgrade;
    @BindView(R2.id.tv_system_version)
    TextView mTvSystemVersion;
    @BindView(R2.id.tv_robot_version)
    TextView mTvRobotVersion;
    @BindView(R2.id.tv_system_update_tip)
    TextView mTvSystemUpdateTip;
    @BindView(R2.id.system_version_progress)
    ProgressBar mSystemVersionProgress;
    @BindView(R2.id.tv_system_progress)
    TextView mTvSystemProgress;
    @BindView(R2.id.tv_firmware_tip)
    TextView mTvFirmwareTip;
    @BindView(R2.id.tv_firmware_version)
    TextView mTvFirmwareVersion;
    @BindView(R2.id.tv_firmware_update_tip)
    TextView mTvFirmwareUpdateTip;
    @BindView(R2.id.firmware_version_progress)
    ProgressBar mFirmwareVersionProgress;
    @BindView(R2.id.tv_firmware_progress)
    TextView mTvFirmwareProgress;
    @BindView(R2.id.tv_hardware_serial)
    TextView mTvHardwareSerial;
    @BindView(R2.id.iv_download_system_fail_warning)
    ImageView mIvDownloadSystemFailWarning;
    @BindView(R2.id.iv_download_firm_fail_warning)
    ImageView mIvDownloadFirmFailWarning;


    private boolean mCurrentAutoUpgrade = false;


    @Override
    public int getContentViewId() {
        return R.layout.ble_activity_robot_statu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        mPresenter.init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppStatusUtils.setBtBussiness(true);
        ViseLog.d("-onResume-");
        mPresenter.getRobotAutoState();

    }

    @OnClick({R2.id.iv_robot_back, R2.id.ckb_auto_upgrade, R2.id.tv_robot_version, R2.id.tv_firmware_version})
    public void clickView(View view) {
        int i = view.getId();
        if (i == R.id.iv_robot_back) {
            finish();
        } else if (i == R.id.ckb_auto_upgrade) {
            ViseLog.d("ckb_auto_upgrade");
            //成功之后才切换
            ckbAutoUpgrade.setChecked(!ckbAutoUpgrade.isChecked());
            new BaseDialog.Builder(this)
                    .setMessage(mCurrentAutoUpgrade ? R.string.about_robot_auto_update_off : R.string.about_robot_auto_update_on)
                    .setConfirmButtonId(mCurrentAutoUpgrade ? R.string.common_btn_switch_off : R.string.common_btn_switch_on)
                    .setConfirmButtonColor(mCurrentAutoUpgrade ? R.color.base_color_red : R.color.base_blue)
                    .setCancleButtonID(R.string.base_cancel)
                    .setCancleButtonColor(R.color.black)
                    .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                        @Override
                        public void onClick(DialogPlus dialog, View view) {
                            if (view.getId() == R.id.button_confirm) {
                                dialog.dismiss();
                                switchAutoUpgradeStatus();
                            } else if (view.getId() == R.id.button_cancle) {
                                dialog.dismiss();
                            }
                        }
                    }).create().show();

        } else if (i == R.id.tv_firmware_version) {//固件版本是否更新
            if (true) {
                BaseUpdateRobotDialog.getInstance().show(new BaseUpdateRobotDialog.IDialogClick() {
                    @Override
                    public void onConnect() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        } else if (i == R.id.tv_robot_version) {//系统版本是否更新
            if (true) {
                BaseUpdateTipDialog.getInstance().show();
            }
        }
    }


    @Override
    public void setRobotSoftVersion(SystemRobotInfo systemRobotInfo) {
        if (!TextUtils.isEmpty(systemRobotInfo.curVersion)) {
            mTvRobotVersion.setText(systemRobotInfo.curVersion);
        }
    }


    @Override
    public void setAutoUpgradeStatus(int status) {
        ViseLog.d("setAutoUpgradeStatus = " + status);

        if (status == 0) {//关闭
            mCurrentAutoUpgrade = false;
        } else if (status == 1) {
            mCurrentAutoUpgrade = true;
        } else {
            //2 设置中... 不做处理
        }
        ckbAutoUpgrade.setChecked(mCurrentAutoUpgrade);
    }

    /**
     * 系统版本下载进度
     *
     * @param progressInfo
     */
    @Override
    public void updateUpgradeProgress(UpgradeProgressInfo progressInfo) {

        ViseLog.d("UpgradeProgressInfo = " + progressInfo);
        if (progressInfo != null) {
            if (progressInfo.status == 0) {//download fail
                mTvSystemUpdateTip.setText(SkinManager.getInstance().getTextById(R.string.about_robot_auto_update_download_fail));
                mTvSystemUpdateTip.setTextColor(getResources().getColor(R.color.base_color_red));
                mTvSystemUpdateTip.setVisibility(View.VISIBLE);
                mIvDownloadSystemFailWarning.setVisibility(View.VISIBLE);
            } else if (progressInfo.status == 1) {//downloading
                if (!TextUtils.isEmpty(progressInfo.progress)) {
                    mTvSystemUpdateTip.setVisibility(View.VISIBLE);
                    mSystemVersionProgress.setVisibility(View.VISIBLE);
                    mTvFirmwareProgress.setVisibility(View.VISIBLE);
                    mSystemVersionProgress.setProgress(Integer.parseInt(progressInfo.progress));
                    mTvFirmwareProgress.setText(progressInfo.progress);
                }
                mIvDownloadSystemFailWarning.setVisibility(View.GONE);

            } else if (progressInfo.status == 2) {//download success
                mIvDownloadSystemFailWarning.setVisibility(View.GONE);
                mTvSystemUpdateTip.setVisibility(View.GONE);
                mTvFirmwareProgress.setVisibility(View.GONE);
                mTvFirmwareProgress.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 胸口版下载进度
     *
     * @param progressInfo
     */
    @Override
    public void updateFirmProgress(UpgradeProgressInfo progressInfo) {
        ViseLog.d("UpgradeProgressInfo = " + progressInfo);
        if (progressInfo != null) {
            if (progressInfo.status == 0) {//download fail
                mTvFirmwareUpdateTip.setText(SkinManager.getInstance().getTextById(R.string.about_robot_auto_update_download_fail));
                mTvFirmwareUpdateTip.setTextColor(getResources().getColor(R.color.base_color_red));
                mTvFirmwareUpdateTip.setVisibility(View.VISIBLE);
                mIvDownloadFirmFailWarning.setVisibility(View.VISIBLE);
            } else if (progressInfo.status == 1) {//downloading
                if (!TextUtils.isEmpty(progressInfo.progress)) {
                    mTvFirmwareUpdateTip.setVisibility(View.VISIBLE);
                    mFirmwareVersionProgress.setVisibility(View.VISIBLE);
                    mTvSystemProgress.setVisibility(View.VISIBLE);
                    mFirmwareVersionProgress.setProgress(Integer.parseInt(progressInfo.progress));
                    mTvSystemProgress.setText(progressInfo.progress);
                }
                mIvDownloadFirmFailWarning.setVisibility(View.GONE);

            } else if (progressInfo.status == 2) {//download success
                mIvDownloadFirmFailWarning.setVisibility(View.GONE);
                mTvFirmwareUpdateTip.setVisibility(View.GONE);
                mFirmwareVersionProgress.setVisibility(View.GONE);
                mTvSystemProgress.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setRobotHardVersion(String hardVersion) {
        mTvHardwareSerial.setText(hardVersion);
    }


    @Override
    protected void onStop() {
        super.onStop();
        AppStatusUtils.setBtBussiness(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unRegister();
        mUnbinder.unbind();
    }


    /**
     * 切换自动升级开关状态
     */
    private void switchAutoUpgradeStatus() {
//        BaseLoadingDialog.dismiss(this);
//        BaseLoadingDialog.show(this);

        if (mCurrentAutoUpgrade) {
            mCurrentAutoUpgrade = false;
        } else {
            mCurrentAutoUpgrade = true;
        }
        mPresenter.doChangeAutoUpgrade(mCurrentAutoUpgrade);
    }

    /**
     * 显示升级小红点
     *
     * @param isShow 是否显示
     */
    private void showRedDot(boolean isShow) {
        if (isShow) {
            Drawable img = getResources().getDrawable(R.drawable.ble_update_red_dot);
            if (img != null) {
                img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
                // tvAboutCheck.setCompoundDrawables(img, null, null, null);
            }
        } else {
            //tvAboutCheck.setCompoundDrawables(null, null, null, null);
        }
    }


}
