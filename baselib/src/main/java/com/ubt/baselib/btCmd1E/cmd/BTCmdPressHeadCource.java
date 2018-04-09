package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 14:12
 * @描述: 拍头课程启用禁用 0x79
        参数1B : 0.不进入拍头课程 1为进入拍头课程
 */

public class BTCmdPressHeadCource extends BaseBTReq {
    public static final byte INTO = 0x01;
    public static final byte OUT = 0x00;
    byte    cmd = BTCmd.DV_CAN_CLICK_HEAD;
    byte[] parm = {0x00};

    public BTCmdPressHeadCource(byte bparm) {
        parm[0] = bparm;
        initReq(cmd, parm);
    }
}
