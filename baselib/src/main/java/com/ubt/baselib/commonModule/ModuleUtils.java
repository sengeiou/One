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

    String Main_MainActivity = "/main/MainActivity";  //主模块
    String Setting_UserCenterActivity="/setting/UserCenterActivity";//设置模块

    String Bluetooh_BleGuideActivity="/bluetooh/BleGuideActivity"; //蓝牙模块引导
    String Bluetooh_BleStatuActivity="/bluetooh/BleStatuActivity"; //蓝牙模块状态

    String Actions_ActionProgram = "/actions/ActionProgram";  //动作编程模块
    String Blockly_BlocklyProgram = "/blockly/BlocklyProgram";  //编程猫模块
    String Community_ActionProgram = "/community/CommunityCenter";  //社区模块
    String Joystick_ActionProgram = "/joystick/JoystickPlay";  //遥控机模块

    String Playcenter_module = "/playcenter/PlayCenter";  //播放中心模块





}
