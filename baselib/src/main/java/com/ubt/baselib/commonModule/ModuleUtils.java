package com.ubt.baselib.commonModule;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/4 13:43
 * @描述:
 */

public interface ModuleUtils {
    /**
     * 命名规则: 模块组名/模块名
     * 常量规则按：  模块组名_模块名
     * 可扩展多级: 模块组名/模块名/模块名/模块名
     * 注意：不同的库需要不同的模块组名，模块组名不能一样，模块名可以一样。
     * 封装的各个模块之间需要调用才需要在这里定义，
     * 单个模块内的调用在单个模块内部定义。
     */

    /**
     * 登录模块
     */
    String Login_Module = "/login/LoginActivity";
    String Login_UserEdit = "/login/UserEditActivity";

    /**
     * 主模块
     */
    String Main_MainActivity = "/main/MainActivity";

    /**
     * 设置模块
     */
    String Setting_UserCenterActivity="/setting/UserCenterActivity";



}
