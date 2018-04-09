package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 14:15
 * @描述: 获取productId，dsn 0x82
        发送参数: 无
        应答参数：字符串：productId + “，” + dsn
 */

public class BTCmdGetProductIDAndDSN extends BaseBTReq {
    byte    cmd = BTCmd.DV_PRODUCT_AND_DSN;
    byte[] parm = {0x00};

    public BTCmdGetProductIDAndDSN() {
        initReq(cmd, parm);
    }
}
