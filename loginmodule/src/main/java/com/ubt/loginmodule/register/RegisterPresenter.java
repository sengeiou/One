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
import com.ubt.loginmodule.LoginHttpEntity;
import com.ubt.loginmodule.requestModel.GetCodeRequest;
import com.ubt.loginmodule.requestModel.RegisterRequest;
import com.ubt.loginmodule.requestModel.UpdateUserInfoRequest;
import com.ubt.loginmodule.userModel.UserIdModel;
import com.vise.log.ViseLog;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.request.PostRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class RegisterPresenter extends BasePresenterImpl<RegisterContract.View> implements RegisterContract.Presenter{

    private static final int START_YEAR = 1900;
    private static final int END_YEAR = 2014;

    @Override
    public void sendSecurityCode(String account) {
        GetCodeRequest getCodeRequest = new GetCodeRequest();
        getCodeRequest.setEmail(account);
        ViseHttp.BASE(new PostRequest(LoginHttpEntity.GET_CODE)
                .setJson(GsonImpl.get().toJson(getCodeRequest)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        ViseLog.d("GET_CODE onSuccess:" + data);
                        if(mView != null){
                            mView.sendSecurityCodeSuccess(true);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ViseLog.e("GET_CODE onFail:" + errMsg);
                        if(mView != null){
                            mView.sendSecurityCodeSuccess(false);
                        }
                    }

                });


    }

    @Override
    public void signUp(final String account, String password, String code) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(account);
        registerRequest.setPassword(password);
        registerRequest.setCode(code);
        ViseLog.d("url:" + LoginHttpEntity.REGISTER + "_json:" + GsonImpl.get().toJson(registerRequest));
        ViseHttp.BASE(new PostRequest(LoginHttpEntity.REGISTER)
                .setJson(GsonImpl.get().toJson(registerRequest)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        ViseLog.d("REGISTER onSuccess:" + data);
                        BaseResponseModel<UserIdModel> baseResponseModel = GsonImpl.get().toObject(data,
                                new TypeToken<BaseResponseModel<UserIdModel>>() {
                                }.getType());

                        if(baseResponseModel.status){
                            if(null != baseResponseModel.models){
                                UserIdModel userIdModel = baseResponseModel.models;
                                ViseLog.d("userIdModel:" + userIdModel);
                                UserInfoModel userInfoModel = new UserInfoModel();
                                userInfoModel.setUserId(userIdModel.getUserId());
                                userInfoModel.setEmail(account);
                                ViseLog.d("userInfoModel:" + userInfoModel);
                                SPUtils.getInstance().saveObject(Constant1E.SP_USER_INFO, userInfoModel);
//                                SPUtils.getInstance().put(LoginSP.SP_USER_ID,userIdModel.getUserId());
//                                SPUtils.getInstance().put(LoginSP.SP_EMAIL, account);
//                                SpCache spCache = new SpCache(ContextUtils.getContext());
//                                spCache.put(LoginSP.SP_USER_ID, userIdModel.getUserId());
//                                spCache.put(LoginSP.SP_EMAIL, account);
                            }
                            if(mView != null){
                                mView.signUpSuccess();
                            }
                        }else{
                            ToastUtils.showShort(baseResponseModel.info);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ViseLog.e("REGISTER onFail:" + errMsg);
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
    public void updateUserInfo(String userName, String sex, String birth) {
        UpdateUserInfoRequest updateUserInfoRequest = new UpdateUserInfoRequest();
        final UserInfoModel userInfoModel = (UserInfoModel) SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
        ViseLog.d("userInfoModel:" + userInfoModel);
//        updateUserInfoRequest.setEmail(SPUtils.getInstance().getString(LoginSP.SP_EMAIL));
//        updateUserInfoRequest.setUserId(SPUtils.getInstance().getString(LoginSP.SP_USER_ID));
        updateUserInfoRequest.setEmail(userInfoModel.getEmail());
        updateUserInfoRequest.setUserId(userInfoModel.getUserId());
        if(!TextUtils.isEmpty(userName)){
            updateUserInfoRequest.setNickName(userName);
        }
        if(!TextUtils.isEmpty(sex)){
            updateUserInfoRequest.setSex(sex);
        }
        if(!TextUtils.isEmpty(birth)){
            updateUserInfoRequest.setBirthDate(birth);
        }

        ViseLog.d("url:" + LoginHttpEntity.USER_UPDATE + "params:" + updateUserInfoRequest.toString());

        ViseHttp.BASE(new PostRequest(LoginHttpEntity.USER_UPDATE)
                .setJson(GsonImpl.get().toJson(updateUserInfoRequest)))
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String o) {
                        ViseLog.d("USER_UPDATE onSuccess:" + o.toString());
                        if(mView != null){
                            //TODO
                            mView.updateUserInfoSuccess(true);
                        }
                    }

                    @Override
                    public void onFail(int i, String s) {
                        if(mView != null){
                            mView.updateUserInfoSuccess(false);
                        }
                    }
                });


    }
}
