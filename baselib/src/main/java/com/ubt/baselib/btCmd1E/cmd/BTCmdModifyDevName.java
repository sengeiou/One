package com.ubt.baselib.btCmd1E.cmd;

import android.text.TextUtils;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

import java.io.UnsupportedEncodingException;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 16:39
 * @描述: 修改设备名 参数：新的设备名
 */

public class BTCmdModifyDevName extends BaseBTReq {
    byte    cmd = BTCmd.DV_MODIFYNAME;
    byte[] parm = {0x30};

    public BTCmdModifyDevName(String devName){
        try {
            if(!TextUtils.isEmpty(devName)) {
                initReq(cmd, devName.getBytes("GBK"));
            }else{
                initReq(cmd, parm);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
