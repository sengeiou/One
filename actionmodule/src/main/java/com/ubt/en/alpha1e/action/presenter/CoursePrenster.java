package com.ubt.en.alpha1e.action.presenter;

import android.content.ContentValues;
import android.content.Context;

import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.model1E.LocalActionRecord;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.en.alpha1e.action.contact.CourseContract;
import com.ubt.en.alpha1e.action.model.request.ActionHttpEntity;
import com.ubt.en.alpha1e.action.model.request.SaveCourseProQuest;
import com.vise.log.ViseLog;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import org.litepal.crud.DataSupport;

/**
 * @author：liuhai
 * @date：2018/5/2 17:07
 * @modifier：ubt
 * @modify_date：2018/5/2 17:07
 * [A brief description]
 * version
 */

public class CoursePrenster extends BasePresenterImpl<CourseContract.View> implements CourseContract.Presenter {


    @Override
    public void getCourseOneData(Context context) {

    }

    @Override
    public void getCourseTwoData(Context context) {

    }

    /**
     * 保存课程记录到本地和后台
     *
     * @param currentCourse 当前学习的课程关卡
     * @param courseLevel   当前学习到第几课
     */
    @Override
    public void savaCourseDataToDB(int currentCourse, int courseLevel) {
        ViseLog.d("保存进度到数据库1" + "currentCourse==" + currentCourse + "courseLevel" + courseLevel);
        LocalActionRecord record = DataSupport.findFirst(LocalActionRecord.class);
        if (null != record) {
            ViseLog.d("保存进度到数据库2" + record.toString());
            int course = record.getCourseLevel();
            int level = record.getPeriodLevel();
            if ((currentCourse > course) || (course == currentCourse && level < courseLevel)) {
                ViseLog.d("savaCourseDataToDB", "保存进度到数据库3" + "保存成功");
                ContentValues values = new ContentValues();
                values.put("CourseLevel", currentCourse);
                values.put("periodLevel", courseLevel);
                values.put("isUpload", false);
                DataSupport.updateAll(LocalActionRecord.class, values);
                saveLastProgress(String.valueOf(currentCourse), String.valueOf(courseLevel));
            }
        }
    }

    /**
     * 保存课程最新进度
     */
    public void saveLastProgress(String progressOne, String courseTwo) {
        SaveCourseProQuest proQequest = new SaveCourseProQuest();
        proQequest.setCourseOne("1");
        proQequest.setProgressOne(progressOne);
        proQequest.setCourseTwo(progressOne);
        proQequest.setProgressTwo(courseTwo);
        proQequest.setType(2);
        proQequest.setUserId(String.valueOf(SPUtils.getInstance().getInt(Constant1E.SP_USER_ID)));
        proQequest.setToken(SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN));
        ViseHttp.POST(ActionHttpEntity.SAVE_COURSE_PROGRESS)
                .setJson(GsonImpl.get().toJson(proQequest))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ViseLog.d("response===" + response);
                        LocalActionRecord record = DataSupport.findFirst(LocalActionRecord.class);
                        if (null != record) {
                            ContentValues values = new ContentValues();
                            values.put("isUpload", true);
                            DataSupport.updateAll(LocalActionRecord.class, values);
                        }
                    }

                    @Override
                    public void onFail(int i, String s) {
                        ViseLog.d("e===" + s);
                    }
                });

    }

}
