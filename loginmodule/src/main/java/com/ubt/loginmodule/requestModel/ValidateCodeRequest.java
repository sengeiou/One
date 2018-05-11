package com.ubt.loginmodule.requestModel;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class ValidateCodeRequest  {

    private int accountType;
    private String account;
    private String captcha;

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    @Override
    public String toString() {
        return "ValidateCodeRequest{" +
                "accountType=" + accountType +
                ", account='" + account + '\'' +
                ", captcha='" + captcha + '\'' +
                '}';
    }
}
