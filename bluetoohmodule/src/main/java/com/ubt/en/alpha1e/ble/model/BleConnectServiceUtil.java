package com.ubt.en.alpha1e.ble.model;

import android.content.Context;
import android.content.Intent;

import com.ubt.baselib.model1E.ManualEvent;
import com.ubt.en.alpha1e.ble.service.AutoConnectService;
import com.vise.log.ViseLog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author：liuhai
 * @date：2018/4/27 14:35
 * @modifier：ubt
 * @modify_date：2018/4/27 14:35
 * [A brief description]
 * version
 */

public class BleConnectServiceUtil {

    /**
     * 进入退出手动连接
     *
     * @param manualEvent 进入为true 退出为false
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doEntryManalConnect(ManualEvent manualEvent) {
        if (manualEvent.getEvent() == ManualEvent.Event.START_AUTOSERVICE) {
            ViseLog.d("启动Auto Service");
            Context context = manualEvent.getContext();
            if (manualEvent.isManual()) {
                startService(context);
            } else {
                stopService(context);
            }
        }
    }


    public void startService(Context context) {
        context.startService(new Intent(context, AutoConnectService.class));
    }

    public void stopService(Context context) {
        context.stopService(new Intent(context, AutoConnectService.class));
    }
}
