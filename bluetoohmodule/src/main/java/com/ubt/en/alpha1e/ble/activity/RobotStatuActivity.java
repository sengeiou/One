package com.ubt.en.alpha1e.ble.activity;

import android.content.Intent;
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
import com.ubt.baselib.btCmd1E.cmd.BTCmdStartUpgradeSoft;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseDialog;
import com.ubt.baselib.customView.BaseUpdateTipDialog;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.ble.Contact.RobotStatuContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.R2;
import com.ubt.en.alpha1e.ble.model.BleDownloadLanguageRsp;
import com.ubt.en.alpha1e.ble.model.BleRobotVersionInfo;
import com.ubt.en.alpha1e.ble.model.BleSwitchLanguageRsp;
import com.ubt.en.alpha1e.ble.model.SystemRobotInfo;
import com.ubt.en.alpha1e.ble.model.UpgradeProgressInfo;
import com.ubt.en.alpha1e.ble.presenter.RobotStatuPrenster;
import com.vise.log.ViseLog;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


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

    private SystemRobotInfo mSystemRobotInfo;
    private BleRobotVersionInfo mBleRobotVersionInfo;

    @Override
    public int getContentViewId() {
        return R.layout.ble_activity_robot_statu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        mPresenter.init(this);
        showRedDot(mTvRobotVersion, false);
        showRedDot(mTvFirmwareVersion, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViseLog.d("-onResume-");
        mPresenter.getRobotAutoState();
        AppStatusUtils.setBtBussiness(false);
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

        } else if (i == R.id.tv_firmware_version) {//胸口版固件版本是否更新
            Disposable mDisposable = Observable.intervalRange(1, 100, 0, 100, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            ViseLog.d("long===" + String.valueOf(aLong));
                            Intent intent = new Intent(RobotStatuActivity.this, LanguageUpdateActivity.class);
                            intent.putExtra("progress", String.valueOf(aLong));
                            RobotStatuActivity.this.startActivity(intent);

                        }
                    });
            if (mBleRobotVersionInfo != null && !TextUtils.isEmpty(mBleRobotVersionInfo.new_firmware_ver)) {
                new BaseDialog.Builder(this)
                        .setMessage(R.string.base_upgrade_tip)
                        .setConfirmButtonId(R.string.base_confirm)
                        .setConfirmButtonColor(R.color.base_blue)
                        .setCancleButtonID(R.string.base_cancel)
                        .setCancleButtonColor(R.color.black)
                        .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                if (view.getId() == R.id.button_confirm) {
                                    dialog.dismiss();
                                    BlueClientUtil.getInstance().sendData(new BTCmdStartUpgradeSoft(BTCmdStartUpgradeSoft.REQUEST_UPDATE).toByteArray());
                                } else if (view.getId() == R.id.button_cancle) {
                                    dialog.dismiss();
                                }
                            }
                        }).create().show();
            }
        } else if (i == R.id.tv_robot_version) {//系统版本是否更新
            if (!TextUtils.isEmpty(mSystemRobotInfo.toVersion)) {
                BaseUpdateTipDialog.getInstance().show();
                new BaseDialog.Builder(this)
                        .setMessage(R.string.base_upgrade_tip)
                        .setConfirmButtonId(R.string.base_confirm)
                        .setConfirmButtonColor(R.color.base_blue)
                        .setCancleButtonID(R.string.base_cancel)
                        .setCancleButtonColor(R.color.black)
                        .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                if (view.getId() == R.id.button_confirm) {
                                    dialog.dismiss();
                                    BlueClientUtil.getInstance().sendData(new BTCmdStartUpgradeSoft(BTCmdStartUpgradeSoft.REQUEST_UPDATE).toByteArray());
                                } else if (view.getId() == R.id.button_cancle) {
                                    dialog.dismiss();
                                }
                            }
                        }).create().show();
//            Disposable mDisposable = Observable.intervalRange(1, 100, 0, 100, TimeUnit.MILLISECONDS)
//                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
//                        @Override
//                        public void accept(Long aLong) throws Exception {
//                            ViseLog.d("long===" + String.valueOf(aLong));
//                            JSONObject object = new JSONObject();
//                            object.put("status", aLong == 100 ? 2 : 1);
//                            object.put("progress", "" + aLong);
//                            object.put("totalSize", "20M");
//                            UpgradeProgressInfo upgradeProgressInfo = GsonImpl.get().toObject(object.toString(), UpgradeProgressInfo.class);
//                            downSystemProgress(upgradeProgressInfo);
//
//                        }
//                    });
            }
        }
    }

    /**
     * 设置机器人版本信息
     *
     * @param systemRobotInfo
     */
    @Override
    public void setRobotSoftVersion(SystemRobotInfo systemRobotInfo) {
        if (!TextUtils.isEmpty(systemRobotInfo.curVersion)) {
            mTvRobotVersion.setText(systemRobotInfo.curVersion);
        }
        if (!TextUtils.isEmpty(systemRobotInfo.toVersion)) {
            showRedDot(mTvRobotVersion, true);
        } else {
            showRedDot(mTvRobotVersion, false);
        }
        mSystemRobotInfo = systemRobotInfo;
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
    public void downSystemProgress(UpgradeProgressInfo progressInfo) {

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
                    mTvSystemProgress.setVisibility(View.VISIBLE);
                    mSystemVersionProgress.setProgress(Integer.parseInt(progressInfo.progress));
                    mTvSystemProgress.setText(progressInfo.progress + "%");

                }
                mIvDownloadSystemFailWarning.setVisibility(View.GONE);

            } else if (progressInfo.status == 2) {//download success
                mIvDownloadSystemFailWarning.setVisibility(View.GONE);
                mTvSystemUpdateTip.setVisibility(View.GONE);
                mSystemVersionProgress.setVisibility(View.GONE);
                mTvSystemProgress.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 胸口版下载进度
     *
     * @param progressInfo
     */
    @Override
    public void downloadFirmProgress(BleDownloadLanguageRsp progressInfo) {
        ViseLog.d("UpgradeProgressInfo = " + progressInfo);
        if (progressInfo != null) {
            if (progressInfo.name.equals("chip_firmware")) {
                mTvFirmwareUpdateTip.setVisibility(View.VISIBLE);
                mFirmwareVersionProgress.setVisibility(View.VISIBLE);
                mTvFirmwareProgress.setVisibility(View.VISIBLE);
                mIvDownloadFirmFailWarning.setVisibility(View.GONE);
                mFirmwareVersionProgress.setProgress(progressInfo.progess);
                mTvFirmwareProgress.setText(progressInfo.progess + "%");
                if (progressInfo.result == 0 && progressInfo.progess == 100) {
                    mIvDownloadFirmFailWarning.setVisibility(View.GONE);
                    mTvFirmwareUpdateTip.setVisibility(View.GONE);
                    mFirmwareVersionProgress.setVisibility(View.GONE);
                    mTvFirmwareProgress.setVisibility(View.GONE);
                } else if (progressInfo.result == 1 || progressInfo.result == 2) {
                    //  mTvFirmwareUpdateTip.setText(SkinManager.getInstance().getTextById(R.string.about_robot_auto_update_download_fail));
                    // mTvFirmwareUpdateTip.setTextColor(getResources().getColor(R.color.base_color_red));
                    mIvDownloadFirmFailWarning.setVisibility(View.GONE);
                    mTvFirmwareUpdateTip.setVisibility(View.GONE);
                    mFirmwareVersionProgress.setVisibility(View.GONE);
                    mTvFirmwareProgress.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 设置机器人胸口版信息
     *
     * @param robotVersionInfo
     */
    @Override
    public void setRobotVersionInfo(BleRobotVersionInfo robotVersionInfo) {
        mBleRobotVersionInfo = robotVersionInfo;
        mTvFirmwareVersion.setText(robotVersionInfo.firmware_ver);
        if (robotVersionInfo != null && !TextUtils.isEmpty(robotVersionInfo.new_firmware_ver)) {
            showRedDot(mTvFirmwareVersion, true);
        } else {
            showRedDot(mTvFirmwareVersion, false);
        }
    }

    @Override
    public void setRobotHardVersion(String hardVersion) {
        if (!TextUtils.isEmpty(hardVersion)) {
            ViseLog.d("hardVersion===" + hardVersion);
            mTvHardwareSerial.setText(hardVersion.trim());
        }
    }

    /**
     * 胸口版固件升级进度
     *
     * @param switchLanguageRsp
     */
    @Override
    public void updateFirmVersionProgress(BleSwitchLanguageRsp switchLanguageRsp) {

    }

    @Override
    protected void onStop() {
        super.onStop();
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
    private void showRedDot(TextView textView, boolean isShow) {
        if (isShow) {
            Drawable img = getResources().getDrawable(R.drawable.ble_update_red_dot);
            if (img != null) {
                img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
                textView.setCompoundDrawables(null, null, img, null);
            }
        } else {
            textView.setCompoundDrawables(null, null, null, null);
        }
    }


}
