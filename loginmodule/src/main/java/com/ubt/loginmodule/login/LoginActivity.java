package com.ubt.loginmodule.login;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.loginmodule.LoginUtil;
import com.ubt.loginmodule.R;
import com.vise.log.ViseLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 * @author wmma
 */

@Route(path = ModuleUtils.Login_Module)
public class LoginActivity extends MVPBaseActivity <LoginContract.View, LoginPresenter>implements LoginContract.View {

    @BindView(R.id.mainLayout)
    ConstraintLayout layout;

    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.edt_email)
    EditText edtAccount;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_forgot_password)
    Button btnForgotPassword;


    @Override
    public int getContentViewId() {
        return R.layout.login_activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViseLog.d( "LoginActivity onCreate");
        ButterKnife.bind(this);

    }

    @OnClick({R.id.tv_register, R.id.btn_login, R.id.btn_forgot_password})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.tv_register:
                login();
                break;
            case R.id.btn_login:
                break;
            case R.id.btn_forgot_password:
                break;
            default:
                break;
        }

    }

    private void login(){
        String account = edtAccount.getText().toString();
        String password = edtPassword.getText().toString();
        if(TextUtils.isEmpty(account)){
            Snackbar.make(layout, R.string.login_no_account, Snackbar.LENGTH_SHORT).show();
//            ToastUtils.showShort(getTextById(R.string.login_no_account));
            return;
        }else{
            if(!LoginUtil.isEmail(account)){
                ToastUtils.showShort(getTextById(R.string.login_error_email));
                return;
            }
        }

        if(TextUtils.isEmpty(password)){
            ToastUtils.showShort(getTextById(R.string.login_no_password));
            return;
        }

        mPresenter.loginUseEmail(account, password);

    }





    @Override
    public void loginSuccess() {
        ToastUtils.showShort(getTextById(R.string.login_login_failed));
    }

    @Override
    public void loginFailed() {
        ToastUtils.showShort(getTextById(R.string.login_login_failed));
    }
}



