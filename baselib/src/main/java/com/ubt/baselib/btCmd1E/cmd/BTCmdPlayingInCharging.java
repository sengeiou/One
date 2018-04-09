package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 10:49
 * @描述:
 * 设置边充边玩状态。参数<P1>。P1==1，表示允许边充边玩；P1==0，表示禁止边充边玩。
 * 当主机发送动作执行命令且机器人处于禁止边充边玩状态时有回复，参数是0。
 */

public class BTCmdPlayingInCharging extends BaseBTReq {
    public static final byte PERMIT = 0x01;
    public static final byte FORBID = 0x00;
    byte    cmd = BTCmd.SET_PALYING_CHARGING;
    byte[] parm = {0x00};

    public BTCmdPlayingInCharging(byte bparm) {
        parm[0] = bparm;
        initReq(cmd, parm);
    }
}
