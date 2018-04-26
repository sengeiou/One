package com.ubt.loginmodule.login;


import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.app.abby.tsnackbar.TSnackbar;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.loginmodule.LoginUtil;
import com.ubt.loginmodule.R;
import com.ubt.loginmodule.R2;
import com.ubt.loginmodule.TextWatcherUtil;
import com.ubt.loginmodule.findPassword.FindPasswordActivity;
import com.ubt.loginmodule.register.RegisterActivity;
import com.vise.log.ViseLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 * @author
 */

@Route(path = ModuleUtils.Login_Module)
public class LoginActivity extends MVPBaseActivity <LoginContract.View, LoginPresenter>implements LoginContract.View {


    @BindView(R2.id.tv_register)
    TextView tvRegister;
    @BindView(R2.id.edt_email)
    EditText edtAccount;
    @BindView(R2.id.iv_clear_account)
    ImageView ivClearAccount;
    @BindView(R2.id.edt_password)
    EditText edtPassword;
    @BindView(R2.id.iv_clear_password)
    ImageView ivClearPassword;
    @BindView(R2.id.view_div)
    View viewDiv;
    @BindView(R2.id.tv_forgot_password)
    TextView tvForgotPassword;
    @BindView(R2.id.btn_login)
    Button btnLogin;
    @BindView(R2.id.iv_show_password)
    ImageView ivShowPassword;


    private boolean showPassword = false;

    Unbinder unbinder;


    @Override
    public int getContentViewId() {
        return R.layout.login_activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViseLog.d( "LoginActivity onCreate");
        unbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
       edtAccount.addTextChangedListener(new TextWatcherUtil(edtAccount, new TextWatcherUtil.TextWatcherListener() {

            @Override
            public void hasText() {
                ivClearAccount.setVisibility(View.VISIBLE);
                if(edtPassword.getText().length()>0){
                    btnLogin.setEnabled(true);
                }
            }

            @Override
            public void noText() {
                ivClearAccount.setVisibility(View.INVISIBLE);
                btnLogin.setEnabled(false);
            }
        }));

        edtPassword.addTextChangedListener(new TextWatcherUtil(edtPassword, new TextWatcherUtil.TextWatcherListener() {
            @Override
            public void hasText() {
                ivClearPassword.setVisibility(View.VISIBLE);
                viewDiv.setVisibility(View.VISIBLE);
                if(edtAccount.getText().length()>0){
                    btnLogin.setEnabled(true);
                }
            }

            @Override
            public void noText() {
                ivClearPassword.setVisibility(View.INVISIBLE);
                viewDiv.setVisibility(View.INVISIBLE);
                btnLogin.setEnabled(false);
            }
        }));
    }

    @OnClick({R2.id.tv_register, R2.id.btn_login, R2.id.tv_forgot_password, R2.id.iv_clear_account, R2.id.iv_clear_password, R2.id.iv_show_password})
    public void onClickView(View view){
        int i = view.getId();
        if (i == R.id.tv_register) {
            startActivity(new Intent(this, RegisterActivity.class));

        } else if (i == R.id.btn_login) {
            login();

        } else if (i == R.id.tv_forgot_password) {
            startActivity(new Intent(this, FindPasswordActivity.class));

        } else if (i == R.id.iv_clear_account) {
            edtAccount.setText("");

        } else if (i == R.id.iv_clear_password) {
            edtPassword.setText("");

        } else if (i == R.id.iv_show_password) {
            if (showPassword) {
                //if show hidden
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                edtPassword.setSelection(edtPassword.getText().length());
                ivShowPassword.setImageResource(R.drawable.ic_password_disshow);
                showPassword = false;
            } else {
                // if hidden show
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                edtPassword.setSelection(edtPassword.getText().length());
                ivShowPassword.setImageResource(R.drawable.ic_password_show);
                showPassword = true;
            }

        } else {
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void login(){
        ViseLog.d("login in");
        String account = edtAccount.getText().toString();
        String password = edtPassword.getText().toString();

        if(!LoginUtil.isEmail(account)){
            TSnackbar.make(getWindow().getDecorView(),R.string.login_wrong_password,TSnackbar.LENGTH_LONG)
                    .setBackgroundColor(getResources().getColor(R.color.login_bg_red_color))
                    .setMessageGravity(Gravity.CENTER)
                    .setMessageTextColor(getResources().getColor(R.color.white))
                    .show();
            return;
        }

        mPresenter.loginUseEmail(account, password);

    }

    @Override
    public void loginSuccess() {
        ToastUtils.showShort(getTextById(R.string.login_login_success));
        ARouter.getInstance().build(ModuleUtils.Main_MainActivity).navigation();
        this.finish();
    }

    @Override
    public void loginFailed() {
        ToastUtils.showShort(getTextById(R.string.login_login_failed));
    }
}



