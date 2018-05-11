package com.ubt.loginmodule.requestModel;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class GetUserInfoRequest {

    private String token;
    private int userId;

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

    @Override
    public String toString() {
        return "GetUserInfoRequest{" +
                "token='" + token + '\'' +
                ", userId=" + userId +
                '}';
    }
}
