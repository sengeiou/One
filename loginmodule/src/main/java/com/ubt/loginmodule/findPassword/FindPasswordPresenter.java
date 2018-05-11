package com.ubt.loginmodule.findPassword;

import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.loginmodule.LoginConstant.LoginSP;
import com.ubt.loginmodule.LoginHttpEntity;
import com.ubt.loginmodule.LoginUtil;
import com.ubt.loginmodule.requestModel.GetCodeRequest;
import com.ubt.loginmodule.requestModel.ResetPasswordRequest;
import com.ubt.loginmodule.requestModel.ValidateCodeRequest;
import com.vise.log.ViseLog;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import org.json.JSONException;
import org.json.JSONObject;

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
        GetCodeRequest getCodeRequest = new GetCodeRequest();
        getCodeRequest.setAppId(LoginSP.APPID);
        getCodeRequest.setEmail(email);
        getCodeRequest.setEmailType(2);
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
                            mView.requestSecurityCodeSuccess();
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        //请求失败，errCode为错误码，errMsg为错误描述
                        ViseLog.e("GET_CODE onFail:" + errCode + "-errMsg:" +  errMsg);
                        try {
                            JSONObject jsonObject = new JSONObject(errMsg);
                            String msg = jsonObject.getString("message");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(mView != null){
                            mView.requestSecurityCodeFailed();
                        }


                    }
                });



    }

    @Override
    public void requestVerifyAccount(final String email, String code) {
        ValidateCodeRequest request = new ValidateCodeRequest();
        request.setAccount(email);
        request.setCaptcha(code);
        request.setAccountType(1);

       /* Map<String,Object> map = new HashMap<String, Object>();
        map.put("account", email);
        map.put("captcha", code);
        map.put("accountType", 1);*/

        ViseHttp.GET(LoginHttpEntity.VALIDATE_CODE).baseUrl(LoginHttpEntity.BASE_LOGIN_URL)
                .addParam("account", email)
                .addParam("captcha", code)
                .addParam("accountType", "1").request(new ACallback<String>() {
            @Override
            public void onSuccess(String o) {
              ViseLog.d("VALIDATE_CODE onSuccess:" + o);
                try {
                    JSONObject jsonObject = new JSONObject(o);
                    int userId = jsonObject.getInt("userId");
                    String token = jsonObject.getString("token");
                    SPUtils.getInstance().put(Constant1E.SP_USER_ID, userId);
                    SPUtils.getInstance().put(Constant1E.SP_USER_TOKEN, token);
                    if(mView != null){
                        mView.requestVerifyAccountSuccess(email);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int i, String s) {
                ViseLog.d("VALIDATE_CODE onFail:" + s);
                if(mView != null){
                    mView.requestVerifyAccountFailed();
                }
            }
        });



    }

    @Override
    public void resetPassword(String email,String password) {

        String token  = SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN);
        int userId = SPUtils.getInstance().getInt(Constant1E.SP_USER_ID);
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setPassword(LoginUtil.encodeByMD5(password));
        resetPasswordRequest.setToken(token);
        resetPasswordRequest.setUserId(userId);

    ViseLog.d("resetPasswordRequest:" + GsonImpl.get().toJson(resetPasswordRequest));

    ViseHttp.PATCH(LoginHttpEntity.RESET).baseUrl(LoginHttpEntity.BASE_LOGIN_URL)
            .setJson(GsonImpl.get().toJson(resetPasswordRequest)).request(new ACallback<String>() {
        @Override
        public void onSuccess(String s) {
            ViseLog.d("RESET onSuccess:" + s);
            if(mView != null) {
                mView.resetPasswordSuccess();
            }
        }

        @Override
        public void onFail(int i, String s) {
            ViseLog.d("RESET onFail:" + s);
            ToastUtils.showShort("onFail:" + s);
            if(mView != null) {
                mView.resetPasswordFailed();
            }
        }
    });


    }
}
