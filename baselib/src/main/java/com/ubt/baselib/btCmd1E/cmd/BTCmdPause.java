package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 16:05
 * @描述: 播放控制：0x07 参数：00 － 暂停 01 － 继续
 */

public class BTCmdPause extends BaseBTReq {
    public static final byte CONTINUE = 0x01;
    public static final byte PAUSE = 0x00;
    byte    cmd = BTCmd.DV_PAUSE;
    byte[] parm = {0x00};

    public BTCmdPause(byte bparm) {
        parm[0] = bparm;
        initReq(cmd, parm);
    }
}
