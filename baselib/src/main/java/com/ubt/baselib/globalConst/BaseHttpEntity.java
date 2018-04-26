package com.ubt.baselib.globalConst;

import com.vise.log.ViseLog;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update http请求URL常量定义
 */


public class BaseHttpEntity {
    /**
     * 基础URL定义
     */
    public static String BASE_XG_URL = "https://test79.ubtrobot.com/xinge-push-rest/";
    public static String BASIC_THIRD_LOGIN_URL = "http://10.10.20.71:8010/user-service-rest/v2/"; //测试环境后续上线需要修改正式环境
    public static String BASIC_UBX_SYS = "http://10.10.1.14:8088/alphaebot/"; //测试环境
    public static String BASIC_UBX_SYS_BIND = "http://10.10.1.12:8085/equipment/"; //绑定相关 测试环境
    public static String BLOCKLY_CODEMAO_URL = "https://dev-ubt.codemao.cn/";


    public static void init(boolean isIssue){
        ViseLog.i("isIssue="+isIssue);
        if(isIssue){
            BASE_XG_URL = "https://account.ubtrobot.com/xinge-push-rest/";
            BASIC_THIRD_LOGIN_URL="https://account.ubtrobot.com/user-service-rest/v2/";
            BASIC_UBX_SYS="https://prodapi.ubtrobot.com/alpha1e/";
            BASIC_UBX_SYS_BIND="https://prodapi.ubtrobot.com/equipment/";
            BLOCKLY_CODEMAO_URL = "https://ubt.codemao.cn/";
        }
    }
}
