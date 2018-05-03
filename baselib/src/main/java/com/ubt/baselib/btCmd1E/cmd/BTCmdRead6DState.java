package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 15:23
 * @描述:
 */

public class BTCmdRead6DState extends BaseBTReq {
    byte    cmd = BTCmd.DV_6D_GESTURE;
    byte[] parm = {0x00};

    public BTCmdRead6DState() {
        initReq(cmd, parm);
    }
}
