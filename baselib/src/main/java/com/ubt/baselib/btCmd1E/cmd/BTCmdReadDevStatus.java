package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 16:44
 * @描述: * 读取状态：0x0a 下位机返回：声音状态(00+声音状态(00 静音 01有声音)) 播放状态(01+(播放状态00 暂停 01非暂停))
 *          音量状态(02+1B(255)低字节在前) 灯状态：（03+00:关， 01开） SD卡状态：04 1-OK 0-NO
 */

public class BTCmdReadDevStatus extends BaseBTReq {
    byte    cmd = BTCmd.DV_READSTATUS;
    byte[] parm = {0x00};

    public BTCmdReadDevStatus(){
        initReq(cmd, parm);
    }
}
