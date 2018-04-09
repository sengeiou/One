package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 10:08
 * @描述: 控制1个舵机动作
 */

public class BTCmdCtrl0neEngine extends BaseBTReq {
    byte    cmd = BTCmd.CTRL_ONE_ENGINE_ON;

    public BTCmdCtrl0neEngine(byte[] bparm) {
        initReq(cmd, bparm);
    }
}
