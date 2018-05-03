package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 15:23
 * @描述:
 */

public class BTCmdStopPlayEmoji extends BaseBTReq {
    byte    cmd = BTCmd.DV_SET_STOP_EMOJI;
    byte[] parm = {0x00};

    public BTCmdStopPlayEmoji() {
        initReq(cmd, parm);
    }
}
