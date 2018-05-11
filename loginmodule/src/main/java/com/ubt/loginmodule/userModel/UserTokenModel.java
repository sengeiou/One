package com.ubt.loginmodule.userModel;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 * token": {
"expireAt": 0,
"token": "string"
}
 */


public class UserTokenModel {

    private String expireAt;
    private String token;

    public String getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(String expireAt) {
        this.expireAt = expireAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserTokenModel{" +
                "expireAt='" + expireAt + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
