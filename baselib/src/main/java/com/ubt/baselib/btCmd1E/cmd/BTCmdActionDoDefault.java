package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @author：liuhai
 * @date：2018/4/25 11:17
 * @modifier：ubt
 * @modify_date：2018/4/25 11:17
 * [A brief description]
 * version
 */

public class BTCmdActionDoDefault extends BaseBTReq {
    byte    cmd = BTCmd.DV_SET_ACTION_DEFAULT;

    public BTCmdActionDoDefault(byte[] bparm) {
        initReq(cmd, bparm);
    }
}
