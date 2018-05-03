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
    private static String BASE_ACTION_URL = "http://10.10.1.14:8080/alpha1e/";

    /**
     * 获取最新进度
     */
    public static String BASE_GET_LAST_PROGRESS = BASE_ACTION_URL + "course/getCourseProgress";


    /**
     * 获取所有上传分数
     */
    public static String BASE_GET_ALL_SCORE = BASE_ACTION_URL + "course/getCourseStatus";


    /**
     * 保存课程最新进度
     */
    public static final String SAVE_COURSE_PROGRESS = BASE_ACTION_URL + "course/addCourseProgress";

    /**
     * 保存关卡分数
     */
    public static final String COURSE_SAVE_STATU = BASE_ACTION_URL + "course/addCourseStatus";
}
