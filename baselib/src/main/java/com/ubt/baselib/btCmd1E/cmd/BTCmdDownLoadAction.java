package com.ubt.baselib.btCmd1E.cmd;

import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BaseBTReq;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/26 11:14
 * @描述:
 * * 发送：{"actionId":"1","actionName":"name","actionPath":"http://"}
 * actionId : 动作ID
 * actionName : 动作名称
 * actionPath : 动作下载URL
 * <p>
 * 回复：{"status":"1","progress":"20","actionId":"1"}
 * status : 1 下载中 2 下载成功 3 未联网 4 解压失败 0 下载失败
 * progress : 下载进度
 * actionId : 动作ID
 */

public class BTCmdDownLoadAction extends BaseBTReq {
    byte    cmd = BTCmd.DV_DO_DOWNLOAD_ACTION;
    byte[] parm = {0x00};

    public BTCmdDownLoadAction(String actionID, String actionName, String actionPath){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("actionId", actionID);
            jsonObject.put("actionName",actionName);
            jsonObject.put("actionPath",actionPath);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        parm = jsonObject.toString().getBytes();
        initReq(cmd, parm);
    }
}
