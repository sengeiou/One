package com.ubt.en.alpha1e.BlueTooth;

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


    @Override
    public void onReadData(BluetoothDevice bluetoothDevice, byte[] bytes) {
        mReadData.setBluetoothDevice(bluetoothDevice);
        mReadData.setDatas(bytes);
        EventBus.getDefault().post(mReadData);
    }

    @Override
    public void onActionStateChanged(int preState, int state) {
        mStateChanged.setPreState(preState);
        mStateChanged.setState(state);
        EventBus.getDefault().post(mStateChanged);
    }

    @Override
    public void onActionDiscoveryStateChanged(String discoveryState) {
        mDiscover.setDiscoveryState(discoveryState);
        EventBus.getDefault().post(mDiscover);
    }

    @Override
    public void onActionScanModeChanged(int preScanMode, int scanMode) {
        mScanMode.setPreScanMode(preScanMode);
        mScanMode.setScanMode(scanMode);
        EventBus.getDefault().post(mScanMode);
    }

    @Override
    public void onBluetoothServiceStateChanged(int state) {
        mServiceState.setState(state);
        EventBus.getDefault().post(mServiceState);
    }


    @Override
    public void onActionDeviceFound(BluetoothDevice bluetoothDevice, int rssi) {
        mDeviceFound.setBluetoothDevice(bluetoothDevice);
        mDeviceFound.setRssi(rssi);
        EventBus.getDefault().post(mDeviceFound);
    }
}
