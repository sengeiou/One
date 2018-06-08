package com.ubt.en.alpha1e.ble.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.BlueTooth.BTServiceStateChanged;
import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BTCmdHelper;
import com.ubt.baselib.btCmd1E.BluetoothParamUtil;
import com.ubt.baselib.btCmd1E.IProtolPackListener;
import com.ubt.baselib.btCmd1E.ProtocolPacket;
import com.ubt.baselib.btCmd1E.cmd.BTCmdGetRobotVersionMsg;
import com.ubt.baselib.btCmd1E.cmd.BTCmdReadAutoUpgradeState;
import com.ubt.baselib.btCmd1E.cmd.BTCmdReadHardwareVer;
import com.ubt.baselib.btCmd1E.cmd.BTCmdReadSoftVer;
import com.ubt.baselib.btCmd1E.cmd.BTCmdSetAutoUpgrade;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.ble.Contact.RobotStatuContact;
import com.ubt.en.alpha1e.ble.model.BleBaseModelInfo;
import com.ubt.en.alpha1e.ble.model.BleRobotVersionInfo;
import com.ubt.en.alpha1e.ble.model.RobotStatu;
import com.ubt.en.alpha1e.ble.model.SystemRobotInfo;
import com.ubt.en.alpha1e.ble.model.UpgradeProgressInfo;
import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class RobotStatuPrenster extends BasePresenterImpl<RobotStatuContact.View> implements RobotStatuContact.Presenter, IProtolPackListener {


    private BlueClientUtil mBlueClientUtil;

    private RobotStatu mRobotStatu;

    @Override
    public void init(Context context) {
        mBlueClientUtil = BlueClientUtil.getInstance();
        EventBus.getDefault().register(this);
        mRobotStatu = new RobotStatu();
    }


    /**
     * 蓝牙连接断开状态
     *
     * @param serviceStateChanged
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBluetoothServiceStateChanged(BTServiceStateChanged serviceStateChanged) {
        ViseLog.i("getState:" + serviceStateChanged.toString());
        switch (serviceStateChanged.getState()) {
            case BluetoothState.STATE_CONNECTED://蓝牙配对成功
                ViseLog.d("蓝牙配对成功");
                break;
            case BluetoothState.STATE_CONNECTING://正在连接
                ViseLog.e("正在连接");
                break;
            case BluetoothState.STATE_DISCONNECTED:
                ViseLog.e("蓝牙连接断开");

                break;
            default:

                break;
        }
    }


    /**
     * 读取蓝牙回调数据
     *
     * @param readData
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadData(BTReadData readData) {
       // ViseLog.i("data:" + HexUtil.encodeHexStr(readData.getDatas()));
        BTCmdHelper.parseBTCmd(readData.getDatas(), this);
    }

    /**
     * 蓝牙数据解析回调
     *
     * @param packet
     */
    @Override
    public void onProtocolPacket(ProtocolPacket packet) {
        switch (packet.getmCmd()) {

            case BTCmd.DV_READ_ROBOT_SOFT_VERSION:

                String systemCommedInfo = BluetoothParamUtil.bytesToString(packet.getmParam());
                ViseLog.d("机器人版本号 commonCmdJson= " + systemCommedInfo.toString());
                SystemRobotInfo systemRobotInfo = GsonImpl.get().toObject(systemCommedInfo, SystemRobotInfo.class);
                if (mView != null) {
                    mView.setRobotSoftVersion(systemRobotInfo);
                }
                break;

            case BTCmd.DV_READ_AUTO_UPGRADE_STATE://自动读取
                ViseLog.d("机器人 AUTO_UPGRADE_STATE：" + new String(packet.getmParam()) + "    packet = " + packet.getmParam() + " / " + packet.getmParam()[0]);
                if (mView != null) {
                    mView.setAutoUpgradeStatus(packet.getmParam()[0]);
                }

                break;
            case BTCmd.DV_SET_AUTO_UPGRADE://手动切换结果
                ViseLog.d("机器人 DV_SET_AUTO_UPGRADE：" + new String(packet.getmParam()) + "    packet = " + packet.getmParam() + " / " + packet.getmParam()[0]);
                if (mView != null) {
                    mView.setAutoUpgradeStatus(packet.getmParam()[0]);
                }
                break;
            case BTCmd.DV_DO_UPGRADE_PROGRESS:
                String upgradeProgressJson = BluetoothParamUtil.bytesToString(packet.getmParam());
                ViseLog.d("upgradeProgressJson = " + upgradeProgressJson);
                UpgradeProgressInfo upgradeProgressInfo = GsonImpl.get().toObject(upgradeProgressJson, UpgradeProgressInfo.class);
                if (mView != null) {
                    mView.updateUpgradeProgress(upgradeProgressInfo);
                }
                break;
            case BTCmd.DV_READ_HARDWARE_VERSION:
                ViseLog.d("机器人硬件版本号：" + new String(packet.getmParam()));
                if (mView != null) {
                    mView.setRobotHardVersion(new String(packet.getmParam()));
                }
                break;
            case BTCmd.DV_COMMON_CMD:
                ViseLog.d("DV_COMMON_CMD = " + BluetoothParamUtil.bytesToString(packet.getmParam()));
                String commonCmdJson = BluetoothParamUtil.bytesToString(packet.getmParam());
                BleBaseModelInfo bleBaseModel = GsonImpl.get().toObject(commonCmdJson, BleBaseModelInfo.class);
                ViseLog.d("bleBaseModel.event = " + bleBaseModel.event);
                if (bleBaseModel.event == 1) {
                    BleRobotVersionInfo robotLanguageInfo = GsonImpl.get().toObject(commonCmdJson, BleRobotVersionInfo.class);
                    if (mView != null) {
                        //  mView.setRobotLanguage(robotLanguageInfo);
                    }
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private Disposable mDisposable;

    /**
     * 获取机器人连接状态
     */
    @Override
    public void getRobotAutoState() {
        BluetoothDevice device = mBlueClientUtil.getConnectedDevice();
        if (mView != null) {
            if (mBlueClientUtil.getConnectionState() == BluetoothState.STATE_CONNECTED) {
                mDisposable = Observable.intervalRange(1, 4, 0, 200, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        ViseLog.d("long===" + String.valueOf(aLong));
                        if (aLong == 1) {
                            ViseLog.d("获取机器人自动更新请求");
                            mBlueClientUtil.sendData(new BTCmdReadAutoUpgradeState().toByteArray());
                        } else if (aLong == 2) {
                            ViseLog.d("获取机器人软件版本请求");
                            mBlueClientUtil.sendData(new BTCmdReadSoftVer().toByteArray());
                        } else if (aLong == 3) {
                            ViseLog.d("获取机器人硬件版本请求");
                            mBlueClientUtil.sendData(new BTCmdReadHardwareVer().toByteArray());
                        } else if (aLong == 4) {
                            ViseLog.d("获取机器人语言包请求");
                            mBlueClientUtil.sendData(new BTCmdGetRobotVersionMsg().toByteArray());
                        }
                    }
                });

            }

        }

    }


    @Override
    public void doChangeAutoUpgrade(boolean is0pen) {
        byte[] params = new byte[1];
        if (is0pen) {
            params[0] = BTCmdSetAutoUpgrade.ON;
        } else {
            params[0] = BTCmdSetAutoUpgrade.OFF;
        }

        mBlueClientUtil.sendData(new BTCmdSetAutoUpgrade(params[0]).toByteArray());
    }



}
