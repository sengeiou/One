package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 17:08
 * @描述: 读取闹铃时间
 */

public class BTCmdReadAlarm extends BaseBTReq {
    byte    cmd = BTCmd.DV_READ_ALARM;
    byte[] parm = {0x00};

    public BTCmdReadAlarm() {
        initReq(cmd, parm);
    }
}
