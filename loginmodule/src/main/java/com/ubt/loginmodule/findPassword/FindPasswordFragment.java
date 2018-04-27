package com.ubt.loginmodule.findPassword;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.abby.tsnackbar.TSnackbar;
import com.ubt.baselib.mvp.MVPBaseFragment;
import com.ubt.loginmodule.LoginConstant.LoginSP;
import com.ubt.loginmodule.LoginUtil;
import com.ubt.loginmodule.R;
import com.ubt.loginmodule.R2;
import com.ubt.loginmodule.TextWatcherUtil;
import com.vise.log.ViseLog;

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


public class FindPasswordFragment extends MVPBaseFragment<FindPasswordContract.View, FindPasswordPresenter> implements FindPasswordContract.View {

    Unbinder unbinder;
    @BindView(R2.id.iv_back)
    ImageView ivBack;
    @BindView(R2.id.tv_next)
    TextView tvNext;
    @BindView(R2.id.edt_email)
    EditText edtEmail;
    @BindView(R2.id.edt_security_code)
    EditText edtSecurityCode;
    @BindView(R2.id.btn_send_security)
    Button btnSendSecurity;
    @BindView(R2.id.iv_clear_account)
    ImageView ivClearAccount;
    @BindView(R2.id.tv_valid_email)
    TextView tvValidEmail;
    RequestCountDown requestCountDown;
    private static final long REQUEST_TIME = 181 * 1000;

    public static FindPasswordFragment newInstance() {
        return new FindPasswordFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_forget_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        requestCountDown = new RequestCountDown(REQUEST_TIME, 1000);
        initView();
        return view;
    }

    private void initView() {
        edtEmail.addTextChangedListener(new TextWatcherUtil(edtEmail, new TextWatcherUtil.TextWatcherListener() {
            @Override
            public void hasText() {
                ivClearAccount.setVisibility(View.VISIBLE);
                btnSendSecurity.setEnabled(true);
            }

            @Override
            public void noText() {
                ivClearAccount.setVisibility(View.INVISIBLE);
                btnSendSecurity.setEnabled(false);
            }
        }));

        edtSecurityCode.addTextChangedListener(new TextWatcherUtil(edtSecurityCode, new TextWatcherUtil.TextWatcherListener() {
            @Override
            public void hasText() {
                tvNext.setEnabled(true);
                tvNext.setTextColor(getResources().getColor(R.color.login_text_blue_color));
            }

            @Override
            public void noText() {
                tvNext.setEnabled(false);
                tvNext.setTextColor(getResources().getColor(R.color.login_text_hint_color));
            }
        }));
    }

    @OnClick({R2.id.tv_next, R2.id.btn_send_security, R2.id.iv_back, R2.id.iv_clear_account})
    public void onClickView(View view) {
        int i = view.getId();
        if (i == R.id.tv_next) {
            String email = edtEmail.getText().toString();
            String code = edtSecurityCode.getText().toString();
            mPresenter.requestVerifyAccount(email, code);

        } else if (i == R.id.btn_send_security) {
            String account = edtEmail.getText().toString();
            if (LoginUtil.isEmail(account)) {
                tvValidEmail.setVisibility(View.INVISIBLE);
                mPresenter.requestSecurityCode(account);
                requestCountDown.start();
                btnSendSecurity.setEnabled(false);
            } else {
                tvValidEmail.setVisibility(View.VISIBLE);
            }

        } else if (i == R.id.iv_back) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
                pop();
            } else {
                getActivity().finish();
            }

        } else if (i == R.id.iv_clear_account) {
            edtEmail.setText("");

        } else {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if(requestCountDown != null){
            requestCountDown.cancel();
        }
    }

    @Override
    public void requestSecurityCodeSuccess() {
        ViseLog.d("requestSecurityCodeSuccess TsnackBar");
        TSnackbar.make(getActivity().getWindow().getDecorView(),R.string.login_sent_code_prompt,TSnackbar.LENGTH_LONG)
                .setBackgroundColor(getResources().getColor(R.color.login_bg_green_color))
                .setMessageGravity(Gravity.CENTER)
                .setMessageTextColor(getResources().getColor(R.color.white))
                .show();
    }

    @Override
    public void requestSecurityCodeFailed() {
        TSnackbar.make(getActivity().getWindow().getDecorView(),R.string.login_sent_code_fail_prompt,TSnackbar.LENGTH_LONG)
                .setBackgroundColor(getResources().getColor(R.color.login_bg_red_color))
                .setMessageGravity(Gravity.CENTER)
                .setMessageTextColor(getResources().getColor(R.color.white))
                .show();
    }

    @Override
    public void requestVerifyAccountSuccess(String email) {
        ResetPasswordFragment resetPasswordFragment = ResetPasswordFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(LoginSP.SP_EMAIL, email);
        ViseLog.d("fragment:" + resetPasswordFragment);
        resetPasswordFragment.putNewBundle(bundle);
        start(resetPasswordFragment);
    }

    @Override
    public void requestVerifyAccountFailed() {
        TSnackbar.make(getActivity().getWindow().getDecorView(),R.string.login_code_not_correct,TSnackbar.LENGTH_LONG)
                .setBackgroundColor(getResources().getColor(R.color.login_bg_red_color))
                .setMessageGravity(Gravity.CENTER)
                .setMessageTextColor(getResources().getColor(R.color.white))
                .show();
    }

    @Override
    public void resetPasswordSuccess() {

    }

    @Override
    public void resetPasswordFailed() {

    }

    class RequestCountDown extends CountDownTimer {

        public RequestCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

            btnSendSecurity.setText(getString(R.string.login_resend_security_code));
            btnSendSecurity.setEnabled(true);


        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnSendSecurity.setText("" + (millisUntilFinished / 1000) + " s");
        }
    }

}
