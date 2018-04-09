package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 14:52
 * @描述:  获取行为事项播放状态
 * 回复：{
 * "eventId": "123",
 * "playAudioSeq": "0",
 * "audioState": "" //no play,playing
 * }
 */

public class BTCmdReadHibitsPlayStatus extends BaseBTReq {
    byte    cmd = BTCmd.DV_READ_HIBITS_PLAY_STATUS;
    byte[] parm = {0x00};

    public BTCmdReadHibitsPlayStatus() {
        initReq(cmd, parm);
    }
}
