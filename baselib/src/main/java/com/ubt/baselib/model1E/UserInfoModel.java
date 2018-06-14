package com.ubt.baselib.model1E;

import java.io.Serializable;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class UserInfoModel implements Serializable{

    private String userId;
    private String nickName;
    private String email;
    private String sex;
    private String birthDay;
    private String headPic;
    private String country;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDate() {
        return birthDay;
    }

    public void setBirthDate(String birthDate) {
        this.birthDay = birthDate;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "UserInfoModel{" +
                "userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", sex='" + sex + '\'' +
                ", birthDate='" + birthDay + '\'' +
                ", headPic='" + headPic + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
