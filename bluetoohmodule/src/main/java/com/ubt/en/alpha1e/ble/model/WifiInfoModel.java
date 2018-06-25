package com.ubt.en.alpha1e.ble.model;

/**
 * @author：liuhai
 * @date：2018/6/25 19:34
 * @modifier：ubt
 * @modify_date：2018/6/25 19:34
 * [A brief description]
 * version
 */

public class WifiInfoModel {
    private String ESSID;
    private String EncryptionKey;
    private String IE;
    private String Quality;
    private String SignalLevel;

    public String getESSID() {
        return ESSID;
    }

    public void setESSID(String ESSID) {
        this.ESSID = ESSID;
    }

    public String getEncryptionKey() {
        return EncryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        EncryptionKey = encryptionKey;
    }

    public String getIE() {
        return IE;
    }

    public void setIE(String IE) {
        this.IE = IE;
    }

    public String getQuality() {
        return Quality;
    }

    public void setQuality(String quality) {
        Quality = quality;
    }

    public String getSignalLevel() {
        return SignalLevel;
    }

    public void setSignalLevel(String signalLevel) {
        SignalLevel = signalLevel;
    }

    @Override
    public String toString() {
        return "WifiInfoModel{" +
                "ESSID='" + ESSID + '\'' +
                ", EncryptionKey='" + EncryptionKey + '\'' +
                ", IE='" + IE + '\'' +
                ", Quality='" + Quality + '\'' +
                ", SignalLevel='" + SignalLevel + '\'' +
                '}';
    }
}
