package com.ubt.baselib;

import com.ubt.baselib.globalConst.BaseHttpEntity;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/5 14:07
 * @描述: 初始化基础库
 */

public class ConfigureBaseLib {

    private static ConfigureBaseLib instance;
    private boolean isIssue = false;

    private ConfigureBaseLib() {
    }

    public static ConfigureBaseLib getInstance() {
        if(instance == null){
            synchronized (ConfigureBaseLib.class){
                if(instance == null){
                    instance = new ConfigureBaseLib();
                }
            }
        }
        return instance;
    }


    public void init(boolean isIssue){
        this.isIssue = isIssue;
        BaseHttpEntity.init(this.isIssue);
    }

}
