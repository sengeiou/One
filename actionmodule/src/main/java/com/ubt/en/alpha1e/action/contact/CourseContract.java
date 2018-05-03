package com.ubt.en.alpha1e.action.contact;

import android.content.Context;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;

/**
 * @author：liuhai
 * @date：2018/5/2 17:05
 * @modifier：ubt
 * @modify_date：2018/5/2 17:05
 * [A brief description]
 * version
 */

public class CourseContract {
    public interface View extends BaseView {
        //void getCourseOneData(List<ActionCourseOneContent> list);
    }

    public interface  Presenter extends BasePresenter<View> {
        void getCourseOneData(Context context);
        void getCourseTwoData(Context context);
        void savaCourseDataToDB(int currentCourse, int courseLevel);
    }
}
