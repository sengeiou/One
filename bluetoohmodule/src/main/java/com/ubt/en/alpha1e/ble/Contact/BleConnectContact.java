package com.ubt.en.alpha1e.ble.Contact;

import android.bluetooth.BluetoothDevice;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;

/**
 * @author：liuhai
 * @date：2018/4/11 11:08
 * @modifier：ubt
 * @modify_date：2018/4/11 11:08
 * [A brief description]
 * version
 */

public class BleConnectContact {
    public interface View extends BaseView {
        void dealBleDevice(BluetoothDevice device, int rssi);

        void locaPermissionSuccess();

        void locaPermissionFailed();

        void connectSuccess();

        void connextFailed();
    }

    public interface Presenter extends BasePresenter<View> {

        void applyLocationPermission();

        void startScanBle();

        void stopScanBle();

        BluetoothDevice getBleConnectDevice();
        void disconnect();

        void connect(BluetoothDevice device);

    }
}
