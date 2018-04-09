package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 10:26
 * @描述:
 * * 读写SN命令。参数<P1><P2>。 P1 == 0，表示读SN，无P2参数；P1 ==
 * 1，表示写SN，P2为写入的设备SN，不定长度最大16字节字符串。 设备应答：<P1><P2>。参数P1 ==
 * 0，表示读SN，P2为读取的设备SN，不定长度最大16字节字符串； P1 ==
 * 1，表示写SN，P2为写入状态，P2==0成功，P2==1失败。如果下发的SN和设备端一样，设备端不会进行写操作，但会反回成功标志。
 */

public class BTCmdReadSNCode extends BaseBTReq {
    byte    cmd = BTCmd.READ_SN_CODE;
    byte[] parm = {0x00};

    public BTCmdReadSNCode() {
        initReq(cmd, parm);
    }
}
