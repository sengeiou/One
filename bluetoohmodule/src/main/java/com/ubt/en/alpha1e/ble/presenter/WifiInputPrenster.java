package com.ubt.en.alpha1e.ble.presenter;

import android.content.Context;

import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.en.alpha1e.ble.Contact.WifiInputContact;

/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class WifiInputPrenster extends BasePresenterImpl<WifiInputContact.View> implements WifiInputContact.Presenter {
    @Override
    public void init(Context context) {
        
    }

    @Override
    public void sendPasswd(String wifiName, String passwd) {

    }
}
