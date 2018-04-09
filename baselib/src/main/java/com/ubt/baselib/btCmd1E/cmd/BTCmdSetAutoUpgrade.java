package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 11:06
 * @描述:
 * 切换自动升级状态.
 * 0 为未开启， 1为已开启 , 2 设置中
 */

public class BTCmdSetAutoUpgrade extends BaseBTReq {
    public static final byte ON = 0x01;
    public static final byte OFF = 0x00;
    byte    cmd = BTCmd.DV_SET_AUTO_UPGRADE;
    byte[] parm = {0x00};

    public BTCmdSetAutoUpgrade(byte bparm) {
        parm[0] = bparm;
        initReq(cmd, parm);
    }
}
