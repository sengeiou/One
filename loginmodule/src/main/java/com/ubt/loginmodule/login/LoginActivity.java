package com.ubt.loginmodule.login;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.app.abby.tsnackbar.TSnackbar;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.model1E.UserInfoModel;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.loginmodule.LoginUtil;
import com.ubt.loginmodule.R;
import com.ubt.loginmodule.R2;
import com.ubt.loginmodule.TextWatcherUtil;
import com.ubt.loginmodule.findPassword.FindPasswordActivity;
import com.ubt.loginmodule.register.RegisterActivity;
import com.ubt.loginmodule.third.ITwitterLoginListener;
import com.ubt.loginmodule.third.MyTwitter;
import com.vise.log.ViseLog;

import java.util.Arrays;

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
public class LoginActivity extends MVPBaseActivity <LoginContract.View, LoginPresenter>implements LoginContract.View,ITwitterLoginListener {


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
    @BindView(R2.id.cl_root)
    ConstraintLayout constraintLayout;
    @BindView(R2.id.login_button)
    ImageView loginButton;
    @BindView(R2.id.tv_facebook)
    TextView tvFacebook;
    @BindView(R2.id.iv_twitter)
    ImageView ivTwitter;
    @BindView(R2.id.iv_ins)
    ImageView ivIns;


    private boolean showPassword = false;
    Unbinder unbinder;
    private NavigationCallback navigationCallback;

    private CallbackManager callbackManager;
    private static AccessToken accessToken;

    private int loginType = 0; //0,表示默认邮箱， 1，表示facebook，2，表示twitter ， 3，表示Instagram


    private static LoginActivity mInstance;

    @Override
    public int getContentViewId() {
        return R.layout.login_activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViseLog.d( "LoginActivity onCreate");
        mInstance = this;
        unbinder = ButterKnife.bind(this);
        initView();
        initNavigationListener();
        callbackManager = CallbackManager.Factory.create();
        initFacebook();
    }

    private void initFacebook() {

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginType = 1;
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                accessToken = loginResult.getAccessToken();
                                String userId = accessToken.getUserId();
                                String token = accessToken.getToken();
//                                tvFacebook.setText("userId:" + userId + "token:" + token);
                                BaseLoadingDialog.show(LoginActivity.this);
                                mPresenter.loginThird(token, userId, "Facebook");
                            }

                            @Override
                            public void onCancel() {
                                ViseLog.e("Login onCancel");
                                ToastUtils.showShort("login onCancel:" );
                            }

                            @Override
                            public void onError(FacebookException e) {
                                ViseLog.e("Login onError:" + e.getMessage());
                                ToastUtils.showShort("login onError:" );
                            }
                        });
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("public_profile"));
            }
        });

        ivTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginType = 2;
                loginWithTwitter();

            }
        });


    }

    private void loginWithTwitter() {
        MyTwitter.doLogin(LoginActivity.this, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(loginType == 1){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }else if(loginType == 2){
            MyTwitter.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void initNavigationListener() {
        navigationCallback = new NavigationCallback() {
            @Override
            public void onFound(Postcard postcard) {

            }

            @Override
            public void onLost(Postcard postcard) {

            }

            @Override
            public void onArrival(Postcard postcard) {
                ViseLog.i("postcard="+postcard.toString());
                LoginActivity.this.finish();
            }

            @Override
            public void onInterrupt(Postcard postcard) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViseLog.d("执行时间");
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

    @OnClick({R2.id.tv_register, R2.id.btn_login, R2.id.tv_forgot_password, R2.id.iv_clear_account, R2.id.iv_clear_password, R2.id.iv_show_password, R2.id.cl_root, R2.id.iv_ins})
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

        } else if(i == R.id.cl_root){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm.isActive() ){
                imm.hideSoftInputFromWindow(/*getCurrentFocus().getApplicationWindowToken()*/view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }else if(i == R.id.iv_ins){
            ToastUtils.showShort("coming soon");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mInstance = null;
    }

    public static void finishSelf(){
        if (mInstance!=null&&!mInstance.isFinishing()){
            mInstance.finish();
        }
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
        BaseLoadingDialog.show(this);
        mPresenter.loginUseEmail(account, password);

    }

    @Override
    public void loginSuccess() {
        BaseLoadingDialog.dismiss(this);
        UserInfoModel userInfoModel = (UserInfoModel) SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
        if(userInfoModel == null){
            ToastUtils.showShort(SkinManager.getInstance().getTextById(R.string.login_wrong_password));
            return;
        }
        boolean noFirst = SPUtils.getInstance().getBoolean(Constant1E.IS_FIRST_ENTER_GREET);
        if(noFirst){
            ARouter.getInstance().build(ModuleUtils.Main_MainActivity).navigation(LoginActivity.this, navigationCallback);
        }else{
            ARouter.getInstance().build(ModuleUtils.Bluetooh_FirstGreetActivity).navigation(LoginActivity.this, navigationCallback);
        }

    }

    @Override
    public void loginFailed() {
        BaseLoadingDialog.dismiss(this);
    }

    @Override
    public void loginThirdFinish(boolean success) {
        BaseLoadingDialog.dismiss(this);
    }

    @Override
    public void test(String text) {
        tvFacebook.setText("");
        tvFacebook.setText(text);
    }

    @Override
    public void UpdateUserInfo() {
        ViseLog.d("UpdateUserInfo");

      mPresenter.getUserInfo();
    }

    @Override
    public void UpdateUserInfoFinish() {
        BaseLoadingDialog.dismiss(this);
        SPUtils.getInstance().put(Constant1E.SP_LOGIN, true);
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.putExtra(Constant1E.EMPTY_NICK_NAME, true);
        startActivity(intent);
    }

    @Override
    public void getUserInfoFailed() {
        BaseLoadingDialog.dismiss(this);
    }

    @Override
    public void OnLoginComplete(twitter4j.auth.AccessToken accessToken) {
        String token  = accessToken.getToken();
        String userId = "" + accessToken.getUserId();
        BaseLoadingDialog.show(this);
        mPresenter.loginThird(token, userId, "Twitter");

    }
}

