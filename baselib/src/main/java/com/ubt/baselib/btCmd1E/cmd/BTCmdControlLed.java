package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 17:00
 * @描述: 灯控制 参数：0-关 1 开
 */

public class BTCmdControlLed extends BaseBTReq {
    public static final byte OPEN = 0x01;
    public static final byte CLOSE = 0x00;
    byte    cmd = BTCmd.DV_LIGHT;
    byte[] parm = {0x00};

    public BTCmdControlLed(byte bparm) {
        parm[0] = bparm;
        initReq(cmd, parm);
    }
}
