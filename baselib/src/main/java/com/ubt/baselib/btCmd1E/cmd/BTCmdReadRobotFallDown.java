package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 11:29
 * @描述: 读取机器人跌倒状态
 */

public class BTCmdReadRobotFallDown extends BaseBTReq {
    byte    cmd = BTCmd.DV_READ_ROBOT_FALL_DOWN;
    byte[] parm = {0x00};

    public BTCmdReadRobotFallDown() {
        initReq(cmd, parm);
    }
}
