package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 14:40
 * @描述:
 * 传感器控制命令 0 禁止 1 启用
 */

public class BTCmdSensorControl extends BaseBTReq {
    public static final byte START = 0x01;  //开启
    public static final byte STOP = 0x00;   //停止

    byte    cmd = BTCmd.DV_SENSOR_CONTROL;
    byte[] parm = {0x01, 0x00};

    /**
     * 写命令构造函数
    * */
    public BTCmdSensorControl(byte bparm) {
        parm[1] = bparm;
        initReq(cmd, parm);
    }

    /**
     * 读命令构造函数
     * */
    public BTCmdSensorControl(){
        parm[0] = 0x00;
        initReq(cmd, parm);
    }
}
