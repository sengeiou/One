package com.ubt.en.alpha1e.ble.model;

/**
 * @author：liuhai
 * @date：2018/4/20 17:28
 * @modifier：ubt
 * @modify_date：2018/4/20 17:28
 * [A brief description]
 * version
 */

public class RobotStatu {
    
    private String wifiName;
    private String robotNumber;
    private String robotIp;

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getRobotNumber() {
        return robotNumber;
    }

    public void setRobotNumber(String robotNumber) {
        this.robotNumber = robotNumber;
    }

    public String getRobotIp() {
        return robotIp;
    }

    public void setRobotIp(String robotIp) {
        this.robotIp = robotIp;
    }
}
