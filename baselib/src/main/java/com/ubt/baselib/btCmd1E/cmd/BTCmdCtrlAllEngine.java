package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 10:15
 * @描述: 控制16个舵机动作
 */

public class BTCmdCtrlAllEngine extends BaseBTReq {
    byte    cmd = BTCmd.CTRL_ALL_ENGINE;

    public BTCmdCtrlAllEngine(byte[] bparm) {
        initReq(cmd, bparm);
    }
}
