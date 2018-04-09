package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 11:10
 * @描述:
 * * 升级本体软件.
 * 手动升级： 0x43 {小端模式}
 * 参数1: 0未知，　１请求升级，２确定升级（进入升级），　３暂时不升级，４永不升级
 * 回复：
 * 应答：0
 */

public class BTCmdStartUpgradeSoft extends BaseBTReq {

    public static final byte REQUEST_UPDATE = 0x01;
    public static final byte START_UPDATE = 0x02;
    public static final byte STOP_UPDATE = 0x03;
    public static final byte NEVER_UPDATE = 0x04;
    byte    cmd = BTCmd.DV_DO_UPGRADE_SOFT;
    byte[] parm = {0x00};

    public BTCmdStartUpgradeSoft(byte bparm) {
        parm[0] = bparm;
        initReq(cmd, parm);
    }
}
