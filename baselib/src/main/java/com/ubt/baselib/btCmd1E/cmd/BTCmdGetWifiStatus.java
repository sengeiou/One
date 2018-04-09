package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 11:01
 * @描述: 获取WIFI状态
 */

public class BTCmdGetWifiStatus extends BaseBTReq {
    byte    cmd = BTCmd.DV_READ_NETWORK_STATUS;
    byte[] parm = {0x00};

    public BTCmdGetWifiStatus() {
        initReq(cmd, parm);
    }
}
