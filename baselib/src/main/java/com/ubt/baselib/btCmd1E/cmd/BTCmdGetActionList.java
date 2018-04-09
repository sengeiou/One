package com.ubt.baselib.btCmd1E.cmd;

import android.text.TextUtils;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

import java.io.UnsupportedEncodingException;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 14:57
 * @描述: 获取动作表名
 */

public class BTCmdGetActionList extends BaseBTReq {
    byte    cmd = BTCmd.DV_GETACTIONFILE;
    byte[] parm = {0x00};

    public BTCmdGetActionList(String dir) {
        try {
            if(!TextUtils.isEmpty(dir)){
                parm = new byte[dir.length()+1];
                parm[0] = (byte) dir.length();
                System.arraycopy(dir.getBytes("UTF-8"), 0, parm, 1, dir.length());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        initReq(cmd, parm);
    }
}
