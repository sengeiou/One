package com.ubt.loginmodule.register;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.loginmodule.R;
import com.ubt.loginmodule.R2;
import com.vise.log.ViseLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 *  @author wmma
 */
@Route(path = ModuleUtils.Login_Register)
public class RegisterActivity extends MVPBaseActivity<RegisterContract.View, RegisterPresenter> implements RegisterContract.View {

    @BindView(R2.id.iv_back)
    ImageView ivBack;
    @BindView(R2.id.login_tv_skip)
    TextView tvSkip;
    @BindView(R2.id.cl_register_root)
    ConstraintLayout cl_register_root;
    Unbinder unbinder;
    boolean emptyNickName = false;

    @Override
    public int getContentViewId() {
        return R.layout.login_activity_register;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        emptyNickName = getIntent().getBooleanExtra(Constant1E.EMPTY_NICK_NAME, false);
        if(emptyNickName){
            CreateUserNameFragment createUserNameFragment = findFragment(CreateUserNameFragment.class);
            if(createUserNameFragment == null){
                loadRootFragment(R.id.register_container, CreateUserNameFragment.newInstance());
            }
        }else{
            CreateAccountFragment createAccountFragment = findFragment(CreateAccountFragment.class);
            if (createAccountFragment == null) {
                loadRootFragment(R.id.register_container, CreateAccountFragment.newInstance());
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        ViseLog.d("onResume :" + getTopFragment());
    }

    public void setTvSkipVisible(boolean visible){
        if(visible){
            tvSkip.setVisibility(View.INVISIBLE);
        }else{
            tvSkip.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        SPUtils.getInstance().remove(Constant1E.SP_USER_INFO);
    }

    @OnClick({R2.id.iv_back, R2.id.login_tv_skip, R2.id.cl_register_root})
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.iv_back){
            if(getSupportFragmentManager().getBackStackEntryCount() > 1){
                pop();
            }else{
                finish();
            }
        }else if(id == R.id.login_tv_skip){
            CreateUserAgeFragment createUserAgeFragment = findFragment(CreateUserAgeFragment.class);
            if (createUserAgeFragment == null) {
                loadRootFragment(R.id.register_container, CreateUserAgeFragment.newInstance());
            }
        }else if(id == R.id.cl_register_root){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm.isActive() ){
                imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    }

    @Override
    public void sendSuccess() {
    }

    @Override
    public void sendFailed() {
    }

    @Override
    public void signUpSuccess() {
    }

    @Override
    public void signUpFailed() {
    }

    @Override
    public void setYearData(List<String> listYear) {

    }

    @Override
    public void setMonthData(List<String> listMonth) {

    }

    @Override
    public void setDayData(List<String> listDay) {

    }

    @Override
    public void registerFinish() {
    }

    @Override
    public void updateUserInfoSuccess(boolean success) {
    }

    @Override
    public void sendSecurityCodeSuccess(boolean success) {
    }


}
