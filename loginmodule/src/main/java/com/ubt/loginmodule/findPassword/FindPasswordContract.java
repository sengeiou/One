package com.ubt.loginmodule.findPassword;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class FindPasswordContract {

    interface View extends BaseView{
        void requestSecurityCodeSuccess();
        void requestSecurityCodeFailed();
        void requestVerifyAccountSuccess();
        void requestVerifyAccountFailed();
        void resetPasswordSuccess();
        void resetPasswordFailed();
    }

    interface Presenter extends BasePresenter<View> {
        void requestSecurityCode(String email);
        void requestVerifyAccount(String email, String code);
        void resetPassword(String password);
    }
}
