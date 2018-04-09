package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 17:21
 * @描述: 读版软件版本号
 */

public class BTCmdReadSoftVer extends BaseBTReq {
    byte    cmd = BTCmd.DV_READ_SOFTWARE_VERSION;
    byte[] parm = {0x00};

    public BTCmdReadSoftVer() {
        initReq(cmd, parm);
    }
}
