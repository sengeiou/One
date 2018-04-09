package com.ubt.baselib.btCmd1E.cmd;

import android.text.TextUtils;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

import java.io.UnsupportedEncodingException;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 11:33
 * @描述: 播放音效
 */

public class BTCmdPlaySound extends BaseBTReq {
    byte    cmd = BTCmd.DV_SET_PLAY_SOUND;
    byte[] parm = {0x30};

    public BTCmdPlaySound(String soundName){
        try {
            if(!TextUtils.isEmpty(soundName)) {
                initReq(cmd, soundName.getBytes("UTF-8"));
            }else{
                initReq(cmd, parm);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
