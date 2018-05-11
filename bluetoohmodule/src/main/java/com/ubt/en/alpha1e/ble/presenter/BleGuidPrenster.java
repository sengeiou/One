package com.ubt.en.alpha1e.ble.presenter;

import android.content.Context;

import com.ubt.baselib.BlueTooth.BTStateChanged;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.ble.Contact.BleGuideContact;
import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class BleGuidPrenster extends BasePresenterImpl<BleGuideContact.View> implements BleGuideContact.Presenter {

    private BlueClientUtil mBlueClientUtil;


    @Override
    public void init(Context context) {
        mBlueClientUtil = BlueClientUtil.getInstance();
        EventBus.getDefault().register(this);
    }
    /**
     * 监听到蓝牙开启立即申请定位权限
     *
     * @param stateChanged
     */
    @Subscribe
    public void onActionStateChanged(BTStateChanged stateChanged) {
        ViseLog.i(stateChanged.toString());
        if (stateChanged.getState() == BTStateChanged.STATE_ON) {
            ViseLog.e("开启蓝牙");
            if (mView != null) {
                mView.goBleSraechActivity();
            }
        }
    }
    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void checkBlestatu() {
        if (mBlueClientUtil.isEnabled()) {
            if (mView != null) {
                mView.goBleSraechActivity();
            }
        } else {
            mBlueClientUtil.openBluetooth();
        }
    }
}
