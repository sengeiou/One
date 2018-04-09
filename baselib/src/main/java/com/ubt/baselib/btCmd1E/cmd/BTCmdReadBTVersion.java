package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 20:32
 * @描述:
 */

public class BTCmdReadBTVersion extends BaseBTReq {
    byte    cmd = BTCmd.DV_READ_BLUETOOTH_VERSION;
    byte[] parm = {0x00};

    public BTCmdReadBTVersion() {
        initReq(cmd, parm);
    }
}
