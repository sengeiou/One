package com.ubt.en.alpha1e.ble.Contact;

import android.content.Context;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;

/**
 * @author：liuhai
 * @date：2018/4/19 19:00
 * @modifier：ubt
 * @modify_date：2018/4/19 19:00
 * [A brief description]
 * version
 */

public class WifiInputContact {
    public interface View extends BaseView {
        void connectWifiResult(int type );

        void blutoohDisconnect();
    }

    public interface Presenter extends BasePresenter<WifiInputContact.View> {
        void init(Context context);

        void sendPasswd(String wifiName, String passwd);

        void unRegister();
    }
}
