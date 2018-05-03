package com.ubt.en.alpha1e.action.presenter;

import android.content.ContentValues;
import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.ubt.baselib.model1E.BaseResponseModel;
import com.ubt.baselib.model1E.LocalActionRecord;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.contact.ActionCourseContact;
import com.ubt.en.alpha1e.action.model.ActionCourseModel;
import com.ubt.en.alpha1e.action.model.CourseDetailScoreModule;
import com.ubt.en.alpha1e.action.model.CourseLastProgressModule;
import com.ubt.en.alpha1e.action.model.request.SaveCourseProQuest;
import com.ubt.en.alpha1e.action.model.request.SaveCourseStatuRequest;
import com.vise.log.ViseLog;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.request.PostRequest;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class ActionCoursePrenster extends BasePresenterImpl<ActionCourseContact.View> implements ActionCourseContact.Presenter {

    private List<ActionCourseModel> mActionCourseModels = new ArrayList<>();

    private Context mContext;

    public void init(Context context) {
        this.mContext = context;
        initActionCourseData();
    }

    public List<ActionCourseModel> getActionCourseModels() {
        return mActionCourseModels;
    }

    private void initActionCourseData() {
        mActionCourseModels.clear();
        ActionCourseModel courseModel1 = new ActionCourseModel();
        courseModel1.setActionCourcesName("第1关");
        courseModel1.setTitle("第一关 了解动作编辑器");
        courseModel1.setActionLockType(1);
        courseModel1.setDrawableId(R.drawable.ic_action_level1);
        courseModel1.setSize(3);

        mActionCourseModels.add(courseModel1);

        ActionCourseModel courseModel2 = new ActionCourseModel();
        courseModel2.setActionCourcesName("第2关");
        courseModel2.setTitle("第二关 学习动作库");
        courseModel2.setDrawableId(R.drawable.ic_action_level2);
        courseModel2.setActionLockType(0);
        courseModel2.setSize(3);
        mActionCourseModels.add(courseModel2);

        ActionCourseModel courseModel3 = new ActionCourseModel();
        courseModel3.setActionCourcesName("第3关");
        courseModel3.setTitle("第三关 学习音乐库");
        courseModel3.setDrawableId(R.drawable.ic_action_level3);
        courseModel3.setActionLockType(0);
        courseModel3.setSize(2);
        mActionCourseModels.add(courseModel3);

        ActionCourseModel courseModel4 = new ActionCourseModel();
        courseModel4.setActionCourcesName("第4关");
        courseModel4.setTitle("第四关 添加动作+音频");
        courseModel4.setDrawableId(R.drawable.ic_action_level4);
        courseModel4.setActionLockType(0);
        courseModel4.setSize(3);
        mActionCourseModels.add(courseModel4);

        ActionCourseModel courseModel5 = new ActionCourseModel();
        courseModel5.setActionCourcesName("第5关");
        courseModel5.setTitle("第五关 创建动作");
        courseModel5.setDrawableId(R.drawable.ic_action_level5);
        courseModel5.setActionLockType(0);
        courseModel5.setSize(3);
        mActionCourseModels.add(courseModel5);

        ActionCourseModel courseModel6 = new ActionCourseModel();
        courseModel6.setActionCourcesName("第6关");
        courseModel6.setTitle("第六关 创建音频");
        courseModel6.setDrawableId(R.drawable.ic_action_level6);
        courseModel6.setActionLockType(0);
        courseModel6.setSize(4);
        mActionCourseModels.add(courseModel6);

        ActionCourseModel courseModel7 = new ActionCourseModel();
        courseModel7.setActionCourcesName("第7关");
        courseModel7.setTitle("第七关 修改动作");
        courseModel7.setDrawableId(R.drawable.ic_action_level7);
        courseModel7.setActionLockType(0);
        courseModel7.setSize(2);
        mActionCourseModels.add(courseModel7);

        ActionCourseModel courseModel8 = new ActionCourseModel();
        courseModel8.setActionCourcesName("第8关");
        courseModel8.setTitle("第八关 连续动作");
        courseModel8.setDrawableId(R.drawable.ic_action_level8);
        courseModel8.setActionLockType(0);
        courseModel8.setSize(2);
        mActionCourseModels.add(courseModel8);

        ActionCourseModel courseModel9 = new ActionCourseModel();
        courseModel9.setActionCourcesName("第9关");
        courseModel9.setTitle("第九关 快速创建连续动作");
        courseModel9.setDrawableId(R.drawable.ic_action_level9);
        courseModel9.setActionLockType(0);
        courseModel9.setSize(2);
        mActionCourseModels.add(courseModel9);

        ActionCourseModel courseModel10 = new ActionCourseModel();
        courseModel10.setActionCourcesName("第10关");
        courseModel10.setTitle("第十关 自定义动作");
        courseModel10.setDrawableId(R.drawable.ic_action_level10);
        courseModel10.setActionLockType(0);
        courseModel10.setSize(1);
        mActionCourseModels.add(courseModel10);
        getLastCourseProgress();
    }

    /**
     * 获取最新进度
     */
    private void getLastCourseProgress() {
        SaveCourseProQuest proQequest = new SaveCourseProQuest();
        proQequest.setType(2);
        proQequest.setUserId("775562");
        proQequest.setToken("0e5a4625166a4442896aa8fdb97fdace775562");

        LocalActionRecord record = DataSupport.findFirst(LocalActionRecord.class);
        //本地没有记录，说明之前没用过，则根据后台返回保存本地记录
        if (null == record) {
            LocalActionRecord record1 = new LocalActionRecord();
            record1.setUserId("775562");
            record1.setCourseLevel(1);
            record1.setPeriodLevel(0);
            record1.setUpload(true);
            ViseLog.d("record1===" + record1.toString());
            record1.save();
        }

        ViseHttp.BASE(new PostRequest("http://10.10.1.14:8080/alpha1e/course/getCourseProgress")
                .setJson(GsonImpl.get().toJson(proQequest)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ViseLog.d("LOGIN onSuccess:" + response);
                        BaseResponseModel<CourseLastProgressModule> baseResponseModel = GsonImpl.get().toObject(response,
                                new TypeToken<BaseResponseModel<CourseLastProgressModule>>() {
                                }.getType());
                        if (baseResponseModel.status) {
                            CourseLastProgressModule courseLastProgressModule = baseResponseModel.models;
                            LocalActionRecord record = DataSupport.findFirst(LocalActionRecord.class);

                            //本地有记录，证明需要更新本地数据，或者上传记录
                            int course = record.getCourseLevel();
                            int level = record.getPeriodLevel();
                            if (null != courseLastProgressModule) {
                                //如果从后台取得关卡大于本地则更新本地数据
                                //如果从后台取得的关卡跟本地一致则判断课程，如果后台的课程大于本地的课程则更新本地
                                if (Integer.parseInt(courseLastProgressModule.getProgressOne()) > course ||
                                        (Integer.parseInt(courseLastProgressModule.getProgressOne()) == course &&
                                                Integer.parseInt(courseLastProgressModule.getProgressTwo()) > level)) {
                                    ContentValues values = new ContentValues();
                                    values.put("CourseLevel", Integer.parseInt(courseLastProgressModule.getProgressOne()));
                                    values.put("periodLevel", Integer.parseInt(courseLastProgressModule.getProgressTwo()));
                                    values.put("isUpload", true);
                                    DataSupport.updateAll(LocalActionRecord.class, values);
                                }
                            }
                        }
                        getAllCourseScore();
                    }

                    @Override
                    public void onFail(int i, String s) {
                        ToastUtils.showShort("login failed:" + s);
                        getAllCourseScore();
                    }
                });


    }

    /**
     * 获取所有上传关卡的分数获取
     */
    private void getAllCourseScore() {
        SaveCourseStatuRequest statuRequest = new SaveCourseStatuRequest();
        statuRequest.setType(2);
        statuRequest.setUserId("775562");
        statuRequest.setToken("0e5a4625166a4442896aa8fdb97fdace775562");
        ViseHttp.BASE(new PostRequest("http://10.10.1.14:8080/alpha1e/course/getCourseStatus")
                .setJson(GsonImpl.get().toJson(statuRequest)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ViseLog.d("LOGIN onSuccess:" + response);
                        BaseResponseModel<List<CourseDetailScoreModule>> baseResponseModel = GsonImpl.get().toObject(response,
                                new TypeToken<BaseResponseModel<List<CourseDetailScoreModule>>>() {
                                }.getType());
                        if (baseResponseModel.status) {
                            praseCourseData(baseResponseModel.models);
                        } else {
                            praseCourseData(null);

                        }
                    }

                    @Override
                    public void onFail(int i, String s) {
                        ToastUtils.showShort("login failed:" + s);
                        praseCourseData(null);
                    }
                });
    }


    /**
     * 解析获取所有上传关卡的分数
     */
    private void praseCourseData(List<CourseDetailScoreModule> list) {
        LocalActionRecord record = DataSupport.findFirst(LocalActionRecord.class);
        mActionCourseModels.get(0).setActionLockType(1);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                mActionCourseModels.get(i).setActionLockType(1);
                mActionCourseModels.get(i).setActionCourcesScore(1);
                if (i == list.size() - 1 && i < 9) {
                    mActionCourseModels.get(i + 1).setActionLockType(1);
                }
            }
        } else {
            if (null != record) {
                int course = record.getCourseLevel();
                int level = record.getPeriodLevel();//课时3
                ViseLog.d("后台获取数据为null   getCourseScores==" + "course==" + course + "   leavel==" + level);
                for (int i = 0; i < course; i++) {
                    mActionCourseModels.get(i).setActionLockType(1);
                    mActionCourseModels.get(i).setActionCourcesScore(1);
                    if (i == (course - 1)) {
                        mActionCourseModels.get(i).setActionCourcesScore(0);
                    }
                }
            }
        }
        if (mView != null) {
            mView.notifyDataChanged();
        }
        if (null != record && !record.isUpload()) {
            saveLastProgress(String.valueOf(record.getCourseLevel()), String.valueOf(record.getPeriodLevel()));
        }
    }

    /**
     * 保存课程最新进度
     */
    public void saveLastProgress(String progressOne, String courseTwo) {
        SaveCourseProQuest proQequest = new SaveCourseProQuest();
        proQequest.setCourseOne("1");
        proQequest.setProgressOne(progressOne);
        proQequest.setProgressTwo(courseTwo);
        proQequest.setCourseTwo(progressOne);
        proQequest.setType(2);
//        OkHttpClientUtils.getJsonByPostRequest(HttpEntity.SAVE_COURSE_PROGRESS, proQequest, 100)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        UbtLog.d("saveLastProgress", "e===" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        UbtLog.d("saveLastProgress", "response===" + response);
//                        LocalActionRecord record = DataSupport.findFirst(LocalActionRecord.class);
//                        if (null != record) {
//                            ContentValues values = new ContentValues();
//                            values.put("isUpload", true);
//                            DataSupport.updateAll(LocalActionRecord.class, values);
//                        }
//                    }
//                });
    }

}
