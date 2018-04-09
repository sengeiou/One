package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 16:07
 * @描述: 心跳
 */

public class BTCmdHeartBeat extends BaseBTReq {
    byte    cmd = BTCmd.DV_XT;
    byte[] parm = {0x00};

    public BTCmdHeartBeat() {
        initReq(cmd, parm);
    }
}
