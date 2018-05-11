package com.ubt.loginmodule.requestModel;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 * {
"account": "string",
"accountType": 0,
"appId": 0,
"password": "string"
}
 */


public class LoginRequest  {

    private String account;
    private int accountType;
    private String appId;
    private String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "account='" + account + '\'' +
                ", accountType=" + accountType +
                ", appId='" + appId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
