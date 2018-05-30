package com.ubt.baselib.model1E;

/**
 * @author：liuhai
 * @date：2018/4/20 20:43
 * @modifier：ubt
 * @modify_date：2018/4/20 20:43
 * [A brief description]
 * version
 */

public class BleNetWork {
    private boolean statu;
    private String wifiName;
    private String ip;

    public BleNetWork(boolean statu, String wifiName, String ip) {
        this.statu = statu;
        this.wifiName = wifiName;
        this.ip = ip;
    }

    public boolean isStatu() {
        return statu;
    }

    public void setStatu(boolean statu) {
        this.statu = statu;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
