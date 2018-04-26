package com.ubt.loginmodule.register;


import android.os.Bundle;
import android.widget.ImageView;

import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.loginmodule.R;
import com.ubt.loginmodule.R2;

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

public class RegisterActivity extends MVPBaseActivity<RegisterContract.View, RegisterPresenter> implements RegisterContract.View {

    @BindView(R2.id.iv_back)
    ImageView ivBack;
    Unbinder unbinder;

    @Override
    public int getContentViewId() {
        return R.layout.login_activity_register;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        CreateAccountFragment createAccountFragment = findFragment(CreateAccountFragment.class);
        if (createAccountFragment == null) {
            loadRootFragment(R.id.register_container, CreateAccountFragment.newInstance());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R2.id.iv_back)
    public void onClick() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1){
            pop();
        }else{
            finish();
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
