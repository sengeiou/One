package com.ubt.setting.notice;


import com.google.gson.reflect.TypeToken;
import com.ubt.baselib.model1E.BaseResponseModel;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.http1E.OkHttpClientUtils;
import com.ubt.setting.model.NoticeModel;
import com.ubt.setting.request.GetMessageListRequest;
import com.ubt.setting.request.UpdateMessageRequest;
import com.ubt.setting.util.SettingHttpEntity;
import com.vise.log.ViseLog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class NoticePresenter extends BasePresenterImpl<NoticeContract.View> implements NoticeContract.Presenter {
    private static String TAG = NoticePresenter.class.getSimpleName();

    @Override
    public void getNoticeData(final int type, int page, int offset) {
        GetMessageListRequest messageListRequest = new GetMessageListRequest();
        messageListRequest.setOffset(page);
        messageListRequest.setLimit(offset);
        OkHttpClientUtils.getJsonByPostRequest(SettingHttpEntity.MESSAGE_GET_LIST, messageListRequest, 0).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ViseLog.d(TAG, " getNoticeData onError:" + e.getMessage());
                if (mView != null) {
                    mView.setNoticeData(false, type, null);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                ViseLog.d(TAG, "getNoticeData==" + response);

                BaseResponseModel<List<NoticeModel>> baseResponseModel = GsonImpl.get().toObject(response,
                        new TypeToken<BaseResponseModel<List<NoticeModel>>>() {
                        }.getType());
                if (baseResponseModel.status) {
                    if (mView != null) {
                        ViseLog.d(TAG, "getNoticeData.models==" + baseResponseModel.models);
                        mView.setNoticeData(true, type, baseResponseModel.models);
                    }
                } else {
                    if (mView != null) {
                        mView.setNoticeData(false, type, null);
                    }
                }
            }
        });
    }

    /**
     * 更新消息状态
     *
     * @param noticeId
     */
    @Override
    public void updateNoticeStatu(final int noticeId) {
        UpdateMessageRequest messageListRequest = new UpdateMessageRequest();
        messageListRequest.setMessageId(noticeId);
        OkHttpClientUtils.getJsonByPostRequest(SettingHttpEntity.MESSAGE_UPDATE_STATU, messageListRequest, 0).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ViseLog.d(TAG, "updateNoticeStatu onError:" + e.getMessage());
                if (mView != null) {
                    mView.updateStatu(false, noticeId);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                ViseLog.d(TAG, "updateNoticeStatu==" + response);

                BaseResponseModel<List<NoticeModel>> baseResponseModel = GsonImpl.get().toObject(response,
                        new TypeToken<BaseResponseModel<List<NoticeModel>>>() {
                        }.getType());
                if (baseResponseModel.status) {
                    if (mView != null) {
                        ViseLog.d(TAG, "updateNoticeStatu.models==" + baseResponseModel.models);
                        mView.updateStatu(true, noticeId);
                    }
                } else {
                    if (mView != null) {
                        mView.updateStatu(false, noticeId);
                    }
                }
            }
        });
    }

    @Override
    public void deleteNotice(final int noticeId) {
        if (mView != null) {
            mView.showLoading();
        }
        UpdateMessageRequest messageListRequest = new UpdateMessageRequest();
        messageListRequest.setMessageId(noticeId);
        OkHttpClientUtils.getJsonByPostRequest(SettingHttpEntity.MESSAGE_DELETE, messageListRequest, 0).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ViseLog.d(TAG, "onError:" + e.getMessage());
                if (mView != null) {
                    mView.deleteNotice(false, noticeId);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                ViseLog.d(TAG, "deleteNotice==" + response);

                BaseResponseModel<List<NoticeModel>> baseResponseModel = GsonImpl.get().toObject(response,
                        new TypeToken<BaseResponseModel<List<NoticeModel>>>() {
                        }.getType());
                if (baseResponseModel.status) {
                    if (mView != null) {
                        ViseLog.d(TAG, "deleteNotice.models==" + baseResponseModel.models);
                        mView.deleteNotice(true, noticeId);
                    }
                } else {
                    if (mView != null) {
                        mView.deleteNotice(false, noticeId);
                    }
                }
            }
        });

    }
}
