package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 17:35
 * @描述: 读硬件版本号
 */

public class BTCmdReadHardwareVer extends BaseBTReq {
    byte    cmd = BTCmd.DV_READ_HARDWARE_VERSION;
    byte[] parm = {0x00};

    public BTCmdReadHardwareVer(){
        initReq(cmd, parm);
    }
}
