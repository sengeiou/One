package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 10:57
 * @描述: 获取机器人WIFI列表
 */

public class BTCmdGetWifiList extends BaseBTReq {
    public static final byte START_GET_WIFI = 0x00;
    public static final byte CONTINUE_GET_WIFI = 0x01;
    public static final byte FINISH_GET_WIFI = 0x02;
    byte cmd = BTCmd.DV_FIND_WIFI_LIST;
    byte[] parm = {0x00};

    public BTCmdGetWifiList(byte bparm) {
        parm[0] = bparm;
        initReq(cmd, parm);
    }
}
