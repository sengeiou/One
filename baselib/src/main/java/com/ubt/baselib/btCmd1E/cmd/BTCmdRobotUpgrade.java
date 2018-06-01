package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 16:05
 * @描述:
 * 是否进入安装软件（1 电量充足进入安装，0 电量不足，不进入安装）.
 */


public class BTCmdRobotUpgrade extends BaseBTReq {
    public static final byte START = 0x01;
    public static final byte STOP = 0x00;
    byte    cmd = BTCmd.DV_DO_UPGRADE_SOFT;
    byte[] parm = {0x00};

    public BTCmdRobotUpgrade(byte bparm) {
        parm[0] = bparm;
        initReq(cmd, parm);
    }
}
