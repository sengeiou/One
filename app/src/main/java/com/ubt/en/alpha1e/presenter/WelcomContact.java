package com.ubt.en.alpha1e.presenter;

import android.content.Context;

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

public class WelcomContact {
    public interface View extends BaseView {

        void updateLanguageCompleted();

        void getUserInfoCompleted();

    }

    public interface Presenter extends BasePresenter<View> {

        void initLanugage(Context context);

        void refreshToken();


    }
}
