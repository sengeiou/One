package com.ubt.en.alpha1e.ble.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ubt.baselib.BlueTooth.BTDeviceFound;
import com.ubt.baselib.BlueTooth.BTDiscoveryStateChanged;
import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.BlueTooth.BTServiceStateChanged;
import com.ubt.baselib.BlueTooth.BTStateChanged;
import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BTCmdHelper;
import com.ubt.baselib.btCmd1E.IProtolPackListener;
import com.ubt.baselib.btCmd1E.ProtocolPacket;
import com.ubt.baselib.btCmd1E.cmd.BTCmdHandshake;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.PermissionUtils;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.ble.Contact.BleConnectContact;
import com.ubt.en.alpha1e.ble.model.BleDevice;
import com.vise.log.ViseLog;
import com.vise.utils.convert.HexUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class BleConnectPrenster extends BasePresenterImpl<BleConnectContact.View> implements BleConnectContact.Presenter, IProtolPackListener {

    private static final String TAG = "BleConnectPrenster";
    boolean isScanning = false;
    private BlueClientUtil mBlueClient;

    private int mConnectState;
    private int mPreState;
    private Context mContext;

    boolean isConnecting;

    private Date lastTime_onConnectState = null;

    private Date lastTime_DV_HANDSHAKE = null;//握手次数时间


    private static final long TIME_OUT = 30 * 1000;

    /**
     * 蓝牙搜索超时
     */
    private static final int MESSAG_SEARCH_TIMEOUT = 0x111;

    /**
     * 蓝牙连接超时
     */
    private static final int MESSAG_CONNECT_TIMEOUT = 0x112;

    /**
     * 握手连接超时
     */
    private static final int MESSAG_HANDSHAKE_TIMEOUT = 0x113;


    public List<BleDevice> mBleDevices = new ArrayList<>();
    private BluetoothDevice mCurrentRobotInfo;


    @Override
    public void register(Context context) {
        this.mContext = context;
        mBlueClient = BlueClientUtil.getInstance();
        EventBus.getDefault().register(this);
        //判断蓝牙是否开启,开启成功则申请定位权限
        if (mBlueClient.isEnabled()) {
            applyLocationPermission();
        } else {
            mBlueClient.openBluetooth();
        }
        ViseLog.d("threadNmae==  register  " + Thread.currentThread().getName());
    }

    /**
     * 返回数据
     *
     * @return
     */
    @Override
    public List<BleDevice> getBleDevices() {
        return mBleDevices != null ? mBleDevices : new ArrayList<BleDevice>();
    }

    @Subscribe
    public void onActionStateChanged(BTStateChanged stateChanged) {
        ViseLog.i(stateChanged.toString());
        if (stateChanged.getState() == BTStateChanged.STATE_ON) {
            ViseLog.e("开启蓝牙");
            applyLocationPermission();
        }
    }

    /**
     * 发现蓝牙设备
     *
     * @param deviceFound
     */
    @Subscribe
    public void onBlueDeviceFound(BTDeviceFound deviceFound) {
        //ViseLog.i("getAddress:" + deviceFound.getBluetoothDevice().getAddress() + "  rssi:" + deviceFound.getRssi());
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadData(BTReadData readData) {
        ViseLog.i("data:" + HexUtil.encodeHexStr(readData.getDatas()));
        BTCmdHelper.parseBTCmd(readData.getDatas(), this);
        ViseLog.d("threadNmae==  onReadData  " + Thread.currentThread().getName());
    }

    @Override
    public void onProtocolPacket(ProtocolPacket packet) {
        switch (packet.getmCmd()) {
            case BTCmd.DV_HANDSHAKE:
                // 校验握手时间接受多次数据-----------start
                Date curDate = new Date(System.currentTimeMillis());
                float time_difference = 1000;
                if (lastTime_DV_HANDSHAKE != null) {
                    time_difference = curDate.getTime()
                            - lastTime_DV_HANDSHAKE.getTime();
                }
                lastTime_DV_HANDSHAKE = curDate;

                if (time_difference < 1000) {
                    ViseLog.d("1S 接收到多次握手成功次数");
                    return;
                }
                ViseLog.e("-----------握手成功----------与机器人正式连接");

                if (mView != null) {
                    mView.connectSuccess();
                }
                break;
            default:
                break;
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
        mConnectState = serviceStateChanged.getState();
        switch (serviceStateChanged.getState()) {
            case BluetoothState.STATE_CONNECTED://蓝牙配对成功
                ViseLog.d("蓝牙配对成功");
                // stopConnectBleTask();
                mHandler.removeMessages(MESSAG_CONNECT_TIMEOUT);
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
                mBlueClient.sendData(new BTCmdHandshake().toByteArray());
                mHandler.sendEmptyMessageDelayed(MESSAG_HANDSHAKE_TIMEOUT, TIME_OUT);
                break;
            case BluetoothState.STATE_CONNECTING://正在连接
                ViseLog.e("正在连接");
                break;
            case BluetoothState.STATE_DISCONNECTED:
                ViseLog.e("蓝牙连接断开");
                ToastUtils.showShort("蓝牙断开");
                break;
            default:

                break;
        }
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
        } else if (stateChanged.getDiscoveryState() == BTDiscoveryStateChanged.DISCOVERY_FINISHED) {
            if (!isConnecting) {
                ViseLog.d("蓝牙扫描结束------onActionDiscoveryStateChanged");
                mBlueClient.startScan();
            }
            isScanning = false;
        }
    }


    /**
     * 处理每次获取到的蓝牙设备
     *
     * @param device
     * @param rssi
     */

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
                mHandler.removeMessages(MESSAG_SEARCH_TIMEOUT);
                if (mView != null) {
                    mView.searchSuccess();
                }
            }
            if (mView != null) {
                mView.notifyDataSetChanged();
            }
            connectBySHortDistance(device, rssi);
        }

    }

    /**
     * 距离近是自动连接设备
     *
     * @param device
     * @param rssi
     */
    private void connectBySHortDistance(BluetoothDevice device, int rssi) {
        if (!isConnecting && getDistance((short) rssi) < 0.8) {
            if (mBlueClient.getConnectedDevice() != null) {
                ViseLog.d("-蓝牙已经连上，则不使用自动连接--");
                return;
            }
            //stopConnectBleTask();
            mHandler.removeMessages(MESSAG_CONNECT_TIMEOUT);
            ViseLog.d("距离近 进行自动连接");
            mCurrentRobotInfo = device;
            isConnecting = true;
            mBlueClient.cancelScan();
            mBleDevices.clear();
            //开始连接蓝牙
            mBlueClient.connect(mCurrentRobotInfo.getAddress());
            // startConnectBleTask();
            mHandler.sendEmptyMessageDelayed(MESSAG_CONNECT_TIMEOUT, TIME_OUT);
            if (mView != null) {
                mView.connecting();
            }

        }
    }

    //申请定位权限
    public void applyLocationPermission() {
        PermissionUtils.getInstance().request(new PermissionUtils.PermissionLocationCallback() {
            @Override
            public void onSuccessful() {
                mBlueClient.startScan();
                //startSearchBleTask();
                mHandler.sendEmptyMessageDelayed(MESSAG_SEARCH_TIMEOUT, TIME_OUT);
            }

            @Override
            public void onFailure() {
                if (mView != null) {
                    //  mView.locaPermissionFailed();
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

    /**
     * 获取正在连接的蓝牙设备
     *
     * @return
     */
    @Override
    public BluetoothDevice getBleConnectDevice() {
        return mBlueClient.getConnectedDevice();
    }

    //断开连接
    @Override
    public void disconnect() {
        mBlueClient.disconnect();
    }

    /**
     * 连接蓝牙设备
     *
     * @param mac
     */
    @Override
    public void connect(String mac) {
        if (isConnecting) {
            return;
        }
        mHandler.removeMessages(MESSAG_CONNECT_TIMEOUT);
        //  mBlueClient.release();
        mBlueClient.connect(mac);
        // stopConnectBleTask();
        isConnecting = true;
        if (mView != null) {
            mView.connecting();
        }
    }


    @Override
    public void unRegister() {
        mHandler.removeMessages(MESSAG_SEARCH_TIMEOUT);
        mHandler.removeMessages(MESSAG_CONNECT_TIMEOUT);
        mHandler.removeMessages(MESSAG_HANDSHAKE_TIMEOUT);
        EventBus.getDefault().unregister(this);
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAG_SEARCH_TIMEOUT:
                    ViseLog.d("没有搜索到蓝牙!");
                    mBlueClient.cancelScan();
                    if (isConnecting) {
                        return;
                    }
                    if (mView != null) {
                        mView.searchBleFiled();
                    }
                    break;
                case MESSAG_CONNECT_TIMEOUT:
                    isConnecting = false;
                    mBlueClient.disconnect();
                    ViseLog.d("连接蓝牙超时失败");
                    if (mView != null) {
                        mView.connectFailed();
                    }
                    break;
                case MESSAG_HANDSHAKE_TIMEOUT:
                    isConnecting = false;
                    mBlueClient.disconnect();
                    ViseLog.d("连接蓝牙超时失败");
                    if (mView != null) {
                        mView.connectFailed();
                    }
                    break;
            }
        }
    };


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
