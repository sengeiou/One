package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 17:29
 * @描述: 读电量
 */

public class BTCmdReadBattery extends BaseBTReq {
    byte    cmd = BTCmd.DV_READ_BATTERY;
    byte[] parm = {0x00};

    public BTCmdReadBattery() {
        initReq(cmd, parm);
    }
}
