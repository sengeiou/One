package com.ubt.loginmodule.login;


import com.google.gson.reflect.TypeToken;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.model1E.BaseResponseModel;
import com.ubt.baselib.model1E.UserInfoModel;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.loginmodule.LoginHttpEntity;
import com.ubt.loginmodule.requestModel.LoginRequest;
import com.vise.log.ViseLog;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.request.PostRequest;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 *  @author ubt
 */

public class LoginPresenter extends BasePresenterImpl<LoginContract.View> implements LoginContract.Presenter{

    @Override
    public void loginUseEmail(String account, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(account);
        loginRequest.setPassword(password);
        ViseLog.d("url:" + LoginHttpEntity.LOGIN + "__params:" + loginRequest.toString());
        ViseHttp.BASE(new PostRequest(LoginHttpEntity.LOGIN)
                .setJson(GsonImpl.get().toJson(loginRequest)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ViseLog.d("LOGIN onSuccess:" + response);

                        BaseResponseModel<UserInfoModel> baseResponseModel = GsonImpl.get().toObject(response,
                                new TypeToken<BaseResponseModel<UserInfoModel>>() {
                                }.getType());

                        if(baseResponseModel.status){
                            UserInfoModel userInfoModel = baseResponseModel.models;
                            ViseLog.d("LOGIN onSuccess:" + userInfoModel.toString());
                            SPUtils.getInstance().saveObject(Constant1E.SP_USER_INFO, userInfoModel);
                            if(mView != null){
                                mView.loginSuccess();
                            }
                        }else{
                            ToastUtils.showShort(baseResponseModel.info);
                        }


                    }

                    @Override
                    public void onFail(int i, String s) {
                        ToastUtils.showShort("login failed:" + s);
                    }
                });


    }
}
