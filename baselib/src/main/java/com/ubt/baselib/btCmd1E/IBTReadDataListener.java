package com.ubt.baselib.btCmd1E;

import com.ubt.baselib.BlueTooth.BTReadData;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/6/8 10:13
 * @描述:
 */

public interface IBTReadDataListener {
    void onData(BTReadData data);
}
