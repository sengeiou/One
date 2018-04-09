package com.ubt.factorytest.configuration;

import com.ubt.factorytest.utils.GsonUtil;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/3 10:18
 * @描述:
 */

public class FactoryParamConf {

    private String softVer;
    private String firmwareVer;
    private int maxPower;
    private int minPower;

    public String getSoftVer() {
        return softVer;
    }

    public void setSoftVer(String softVer) {
        this.softVer = softVer;
    }

    public String getFirmwareVer() {
        return firmwareVer;
    }

    public void setFirmwareVer(String firmwareVer) {
        this.firmwareVer = firmwareVer;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(int maxPower) {
        this.maxPower = maxPower;
    }

    public int getMinPower() {
        return minPower;
    }

    public void setMinPower(int minPower) {
        this.minPower = minPower;
    }

    public String toJson(){
        return GsonUtil.gson().toJson(this);
    }

    public FactoryParamConf fromJson(String json){
        return GsonUtil.gson().fromJson(json, this.getClass());
    }
}
