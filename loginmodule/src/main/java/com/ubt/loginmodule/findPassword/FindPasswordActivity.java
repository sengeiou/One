package com.ubt.loginmodule.findPassword;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.loginmodule.R;

import butterknife.ButterKnife;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class FindPasswordActivity extends MVPBaseActivity<FindPasswordContract.View, FindPasswordPresenter> implements FindPasswordContract.View {

    @Override
    public int getContentViewId() {
        return R.layout.login_activity_findpassword;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        FindPasswordFragment findPasswordFragment = findFragment(FindPasswordFragment.class);
        if (findPasswordFragment == null) {
            loadRootFragment(R.id.find_container, FindPasswordFragment.newInstance());
        }
    }

    @Override
    public void requestSecurityCodeSuccess() {
    }

    @Override
    public void requestSecurityCodeFailed() {

    }

    @Override
    public void requestVerifyAccountSuccess() {

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
