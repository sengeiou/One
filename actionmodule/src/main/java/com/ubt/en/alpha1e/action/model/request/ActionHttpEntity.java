package com.ubt.en.alpha1e.action.model.request;

import com.ubt.baselib.globalConst.BaseHttpEntity;

/**
 * @author：liuhai
 * @date：2018/5/3 14:33
 * @modifier：ubt
 * @modify_date：2018/5/3 14:33
 * [A brief description]
 * version
 */

public class ActionHttpEntity extends BaseHttpEntity {

    /**
     * 获取最新进度
     */
    public static String BASE_GET_LAST_PROGRESS = "alpha1e/course/getCourseProgress";


    /**
     * 获取所有上传分数
     */
    public static String BASE_GET_ALL_SCORE = "alpha1e/course/getCourseStatus";


    /**
     * 保存课程最新进度
     */
    public static final String SAVE_COURSE_PROGRESS = "alpha1e/course/addCourseProgress";

    /**
     * 保存关卡分数
     */
    public static final String COURSE_SAVE_STATU = "alpha1e/course/addCourseStatus";
}
