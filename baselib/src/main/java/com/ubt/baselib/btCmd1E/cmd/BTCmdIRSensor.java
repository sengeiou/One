package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/4/25 10:33
 * @描述:
 */

public class BTCmdIRSensor extends BaseBTReq{
    public static final byte START = 0x01;  //开启
    public static final byte STOP = 0x00;   //停止

    byte    cmd = BTCmd.DV_CONTROL_IR_SENSOR;
    byte[] parm = {0x01, 0x00};

    public BTCmdIRSensor(byte bparm) {
        parm[1] = bparm;
        initReq(cmd, parm);
    }

    public BTCmdIRSensor() {
        parm[0] = 0x00;
        initReq(cmd, parm);
    }
}
