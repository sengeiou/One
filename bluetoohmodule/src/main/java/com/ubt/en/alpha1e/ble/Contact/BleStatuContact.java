package com.ubt.en.alpha1e.ble.Contact;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;
import com.ubt.en.alpha1e.ble.model.BleNetWork;
import com.ubt.en.alpha1e.ble.model.RobotStatu;

/**
 * @author：liuhai
 * @date：2018/4/11 11:08
 * @modifier：ubt
 * @modify_date：2018/4/11 11:08
 * [A brief description]
 * version
 */

public class BleStatuContact {
    public interface View extends BaseView {

        void setBleConnectStatu(BluetoothDevice device);


        void setRobotStatu(RobotStatu robotStatu);

        void setRobotNetWork(BleNetWork bleNetWork);

        void setRobotSoftVersion(String softVersion);

        void setRobotSN(String SN);

        void goBleSraechActivity();
    }

    public interface Presenter extends BasePresenter<View> {

        void init(Context context);

        void unRegister();

        void getRobotBleConnect();

        void dissConnectRobot();

        void checkBlestatu();

    }
}
