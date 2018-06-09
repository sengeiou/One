package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author：liuhai
 * @date：2018/6/9 9:52
 * @modifier：ubt
 * @modify_date：2018/6/9 9:52
 * [A brief description]
 * version
 */

public class BTCmdUpdateFirmware extends BaseBTReq {
    byte cmd = BTCmd.DV_COMMON_CMD;

    byte[] parm = {0x00};

    public BTCmdUpdateFirmware(String language) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("seq", 12);
            jsonObject.put("event", "10");
            jsonObject.put("name", language);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        parm = jsonObject.toString().getBytes();
        initReq(cmd, parm);
    }
}
