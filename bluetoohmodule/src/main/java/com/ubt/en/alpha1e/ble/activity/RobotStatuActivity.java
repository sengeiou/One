package com.ubt.en.alpha1e.ble.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseDialog;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.baselib.utils.ULog;
import com.ubt.en.alpha1e.ble.Contact.RobotStatuContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.R2;
import com.ubt.en.alpha1e.ble.model.BleDownloadLanguageRsp;
import com.ubt.en.alpha1e.ble.model.BleRobotVersionInfo;
import com.ubt.en.alpha1e.ble.model.BleUpgradeProgressRsp;
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
    ImageView ckbAutoUpgrade;
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
    @BindView(R2.id.tv_voice_version)
    TextView mTvVoiceVersion;
    @BindView(R2.id.rl_test_upgrade)
    RelativeLayout rlTestUpgrade;
    //用于记录点击开关时间
    private long inSaveActTime;

    private boolean mCurrentAutoUpgrade = false;
    private boolean isSendRobotCmd = false;
    private SystemRobotInfo mSystemRobotInfo;
    private BleRobotVersionInfo mBleRobotVersionInfo;

    private DialogPlus systemDialog;

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

    @OnClick({R2.id.iv_robot_back, R2.id.ckb_auto_upgrade, R2.id.rl_system_version, R2.id.rl_firmware_version, R2.id.rl_test_upgrade})
    public void clickView(View view) {
        int i = view.getId();
        if (i == R.id.iv_robot_back) {
            finish();
        } else if (i == R.id.ckb_auto_upgrade) {
            //当当前的时间-进入SaveActivity的时间<1秒时，直接返回
            if ((System.currentTimeMillis() - inSaveActTime) < 1500) {
                ViseLog.d("在1秒内点击切换按钮");
                return;
            }
            inSaveActTime = System.currentTimeMillis();
            ViseLog.d("ckb_auto_u pgrade 点击自动升级开关");
            //成功之后才切换
            // ckbAutoUpgrade.setChecked(!ckbAutoUpgrade.isChecked());
            if (mCurrentAutoUpgrade) {
                new BaseDialog.Builder(this)
                        .setMessage(R.string.about_robot_auto_update_off)
                        .setConfirmButtonId(R.string.base_cancel)
                        .setConfirmButtonColor(R.color.black)
                        .setCancleButtonID(R.string.common_btn_switch_off)
                        .setCancleButtonColor(R.color.base_color_red)
                        .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                if (view.getId() == R.id.button_confirm) {
                                    dialog.dismiss();
                                } else if (view.getId() == R.id.button_cancle) {
                                    dialog.dismiss();
                                    switchAutoUpgradeStatus();

                                }
                            }
                        }).create().show();
            } else {
                switchAutoUpgradeStatus();
            }

        } else if (i == R.id.rl_firmware_version) {//胸口版固件版本是否更新
            if (mBleRobotVersionInfo != null && !TextUtils.isEmpty(mBleRobotVersionInfo.new_firmware_ver)) {
                ULog.d("RobotStatuActivity", "点击胸口版当前电量==" + AppStatusUtils.getCurrentPower());
                if (AppStatusUtils.getCurrentPower() < 35) {
                    ViseLog.d("当前电量较低弹出低电量框");
                    showLowBatteryDialog(this);
                    return;
                }
                ViseLog.d("电量大于35显示更新");
                showUpdateDialog(2);
            }
        } else if (i == R.id.rl_system_version) {//系统版本是否更新
            if (mSystemRobotInfo != null && !TextUtils.isEmpty(mSystemRobotInfo.toVersion) && compareSoftVersion(mSystemRobotInfo)) {
                ULog.d("RobotStatuActivity", "点击系统更新按钮当前电量==" + AppStatusUtils.getCurrentPower());
                if (AppStatusUtils.getCurrentPower() < 35) {
                    ViseLog.d("当前电量较低弹出低电量框");
                    showLowBatteryDialog(this);
                    return;
                }
                ViseLog.d("电量大于35显示更新");
                showUpdateDialog(1);
            }
        } else if (i == R.id.rl_test_upgrade || i == R.id.rl_test_upgrade) {
            ViseLog.d("rl_test_upgrade = " + R.id.rl_test_upgrade + "/" + R.id.rl_test_upgrade + "/" + i);
            mPresenter.doTestUpgradeByApp();
            ToastUtils.showShort("已发送升级命令");
        }
    }


    /**
     * 系统版本或者胸口版又升级对话框
     *
     * @param type
     */
    private void showUpdateDialog(final int type) {
        systemDialog = new BaseDialog.Builder(this)
                .setMessage(R.string.base_upgrade_tip)
                .setConfirmButtonId(R.string.base_not_now)
                .setConfirmButtonColor(R.color.black)
                .setCancleButtonID(R.string.base_update)
                .setCancleButtonColor(R.color.base_blue)
                .setOnDissmissListener(new BaseDialog.OnDissmissListener() {
                    @Override
                    public void onDissmiss() {
                        if (isSendRobotCmd) {
                            mPresenter.sendUpdateVersion(type);
                            isSendRobotCmd = false;
                        }
                    }
                })
                .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.button_confirm) {
                            dialog.dismiss();
                        } else if (view.getId() == R.id.button_cancle) {
                            isSendRobotCmd = true;
                            dialog.dismiss();
                        }
                    }
                }).create();
        systemDialog.show();
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
        if (!TextUtils.isEmpty(systemRobotInfo.toVersion) && compareSoftVersion(systemRobotInfo)) {
            showRedDot(mTvRobotVersion, true);
        } else {
            showRedDot(mTvRobotVersion, false);
        }
        mSystemRobotInfo = systemRobotInfo;
    }

    private boolean compareSoftVersion(SystemRobotInfo systemRobotInfo) {
        boolean isnewVersion = false;
        String curVersion = systemRobotInfo.curVersion.replaceAll("v", "").trim();
        String toVersion = systemRobotInfo.toVersion.replaceAll("v", "").trim();
        ViseLog.d("curVersion==" + curVersion + "    toVersion==" + toVersion);

        String[] curArray = curVersion.split("[.]");
        String[] toArray = toVersion.split("[.]");
        int n = Math.min(curArray.length, toArray.length);
        ViseLog.d("curArray.length==" + curArray.length + "    toArray.length==" + toArray.length + "   n===" + n);
        for (int i = 0; i < n; i++) {
            ViseLog.d("curArray[i]==" + curArray[i] + "    toArray[i]==" + toArray[i]);
            if (Integer.parseInt(toArray[i]) > Integer.parseInt(curArray[i])) {
                ViseLog.d("toversion版本高于curVersion==");
                isnewVersion = true;
                break;
            }
        }
        ViseLog.d("isnewVersion==" + isnewVersion);
        return isnewVersion;
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
        ckbAutoUpgrade.setImageResource(mCurrentAutoUpgrade ? R.drawable.list_open : R.drawable.list_closed);
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
            if (progressInfo.status == 1) {//downloading
                if (!TextUtils.isEmpty(progressInfo.progress)) {
                    mTvSystemUpdateTip.setVisibility(View.VISIBLE);
                    mTvSystemUpdateTip.setText(SkinManager.getInstance().getTextById(R.string.about_robot_auto_update_download1));
                    mTvSystemUpdateTip.setTextColor(getResources().getColor(R.color.base_color_grey));
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
                //mPresenter.getSystemVersion();
            } else {
                mTvSystemUpdateTip.setText(SkinManager.getInstance().getTextById(R.string.about_robot_auto_update_download_fail));
                mTvSystemUpdateTip.setTextColor(getResources().getColor(R.color.base_color_red));
                mTvSystemUpdateTip.setVisibility(View.VISIBLE);
                mIvDownloadSystemFailWarning.setVisibility(View.VISIBLE);
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
                mTvFirmwareUpdateTip.setTextColor(getResources().getColor(R.color.base_color_grey));
                mFirmwareVersionProgress.setVisibility(View.VISIBLE);
                mTvFirmwareProgress.setVisibility(View.VISIBLE);
                mIvDownloadFirmFailWarning.setVisibility(View.GONE);
                mFirmwareVersionProgress.setProgress(progressInfo.progress);
                mTvFirmwareProgress.setText(progressInfo.progress + "%");
                if (progressInfo.result == 0 && progressInfo.progress == 100) {
                    mIvDownloadFirmFailWarning.setVisibility(View.GONE);
                    mTvFirmwareUpdateTip.setVisibility(View.GONE);
                    mFirmwareVersionProgress.setVisibility(View.GONE);
                    mTvFirmwareProgress.setVisibility(View.GONE);
                    mPresenter.getVoiceFirmVersion();
                } else {
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

        if (robotVersionInfo != null) {

            String version = "version:" + "(" + robotVersionInfo.version + ")";

            String firmwareversion = "--firmware_ver:" + "(" + robotVersionInfo.firmware_ver + ")";

            String resource_ver = "--resource_ver:" + "(" + robotVersionInfo.resource_ver + ")";

            mTvVoiceVersion.setText(version + firmwareversion + resource_ver);
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
    public void updateFirmVersionProgress(BleUpgradeProgressRsp switchLanguageRsp) {

    }

    @Override
    public void systemRequestUpdate() {
        if (systemDialog != null && systemDialog.isShowing()) {
            systemDialog.dismiss();
            systemDialog = null;
        }
    }

    @Override
    public void robotNotWifi(int code) {
        ViseLog.d("Code========"+code);
        if (code == 3) {
            showConnectWifiDialog();
        }
    }

    /**
     * 显示连接WIFI对话框
     */
    private void showConnectWifiDialog() {
        new BaseDialog.Builder(this)
                .setMessage(SkinManager.getInstance().getTextById(R.string.base_connect_wifi))
                .setConfirmButtonId(R.string.base_cancel)
                .setConfirmButtonColor(R.color.base_blue)
                .setCancleButtonID(R.string.base_connect)
                .setCancleButtonColor(R.color.base_blue)
                .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.button_confirm) {
                            dialog.dismiss();

                        } else if (view.getId() == R.id.button_cancle) {
                            dialog.dismiss();
                            BleSearchWifiActivity.launch(RobotStatuActivity.this, false, "");
                        }
                    }
                }).create().show();
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

    /**
     * 显示低电量
     */
    private void showLowBatteryDialog(Context context) {

        String titleMsg = SkinManager.getInstance().getTextById(R.string.about_robot_language_low_battery_tips_1);
        String detailMsg = SkinManager.getInstance().getTextById(R.string.about_robot_language_low_battery_tips_3);

        View contentView = LayoutInflater.from(context).inflate(R.layout.ble_dialog_low_battery, null);
        TextView tvTitle = contentView.findViewById(R.id.tv_title);
        TextView tvMessage = contentView.findViewById(R.id.tv_message);
        tvTitle.setText(titleMsg);
        tvMessage.setText(detailMsg);

        ViewHolder viewHolder = new ViewHolder(contentView);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = (int) ((display.getWidth()) * 0.55); //设置宽度

        DialogPlus.newDialog(context)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.CENTER)
                .setContentWidth(width)
                .setContentBackgroundResource(android.R.color.transparent)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.btn_ok) {//点击确定以后刷新列表并解锁下一关

                            dialog.dismiss();
                        }
                    }
                })
                .setCancelable(false)
                .create().show();

    }

}
