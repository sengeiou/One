package com.ubt.en.alpha1e.action.model;

/**
 * @author：liuhai
 * @date：2018/5/2 14:20
 * @modifier：ubt
 * @modify_date：2018/5/2 14:20
 * [A brief description]
 * version
 */

public class CourseDetailScoreModule {
    private String course;//第几个关卡
    private String status;//关卡分数

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CourseDetailScoreModule{" +
                "course='" + course + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
