package com.ubt.en.alpha1e.ble.requestModel;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class GetRobotLanguageRequest {

    private String token;
    private int userId;
    private String equipmentSeq;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEquipmentSeq() {
        return equipmentSeq;
    }

    public void setEquipmentSeq(String equipmentSeq) {
        this.equipmentSeq = equipmentSeq;
    }

    @Override
    public String toString() {
        return "GetRobotLanguageRequest{" +
                "token='" + token + '\'' +
                ", userId=" + userId +
                ", equipmentSeq=" + equipmentSeq +
                '}';
    }
}
