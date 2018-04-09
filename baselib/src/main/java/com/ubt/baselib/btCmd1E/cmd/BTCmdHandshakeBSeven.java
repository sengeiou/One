package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 10:54
 * @描述:
 * 用FBCF协议头读B7看舵机个数和列表,以便后面操作舵机,表示支持UTF8,不回复B7表示为GBK旧版,
 * 不支持多舵机控制,并且主机只能使用FBBF协议头下发.
 */

public class BTCmdHandshakeBSeven extends BaseBTReq {
    byte    cmd = BTCmd.DV_HANDSHAKE_B_SEVEN;
    byte[] parm = {0x00};

    public BTCmdHandshakeBSeven() {
        initReq(cmd, parm);
    }
}
