package com.ubt.baselib.btCmd1E.cmd;

import android.text.TextUtils;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

import java.io.UnsupportedEncodingException;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 14:01
 * @描述: 播放表情
 */

public class BTCmdPlayEmoj extends BaseBTReq {
    byte    cmd = BTCmd.DV_SET_PLAY_EMOJI;
    byte[] parm = {0x30};

    public BTCmdPlayEmoj(String emojiName){
        try {
            if(!TextUtils.isEmpty(emojiName)) {
                initReq(cmd, emojiName.getBytes("UTF-8"));
            }else{
                initReq(cmd, parm);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
