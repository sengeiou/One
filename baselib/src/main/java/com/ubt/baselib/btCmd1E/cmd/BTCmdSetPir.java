package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 11:25
 * @描述:
 * 读取红外传感器与障碍物的距离
 * 参数 01 开启上报， 00 停止上报
 */

public class BTCmdSetPir extends BaseBTReq {
    public static final byte START_PIR = 0x01;
    public static final byte STOP_PIR = 0x00;
    byte    cmd = BTCmd.DV_READ_INFRARED_DISTANCE;
    byte[] parm = {0x00};

    public BTCmdSetPir(byte bparm) {
        parm[0] = bparm;
        initReq(cmd, parm);
    }
}
