package com.ubt.loginmodule.findPassword;

import com.ubt.baselib.mvp.BasePresenterImpl;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class FindPasswordPresenter extends BasePresenterImpl<FindPasswordContract.View> implements FindPasswordContract.Presenter {


    @Override
    public void requestSecurityCode(String email) {
        //TODO okhttp 调用后台接口并返回结果,
        //模拟成功
        if(mView != null){
            mView.requestSecurityCodeSuccess();
        }
    }

    @Override
    public void requestVerifyAccount(String email, String code) {
        //TODO okhttp 调用后台接口并返回结果
        if(mView != null){
            mView.requestVerifyAccountSuccess();
        }

    }

    @Override
    public void resetPassword(String password) {
        if(mView != null) {
            mView.resetPasswordSuccess();
        }
    }
}
