package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 14:09
 * @描述: 停止播放步态
 *
 *  回复
    参数1:1B 0.收到命令 1.停止完成
 */

public class BTCmdStopWalk extends BaseBTReq {
    byte    cmd = BTCmd.DV_STOP_WALK;
    byte[] parm = {0x00};

    public BTCmdStopWalk() {
        initReq(cmd, parm);
    }
}
