package com.ubt.blockly.course;


import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.blockly.BlockHttpEntity;
import com.ubt.blockly.course.model.UpdateCourseRequest;
import com.vise.log.ViseLog;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class BlocklyCoursePresenter extends BasePresenterImpl<BlocklyCourseContract.View> implements BlocklyCourseContract.Presenter{

    private static final String TAG = "BlocklyCoursePresenter";

    @Override
    public void getData() {
        //从后台获取课程数据
    }

    @Override
    public void updateCourseData(int cid) {
        UpdateCourseRequest courseRequest = new UpdateCourseRequest();
        courseRequest.setCurrGraphProgramId(cid);
        ViseHttp.BASE(new PostRequest(BlockHttpEntity.UPDATE_BLOCKLY_COURSE)
                .setJson(GsonImpl.get().toJson(courseRequest)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ViseLog.d("updateCurrentCourse onResponse:" + response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");
                            if(status){
                                if(isAttachView()){
                                    mView.updateSuccess();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(int i, String s) {
                        if(isAttachView()){
                            mView.updateFail();
                        }
                    }
                });



           }
}
