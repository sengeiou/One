package com.ubt.loginmodule.findPassword;

import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.baselib.utils.http1E.OkHttpClientUtils;
import com.ubt.loginmodule.LoginConstant.LoginSP;
import com.ubt.loginmodule.LoginHttpEntity;
import com.ubt.loginmodule.requestModel.GetCodeRequest;
import com.ubt.loginmodule.requestModel.ValidateCodeRequest;
import com.vise.log.ViseLog;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

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
                    long userId = jsonObject.getLong("userId");
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

       /* ViseHttp.BASE(new PostRequest(LoginHttpEntity.VALIDATE_CODE)
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
                });*/


    }

    @Override
    public void resetPassword(String email,String password) {

        String token  = SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN);
        long userId = SPUtils.getInstance().getLong(Constant1E.SP_USER_ID);

    /*    Map<String, String> params = new HashMap<String, String>();
        params.put("password", password);
        params.put("token", token);
        params.put("userId", ""+userId);*/
    String params = "{"
            + "\"password\":" + "\"" + password + "\""
            + ",\n\"token\":" + token
            + ",\n\"userId\":" + userId
            + "}";

        OkHttpClientUtils.getJsonByPatchRequest(LoginHttpEntity.BASE_LOGIN_URL+LoginHttpEntity.RESET, params, 22)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ViseLog.d("RESET onError:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ViseLog.d("RESET onResponse:" + response);
                    }
                });

       /* ViseHttp.PATCH(LoginHttpEntity.RESET).baseUrl(LoginHttpEntity.BASE_LOGIN_URL).addParams(params)
                .request(new ACallback<String>() {
                    @Override
                    public void onSuccess(String o) {
                        ViseLog.d("RESET onSuccess:" + o);
                        if(mView != null) {
                            mView.resetPasswordSuccess();
                        }
                    }

                    @Override
                    public void onFail(int i, String s) {
                        ViseLog.d("RESET onFail:" + s);
                        if(mView != null) {
                            mView.resetPasswordFailed();
                        }
                    }
                });

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
*/
    }
}
