package com.ubt.loginmodule.requestModel;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 *
 * {
"accessToken": "string",
"appId": "string",
"img": "string",
"loginType": "string",
"miniTvsId": "string",
"nickName": "string",
"openId": "string",
"ubtAppId": 0
}
 *
 */


public class LoginThirdRequest {

    private String accessToken;
    private String appId;
    private String img;
    private String loginType;
    private String miniTvsId;
    private String nickName;
    private String openId;
    private int  ubtAppId;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getMiniTvsId() {
        return miniTvsId;
    }

    public void setMiniTvsId(String miniTvsId) {
        this.miniTvsId = miniTvsId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getUbtAppId() {
        return ubtAppId;
    }

    public void setUbtAppId(int ubtAppId) {
        this.ubtAppId = ubtAppId;
    }

    @Override
    public String toString() {
        return "LoginThirdRequest{" +
                "accessToken='" + accessToken + '\'' +
                ", appId='" + appId + '\'' +
                ", img='" + img + '\'' +
                ", loginType='" + loginType + '\'' +
                ", miniTvsId='" + miniTvsId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", openId='" + openId + '\'' +
                ", ubtAppId=" + ubtAppId +
                '}';
    }
}
