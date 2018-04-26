package com.ubt.loginmodule.login;


import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 *  @author ubt
 */

public class LoginContract {

    interface View extends BaseView {
        void loginSuccess();
        void loginFailed();

    }

    interface  Presenter extends BasePresenter<View> {

        void loginUseEmail(String account, String password);
        
    }
}
