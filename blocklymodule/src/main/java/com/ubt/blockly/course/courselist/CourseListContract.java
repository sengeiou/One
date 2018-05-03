package com.ubt.blockly.course.courselist;

import android.content.Context;

import com.ubt.baselib.model1E.CourseData;
import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;

import java.util.List;


/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class CourseListContract {
    interface View extends BaseView {
        void setBlocklyCourseData(List<CourseData> list);
        void updateSuccess();
        void updateFail();
    }

    interface  Presenter extends BasePresenter<View> {
        void getBlocklyCourseList(Context context);
        void updateCurrentCourse(int cid);
    }
}
