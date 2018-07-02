package com.ubt.loginmodule.login;


import com.google.gson.reflect.TypeToken;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.model1E.BaseResponseModel;
import com.ubt.baselib.model1E.UserInfoModel;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.loginmodule.LoginConstant.LoginSP;
import com.ubt.loginmodule.LoginHttpEntity;
import com.ubt.loginmodule.LoginUtil;
import com.ubt.loginmodule.requestModel.GetUserInfoRequest;
import com.ubt.loginmodule.requestModel.LoginRequest;
import com.ubt.loginmodule.requestModel.LoginThirdRequest;
import com.vise.log.ViseLog;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

        ViseHttp.PUT(LoginHttpEntity.LOGIN).baseUrl(LoginHttpEntity.BASE_LOGIN_URL)
                .setJson(GsonImpl.get().toJson(loginRequest))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        ViseLog.d("LOGIN onSuccess:" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject tokenJson = jsonObject.getJSONObject("token");
                            JSONObject userJson = jsonObject.getJSONObject("user");
                            String token = tokenJson.getString("token");
                            int userId = userJson.getInt("userId");
                            String userEmail = userJson.getString("userEmail");
                            ViseLog.d("token:" + token + "-userId:" + userId);
                            SPUtils.getInstance().put(Constant1E.SP_USER_ID, userId);
                            SPUtils.getInstance().put(Constant1E.SP_USER_TOKEN, token);
                            SPUtils.getInstance().put(Constant1E.SP_USER_EMAIL, userEmail);
                            //TODO getuserInfo
                            if(mView != null){
                                mView.UpdateUserInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(int i, String s) {
                        ViseLog.e("login failed:" + i +"-msg:" +  s);
                        if(i>500){
                            ToastUtils.showShort("Service temporarily unavailable,please try again later!");
                        }else{
                            ToastUtils.showShort(LoginUtil.parseErrMsg(s));
                        }
                        if(mView != null){
                            mView.loginFailed();
                        }
                    }
                });

    }



    @Override
    public void loginThird(String token, String userId, String loginType) {

        LoginThirdRequest request = new LoginThirdRequest();
        request.setAccessToken(token);
        request.setAppId("882664668572429");
        request.setLoginType(loginType);
        request.setNickName("alphaebot_en");
        request.setOpenId(userId);
        request.setUbtAppId(Integer.valueOf(LoginSP.APPID));

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-UBT-AppId", LoginSP.APPID);
        headers.put("X-UBT-Sign", LoginSP.APPKEY);


        ViseHttp.PUT(LoginHttpEntity.LOGIN_THIRD).baseUrl(LoginHttpEntity.BASE_LOGIN_URL)
                .addHeaders(headers)
                .setJson(GsonImpl.get().toJson(request))
                .request(new ACallback<String>() {

                    @Override
                    public void onSuccess(String response) {
//                        ToastUtils.showShort("onSuccess:" + response);
                        //第三方登录成功，保存userId和token，判断是否是第一次登录
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject tokenJson = jsonObject.getJSONObject("token");
                            JSONObject userJson = jsonObject.getJSONObject("user");
                            String token = tokenJson.getString("token");
                            int userId = userJson.getInt("userId");
                            String userEmail = userJson.getString("userEmail");
                            ViseLog.d("token:" + token + "-userId:" + userId + "--userEmail:" + userEmail);
                            SPUtils.getInstance().put(Constant1E.SP_USER_ID, userId);
                            SPUtils.getInstance().put(Constant1E.SP_USER_TOKEN, token);
                            if(userEmail == null){
                                SPUtils.getInstance().put(Constant1E.SP_USER_EMAIL, "");
                            }else{
                                SPUtils.getInstance().put(Constant1E.SP_USER_EMAIL, userEmail);
                            }
                            //TODO 判断是否第一次
                            if(mView != null){
                                mView.UpdateUserInfo();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFail(int i, String s) {
                        ToastUtils.showShort(LoginUtil.parseErrMsg(s));
                        if(mView != null){
                            mView.loginThirdFinish(false);
                        }
                    }
                });

    }

    @Override
    public void getUserInfo() {
        GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest();
        getUserInfoRequest.setToken(SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN));
        getUserInfoRequest.setUserId(SPUtils.getInstance().getInt(Constant1E.SP_USER_ID));

        ViseHttp.POST(LoginHttpEntity.USER_GET_INFO).baseUrl(LoginHttpEntity.BASE_URL)
                .setJson(GsonImpl.get().toJson(getUserInfoRequest))
                .request(new ACallback<String>() {

                    @Override
                    public void onSuccess(String data) {
                        ViseLog.d("USER_GET_INFO onSuccess:" + data);

                        BaseResponseModel<UserInfoModel> baseResponseModel = GsonImpl.get().toObject(data,
                                new TypeToken<BaseResponseModel<UserInfoModel>>() {
                                }.getType());
                        if(baseResponseModel.status){
                            UserInfoModel userInfoModel =  baseResponseModel.models;
                            ViseLog.e("userInfoModel:" + (userInfoModel==null));
                            if(userInfoModel== null){
                                if(mView != null){
                                    mView.UpdateUserInfoFinish();
                                }
                            }else{
                                SPUtils.getInstance().saveObject(Constant1E.SP_USER_INFO,userInfoModel);
                                if(mView != null){
                                    mView.loginSuccess();
                                }
                            }

                        }else{
                            ToastUtils.showShort(baseResponseModel.info);
                            if(mView != null){
                                mView.getUserInfoFailed();
                            }
                        }

                    }

                    @Override
                    public void onFail(int i, String s) {
                        ViseLog.d("USER_GET_INFO onFail:"+ i + "s:" +  s);
                        ToastUtils.showShort(s);
                        if(mView != null){
                            mView.getUserInfoFailed();
                        }
                    }
                });
    }

/*
    private void isFirstThird(final String token){

        ViseHttp.GET(LoginHttpEntity.LOGIN_THIRD_FIRST).baseUrl(LoginHttpEntity.BASE_LOGIN_URL)
                .addHeader("authorization", token)
                .addParam("appId", LoginSP.APPID)
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String s) {
                        ToastUtils.showShort("LOGIN_THIRD_FIRST onSuccess:" + s);
                        *//*{
                        "code": 0,
                                "message": "第三方帐号非首次登录登录该app"
                    }*//*
                        //如果是第一次则跳转完善信息页面，如果不是第一次，则获取用户信息，并跳转主页
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("message");
                            if(code == 0){ //非首次
                                //getUserInfo()
                            }else{
                                //完善信息

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(int i, String s) {
                        ToastUtils.showShort("LOGIN_THIRD_FIRST onFail:" + s );
                        if(mView != null){
                            mView.test("s:" + s + "--token:" + token);
                        }
                    }
                });

    }*/
}
