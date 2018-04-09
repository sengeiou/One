package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 16:45
 * @描述: 调整音量 参数：(0~255)
 */

public class BTCmdVolumeAdjust extends BaseBTReq {
    byte    cmd = BTCmd.DV_VOLUME;
    byte[] parm = {0x7f};

    public BTCmdVolumeAdjust(byte bparm){
        parm[0] = bparm;
        initReq(cmd, parm);
    }
}
