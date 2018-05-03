package com.ubt.en.alpha1e.action.model.request;

/**
 * @author：liuhai
 * @date：2018/5/2 14:15
 * @modifier：ubt
 * @modify_date：2018/5/2 14:15
 * [A brief description]
 * version
 */

public class SaveCourseStatuRequest {
    private int type;
    private String status;
    private String course;

    private String userId;
    private String token;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
