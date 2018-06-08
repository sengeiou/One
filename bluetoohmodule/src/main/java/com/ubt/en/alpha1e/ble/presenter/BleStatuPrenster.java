package com.ubt.en.alpha1e.ble.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.BlueTooth.BTServiceStateChanged;
import com.ubt.baselib.BlueTooth.BTStateChanged;
import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BluetoothParamUtil;
import com.ubt.baselib.btCmd1E.ProtocolPacket;
import com.ubt.baselib.btCmd1E.cmd.BTCmdGetRobotVersionMsg;
import com.ubt.baselib.btCmd1E.cmd.BTCmdGetWifiStatus;
import com.ubt.baselib.btCmd1E.cmd.BTCmdReadAutoUpgradeState;
import com.ubt.baselib.btCmd1E.cmd.BTCmdReadSNCode;
import com.ubt.baselib.btCmd1E.cmd.BTCmdReadSoftVer;
import com.ubt.baselib.btCmd1E.cmd.BTCmdSetAutoUpgrade;
import com.ubt.baselib.model1E.BleNetWork;
import com.ubt.baselib.model1E.ManualEvent;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.ble.Contact.BleStatuContact;
import com.ubt.en.alpha1e.ble.model.BleBaseModelInfo;
import com.ubt.en.alpha1e.ble.model.BleRobotVersionInfo;
import com.ubt.en.alpha1e.ble.model.RobotStatu;
import com.ubt.en.alpha1e.ble.model.UpgradeProgressInfo;
import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class BleStatuPrenster extends BasePresenterImpl<BleStatuContact.View> implements BleStatuContact.Presenter {


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
                if (mView != null) {
                    mView.setBleConnectStatu(null);
                }
                break;
            default:

                break;
        }
    }

    /**
     * 监听到蓝牙开启立即申请定位权限
     *
     * @param stateChanged
     */
    @Subscribe
    public void onActionStateChanged(BTStateChanged stateChanged) {
        ViseLog.i(stateChanged.toString());
        if (stateChanged.getState() == BTStateChanged.STATE_ON) {
            ViseLog.e("开启蓝牙");
            if (mView != null) {
                mView.goBleSraechActivity();
            }
        }
    }

    /**
     * 读取蓝牙回调数据
     *
     * @param readData
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadData(BTReadData readData) {
//        ViseLog.i("data:" + HexUtil.encodeHexStr(readData.getDatas()));
//        BTCmdHelper.parseBTCmd(readData.getDatas(), this);
        onProtocolPacket(readData);
    }

    /**
     * 蓝牙数据解析回调
     *
     * @param readData
     */
    private void onProtocolPacket(BTReadData readData) {
        ProtocolPacket packet = readData.getPack();
        switch (packet.getmCmd()) {
            case BTCmd.DV_READ_NETWORK_STATUS:
                String networkInfoJson = BluetoothParamUtil.bytesToString(packet.getmParam());
                ViseLog.d(networkInfoJson);
                BleNetWork bleNetWork = praseNetWork(networkInfoJson);
                if (mView != null) {
                    mView.setRobotNetWork(bleNetWork);
                }
                mBlueClientUtil.sendData(new BTCmdReadSoftVer().toByteArray());
                break;
            case BTCmd.DV_READ_SOFTWARE_VERSION:
                ViseLog.d("机器人版本号：" + new String(packet.getmParam()));
                if (mView != null) {
                    mView.setRobotSoftVersion(new String(packet.getmParam()));
                }
                mBlueClientUtil.sendData(new BTCmdReadSNCode().toByteArray());
                break;

            case BTCmd.READ_SN_CODE:
                ViseLog.d("机器人序列号：" + new String(packet.getmParam()) + "   packet =  " + packet.getmParam());
                if (mView != null) {
                    mView.setRobotSN(new String(packet.getmParam()));
                }
                mBlueClientUtil.sendData(new BTCmdReadAutoUpgradeState().toByteArray());

                mBlueClientUtil.sendData(new BTCmdGetRobotVersionMsg().toByteArray());
                break;

            case BTCmd.DV_READ_AUTO_UPGRADE_STATE:
                ViseLog.d("机器人 AUTO_UPGRADE_STATE：" + new String(packet.getmParam()) + "    packet = " + packet.getmParam() + " / " + packet.getmParam()[0]);
                if(mView != null){
                    mView.setAutoUpgradeStatus(packet.getmParam()[0]);
                }
            case BTCmd.DV_SET_AUTO_UPGRADE:
                ViseLog.d("机器人 DV_SET_AUTO_UPGRADE：" + new String(packet.getmParam()) + "    packet = " + packet.getmParam() + " / " + packet.getmParam()[0]);
                if(mView != null){
                    mView.setAutoUpgradeStatus(packet.getmParam()[0]);
                }
                break;
            case BTCmd.DV_DO_UPGRADE_PROGRESS:
                String upgradeProgressJson= BluetoothParamUtil.bytesToString(packet.getmParam());
                ViseLog.d("upgradeProgressJson = " + upgradeProgressJson);
                UpgradeProgressInfo upgradeProgressInfo = GsonImpl.get().toObject(upgradeProgressJson,UpgradeProgressInfo.class);
                if(mView != null){
                    mView.updateUpgradeProgress(upgradeProgressInfo);
                }
                break;

            case BTCmd.DV_COMMON_CMD:
                ViseLog.d("DV_COMMON_CMD = " + BluetoothParamUtil.bytesToString(packet.getmParam()));

                String commonCmdJson = BluetoothParamUtil.bytesToString(packet.getmParam());
                BleBaseModelInfo bleBaseModel = GsonImpl.get().toObject(commonCmdJson, BleBaseModelInfo.class);

                ViseLog.d("bleBaseModel.event = " + bleBaseModel.event);
                if(bleBaseModel.event == 1){
                    BleRobotVersionInfo robotLanguageInfo = GsonImpl.get().toObject(commonCmdJson, BleRobotVersionInfo.class);
                    if(mView != null){
                        mView.setRobotVersionInfo(robotLanguageInfo);
                    }
                }

                break;
            default:
                break;
        }
    }

    /**
     * 进入退出手动连接
     *
     * @param manualEvent 进入为true 退出为false
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doEntryManalConnect(ManualEvent manualEvent) {
        if (manualEvent.getEvent() == ManualEvent.Event.CONNECT_ROBOT_SUCCESS) {//进入蓝牙联网页面
            getRobotBleConnect();
        }
    }

    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取机器人连接状态
     */
    @Override
    public void getRobotBleConnect() {
        BluetoothDevice device = mBlueClientUtil.getConnectedDevice();
        if (mView != null) {
            if (mBlueClientUtil.getConnectionState() == BluetoothState.STATE_CONNECTED) {
                mView.setBleConnectStatu(device);
                if (device != null) {//查询网络状态
                    // BTCmdGetWifiStatus
                    mBlueClientUtil.sendData(new BTCmdGetWifiStatus().toByteArray());

                }
            } else {
                mView.setBleConnectStatu(null);
            }

        }

    }


    /**
     * 获取机器人信息
     */
    @Override
    public void dissConnectRobot() {
        mBlueClientUtil.disconnect();
        AppStatusUtils.setIsForceDisBT(true);
        ManualEvent manualEvent = new ManualEvent(ManualEvent.Event.MANUAL_DISCONNECT);
        manualEvent.setManual(true);
        EventBus.getDefault().post(manualEvent);
    }

    @Override
    public void checkBlestatu() {
        if (mBlueClientUtil.isEnabled()) {
            if (mView != null) {
                mView.goBleSraechActivity();
            }
        } else {
            mBlueClientUtil.openBluetooth();
        }
    }

    @Override
    public void doChangeAutoUpgrade(boolean is0pen) {
        byte[] params = new byte[1];
        if(is0pen){
            params[0] = BTCmdSetAutoUpgrade.ON;
        }else {
            params[0] = BTCmdSetAutoUpgrade.OFF;
        }

        mBlueClientUtil.sendData(new BTCmdSetAutoUpgrade(params[0]).toByteArray());
    }


    //                "status":	"true",
//                    "name":	"UBT-alpha2-bigbox",
//                    "ip":	"192.168.79.60"
//        }
    public BleNetWork praseNetWork(String netWork) {

        BleNetWork bleNetWork = null;

        try {
            JSONObject object = new JSONObject(netWork);
            boolean statu = object.optBoolean("status");
            String wifiName = object.optString("name");
            String ip = object.optString("ip");
            bleNetWork = new BleNetWork(statu, wifiName, ip);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bleNetWork;
    }
}
