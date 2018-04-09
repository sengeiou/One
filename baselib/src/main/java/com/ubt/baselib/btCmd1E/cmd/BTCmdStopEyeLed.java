package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 14:01
 * @描述: 停止眼睛灯光
 */

public class BTCmdStopEyeLed extends BaseBTReq {
    byte    cmd = BTCmd.DV_SET_STOP_LED_LIGHT;
    byte[] parm = {0x00};

    public BTCmdStopEyeLed() {
        initReq(cmd, parm);
    }
}
