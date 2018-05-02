package com.ubt.baselib.model1E;

import org.litepal.crud.DataSupport;

/**
 * @author：liuhai
 * @date：2018/5/2 11:19
 * @modifier：ubt
 * @modify_date：2018/5/2 11:19
 * [A brief description]
 * version
 */

public class LocalActionRecord extends DataSupport {
    private String userId;
    /**
     * 第几关
     */
    private int CourseLevel;
    /**
     * 第几课时
     */
    private int periodLevel;

    /**
     * 是否上传数据
     */
    private boolean isUpload;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCourseLevel() {
        return CourseLevel;
    }

    public void setCourseLevel(int courseLevel) {
        CourseLevel = courseLevel;
    }

    public int getPeriodLevel() {
        return periodLevel;
    }

    public void setPeriodLevel(int periodLevel) {
        this.periodLevel = periodLevel;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    @Override
    public String toString() {
        return "LocalActionRecord{" +
                "userId='" + userId + '\'' +
                ", CourseLevel=" + CourseLevel +
                ", periodLevel=" + periodLevel +
                ", isUpload=" + isUpload +
                '}';
    }
}
