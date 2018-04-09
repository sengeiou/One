package com.ubt.loginmodule.loginauth;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.model1E.BaseResponseModel;
import com.ubt.baselib.model1E.UserModel;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.baselib.utils.http1E.OkHttpClientUtils;
import com.ubt.globaldialog.customDialog.loading.LoadingDialog;
import com.ubt.loginmodule.LoginHttpEntity;
import com.ubt.loginmodule.R;
import com.ubt.loginmodule.R2;
import com.ubt.loginmodule.requestModel.GetCodeRequest;
import com.ubt.loginmodule.useredit.UserEditActivity;
import com.vise.log.ViseLog;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class LoginAuthActivity extends MVPBaseActivity<LoginAuthContract.View, LoginAuthPresenter> implements LoginAuthContract.View {

    @BindView(R2.id.edt_tel)
    EditText edtTel;
    @BindView(R2.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R2.id.edt_verify_code)
    EditText edtVerifyCode;
    @BindView(R2.id.tv_countdown)
    TextView tvCountdown;
    @BindView(R2.id.btn_confirm)
    Button btnConfirm;

    RequestCountDown requestCountDown;
    private static final long REQUEST_TIME = 61 * 1000;

    private String token;
    private String userId;
    private String nickName;
    private String userImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initControlListener();
        requestCountDown = new RequestCountDown(REQUEST_TIME, 1000);
        token = SPUtils.getInstance().getString(Constant1E.SP_LOGIN_TOKEN);
        userId = SPUtils.getInstance().getString(Constant1E.SP_USER_ID);
        nickName = SPUtils.getInstance().getString(Constant1E.SP_USER_NICKNAME);
        userImage = SPUtils.getInstance().getString(Constant1E.SP_USER_IMAGE);
        ViseLog.d("token:" + token + "--userId:" + userId + "--nickName:" + nickName + "--userImage:" + userImage);
    }


    protected void initControlListener() {

        edtTel.addTextChangedListener(new TextWatcher() {


            String phoneNum = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                phoneNum = charSequence.toString();


            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (CheckPhoneNumberUtil.isChinaPhoneLegal(phoneNum)) {
                    setGetCodeTextEnable(true);
                } else {
                    setGetCodeTextEnable(false);
                }

            }
        });

        edtVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    btnConfirm.setEnabled(true);
                } else {
                    btnConfirm.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCountDown.start();
                setGetCodeTextEnable(false);

                GetCodeRequest getCodeRequest = new GetCodeRequest();
                getCodeRequest.setPhone(edtTel.getText().toString());
                OkHttpClientUtils.getJsonByPostRequest(LoginHttpEntity.REQUEST_SMS_CODE, getCodeRequest, 0).execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ViseLog.e("REQUEST_SMS_CODE Exception:" + e.getMessage());
                        ToastUtils.showShort("获取验证码失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ViseLog.e("REQUEST_SMS_CODE response:" + response);
                    }
                });

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCountDown.cancel();
                LoadingDialog.show(LoginAuthActivity.this);
                String params = "{"
                        + "\"token\":" + "\"" + token + "\""
                        + ",\n\"userId\":" + "\"" + userId + "\""
                        + ",\n\"phone\":" + "\"" + edtTel.getText().toString() + "\""
                        + ",\n\"nickName\":" + "\"" + nickName + "\""
                        + ",\n\"headPic\":" + "\"" + userImage + "\""
                        + ",\n\"code\":" + "\"" + edtVerifyCode.getText().toString() + "\""
                        + "}";

                OkHttpClientUtils.getJsonByPostRequest(LoginHttpEntity.BIND_ACCOUNT, params, 0).execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ViseLog.e("BIND_ACCOUNT Exception:" + e.getMessage());
                        LoadingDialog.dismiss(LoginAuthActivity.this);
                        ToastUtils.showShort("验证码错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ViseLog.d("BIND_ACCOUNT response:" + response);
                        LoadingDialog.dismiss(LoginAuthActivity.this);
                        BaseResponseModel baseResponseModel = GsonImpl.get().toObject(response, BaseResponseModel.class);
                        if (baseResponseModel.status) {
                            ViseLog.d("model==" + baseResponseModel.models);
                            UserModel userModel = (UserModel) SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
                            userModel.setPhone(edtTel.getText().toString());
                            ViseLog.d("userModel:" + userModel);
                            SPUtils.getInstance().saveObject(Constant1E.SP_USER_INFO, userModel);
                            Intent intent = new Intent();
                            intent.setClass(LoginAuthActivity.this, UserEditActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtils.showShort("验证码错误");
                        }

                    }
                });


            }
        });

    }


    @Override
    public int getContentViewId() {
        return R.layout.activity_login_auth;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    private void setGetCodeTextEnable(boolean enable) {

        tvGetCode.setEnabled(enable);
        if (enable == true) {
            tvGetCode.setTextColor(getResources().getColor(R.color.tv_login_blue_color));
        } else {
            tvGetCode.setTextColor(getResources().getColor(R.color.login_line_color));
        }


    }


    /**
     * 类名
     *
     * @author作者<br/> 实现的主要功能。
     * created at
     * 修改者，修改日期，修改内容。
     */


    class RequestCountDown extends CountDownTimer {

        public RequestCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

            tvCountdown.setText("60 s");
            setGetCodeTextEnable(true);


        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvCountdown.setText("" + (millisUntilFinished / 1000) + " s");
        }
    }


}
