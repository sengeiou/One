package com.ubt.loginmodule.login;


import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.http1E.OkHttpClientUtils;
import com.ubt.loginmodule.LoginConstant.LoginSP;
import com.ubt.loginmodule.LoginHttpEntity;
import com.ubt.loginmodule.LoginUtil;
import com.ubt.loginmodule.requestModel.LoginRequest;
import com.vise.log.ViseLog;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 *  @author ubt
 */

public class LoginPresenter extends BasePresenterImpl<LoginContract.View> implements LoginContract.Presenter{

    @Override
    public void loginUseEmail(String account, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAppId(LoginSP.APPID);
        loginRequest.setPassword(LoginUtil.encodeByMD5(password));
        loginRequest.setAccount(account);
        loginRequest.setAccountType(1);

        String params = "{"
                + "\"account\":" + "\"" + account + "\""
                + ",\n\"accountType\":" + 1
                + ",\n\"password\":" + "\"" + LoginUtil.encodeByMD5(password) + "\""
                + ",\n\"appId\":" + 100010074
                + "}";


        OkHttpClientUtils.getJsonByPutRequest(LoginHttpEntity.BASE_LOGIN_URL+LoginHttpEntity.LOGIN, params, 1)
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ViseLog.d("login onError:" + e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ViseLog.d("login onResponse:" + response);

                    }
                });


       /* ViseHttp.PUT(LoginHttpEntity.LOGIN).baseUrl(LoginHttpEntity.BASE_LOGIN_URL)
                .addParam("appId", LoginSP.APPID)
                .addParam("account", account)
                .addParam("password", LoginUtil.encodeByMD5(password))
                .addParam("accountType", "1")
                .request(new ACallback<String>() {

                    @Override
                    public void onSuccess(String response) {
                        ViseLog.d("LOGIN onSuccess:" + response);
                    }

                    @Override
                    public void onFail(int i, String s) {
                        ViseLog.e("login failed:" + i +"-msg:" +  s);
                    }
                });*/

   /*     ViseHttp.BASE(new PutRequest("user/login").baseUrl("https://test79.ubtrobot.com/user-service-rest/v2/").addParam("param",GsonImpl.get().toJson(loginRequest)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ViseLog.d("LOGIN onSuccess:" + response);
                    }

                    @Override
                    public void onFail(int i, String s) {
                        ViseLog.e("login failed:" + s);
                    }
                });
*/
        ViseLog.d("url:" + LoginHttpEntity.LOGIN + "__params:" + GsonImpl.get().toJson(loginRequest));
       /* ViseHttp.BASE(new PostRequest(LoginHttpEntity.LOGIN)
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
                            if(mView != null){
                                mView.loginFailed();
                            }
                        }


                    }

                    @Override
                    public void onFail(int i, String s) {
                        ToastUtils.showShort("login failed:" + s);
                        if(mView != null){
                            mView.loginFailed();
                        }
                    }
                });*/


    }
}
