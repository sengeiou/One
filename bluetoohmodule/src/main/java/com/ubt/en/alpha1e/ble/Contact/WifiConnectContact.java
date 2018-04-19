package com.ubt.en.alpha1e.ble.Contact;

import android.content.Context;
import android.net.wifi.ScanResult;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;

import java.util.List;

/**
 * @author：liuhai
 * @date：2018/4/19 19:00
 * @modifier：ubt
 * @modify_date：2018/4/19 19:00
 * [A brief description]
 * version
 */

public class WifiConnectContact {
    public interface View extends BaseView {
       void getWifiList(List<ScanResult> list);

    }

    public interface Presenter extends BasePresenter<WifiConnectContact.View> {
        void init(Context context);

        void startScanWifi();

        void unRegister();

    }
}
