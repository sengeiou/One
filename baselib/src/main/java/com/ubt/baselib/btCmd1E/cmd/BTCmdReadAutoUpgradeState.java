package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 11:04
 * @描述:
 * 读取自动升级状态.
 * 0 为未开启， 1为已开启
 */

public class BTCmdReadAutoUpgradeState extends BaseBTReq {
    byte    cmd = BTCmd.DV_READ_AUTO_UPGRADE_STATE;
    byte[] parm = {0x00};

    public BTCmdReadAutoUpgradeState() {
        initReq(cmd, parm);
    }
}
