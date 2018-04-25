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
import com.ubt.baselib.btCmd1E.cmd.BTCmdGetWifiStatus;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.ble.Contact.BleStatuContact;
import com.ubt.en.alpha1e.ble.model.BleNetWork;
import com.ubt.en.alpha1e.ble.model.RobotStatu;
import com.vise.log.ViseLog;
import com.vise.utils.convert.HexUtil;

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

public class BleStatuPrenster extends BasePresenterImpl<BleStatuContact.View> implements BleStatuContact.Presenter, IProtolPackListener {


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
     * 读取蓝牙回调数据
     *
     * @param readData
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadData(BTReadData readData) {
        ViseLog.i("data:" + HexUtil.encodeHexStr(readData.getDatas()));
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
            case BTCmd.DV_HANDSHAKE:

                break;
            case BTCmd.DV_READ_NETWORK_STATUS:
                String networkInfoJson = BluetoothParamUtil.bytesToString(packet.getmParam());
                ViseLog.d(networkInfoJson);
                BleNetWork bleNetWork = praseNetWork(networkInfoJson);
                if (mView != null) {
                    mView.setRobotNetWork(bleNetWork);
                }
                break;

            default:
                break;
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
            if (mBlueClientUtil.getConnectionState() == 3) {
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