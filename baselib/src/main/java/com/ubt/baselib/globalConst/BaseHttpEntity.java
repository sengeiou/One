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
    public static String BLOCKLY_CODEMAO_URL = "https://dev-ubt.codemao.cn/";
    public static String BASIC_UBX_SYS = "http://10.10.1.14:8080/";
    public static String BASE_UBX_COMMON = "https://test79.ubtrobot.com/";

    public static void init(boolean isIssue){
        ViseLog.i("isIssue="+isIssue);
        if(isIssue){
            BLOCKLY_CODEMAO_URL = "https://ubt.codemao.cn/";
            BASIC_UBX_SYS = "https://interface.ubtrobot.com/";
            BASE_UBX_COMMON = "https://interface.ubtrobot.com/";
        }
    }


}
