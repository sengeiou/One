package com.ubt.setting.notice;


import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;
import com.ubt.setting.model.NoticeModel;

import java.util.List;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class NoticeContract {
    interface View extends BaseView {
        void showLoading();

        void dissLoding();

        void setNoticeData(boolean isSuccess, int type, List<NoticeModel> list);

        void updateStatu(boolean isSuccess, int messageId);

        void deleteNotice(boolean isSuccess, int messageId);
    }

    interface Presenter extends BasePresenter<View> {
        void getNoticeData(int type, int page, int limit);

        void updateNoticeStatu(int noticeId);

        void deleteNotice(int noticeId);
    }
}