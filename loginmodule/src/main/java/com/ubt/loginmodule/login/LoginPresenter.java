package com.ubt.loginmodule.login;


import com.ubt.baselib.mvp.BasePresenterImpl;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 *  @author ubt
 */

public class LoginPresenter extends BasePresenterImpl<LoginContract.View> implements LoginContract.Presenter{

    @Override
    public void loginUseEmail(String account, String password) {

  /*      String params = "account:" + account; //
        OkHttpClientUtils.getJsonByPostRequest(LoginHttpEntity.LOGIN_USE_EMAIL, params).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if(mView != null){
                    mView.loginFailed();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if(mView != null){
                    mView.loginSuccess();
                }
            }
        });*/

        if(mView != null){
            mView.loginSuccess();
        }
    }
}
