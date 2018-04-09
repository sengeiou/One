package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 10:47
 * @描述:
 */

public class BTCmdReadUIDCode extends BaseBTReq {
    byte    cmd = BTCmd.READ_UID_CODE;
    byte[] parm = {0x00};

    public BTCmdReadUIDCode() {
        initReq(cmd, parm);
    }
}
