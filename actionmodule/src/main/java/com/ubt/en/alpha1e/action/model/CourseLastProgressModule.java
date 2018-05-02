package com.ubt.en.alpha1e.action.model;

/**
 * @author：liuhai
 * @date：2018/5/2 14:05
 * @modifier：ubt
 * @modify_date：2018/5/2 14:05
 * [A brief description]
 * version
 */

public class CourseLastProgressModule {
    private String userId;
    private String courseOne;
    private String progressOne;
    private String courseTwo;
    private String progressTwo;
    private String type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseOne() {
        return courseOne;
    }

    public void setCourseOne(String courseOne) {
        this.courseOne = courseOne;
    }

    public String getProgressOne() {
        return progressOne;
    }

    public void setProgressOne(String progressOne) {
        this.progressOne = progressOne;
    }

    public String getCourseTwo() {
        return courseTwo;
    }

    public void setCourseTwo(String courseTwo) {
        this.courseTwo = courseTwo;
    }

    public String getProgressTwo() {
        return progressTwo;
    }

    public void setProgressTwo(String progressTwo) {
        this.progressTwo = progressTwo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
