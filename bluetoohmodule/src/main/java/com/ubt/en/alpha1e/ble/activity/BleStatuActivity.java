package com.ubt.en.alpha1e.ble.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.orhanobut.dialogplus.DialogPlus;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseDialog;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.en.alpha1e.ble.Contact.BleStatuContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.R2;
import com.ubt.en.alpha1e.ble.model.BleNetWork;
import com.ubt.en.alpha1e.ble.model.RobotStatu;
import com.ubt.en.alpha1e.ble.presenter.BleStatuPrenster;
import com.vise.log.ViseLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


@Route(path = ModuleUtils.Bluetooh_BleStatuActivity)
public class BleStatuActivity extends MVPBaseActivity<BleStatuContact.View, BleStatuPrenster> implements BleStatuContact.View {


    @BindView(R2.id.bleImageview3)
    ImageView mBleImageview3;
    @BindView(R2.id.bleTextview5)
    TextView mBleTextview5;
    @BindView(R2.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R2.id.iv_back_disconnect)
    ImageView mIvBackDisconnect;
    @BindView(R2.id.img_head_disconnect)
    ImageView mImgHeadDisconnect;
    @BindView(R2.id.ble_robot_statu_dissconnect)
    ImageView mBleRobotStatuDissconnect;
    @BindView(R2.id.ble_statu_connect)
    Button mBleConnect;
    @BindView(R2.id.rl_ble_disconnect)
    RelativeLayout mRlBleDisconnect;
    @BindView(R2.id.img_head)
    ImageView mImgHead;
    @BindView(R2.id.ble_robot_statu)
    ImageView mBleRobotStatu;
    @BindView(R2.id.tv_ble_name)
    TextView mTvBleName;
    @BindView(R2.id.tv_wifi_select)
    TextView mTvWifiSelect;
    @BindView(R2.id.rl_robot_wifi)
    RelativeLayout mRlRobotWifi;
    @BindView(R2.id.ble_checkbox)
    CheckBox mBleSwitch;
    @BindView(R2.id.rl_robot_update)
    RelativeLayout mRlRobotUpdate;
    @BindView(R2.id.tv_robot_version)
    TextView mTvRobotVersion;
    @BindView(R2.id.rl_robot_version)
    RelativeLayout mRlRobotVersion;
    @BindView(R2.id.tv_robot_serial)
    TextView mTvRobotSerial;
    @BindView(R2.id.rl_robot_serial)
    RelativeLayout mRlRobotSerial;
    @BindView(R2.id.tv_robot_ip)
    TextView mTvRobotIp;
    @BindView(R2.id.rl_robot_ip)
    RelativeLayout mRlRobotIp;
    @BindView(R2.id.rl_robot_content)
    RelativeLayout mRlRobotContent;
    @BindView(R2.id.ble_tv_connect)
    TextView mTvConnect;
    @BindView(R2.id.rl_robot_disconnect)
    RelativeLayout mRlRobotDisconnect;
    @BindView(R2.id.scroll_view)
    ScrollView mScrollView;

    Unbinder mUnbinder;
    @BindView(R2.id.iv_notconnect_wifi)
    ImageView mIvNotconnectWifi;

    private int fromeType;

    private String wifiName;

    @Override
    public int getContentViewId() {
        return R.layout.ble_activity_ble_statu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        mPresenter.init(this);
        fromeType = getIntent().getIntExtra(Constant1E.ENTER_BLESTATU_ACTIVITY, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getRobotBleConnect();
    }

    @OnClick({R2.id.ble_statu_connect, R2.id.tv_wifi_select, R2.id.ble_tv_connect, R2.id.bleImageview3, R2.id.iv_back_disconnect})
    public void clickView(View view) {
        int i = view.getId();
        if (i == R.id.iv_back_disconnect) {
            finishBleStatuActivity();
        } else if (i == R.id.bleImageview3) {
            finishBleStatuActivity();
        } else if (i == R.id.ble_statu_connect) {
            mPresenter.checkBlestatu();
        } else if (i == R.id.tv_wifi_select) {
            BleSearchWifiActivity.launch(this, false, wifiName);

        } else if (i == R.id.ble_tv_connect) {
            String s = String.format(getResources().getString(R.string.ble_about_robot_disconnect_dialogue), mTvBleName.getText());

            new BaseDialog.Builder(this)
                    .setMessage(s)
                    .setConfirmButtonId(R.string.ble_disconnect)
                    .setConfirmButtonColor(R.color.base_color_red)
                    .setCancleButtonID(R.string.base_cancel)
                    .setCancleButtonColor(R.color.black)
                    .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                        @Override
                        public void onClick(DialogPlus dialog, View view) {
                            if (view.getId() == R.id.button_confirm) {
                                mPresenter.dissConnectRobot();
                                dialog.dismiss();
                            } else if (view.getId() == R.id.button_cancle) {
                                dialog.dismiss();
                            }

                        }
                    }).create().show();


        }
    }


    @Override
    public void setBleConnectStatu(BluetoothDevice device) {
        if (device != null) {
            ViseLog.d("已连接蓝牙");
            mRlBleDisconnect.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);
            mTvBleName.setText(device.getName());
        } else {
            mRlBleDisconnect.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);
            ViseLog.d("没有连接蓝牙");

        }
    }

    @Override
    public void setRobotStatu(RobotStatu robotStatu) {

    }

    @Override
    public void setRobotNetWork(BleNetWork bleNetWork) {
        if (bleNetWork.isStatu()) {
            wifiName = bleNetWork.getWifiName();
            mTvWifiSelect.setText(bleNetWork.getWifiName());
            mTvRobotIp.setText(bleNetWork.getIp());
            mIvNotconnectWifi.setVisibility(View.GONE);
        } else {
            mTvWifiSelect.setText(SkinManager.getInstance().getTextById(R.string.ble_choose_a_wifi));
            mTvRobotIp.setText("");
            mIvNotconnectWifi.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setRobotSoftVersion(String softVersion) {

        if (!TextUtils.isEmpty(softVersion)) {
            mTvRobotVersion.setText(softVersion);
        }
    }

    @Override
    public void setRobotSN(String SN) {
        if (!TextUtils.isEmpty(SN)) {
            mTvRobotSerial.setText(SN);
        }
    }

    @Override
    public void goBleSraechActivity() {
        BleConnectActivity.launch(this, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unRegister();
        mUnbinder.unbind();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishBleStatuActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 结束页面
     * fromeType 0默认从主页跳转过来直接finish
     * 1从第一次进入蓝牙联网页面跳转主页
     * 2逻辑编程页面
     */
    private void finishBleStatuActivity() {
        if (fromeType == 0) {

        } else if (fromeType == 1) {
            ARouter.getInstance().build(ModuleUtils.Main_MainActivity).navigation();
        } else if (fromeType == 2) {
            setResult(RESULT_OK);
        }
        finish();
    }

}
