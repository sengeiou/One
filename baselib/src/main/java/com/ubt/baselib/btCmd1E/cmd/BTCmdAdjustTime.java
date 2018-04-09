package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;
import com.ubt.baselib.utils.TimeUtils;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 17:02
 * @描述: 时间校准
 */

public class BTCmdAdjustTime extends BaseBTReq {
    byte    cmd = BTCmd.DV_ADJUST_TIME;

    public BTCmdAdjustTime() {
        byte[] timeParam = TimeUtils.getCurrentDateTimeBytes();
        initReq(cmd, timeParam);
    }
}
