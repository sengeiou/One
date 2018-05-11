package com.ubt.loginmodule.requestModel;

/**
 *{
 "appId": "string",
 "email": "string",
 "emailType": 0,
 "nickName": "string"
 }
 */

public class GetCodeRequest  {

    private String email;
    private int emailType;
    private String nickName;
    private String appId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEmailType() {
        return emailType;
    }

    public void setEmailType(int emailType) {
        this.emailType = emailType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "GetCodeRequest{" +
                "email='" + email + '\'' +
                ", emailType=" + emailType +
                ", nickName='" + nickName + '\'' +
                ", appId='" + appId + '\'' +
                '}';
    }
}
