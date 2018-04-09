package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 14:51
 * @描述: 1E 机器人控制舵机命令
 * 参数1 1B: 1.掉电；2.上电
 * 参数2 1B：1.左手；2.右手 3.双脚。
 */

public class BTCmdControlEngineGroup extends BaseBTReq {
    byte    cmd = BTCmd.DV_CONTROL_ENGINE_COMMAND;

    public BTCmdControlEngineGroup(byte[] bparm) {
        initReq(cmd, bparm);
    }
}
