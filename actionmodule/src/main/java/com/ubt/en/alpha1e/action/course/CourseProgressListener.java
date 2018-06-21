package com.ubt.en.alpha1e.action.course;

/**
 * @author：liuhai
 * @date：2018/5/2 19:28
 * @modifier：ubt
 * @modify_date：2018/5/2 19:28
 * [A brief description]
 * version
 */

public interface CourseProgressListener {
    void completeCurrentCourse(int current);

    void finishActivity();

    void completeSuccess(boolean isSuccess);

    //显示进度对话框
    void showProgressDialog();

    void showGuide();
}
