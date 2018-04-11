package com.ubt.en.alpha1e.ble.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ubt.baselib.BlueTooth.BTDeviceFound;
import com.ubt.baselib.BlueTooth.BTDiscoveryStateChanged;
import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.BlueTooth.BTServiceStateChanged;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.en.alpha1e.ble.Contact.BleConnectContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.R2;
import com.ubt.en.alpha1e.ble.model.BleDevice;
import com.ubt.en.alpha1e.ble.model.BluetoothDeviceListAdapter;
import com.ubt.en.alpha1e.ble.presenter.BleConnectPrenster;
import com.ubt.globaldialog.customDialog.loading.LoadingDialog;
import com.vise.log.ViseLog;
import com.vise.utils.convert.HexUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BleConnectActivity extends MVPBaseActivity<BleConnectContact.View, BleConnectPrenster> implements BleConnectContact.View {

    Unbinder mUnbinder;
    @BindView(R2.id.ble_buletooth_device_list)
    RecyclerView mBuletoothDeviceList;

    private List<BleDevice> mBleDevices = new ArrayList<>();
    boolean isScanning;//是否正在扫描

    private MyHandler mMyHandler = new MyHandler(BleConnectActivity.this);
    private boolean isConnecting;//是否正在连接

    private BluetoothDeviceListAdapter mDeviceListAdapter;

    /**
     * 蓝牙开启
     */
    private static final int MESSAG_BLE_TURNON = 0x111;
    private BluetoothDevice mCurrentRobotInfo;

    @Override
    public int getContentViewId() {
        return R.layout.ble_activity_connect;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        initUi();
        mPresenter.init(this);
        registerBroadcastReceiver();
        //判断蓝牙是否开启
        if (mPresenter.isBluetoohEnable()) {
            mPresenter.applyLocationPermission();
        } else {
            mPresenter.openBluetooh();
        }
    }

    private void initUi() {
        LoadingDialog.show(this);
        mDeviceListAdapter = new BluetoothDeviceListAdapter(R.layout.ble_item_bledevice_layout, mBleDevices);
        mBuletoothDeviceList.setLayoutManager(new LinearLayoutManager(this));
        mBuletoothDeviceList.setAdapter(mDeviceListAdapter);
    }

    /**
     * 注册广播
     */
    public void registerBroadcastReceiver() {
        // TODO Auto-generated method stub
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    /**
     * 机器人状态改变、新消息、边充边玩接收广播
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    if (blueState == BluetoothAdapter.STATE_TURNING_ON) {
                        ViseLog.d("蓝牙开启");
                        mMyHandler.sendEmptyMessage(MESSAG_BLE_TURNON);
                    }
                    break;
                default:

            }
        }
    };


    MyTimerTask task = new MyTimerTask();

    //超时处理
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            ViseLog.d("30S 时间到");
            if (mBleDevices.size() == 0) {
                ViseLog.d("没有搜索到蓝牙!");
                mPresenter.stopScanBle();
                if (isConnecting) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            rl_content_bluetooth_connect_sucess.setVisibility(View.INVISIBLE);
//                            rl_content_bluetooth_connect_fail.setVisibility(View.INVISIBLE);
//                            rl_content_device_list.setVisibility(View.INVISIBLE);
//                            rl_content_no_device.setVisibility(View.VISIBLE);
//                            rl_content_device_researching.setVisibility(View.INVISIBLE);
//                            rl_content_bluetooth_connecting.setVisibility(View.INVISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }


    /**
     * 发现蓝牙设备
     *
     * @param deviceFound
     */
    @org.greenrobot.eventbus.Subscribe
    public void onBlueDeviceFound(BTDeviceFound deviceFound) {
        ViseLog.i("getAddress:" + deviceFound.getBluetoothDevice().getAddress() + "  rssi:" + deviceFound.getRssi());
        BluetoothDevice device = deviceFound.getBluetoothDevice();
        int rssi = (short) deviceFound.getRssi();
        String name = device.getName();
        if (name != null && name.toLowerCase().contains("alpha1e")) {
            dealBleDevice(device, rssi);
        }
    }

    /**
     * 读取蓝牙回调数据
     *
     * @param readData
     */
    @org.greenrobot.eventbus.Subscribe
    public void onReadData(BTReadData readData) {
        ViseLog.i("data:" + HexUtil.encodeHexStr(readData.getDatas()));
    }

    /**
     * 蓝牙连接断开状态
     *
     * @param serviceStateChanged
     */
    @org.greenrobot.eventbus.Subscribe
    public void onBluetoothServiceStateChanged(BTServiceStateChanged serviceStateChanged) {
        ViseLog.i("getState:" + serviceStateChanged.toString());
    }

    /**
     * 蓝牙搜索状态
     *
     * @param stateChanged
     */
    @org.greenrobot.eventbus.Subscribe
    public void onActionDiscoveryStateChanged(BTDiscoveryStateChanged stateChanged) {
        ViseLog.i("getDiscoveryState:" + stateChanged.getDiscoveryState());
        if (stateChanged.getDiscoveryState() == BTDiscoveryStateChanged.DISCOVERY_STARTED) {
            isScanning = true;
        } else {
            mPresenter.startScanBle();
            isScanning = false;
        }
    }

    /**
     * 处理蓝牙设备
     *
     * @param device
     * @param rssi
     */
    @Override
    public synchronized void dealBleDevice(BluetoothDevice device, int rssi) {
        if (isConnecting) {
            return;
        }
        if (device != null) {
            boolean isNewDevice = true;
            for (BleDevice bleDevice : mBleDevices) {
                if (bleDevice.getMac().equals(device.getAddress())) {
                    ViseLog.d("mac : " + device.getAddress() + "    isNewDevice = " + isNewDevice + "   rssi = " + rssi);
                    isNewDevice = false;
                    break;
                }
            }
            if (isNewDevice) {
                mBleDevices.add(new BleDevice(device.getName(), device.getAddress(), 1));
            }

            if (!isConnecting && mBleDevices.size() > 0) {
                LoadingDialog.dismiss(this);
            }

            mDeviceListAdapter.notifyDataSetChanged();

            if (!isConnecting && getDistance((short) rssi) < 0.8) {

                if (mPresenter.getBleConnectDevice() != null) {
                    ViseLog.d("-蓝牙已经连上，则不使用自动连接--");
                    return;
                }

                ViseLog.d("距离近 进行自动连接");
                mCurrentRobotInfo = device;
                isConnecting = true;
                mPresenter.stopScanBle();
                mBleDevices.clear();
                //开始连接蓝牙
                mPresenter.connect(mCurrentRobotInfo);

            }
        }

    }


    /**
     * 定位权限申请成功，开始搜索蓝牙列表
     */
    @Override
    public void locaPermissionSuccess() {
        mPresenter.startScanBle();
    }

    /**
     * 定位权限申请失败
     */
    @Override
    public void locaPermissionFailed() {

    }

    /**
     * 连接蓝牙成功
     */
    @Override
    public void connectSuccess() {

    }


    /**
     * 连接蓝牙失败
     */
    @Override
    public void connextFailed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        mMyHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }

    //静态内部类防止Handler内存泄漏
    private class MyHandler extends Handler {
        WeakReference<BleConnectActivity> mWeakReference;

        public MyHandler(BleConnectActivity bleConnectActivity) {
            mWeakReference = new WeakReference<BleConnectActivity>(bleConnectActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAG_BLE_TURNON) {//蓝牙开启以后就要去判断定位权限
                mPresenter.applyLocationPermission();
            }
        }
    }


    /**
     * 更加rssi信号转换成距离
     * d=10^((ABS(RSSI)-A)/(10*n))、A 代表在距离一米时的信号强度(45 ~ 49), n 代表环境对信号的衰减系数(3.25 ~ 4.5)
     *
     * @param rssi
     * @return
     */
    public float getDistance(short rssi) {
        //return (float) Math.pow(10, (Math.abs(rssi) - 45) / (10 * 3.25));
        float A_Value = 49;
        float n_Value = 3.5f;
        int iRssi = Math.abs(rssi);
        float power = (iRssi - A_Value) / (10 * n_Value);
        return (float) Math.pow(10, power);
    }
}
