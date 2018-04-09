package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 16:58
 * @描述: 掉电
 */

public class BTCmdPowerOff extends BaseBTReq {
    byte    cmd = BTCmd.DV_DIAODIAN;
    byte[] parm = {0x00};

    public BTCmdPowerOff() {
        initReq(cmd, parm);
    }
}
