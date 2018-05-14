package com.ubt.loginmodule.register;


import android.text.TextUtils;

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
import com.ubt.loginmodule.requestModel.GetCodeRequest;
import com.ubt.loginmodule.requestModel.RegisterRequest;
import com.ubt.loginmodule.requestModel.UpdateUserInfoRequest;
import com.vise.log.ViseLog;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class RegisterPresenter extends BasePresenterImpl<RegisterContract.View> implements RegisterContract.Presenter{

    private static final int START_YEAR = 1900;
    private static final int END_YEAR = 2018;

    @Override
    public void sendSecurityCode(String account) {
        GetCodeRequest getCodeRequest = new GetCodeRequest();
        getCodeRequest.setAppId(LoginSP.APPID);
        getCodeRequest.setEmail(account);
        getCodeRequest.setEmailType(1);
        getCodeRequest.setNickName("");
        ViseLog.d("url:" + LoginHttpEntity.GET_CODE + "_params:" + GsonImpl.get().toJson(getCodeRequest));


        ViseHttp.POST(LoginHttpEntity.GET_CODE)
                .baseUrl(LoginHttpEntity.BASE_LOGIN_URL).setJson( GsonImpl.get().toJson(getCodeRequest))
                .request(new ACallback<String>() {
            @Override
            public void onSuccess(String data) {
                //请求成功
                ViseLog.d("GET_CODE onSuccess:" + data);
                if(mView != null){
                    mView.sendSecurityCodeSuccess(true);
                }
            }

            @Override
            public void onFail(int errCode, String errMsg) {
                //请求失败，errCode为错误码，errMsg为错误描述
                ViseLog.e("GET_CODE onFail:" + errCode + "-errMsg:" +  errMsg);
                ToastUtils.showShort(LoginUtil.parseErrMsg(errMsg));
                try {
                    JSONObject jsonObject = new JSONObject(errMsg);
                    String msg = jsonObject.getString("message");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(mView != null){
                    mView.sendSecurityCodeSuccess(false);
                }


            }
        });

    }

    @Override
    public void signUp(final String account, String password, String code) {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setAppId(LoginSP.APPID);
        registerRequest.setAccount(account);
        registerRequest.setAccountType(1);
        registerRequest.setPassword(LoginUtil.encodeByMD5(password));
        registerRequest.setCaptcha(code);
        ViseLog.d("url:" +LoginHttpEntity.BASE_LOGIN_URL + LoginHttpEntity.REGISTER + "_json:" + GsonImpl.get().toJson(registerRequest));

        ViseHttp.POST(LoginHttpEntity.REGISTER)
                .baseUrl(LoginHttpEntity.BASE_LOGIN_URL)
                .setJson(GsonImpl.get().toJson(registerRequest))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        //请求成功
                        ViseLog.d("REGISTER onSuccess:" + data);
//                        onSuccess(RegisterPresenter.java:88): REGISTER onSuccess:{"token":{"token":"3d0b8641972a4ec89c1fd2e31bcaf3f4806102","expireAt":1526437666734},"user":{"userId":806102,"userName":"weimin.ma@ubtrobot.com","userEmail":"weimin.ma@ubtrobot.com","userPhone":null,"userGender":null,"userImage":null,"countryCode":null,"nickName":"alpha1e-overseasuser1525832866638","userBirthday":null,"countryName":null,"emailVerify":null,"pwdCreateType":0,"userExtraPhone":null,"userExtraEmail":null}}
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            JSONObject tokenJson = jsonObject.getJSONObject("token");
                            JSONObject userJson = jsonObject.getJSONObject("user");
                            String token = tokenJson.getString("token");
                            int userId = userJson.getInt("userId");
                            String userEmail = userJson.getString("userEmail");
                            ViseLog.d("token:" + token + "-userId:" + userId);
                            SPUtils.getInstance().put(Constant1E.SP_USER_ID, userId);
                            SPUtils.getInstance().put(Constant1E.SP_USER_TOKEN, token);
                            SPUtils.getInstance().put(Constant1E.SP_USER_EMAIL, userEmail);
                            if(mView != null){
                                mView.signUpSuccess();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        //请求失败，errCode为错误码，errMsg为错误描述
                        ViseLog.e("REGISTER onFail:" + errMsg +"-errCode:" +errCode);
                        if(mView != null){
                            mView.signUpFailed();
                        }
                    }
                });



    }

    @Override
    public void getYearData() {
        List<String> listYear = new ArrayList<String>();
        for(int i=START_YEAR; i<=END_YEAR; i++){
            listYear.add(String.valueOf(i));
        }

        if(mView != null){
            mView.setYearData(listYear);
        }

    }

    @Override
    public void getMonthData() {
        List<String> listMonth = new ArrayList<String>();
        for(int i=1; i<=12; i++){
            listMonth.add(String.valueOf(i));
        }

        if(mView != null){
            mView.setMonthData(listMonth);
        }
    }

    @Override
    public void getDayData(int dayCount) {
        List<String> listDay = new ArrayList<String>();
        for(int i=1; i<=dayCount; i++){
            listDay.add(String.valueOf(i));
        }

        if(mView != null){
            mView.setDayData(listDay);
        }
    }

    @Override
    public void register() {

        if(mView != null){
            mView.registerFinish();
        }
    }

    @Override
    public void updateUserInfo(final String userName, final String sex, final String birth) {
        UpdateUserInfoRequest updateUserInfoRequest = new UpdateUserInfoRequest();
        final UserInfoModel userInfoModel = (UserInfoModel) SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
        ViseLog.d("userInfoModel:" + userInfoModel);
        updateUserInfoRequest.setEmail(SPUtils.getInstance().getString(Constant1E.SP_USER_EMAIL));
        updateUserInfoRequest.setUserId(String.valueOf(SPUtils.getInstance().getInt(Constant1E.SP_USER_ID)));
        updateUserInfoRequest.setToken(SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN));
        if(!TextUtils.isEmpty(userName)){
            updateUserInfoRequest.setNickName(userName);
        }
        if(!TextUtils.isEmpty(sex)){
            updateUserInfoRequest.setSex(sex);
        }
        if(!TextUtils.isEmpty(birth)){
            updateUserInfoRequest.setBirthDay(birth);
        }

        ViseLog.d("url:" +LoginHttpEntity.BASE_URL + LoginHttpEntity.USER_UPDATE + "params:" + updateUserInfoRequest.toString());

        ViseHttp.POST(LoginHttpEntity.USER_UPDATE).baseUrl(LoginHttpEntity.BASE_URL)
                .setJson(GsonImpl.get().toJson(updateUserInfoRequest))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        ViseLog.d("USER_UPDATE onSuccess:" + data.toString());
                        BaseResponseModel<UserInfoModel> baseResponseModel = GsonImpl.get().toObject(data,
                                new TypeToken<BaseResponseModel<UserInfoModel>>() {
                                }.getType());
                        if(baseResponseModel.status){
                            UserInfoModel userInfoModel =  baseResponseModel.models;
                            SPUtils.getInstance().saveObject(Constant1E.SP_USER_INFO,userInfoModel);
                            if(mView != null){
                                mView.updateUserInfoSuccess(true);
                            }
                        }else{
                            if(mView != null){
                                mView.updateUserInfoSuccess(false);
                            }
                        }
                    }

                    @Override
                    public void onFail(int i, String s) {
                        ViseLog.d("USER_UPDATE onFail:" + s);
                        if(mView != null){
                            mView.updateUserInfoSuccess(false);
                        }
                    }

                });



    }
}
