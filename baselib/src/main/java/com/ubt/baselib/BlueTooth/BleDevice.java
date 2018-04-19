package com.ubt.baselib.BlueTooth;

import org.litepal.crud.DataSupport;

/**
 * @author：liuhai
 * @date：2018/4/11 17:15
 * @modifier：ubt
 * @modify_date：2018/4/11 17:15
 * [A brief description]
 * version
 */

public class BleDevice extends DataSupport {
    private String bleName;
    private String mac;
    private int statu;

//    public BleDevice(String bleName, String mac, int statu) {
//        this.bleName = bleName;
//        this.mac = mac;
//        this.statu = statu;
//    }

    public String getBleName() {
        return bleName;
    }

    public void setBleName(String bleName) {
        this.bleName = bleName;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    @Override
    public String toString() {
        return "BleDevice{" +
                "bleName='" + bleName + '\'' +
                ", mac='" + mac + '\'' +
                ", statu=" + statu +
                '}';
    }
}
