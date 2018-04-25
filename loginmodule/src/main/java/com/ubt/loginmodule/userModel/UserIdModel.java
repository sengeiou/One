package com.ubt.loginmodule.userModel;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class UserIdModel {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserIdModel{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
