package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 15:23
 * @描述:
 */

public class BTCmdSoundStopPlay extends BaseBTReq {
    byte    cmd = BTCmd.DV_SET_STOP_VOICE;
    byte[] parm = {0x00};

    public BTCmdSoundStopPlay() {
        initReq(cmd, parm);
    }
}
