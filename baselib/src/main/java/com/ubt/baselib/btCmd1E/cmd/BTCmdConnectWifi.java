package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 11:00
 * @描述:
 */

public class BTCmdConnectWifi extends BaseBTReq {
    byte    cmd = BTCmd.DV_DO_NETWORK_CONNECT;
    byte[] parm = {0x00};

    public BTCmdConnectWifi(String wifiName, String wifiPwd){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ESSID", wifiName);
            jsonObject.put("PassKey",wifiPwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        parm = jsonObject.toString().getBytes();
        initReq(cmd, parm);
    }
}
