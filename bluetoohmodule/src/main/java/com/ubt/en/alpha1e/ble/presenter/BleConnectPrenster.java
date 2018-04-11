package com.ubt.en.alpha1e.ble.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.ubt.baselib.btCmd1E.cmd.BTCmdActionStopPlay;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.ByteHexHelper;
import com.ubt.baselib.utils.PermissionUtils;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientListenerAdapter;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.ble.Contact.BleConnectContact;
import com.ubt.en.alpha1e.ble.model.BleDevice;
import com.vise.log.ViseLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class BleConnectPrenster extends BasePresenterImpl<BleConnectContact.View> implements BleConnectContact.Presenter {

    private static final String TAG = "BleConnectPrenster";
    boolean isScanning = false;
    private BlueClientUtil mBlueClient;

    private int mConnectState;
    private int mPreState;
    private Context mContext;
    private Date lastTime_onConnectState = null;
    public Timer timer = new Timer();

    public List<BleDevice> mBleDevices = new ArrayList<>();

    public void init(Context context) {
        this.mContext = context;
        mBlueClient = BlueClientUtil.getInstance();
        mBlueClient.setBlueListener(mBTListener);
    }

    private BlueClientListenerAdapter mBTListener = new BlueClientListenerAdapter() {
        @Override
        public void onActionDiscoveryStateChanged(String discoveryState) {
            Log.d(TAG, "zz onActionDiscoveryStateChanged:" + discoveryState);
            if (discoveryState.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                isScanning = true;

            } else if (discoveryState.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                isScanning = false;
                startScanBle();
            }
        }

        @Override
        public void onBluetoothServiceStateChanged(int state) {
            Log.i(TAG, "onBluetoothServiceStateChanged state=" + state);
            mConnectState = state;
            switch (state) {
                case BluetoothState.STATE_CONNECTED://蓝牙配对成功
                    // 收到蓝牙连接状态命令时间相隔-----------start
                    Date curDate = new Date(System.currentTimeMillis());
                    float time_difference = 1000;
                    if (lastTime_onConnectState != null) {
                        time_difference = curDate.getTime() - lastTime_onConnectState.getTime();
                    }
                    lastTime_onConnectState = curDate;
                    if (time_difference < 500) {
                        return;
                    }
                    mBlueClient.sendData(new BTCmdActionStopPlay().toByteArray());
                    break;
                case BluetoothState.STATE_CONNECTING://正在连接

                    break;
                case BluetoothState.STATE_DISCONNECTED:

                    break;
                default:
                    if (mPreState == BluetoothState.STATE_CONNECTING) {

                    }
                    break;
            }
            mPreState = state;
        }

        @Override
        public void onActionDeviceFound(BluetoothDevice device, int rssi) {
            String name = device.getName();
            ViseLog.d("name-====" + name);
            if (name != null && name.toLowerCase().contains("alpha1e") && mView != null) {
                mView.dealBleDevice(device, rssi);
            }
        }
        @Override
        public void onReadData(BluetoothDevice device, byte[] data) {
            Log.d(TAG, "zz ActionPresenter onReadData data:" + ByteHexHelper.bytesToHexString(data));
           // FactoryTool.getInstance().parseBTCmd(data,ActionPresenter.this);
        }

    };

    //申请定位权限
    @Override
    public void applyLocationPermission() {

        PermissionUtils.getInstance().request(new PermissionUtils.PermissionLocationCallback() {
            @Override
            public void onSuccessful() {
                if (mView != null) {
                    mView.locaPermissionSuccess();
                }
            }

            @Override
            public void onFailure() {
                if (mView != null) {
                    mView.locaPermissionFailed();
                }
            }

            @Override
            public void onRationSetting() {

            }

            @Override
            public void onCancelRationSetting() {

            }
        }, PermissionUtils.PermissionEnum.LOACTION, mContext);
    }


    /**
     * 开始扫描蓝牙设备
     * 启动一个定时器30S超时搜索失败
     */
    @Override
    public void startScanBle() {
        mBlueClient.startScan();
    }

    /**
     * 停止扫描
     **/
    @Override
    public void stopScanBle() {
        mBlueClient.cancelScan();
    }

    @Override
    public BluetoothDevice getBleConnectDevice() {
        return mBlueClient.getConnectedDevice();
    }

    //断开连接
    @Override
    public void disconnect() {

    }

    /**
     * 连接蓝牙设备
     *
     * @param device
     */
    @Override
    public void connect(BluetoothDevice device) {
        mBlueClient.connect(device.getAddress());
    }

    /**
     * 是否开启蓝牙
     *
     * @return
     */
    public boolean isBluetoohEnable() {
        return mBlueClient.isEnabled();
    }

    public void openBluetooh() {
        mBlueClient.openBluetooth();
    }
}
