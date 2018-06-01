package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 11:01
 * @描述: 获取WIFI状态
 */

public class BTCmdGetLanguageStatus extends BaseBTReq {
    byte    cmd = BTCmd.DV_COMMON_CMD;

    byte[] parm = {0x00};

    public BTCmdGetLanguageStatus(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("seq", getSeq() + "");
            jsonObject.put("event","1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        parm = jsonObject.toString().getBytes();
        initReq(cmd, parm);
    }
}
