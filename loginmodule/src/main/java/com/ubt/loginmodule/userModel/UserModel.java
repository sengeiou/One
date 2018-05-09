package com.ubt.loginmodule.userModel;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */

/*user": {
        "countryCode": "string",
        "countryName": "string",
        "emailVerify": 0,
        "nickName": "string",
        "pwdCreateType": 0,
        "userBirthday": "string",
        "userEmail": "string",
        "userExtraEmail": "string",
        "userExtraPhone": "string",
        "userGender": 0,
        "userId": 0,
        "userImage": "string",
        "userName": "string",
        "userPhone": "string"
        }*/


public class UserModel {

    private String countryCode;
    private String countryName;
    private int    emailVerify;
    private String nickName;
    private int pwdCreateType;
    private String userBirthday;
    private String userEmail;
    private String userExtraEmail;
    private String userExtraPhone;
    private int userGender;
    private long  userId;
    private String userImage;
    private String userName;
    private String userPhone;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getEmailVerify() {
        return emailVerify;
    }

    public void setEmailVerify(int emailVerify) {
        this.emailVerify = emailVerify;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPwdCreateType() {
        return pwdCreateType;
    }

    public void setPwdCreateType(int pwdCreateType) {
        this.pwdCreateType = pwdCreateType;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserExtraEmail() {
        return userExtraEmail;
    }

    public void setUserExtraEmail(String userExtraEmail) {
        this.userExtraEmail = userExtraEmail;
    }

    public String getUserExtraPhone() {
        return userExtraPhone;
    }

    public void setUserExtraPhone(String userExtraPhone) {
        this.userExtraPhone = userExtraPhone;
    }

    public int getUserGender() {
        return userGender;
    }

    public void setUserGender(int userGender) {
        this.userGender = userGender;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", emailVerify=" + emailVerify +
                ", nickName='" + nickName + '\'' +
                ", pwdCreateType=" + pwdCreateType +
                ", userBirthday='" + userBirthday + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userExtraEmail='" + userExtraEmail + '\'' +
                ", userExtraPhone='" + userExtraPhone + '\'' +
                ", userGender=" + userGender +
                ", userId=" + userId +
                ", userImage='" + userImage + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                '}';
    }
}
