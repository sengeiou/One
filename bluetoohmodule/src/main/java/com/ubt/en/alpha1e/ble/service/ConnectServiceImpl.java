package com.ubt.en.alpha1e.ble.service;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ubt.baselib.commonModule.ModuleUtils;

/**
 * @author：liuhai
 * @date：2018/4/23 17:02
 * @modifier：ubt
 * @modify_date：2018/4/23 17:02
 * [A brief description]
 * version
 */


@Route(path = ModuleUtils.Bluetooh_ConnectService)
public class ConnectServiceImpl implements ConnectService {
    Context mContext;

    @Override
    public void startAutoService() {
        mContext.startService(new Intent(mContext, AutoConnectService.class));
    }

    @Override
    public void init(Context context) {
        this.mContext = context;
    }
}
