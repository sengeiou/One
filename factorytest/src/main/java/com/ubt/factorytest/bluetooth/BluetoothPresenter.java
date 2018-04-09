package com.ubt.factorytest.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientListenerAdapter;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.factorytest.R;
import com.ubt.factorytest.bluetooth.recycleview.BTBean;
import com.ubt.factorytest.utils.ContextUtils;
import com.ubt.factorytest.utils.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/11/21 09:47
 * @描述:
 */

public class BluetoothPresenter implements BluetoothContract.Presenter{
    private static final String TAG = "BluetoothPresenter";

    private BluetoothContract.View mView;

//    private BluetoothController mBluetoothController;

    private BlueClientUtil mBlueClient;

    boolean isScanning = false;

    private List<BTBean> mBTList = new ArrayList<>();

    private int mConnectState;
    private int mPreState;

    public BluetoothPresenter(BluetoothContract.View view){
        mView = view;
        mBlueClient = BlueClientUtil.getInstance();
//        mBluetoothController = BluetoothController.getInstance();
        mView.setPresenter(this);
        initBTListener();
    }

    private BlueClientListenerAdapter mBTListener = new BlueClientListenerAdapter() {

        @Override
        public void onActionDiscoveryStateChanged(String discoveryState) {
            Log.d(TAG, "zz onActionDiscoveryStateChanged:" + discoveryState);
            if (discoveryState.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                isScanning = true;
                if(mConnectState != BluetoothState.STATE_CONNECTING) {
                    mView.setProcessBarOn(isScanning);
                }
                mView.setScanMenuTitle(ContextUtils.getContext().getResources()
                        .getString(R.string.action_stop));
            } else if (discoveryState.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                isScanning = false;
                mView.setProcessBarOn(isScanning);
                mView.setScanMenuTitle(ContextUtils.getContext().getResources()
                        .getString(R.string.action_scan));
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }
            }
        }

        @Override
        public void onBluetoothServiceStateChanged(int state) {
            Log.i(TAG,"onBluetoothServiceStateChanged state="+state);
            mConnectState = state;
            switch (state){
                case BluetoothState.STATE_CONNECTED:
                    mView.isBTConnectOK(true);
                    mView.setProcessBarOn(false);
                    break;
                case BluetoothState.STATE_CONNECTING:
                    mView.setProcessBarOn(true);
                    mView.btConnectting();
                    break;
                case BluetoothState.STATE_DISCONNECTED:
                    break;
                default:
                    if(mPreState == BluetoothState.STATE_CONNECTING) {
                        mView.isBTConnectOK(false);
                        mView.setProcessBarOn(false);
                    }
                    break;
            }
            mPreState = state;
        }

        @Override
        public void onActionDeviceFound(BluetoothDevice device, int rssi) {
            String name = device.getName();
            if(name!= null &&name.toLowerCase().contains("alpha1e")) {
                if(!isListHasTheSame(mBTList, name)) {
                    mBTList.add(new BTBean(BTBean.BT_ITEM_VIEW, device.getName(),
                            device.getAddress(), rssi + ""));
                    mView.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    public void start() {

    }

    @Override
    public void reInitBT() {
        /*if (mBluetoothController != null){
            mBluetoothController.disconnect();
            mBluetoothController.release();
            mBluetoothController.build(ContextUtils.getContext());
        }*/
    }

    @Override
    public void initBTListener() {
        mBlueClient.setBlueListener(mBTListener);
    }

    @Override
    public boolean bStartBTScan() {
        return mBlueClient.startScan();
    }

    @Override
    public boolean bCancelBTScan() {
        return mBlueClient.cancelScan();
    }

    @Override
    public boolean isBTScaning() {
        return isScanning;
    }

    @Override
    public List<BTBean> getBTList() {
        return mBTList;
    }

    @Override
    public void cleanBTList() {
        mBTList.clear();
    }

    @Override
    public void connectBT(String mac) {
        if(mConnectState != BluetoothState.STATE_CONNECTING) {
            EspressoIdlingResource.increment();
            mBlueClient.connect(mac);
        }else{
            Log.e(TAG,"connectBT 正在连接中，抛弃当前连接");
        }
    }

    @Override
    public boolean isBTEnabled(){
        return mBlueClient.isEnabled();
    }

    @Override
    public boolean openBluetooth(){
        return mBlueClient.openBluetooth();
    }

    private boolean isListHasTheSame(List<BTBean> btList, String name){
        boolean has = false;

        for (BTBean BTBean: btList) {
            if(BTBean.getBtName().equals(name)){
                has = true;
                break;
            }
        }

        return has;
    }

}
