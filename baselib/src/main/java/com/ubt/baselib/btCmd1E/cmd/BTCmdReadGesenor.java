package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 11:27
 * @描述: 读取机器人的姿态角度（前倾角/后倾角/左倾角/右倾角 角度）
 */

public class BTCmdReadGesenor extends BaseBTReq {
    public static final byte START_GSENSIR = 0x01;
    public static final byte STOP_GSENSIR = 0x00;

    byte    cmd = BTCmd.DV_READ_ROBOT_GYROSCOPE_DATA;
    byte[] parm = {0x00};

    public BTCmdReadGesenor(byte bparm) {
        parm[0] = bparm;
        initReq(cmd, parm);
    }
}
