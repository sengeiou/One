package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 16:02
 * @描述: 声音控制：0x06 参数： 00 － 静音 01 - 打开声音
 */

public class BTCmdVoiceControl extends BaseBTReq {
    public static final byte VOICE_OPEN = 0x01;
    public static final byte VOICE_SILENT = 0x00;
    byte    cmd = BTCmd.DV_VOICE;
    byte[] parm = {0x00};

    public BTCmdVoiceControl(byte bparm) {
        parm[0] = bparm;
        initReq(cmd, parm);
    }
}
