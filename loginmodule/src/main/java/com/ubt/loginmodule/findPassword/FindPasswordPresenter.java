package com.ubt.loginmodule.findPassword;

import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.loginmodule.LoginHttpEntity;
import com.ubt.loginmodule.requestModel.LoginRequest;
import com.ubt.loginmodule.requestModel.ValidateCodeRequest;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.request.PostRequest;

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
/*        final BaseLoginRequest baseLoginRequest = new BaseLoginRequest();
//        baseLoginRequest.setEmail(email);
        ViseHttp.BASE(new PostRequest(LoginHttpEntity.FIND_PASSWORD)
                .setJson(GsonImpl.get().toJson(baseLoginRequest)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        BaseResponseModel baseResponseModel = GsonImpl.get().toObject(response,
                                new TypeToken<BaseResponseModel>() {
                                }.getType());

                        ViseLog.d("response:" + response + "_baseResponseModel:" + baseResponseModel);

                        if(baseResponseModel.status){
                            ViseLog.d("status true");
                            if(mView != null){
                                mView.requestSecurityCodeSuccess();
                            }
                        }else{
                            if(mView != null){
                                mView.requestSecurityCodeFailed();
                            }
                        }

                    }

                    @Override
                    public void onFail(int i, String s) {
                        if(mView != null){
                            mView.requestSecurityCodeFailed();
                        }
                    }
                });*/

    }

    @Override
    public void requestVerifyAccount(final String email, String code) {
        ValidateCodeRequest request = new ValidateCodeRequest();
//        request.setEmail(email);
        request.setCode(code);
        ViseHttp.BASE(new PostRequest(LoginHttpEntity.VALIDATE_CODE)
                .setJson(GsonImpl.get().toJson(request)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String o) {
                        if(mView != null){
                            mView.requestVerifyAccountSuccess(email);
                        }
                    }

                    @Override
                    public void onFail(int i, String s) {
                        if(mView != null){
                            mView.requestVerifyAccountFailed();
                        }
                    }
                });


    }

    @Override
    public void resetPassword(String email,String password) {
        LoginRequest loginRequest = new LoginRequest(); //由于重置和登陆接口参数一样，故使用登录请求实体
//        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
        ViseHttp.BASE(new PostRequest(LoginHttpEntity.RESET)
                .setJson(GsonImpl.get().toJson(loginRequest)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String o) {
                        if(mView != null) {
                            mView.resetPasswordSuccess();
                        }
                    }

                    @Override
                    public void onFail(int i, String s) {
                        if(mView != null) {
                            mView.resetPasswordFailed();
                        }
                    }
                });

    }
}
