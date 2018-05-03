package com.ubt.blockly.course.courselist;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.ubt.baselib.model1E.BaseResponseModel;
import com.ubt.baselib.model1E.CourseData;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.blockly.BlockHttpEntity;
import com.ubt.blockly.course.model.BaseRequest;
import com.ubt.blockly.course.model.BlocklyCourseModel;
import com.ubt.blockly.course.model.UpdateCourseRequest;
import com.vise.log.ViseLog;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.request.PostRequest;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class CourseListPresenter extends BasePresenterImpl<CourseListContract.View> implements CourseListContract.Presenter{

    private static final String TAG = "CourseListPresenter";

    @Override
    public void getBlocklyCourseList(final Context context) {
        //从服务器获取数据并保存本地数据
        final BaseRequest request = new BaseRequest();
        request.setUserId("775562");
        ViseHttp.BASE(new PostRequest(BlockHttpEntity.BLOCKLY_COURSE_LIST)
                .setJson(GsonImpl.get().toJson(request)))
                .request(new ACallback<Object>() {
                    @Override
                    public void onSuccess(Object response) {
                        ViseLog.d("BLOCKLY_COURSE_LIST onResponse:" + response.toString());
                        BaseResponseModel<List<BlocklyCourseModel>> baseResponseModel = GsonImpl.get().toObject(response.toString(),
                                new TypeToken<BaseResponseModel<List<BlocklyCourseModel>>>(){}.getType());

                        for(int i=0; i<baseResponseModel.models.size(); i++){
                            CourseData courseData = new CourseData();
                            courseData.setSequence(baseResponseModel.models.get(i).getSequence());
                            courseData.setCurrGraphProgramId(baseResponseModel.models.get(i).getCurrGraphProgramId());
                            courseData.setName(baseResponseModel.models.get(i).getName());
                            courseData.setStatus(baseResponseModel.models.get(i).getStatus());
                            courseData.setUserId(baseResponseModel.models.get(i).getUserId());
                            courseData.setThumbnailUrl(baseResponseModel.models.get(i).getThumbnailUrl());
                            courseData.setVideoUrl(baseResponseModel.models.get(i).getVideoUrl());
                            courseData.setCid(baseResponseModel.models.get(i).getCid());
                            courseData.setSubTitle(baseResponseModel.models.get(i).getSubTitle());
                            courseData.saveOrUpdate("cid = ?" , String.valueOf(baseResponseModel.models.get(i).getCid()));

                            ViseLog.d(TAG, "courseData:" + courseData.toString());
                        }


                        if (isAttachView()) {
                            List<CourseData> courseDataList = DataSupport.findAll(CourseData.class);
                            ViseLog.d(TAG, "courseDataList:" + courseDataList.toString());
                            mView.setBlocklyCourseData(courseDataList);
                        }
                    }

                    @Override
                    public void onFail(int i, String s) {
                        ViseLog.d( "BLOCKLY_COURSE_LIST onError e:" + s.toString());
                        ToastUtils.showShort("网络出错,请检查是否开启网络");
                        if (isAttachView()) {
                            List<CourseData> courseDataList = DataSupport.findAll(CourseData.class);
                            ViseLog.d(TAG, "courseDataList:" + courseDataList.toString());
                            mView.setBlocklyCourseData(courseDataList);
                        }
                    }
                });


    }

    @Override
    public void updateCurrentCourse(int cid) {
        UpdateCourseRequest courseRequest = new UpdateCourseRequest();
        courseRequest.setCurrGraphProgramId(cid);
        ViseHttp.BASE(new PostRequest(BlockHttpEntity.UPDATE_BLOCKLY_COURSE)
                .setJson(GsonImpl.get().toJson(courseRequest)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ViseLog.d(TAG, "updateCurrentCourse onResponse:" + response.toString());
                        if(isAttachView()){
                            mView.updateSuccess();
                        }

                    }

                    @Override
                    public void onFail(int i, String s) {
                        ViseLog.e("updateCurrentCourse onError:" + s.toString());
                        if(isAttachView()){
                            mView.updateFail();
                        }
                    }
                });



    }




}
