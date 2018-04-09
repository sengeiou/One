package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 14:54
 * @描述:  获取行为事项播放状态
 * 回复：{"eventId": "123",
 * "playAudioSeq": "0"
 * "cmd": "start"    //stop,start,pause,unpause
 * }
 */

public class BTCmdControlHibitsPlay extends BaseBTReq {
    byte    cmd = BTCmd.DV_CONTROL_HIBITS_PLAY;
    byte[] parm = {0x00};

    public BTCmdControlHibitsPlay(String eventId, String playAudioSeq, String audioState){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("eventId", eventId);
            jsonObject.put("playAudioSeq",playAudioSeq);
            jsonObject.put("cmd",audioState);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        parm = jsonObject.toString().getBytes();
        initReq(cmd, parm);
    }
}
