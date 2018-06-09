package com.ubt.en.alpha1e.ble.Contact;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.ubt.baselib.model1E.BleNetWork;
import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;
import com.ubt.en.alpha1e.ble.model.BleDownloadLanguageRsp;
import com.ubt.en.alpha1e.ble.model.BleRobotVersionInfo;
import com.ubt.en.alpha1e.ble.model.SystemRobotInfo;
import com.ubt.en.alpha1e.ble.model.UpgradeProgressInfo;

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



        void setRobotNetWork(BleNetWork bleNetWork);

        void setRobotSoftVersion(SystemRobotInfo systemRobotInfo);

        void setRobotSN(String SN);

        void goBleSraechActivity();

        void downSystemProgress(UpgradeProgressInfo progressInfo);

        void downLanguageProgress(BleDownloadLanguageRsp progressInfo);

        void setRobotVersionInfo(BleRobotVersionInfo robotVersionInfo);
    }

    public interface Presenter extends BasePresenter<View> {

        void init(Context context);

        void unRegister();

        void getRobotBleConnect();

        void dissConnectRobot();

        void checkBlestatu();

        /** 改变 1E 自动升级状态
         * @param is0pen false 为未开启， true为已开启
         */
        void doChangeAutoUpgrade(boolean is0pen);
    }
}
