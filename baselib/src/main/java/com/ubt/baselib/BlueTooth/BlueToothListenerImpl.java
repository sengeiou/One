package com.ubt.baselib.BlueTooth;

import android.bluetooth.BluetoothDevice;

import com.ubt.bluetoothlib.blueClient.IBlueClientListener;

import org.greenrobot.eventbus.EventBus;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/2/5 09:47
 * @描述:
 */

public class BlueToothListenerImpl implements IBlueClientListener {
    BTReadData mReadData = new BTReadData();
    BTStateChanged mStateChanged = new BTStateChanged();
    BTDiscoveryStateChanged mDiscover = new BTDiscoveryStateChanged();
    BTScanModeChanged mScanMode = new BTScanModeChanged();
    BTServiceStateChanged mServiceState = new BTServiceStateChanged();
    BTDeviceFound mDeviceFound = new BTDeviceFound();


    /**
     * 接收到蓝牙数据
     * @param bluetoothDevice
     * @param bytes
     */
    @Override
    public void onReadData(BluetoothDevice bluetoothDevice, byte[] bytes) {
        mReadData.setBluetoothDevice(bluetoothDevice);
        mReadData.setDatas(bytes);
        EventBus.getDefault().post(mReadData);
    }

    /**
     *  蓝牙开关 打开或关闭状态
     * @param preState
     * @param state
     */
    @Override
    public void onActionStateChanged(int preState, int state) {
        mStateChanged.setPreState(preState);
        mStateChanged.setState(state);
        EventBus.getDefault().post(mStateChanged);
    }

    /**
     *  扫描开始或结束状态
     * @param discoveryState 扫描状态
     *      DISCOVERY_STARTED ; //扫描开始
            DISCOVERY_FINISHED ; //扫描结束
     */
    @Override
    public void onActionDiscoveryStateChanged(String discoveryState) {
        mDiscover.setDiscoveryState(discoveryState);
        EventBus.getDefault().post(mDiscover);
    }

    /**
     *  蓝牙是能被扫描到，或者是否能扫描其它设备
     *   SCAN_MODE_CONNECTABLE         表明该蓝牙可以扫描其他蓝牙设备
         SCAN_MODE_CONNECTABLE_DISCOVERABLE 表 明该蓝牙设备同时可以扫码其他蓝牙设备，并且可以被其他蓝牙设备扫描到。
         SCAN_MODE_NONE ： 该蓝牙不能扫描以及被扫描。
     * @param preScanMode
     * @param scanMode
     */
    @Override
    public void onActionScanModeChanged(int preScanMode, int scanMode) {
        mScanMode.setPreScanMode(preScanMode);
        mScanMode.setScanMode(scanMode);
        EventBus.getDefault().post(mScanMode);
    }

    /**
     *   蓝牙适配状态变化
     * @param state BluetoothState.STATE_CONNECTED:
                    BluetoothState.STATE_CONNECTING:
                    BluetoothState.STATE_DISCONNECTED:
     */
    @Override
    public void onBluetoothServiceStateChanged(int state) {
        mServiceState.setState(state);
        EventBus.getDefault().post(mServiceState);
    }


    /**
     *  扫描到设备后回调
     * @param bluetoothDevice 蓝牙信息
     * @param rssi 信号强度
     */
    @Override
    public void onActionDeviceFound(BluetoothDevice bluetoothDevice, int rssi) {
        mDeviceFound.setBluetoothDevice(bluetoothDevice);
        mDeviceFound.setRssi(rssi);
        EventBus.getDefault().post(mDeviceFound);
    }
}
