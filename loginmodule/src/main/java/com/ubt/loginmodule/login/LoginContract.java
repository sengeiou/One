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
        void loginThirdFinish(boolean success);
        void test(String text);
        void UpdateUserInfo();
        void UpdateUserInfoFinish();

    }

    interface  Presenter extends BasePresenter<View> {

        void loginUseEmail(String account, String password);
        void loginThird(String token, String userId, String loginType);
        void getUserInfo();
        
    }
}
