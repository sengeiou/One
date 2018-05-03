package com.ubt.loginmodule.findPassword;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.inputmethod.InputMethodManager;

import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.loginmodule.R;
import com.ubt.loginmodule.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */

public class FindPasswordActivity extends MVPBaseActivity<FindPasswordContract.View, FindPasswordPresenter> implements FindPasswordContract.View {

    Unbinder unbinder;
    @BindView(R2.id.find_container)
    ConstraintLayout constraintLayout;

    @Override
    public int getContentViewId() {
        return R.layout.login_activity_findpassword;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        FindPasswordFragment findPasswordFragment = findFragment(FindPasswordFragment.class);
        if (findPasswordFragment == null) {
            loadRootFragment(R.id.find_container, FindPasswordFragment.newInstance());
        }
    }

    @OnClick(R2.id.find_container)
    public void onClick() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive() ){
            imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void requestSecurityCodeSuccess() {
    }

    @Override
    public void requestSecurityCodeFailed() {

    }

    @Override
    public void requestVerifyAccountSuccess(String email) {

    }

    @Override
    public void requestVerifyAccountFailed() {

    }

    @Override
    public void resetPasswordSuccess() {

    }

    @Override
    public void resetPasswordFailed() {

    }
}
