package com.ubt.en.alpha1e.ble.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.BlueTooth.BTServiceStateChanged;
import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BluetoothParamUtil;
import com.ubt.baselib.btCmd1E.ProtocolPacket;
import com.ubt.baselib.btCmd1E.cmd.BTCmdGetWifiList;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.ble.Contact.WifiConnectContact;
import com.ubt.en.alpha1e.ble.model.WifiInfoModel;
import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class WifiConnectPrenster extends BasePresenterImpl<WifiConnectContact.View> implements WifiConnectContact.Presenter {

    private Context mContext;


    private BlueClientUtil mBlueClientUtil;

    private List<WifiInfoModel> mWifiInfoModels = new ArrayList<>();


    @Override
    public void init(Context context) {
        this.mContext = context;
        mBlueClientUtil = BlueClientUtil.getInstance();
        EventBus.getDefault().register(this);
        if (isBlutoohConnected()) {
            mBlueClientUtil.sendData(new BTCmdGetWifiList(BTCmdGetWifiList.START_GET_WIFI).toByteArray());
        }
    }


    public List<WifiInfoModel> getWifiInfoModels() {
        return mWifiInfoModels;
    }

    @Override
    public void startScanWifi() {

    }

    /**
     * 读取蓝牙回调数据
     *
     * @param readData
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadData(BTReadData readData) {
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
            case BTCmd.DV_FIND_WIFI_LIST:
                ViseLog.d("开始获取wifi=数量为===" + packet.getmParam()[0]);
                break;
            case BTCmd.DV_GET_WIFI_LIST_INFO:
                String wifiinfo = BluetoothParamUtil.bytesToString(packet.getmParam());
                WifiInfoModel wifiInfoModel = GsonImpl.get().toObject(wifiinfo, WifiInfoModel.class);
                ViseLog.d("机器人回复wifi==wifiInfoModel====" + wifiInfoModel.toString());
                dealWifiInfo(wifiInfoModel);
                if (isBlutoohConnected()) {
                    mBlueClientUtil.sendData(new BTCmdGetWifiList(BTCmdGetWifiList.CONTINUE_GET_WIFI).toByteArray());
                }
                break;
            case BTCmd.DV_GET_WIFI_LIST_FINISH:
                ViseLog.d("机器人结束发送wifi列表===" + packet.getmParam()[0]);
                break;
            default:
                break;
        }
    }

    /**
     * 处理机器人回复的wifi信息
     *
     * @param wifiInfoModel
     */
    private synchronized void dealWifiInfo(WifiInfoModel wifiInfoModel) {
        if (!TextUtils.isEmpty(wifiInfoModel.getESSID())) {
            boolean isNewDevice = true;
            for (WifiInfoModel model : mWifiInfoModels) {
                if (wifiInfoModel.getESSID().equals(model.getESSID())) {
                    ViseLog.d("ESSID==" + wifiInfoModel.getESSID() + "    isNewDevice = " + isNewDevice);
                    isNewDevice = false;
                    break;
                }
            }
            if (isNewDevice) {
                mWifiInfoModels.add(wifiInfoModel);
            }
            //注意返回结束获取，进入联网那个页面结束获取
            if (mView != null) {
                mView.notifyDataSetChanged();
            }
        }
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
                ViseLog.e("蓝牙连接断开");
                if (mView != null) {
                    mView.blutoohDisconnect();
                }
                break;
            default:

                break;
        }
    }


    /**
     * 销毁wifi注册广播
     */
    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean isBlutoohConnected() {
        return mBlueClientUtil.getConnectionState() == BluetoothState.STATE_CONNECTED;
    }

    @Override
    public void stopGetWifiList() {
        if (isBlutoohConnected()) {
            mBlueClientUtil.sendData(new BTCmdGetWifiList(BTCmdGetWifiList.FINISH_GET_WIFI).toByteArray());
        }
    }

}
