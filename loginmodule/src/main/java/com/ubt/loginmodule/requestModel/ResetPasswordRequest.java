package com.ubt.loginmodule.requestModel;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 * {
"password": "string",
"token": "string",
"userId": 0
}
 */


public class ResetPasswordRequest {

    private String password;
    private String token;
    private int userId;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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
        return "ResetPasswordRequest{" +
                "password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", userId=" + userId +
                '}';
    }
}
