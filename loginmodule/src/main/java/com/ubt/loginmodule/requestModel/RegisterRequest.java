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
"captcha": "string",
"password": "string"
}
 */


public class RegisterRequest  {

    private String account;
    private int accountType;
    private String captcha;
    private String password;
    private String appId;

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

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "account='" + account + '\'' +
                ", accountType=" + accountType +
                ", captcha='" + captcha + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
