package com.ubt.baselib.btCmd1E.cmd;

import android.support.annotation.NonNull;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 13:55
 * @描述:
 *  1BYTE	亮灯模式：01-常亮，02-快闪，03-慢闪，04-熄灯
    3BYTE	颜色：每一种颜色对应字节顺序为R G B
    1BYTE	亮灯时长，单位为秒
 */

public class BTCmdSetLedEffect extends BaseBTReq {
    byte    cmd = BTCmd.DV_SET_LED_LIGHT;

    public BTCmdSetLedEffect(@NonNull byte[] bParam) {
        initReq(cmd, bParam);
    }
}
