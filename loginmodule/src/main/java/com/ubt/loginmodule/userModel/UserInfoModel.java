package com.ubt.loginmodule.userModel;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class UserInfoModel {

    private String userId;
    private String nickName;
    private String email;
    private String sex;
    private String birthDate;
    private String headPic;

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
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    @Override
    public String toString() {
        return "UserInfoModel{" +
                "userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", sex='" + sex + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", headPic='" + headPic + '\'' +
                '}';
    }
}
