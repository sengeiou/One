package com.ubt.en.alpha1e.ble.presenter;

import android.content.Context;

import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.BlueTooth.BTServiceStateChanged;
import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BTCmdHelper;
import com.ubt.baselib.btCmd1E.IProtolPackListener;
import com.ubt.baselib.btCmd1E.ProtocolPacket;
import com.ubt.baselib.btCmd1E.cmd.BTCmdConnectWifi;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.ble.Contact.WifiInputContact;
import com.vise.log.ViseLog;
import com.vise.utils.convert.HexUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class WifiInputPrenster extends BasePresenterImpl<WifiInputContact.View> implements WifiInputContact.Presenter, IProtolPackListener {

    BlueClientUtil mBlueClientUtil;

    @Override
    public void init(Context context) {
        EventBus.getDefault().register(this);
        mBlueClientUtil = BlueClientUtil.getInstance();

    }

    @Override
    public void sendPasswd(String wifiName, String passwd) {
        mBlueClientUtil.sendData(new BTCmdConnectWifi(wifiName, passwd).toByteArray());
    }

    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * 蓝牙连接断开状态
     *
     * @param serviceStateChanged
     */
    @org.greenrobot.eventbus.Subscribe
    public void onBluetoothServiceStateChanged(BTServiceStateChanged serviceStateChanged) {
        ViseLog.i("getState:" + serviceStateChanged.toString());
        switch (serviceStateChanged.getState()) {
            case BluetoothState.STATE_CONNECTED://蓝牙配对成功

                break;
            case BluetoothState.STATE_CONNECTING://正在连接
                ViseLog.e("正在连接");
                break;
            case BluetoothState.STATE_DISCONNECTED:
                ToastUtils.showShort("蓝牙断开");
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
            case BTCmd.DV_DO_NETWORK_CONNECT:
                ViseLog.d("param====" + packet.getmParam()[0]);
                if (mView != null) {
                    mView.connectWifiResult(packet.getmParam()[0]);
                }
                break;
            default:
                break;
        }
    }

}
