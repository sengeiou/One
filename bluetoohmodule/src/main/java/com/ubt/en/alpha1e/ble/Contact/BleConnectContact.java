package com.ubt.en.alpha1e.ble.Contact;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;
import com.ubt.en.alpha1e.ble.model.BleDevice;

import java.util.List;

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

        void notifyDataSetChanged();

        void searchSuccess();

        void searchBleFiled();

        void connectSuccess();

        void connectFailed();

        void connecting();
    }

    public interface Presenter extends BasePresenter<View> {

        void register(Context context);

        List<BleDevice> getBleDevices();

        void startScanBle();

        void stopScanBle();

        BluetoothDevice getBleConnectDevice();

        void disconnect();

        void connect(String mac);

        void unRegister();

    }
}
