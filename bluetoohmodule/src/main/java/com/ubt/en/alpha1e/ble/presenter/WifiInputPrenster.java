package com.ubt.en.alpha1e.ble.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.BlueTooth.BTServiceStateChanged;
import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BTCmdHelper;
import com.ubt.baselib.btCmd1E.IProtolPackListener;
import com.ubt.baselib.btCmd1E.ProtocolPacket;
import com.ubt.baselib.btCmd1E.cmd.BTCmdConnectWifi;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.ble.Contact.WifiInputContact;
import com.vise.log.ViseLog;

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

    private int MESSAGE_TIMEOUt = 60 * 1000;
    private int MESSAGE_WHAT = 0x11;

    @Override
    public void init(Context context) {
        EventBus.getDefault().register(this);
        mBlueClientUtil = BlueClientUtil.getInstance();

    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_WHAT) {
                if (mView != null) {
                    mView.connectWifiResult(3);
                }
            }
        }
    };

    @Override
    public void sendPasswd(String wifiName, String passwd) {
        mBlueClientUtil.sendData(new BTCmdConnectWifi(wifiName, passwd).toByteArray());
        mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, MESSAGE_TIMEOUt);
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
                 mHandler.removeMessages(MESSAGE_WHAT);
                if (mView != null) {
                   // mView.connectWifiResult(3);
                    mView.blutoohDisconnect();
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
//        ViseLog.i("data:" + HexUtil.encodeHexStr(readData.getDatas()));
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
                mHandler.removeMessages(MESSAGE_WHAT);
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
