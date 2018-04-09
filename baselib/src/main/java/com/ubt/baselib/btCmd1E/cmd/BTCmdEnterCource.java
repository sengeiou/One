package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 14:04
 * @描述: 1E进入课程
 * 参数:1B： 1:进入课程引导状态; 0:离开课程引导状态
 */

public class BTCmdEnterCource extends BaseBTReq {
    public static final byte INTO = 0x01;
    public static final byte OUT = 0x00;
    byte    cmd = BTCmd.DV_ENTER_COURSE;
    byte[] parm = {0x00};

    public BTCmdEnterCource(byte bparm) {
        parm[0] = bparm;
        initReq(cmd, parm);
    }
}
