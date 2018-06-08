package com.ubt.baselib.utils;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/5/24 10:59
 * @描述:
 */

public class AppStatusUtils {
    /**
     * 是否在特殊处理模式，主要是指动作编程或课程原理模块进行中 用于低电处理
     */
    private static boolean bussiness = false;
    private static int currentPower = 0; //当前电量
    private static int chargingStatus = 0; //是否充电    00/否 01/是 02/没有电池  03/已充满电
    private static boolean isForceDisBT = false; //是否是主动断开蓝牙

    private static boolean btBussiness = false; //蓝牙特殊状态处理


    /**
     * 是否在低电特殊状态中包括：
     * 1，动作编辑模块
     * @return
     */
    public static boolean isBussiness() {
        return bussiness;
    }

    public static void setBussiness(boolean bussiness) {
        AppStatusUtils.bussiness = bussiness;
    }


    public static void setCurrentPower(int currentPower) {
        AppStatusUtils.currentPower = currentPower;
    }

    public static int getCurrentPower() {
        return currentPower;
    }

    public static void setChargingStatus(int chargingStatus) {
        AppStatusUtils.chargingStatus = chargingStatus;
    }

    /**
     * 是否在低电模式
     * @return
     */
    public static boolean isLowPower(){
        return (chargingStatus == 0 &&currentPower <= 5);
    }


    /**
     * 是否是主动断开的蓝牙
     * @return
     */
    public static boolean isForceDisBT() {
        return isForceDisBT;
    }

    public static void setIsForceDisBT(boolean isForceDisBT) {
        AppStatusUtils.isForceDisBT = isForceDisBT;
    }


    /**
     * 是否在蓝牙特殊状态中 特殊状态包括 ：
     * 1，机器人联网中
     * 2，编程界面
     * @return
     */
    public static boolean isBtBussiness() {
        return btBussiness;
    }

    public static void setBtBussiness(boolean btBussiness) {
        AppStatusUtils.btBussiness = btBussiness;
    }
}
