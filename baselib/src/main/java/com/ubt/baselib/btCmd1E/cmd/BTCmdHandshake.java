package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 14:54
 * @描述: 握手
 */

public class BTCmdHandshake extends BaseBTReq{
    byte    cmd = BTCmd.DV_HANDSHAKE;
    byte[] parm = {0x00};

    public BTCmdHandshake() {
        initReq(cmd, parm);
    }
}
