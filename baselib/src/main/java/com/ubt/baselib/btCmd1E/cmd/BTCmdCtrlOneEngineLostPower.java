package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;
import com.ubt.baselib.utils.ByteHexHelper;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 10:18
 * @描述: 单个舵机掉电
 */

public class BTCmdCtrlOneEngineLostPower extends BaseBTReq{
    byte    cmd = BTCmd.CTRL_ONE_ENGINE_LOST_POWER;
    byte[] parm = {0x00};

    public BTCmdCtrlOneEngineLostPower(int id) {
        parm[0] = ByteHexHelper.intToHexByte(id);
        initReq(cmd, parm);
    }
}
