package com.ubt.en.alpha1e.action.contact;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;

/**
 * @author：liuhai
 * @date：2018/4/11 11:08
 * @modifier：ubt
 * @modify_date：2018/4/11 11:08
 * [A brief description]
 * version
 */

public class ActionCourseContact {
    public interface View extends BaseView {
        void notifyDataChanged();
    }

    public interface Presenter extends BasePresenter<View> {

    }
}
