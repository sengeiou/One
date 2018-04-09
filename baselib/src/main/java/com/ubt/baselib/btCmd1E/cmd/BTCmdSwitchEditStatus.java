package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 14:27
 * @描述:  公共状态切换命令
 * 进参数1（1B ）：状态切换类型：
    0:未知状态，
    １:进入逻辑编程状态，
    ２:退出逻辑编程状态，
    3:进入动作编缉状态，
    4:退出动作编缉状态
 */

public class BTCmdSwitchEditStatus extends BaseBTReq {
    public static final byte INTO_LOGIC = 0x01;
    public static final byte OUT_LOGIC = 0x02;
    public static final byte INTO_ACTION_EDIT = 0x03;
    public static final byte OUT_ACTION_EDIT = 0x04;

    byte    cmd = BTCmd.DV_INTO_EDIT;
    byte[] parm = {0x00};

    public BTCmdSwitchEditStatus(byte bparm) {
        parm[0] = bparm;
        initReq(cmd, parm);
    }
}
