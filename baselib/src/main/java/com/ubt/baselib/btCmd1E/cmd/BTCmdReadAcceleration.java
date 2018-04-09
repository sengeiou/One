package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 11:31
 * @描述:  读取机器人加速度, 0x01开启上报， 0x00关闭上报
 */

public class BTCmdReadAcceleration extends BaseBTReq {
    public static final byte START_ACC = 0x01;
    public static final byte STOP_ACC = 0x00;

    byte    cmd = BTCmd.DV_READ_ROBOT_ACCELERATION;
    byte[] parm = {0x00};

    public BTCmdReadAcceleration(byte bparm) {
        parm[0] = bparm;
        initReq(cmd, parm);
    }
}
