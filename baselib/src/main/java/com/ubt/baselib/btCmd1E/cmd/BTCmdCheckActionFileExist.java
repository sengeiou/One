package com.ubt.baselib.btCmd1E.cmd;

import android.text.TextUtils;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

import java.io.UnsupportedEncodingException;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 11:21
 * @描述:
 * 判断动作文件是否存在
 * 发送参数：文件名
 * 回复 0 不存在 1 存在
 */

public class BTCmdCheckActionFileExist extends BaseBTReq {
    byte    cmd = BTCmd.DV_DO_CHECK_ACTION_FILE_EXIST;
    byte[] parm = {0x30};

    public BTCmdCheckActionFileExist(String actionName){
        try {
            if(!TextUtils.isEmpty(actionName)) {
                initReq(cmd, actionName.getBytes("GBK"));
            }else{
                initReq(cmd, parm);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
