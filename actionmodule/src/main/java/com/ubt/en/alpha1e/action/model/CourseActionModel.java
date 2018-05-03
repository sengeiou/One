package com.ubt.en.alpha1e.action.model;

/**
 * @author：liuhai
 * @date：2018/5/2 19:36
 * @modifier：ubt
 * @modify_date：2018/5/2 19:36
 * [A brief description]
 * version
 */

public class CourseActionModel {
    private String courseName;
    private int statu;

    public CourseActionModel(String courseName, int statu) {
        this.courseName = courseName;
        this.statu = statu;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    @Override
    public String toString() {
        return "CourseActionModel{" +
                "courseName='" + courseName + '\'' +
                ", statu=" + statu +
                '}';
    }
}
