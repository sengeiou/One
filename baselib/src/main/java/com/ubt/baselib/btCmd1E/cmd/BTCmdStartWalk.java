package com.ubt.baselib.btCmd1E.cmd;

import android.support.annotation.NonNull;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 14:06
 * @描述: 播放步态行走
 *  参数1: 1B   方向  0.向前走 1.后退 2.左移 3.右移
    参数2: 1B   速度 1.慢 2.中 3.快
    参数3: 4B   步数 目前支持(0~100) 0理论上表示一直走下去

    回复
    参数1: 1B  0.执行失败 1.即将行走 2.开始行走 3.行走完成(其中2和3为主动上报)
 */

public class BTCmdStartWalk extends BaseBTReq {
    byte    cmd = BTCmd.DV_WALK;

    public BTCmdStartWalk(@NonNull byte[] walk) {
        initReq(cmd, walk);
    }
}
