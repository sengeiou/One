package com.ubt.en.alpha1e.ble.service;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author：liuhai
 * @date：2018/4/23 17:02
 * @modifier：ubt
 * @modify_date：2018/4/23 17:02
 * [A brief description]
 * version
 */

public interface ConnectService extends IProvider {
    void startAutoService();
    void stopAutoService();
}
