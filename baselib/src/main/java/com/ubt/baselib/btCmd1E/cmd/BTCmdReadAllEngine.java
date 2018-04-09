package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 10:52
 * @描述:
 * 取16个舵机角度 回复：1个参数，长度16B（对应1-16号舵机的角度）。 单个舵机角度含义：FF，舵机没应答；FE，舵机ID不对
 */

public class BTCmdReadAllEngine extends BaseBTReq {
    byte    cmd = BTCmd.READ_ALL_ENGINE;
    byte[] parm = {0x00};

    public BTCmdReadAllEngine() {
        initReq(cmd, parm);
    }
}
