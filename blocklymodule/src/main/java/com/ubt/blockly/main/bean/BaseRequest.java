package com.ubt.blockly.main.bean;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class BaseRequest {
    private String userId;
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
