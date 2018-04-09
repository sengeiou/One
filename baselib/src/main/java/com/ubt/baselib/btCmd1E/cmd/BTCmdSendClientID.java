package com.ubt.baselib.btCmd1E.cmd;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

import java.io.UnsupportedEncodingException;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 14:22
 * @描述:
 */

public class BTCmdSendClientID extends BaseBTReq {
    byte    cmd = BTCmd.DV_CLIENT_ID;
    byte[] parm = {0x30};

    public BTCmdSendClientID(@NonNull String clientID){
        try {
            if(!TextUtils.isEmpty(clientID)) {
                initReq(cmd, clientID.getBytes("UTF-8"));
            }else{
                initReq(cmd, parm);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
