package com.ubt.loginmodule.register;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.abby.tsnackbar.TSnackbar;
import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.baselib.mvp.MVPBaseFragment;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.loginmodule.LoginUtil;
import com.ubt.loginmodule.R;
import com.ubt.loginmodule.R2;
import com.ubt.loginmodule.TextWatcherUtil;
import com.vise.log.ViseLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @className CreateAccountFragment
 * @date 2018/4/16
 * @author wmma
 */
public class CreateAccountFragment extends  MVPBaseFragment<RegisterContract.View, RegisterPresenter> implements RegisterContract.View {
    private Unbinder unbinder;
    @BindView(R2.id.edt_email) EditText edtEmail;
    @BindView(R2.id.edt_password) EditText edtPassword;
    @BindView(R2.id.edt_security_code) EditText edtSecurityCode;
    @BindView(R2.id.iv_clear_account) ImageView ivClearAccount;
    @BindView(R2.id.iv_clear_password) ImageView ivClearPassword;
    @BindView(R2.id.iv_show_password) ImageView ivShowPassword;
    @BindView(R2.id.btn_send_security) Button btnSendSecurityCode;
    @BindView(R2.id.btn_sign_up) Button btnSignUp;
    @BindView(R2.id.iv_privacy) ImageView ivPrivacy;
    @BindView(R2.id.tv_service_privacy) TextView tvServicePrivacy;
    @BindView(R2.id.view_div) View viewDiv;
    @BindView(R2.id.cl_create_account)
    ConstraintLayout cl_create_account;

    private boolean showPassword = false;
    private boolean select = false;
    RequestCountDown requestCountDown;
    private static final long REQUEST_TIME = 181 * 1000;
    private boolean countDownFinish = true;

    public static CreateAccountFragment newInstance(){
        return new CreateAccountFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_create_account,container, false);
        unbinder = ButterKnife.bind(this, view);
        ((RegisterActivity)getActivity()).setTvSkipVisible(false);
        requestCountDown = new RequestCountDown(REQUEST_TIME, 1000);
        initView();
        return view;
    }

    private void initView(){
        edtEmail.addTextChangedListener(new TextWatcherUtil(edtEmail, new TextWatcherUtil.TextWatcherListener() {

            @Override
            public void hasText() {
                ivClearAccount.setVisibility(View.VISIBLE);
                if(LoginUtil.isEmail(edtEmail.getText().toString()) && countDownFinish){
                    btnSendSecurityCode.setEnabled(true);
                }else{
                    btnSendSecurityCode.setEnabled(false);
                }
                if(edtPassword.getText().length()>0 && edtSecurityCode.getText().length()>0){
                    btnSignUp.setEnabled(true);
                }
            }

            @Override
            public void noText() {
                ivClearAccount.setVisibility(View.INVISIBLE);
                btnSendSecurityCode.setEnabled(false);
                btnSignUp.setEnabled(false);
            }
        }));

        edtPassword.addTextChangedListener(new TextWatcherUtil(edtPassword, new TextWatcherUtil.TextWatcherListener() {
            @Override
            public void hasText() {
                ivClearPassword.setVisibility(View.VISIBLE);
                viewDiv.setVisibility(View.VISIBLE);
                if(edtEmail.getText().length()>0 && edtSecurityCode.getText().length()>0){
                    btnSignUp.setEnabled(true);
                }
            }

            @Override
            public void noText() {
                ivClearPassword.setVisibility(View.INVISIBLE);
                viewDiv.setVisibility(View.INVISIBLE);
                btnSignUp.setEnabled(false);
            }
        }));

        edtSecurityCode.addTextChangedListener(new TextWatcherUtil(edtSecurityCode, new TextWatcherUtil.TextWatcherListener() {
            @Override
            public void hasText() {
                if(edtEmail.getText().length()>0 && edtPassword.getText().length()>0){
                    btnSignUp.setEnabled(true);
                }
            }

            @Override
            public void noText() {
                btnSignUp.setEnabled(false);
            }
        }));
    }

    @OnClick({R2.id.iv_clear_account, R2.id.iv_clear_password, R2.id.iv_show_password, R2.id.btn_send_security,R2.id.btn_sign_up,R2.id.iv_privacy,R2.id.tv_service_privacy,R2.id.cl_create_account})
    public void onClickView(View view){
        int i = view.getId();
        if (i == R.id.iv_clear_account) {
            edtEmail.setText("");

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

        } else if (i == R.id.btn_send_security) {//TODO send code to service
            if (LoginUtil.isEmail(edtEmail.getText().toString())) {
                String email = edtEmail.getText().toString();
                mPresenter.sendSecurityCode(email);
                requestCountDown.start();
                countDownFinish = false;
                btnSendSecurityCode.setEnabled(false);
            } else {
                TSnackbar.make(getActivity().getWindow().getDecorView(), R.string.login_input_wrong_email_warning, TSnackbar.LENGTH_LONG)
                        .setBackgroundColor(getResources().getColor(R.color.login_bg_red_color))
                        .setMessageGravity(Gravity.CENTER)
                        .setMessageTextColor(getResources().getColor(R.color.white))
                        .show();
            }

        } else if (i == R.id.btn_sign_up) {//TODO sign up
            if (!select) {
                TSnackbar.make(getActivity().getWindow().getDecorView(), R.string.login_select_privacy, TSnackbar.LENGTH_LONG)
                        .setBackgroundColor(getResources().getColor(R.color.login_bg_red_color))
                        .setMessageGravity(Gravity.CENTER)
                        .setMessageTextColor(getResources().getColor(R.color.white))
                        .show();
                return;
            }

            if(edtPassword.getText().length()<6){
                TSnackbar.make(getActivity().getWindow().getDecorView(), R.string.login_password_tip, TSnackbar.LENGTH_LONG)
                        .setBackgroundColor(getResources().getColor(R.color.login_bg_red_color))
                        .setMessageGravity(Gravity.CENTER)
                        .setMessageTextColor(getResources().getColor(R.color.white))
                        .show();
                return;
            }


            if (LoginUtil.isEmail(edtEmail.getText().toString())) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String code = edtSecurityCode.getText().toString();
                mPresenter.signUp(email, password, code);
                BaseLoadingDialog.show(getActivity());
            } else {
                TSnackbar.make(getActivity().getWindow().getDecorView(), R.string.login_input_wrong_email_warning, TSnackbar.LENGTH_LONG)
                        .setBackgroundColor(getResources().getColor(R.color.login_bg_red_color))
                        .setMessageGravity(Gravity.CENTER)
                        .setMessageTextColor(getResources().getColor(R.color.white))
                        .show();
            }

        } else if (i == R.id.iv_privacy) {
            if (select) {
                select = false;
                ivPrivacy.setImageResource(R.drawable.ic_policy_row_disselect);
            } else {
                select = true;
                ivPrivacy.setImageResource(R.drawable.ic_policy_row_selected);
            }

        } else if (i == R.id.tv_service_privacy) {//TODO url to h5
            ToastUtils.showShort("Privacy Policy!");

        } else if(i == R.id.cl_create_account){
            InputMethodManager imm = (InputMethodManager) (getActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
            if(imm.isActive() ){
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        countDownFinish = true;
        if(requestCountDown != null){
            requestCountDown.cancel();
        }
        BaseLoadingDialog.dismiss(getActivity());
    }

    @Override
    public void sendSuccess() {
        TSnackbar.make(getActivity().getWindow().getDecorView(),R.string.login_sent_code_prompt,TSnackbar.LENGTH_LONG)
                .setBackgroundColor(getResources().getColor(R.color.login_bg_green_color))
                .setMessageGravity(Gravity.CENTER)
                .setMessageTextColor(getResources().getColor(R.color.white))
                .show();
    }

    @Override
    public void sendFailed() {
        ViseLog.d(" sss sendFailed ");
        TSnackbar.make(getActivity().getWindow().getDecorView(),R.string.login_sent_code_fail_prompt,TSnackbar.LENGTH_LONG)
                .setBackgroundColor(getResources().getColor(R.color.login_bg_red_color))
                .setMessageGravity(Gravity.CENTER)
                .setMessageTextColor(getResources().getColor(R.color.white))
                .show();
        countDownFinish = true;
        if(requestCountDown != null){
            requestCountDown.cancel();
        }
        if(null != btnSendSecurityCode){
            btnSendSecurityCode.setText(getString(R.string.login_resend_security_code));
            btnSendSecurityCode.setEnabled(true);
        }
    }

    @Override
    public void signUpSuccess() {
        ViseLog.d("signUpSuccess");
        BaseLoadingDialog.dismiss(getActivity());
        start(CreateAccountSuccessFragment.newInstance());
    }

    @Override
    public void signUpFailed() {
        BaseLoadingDialog.dismiss(getActivity());
        TSnackbar.make(getActivity().getWindow().getDecorView(),R.string.login_signup_error_prompt,TSnackbar.LENGTH_LONG)
                .setBackgroundColor(getResources().getColor(R.color.login_bg_red_color))
                .setMessageGravity(Gravity.CENTER)
                .setMessageTextColor(getResources().getColor(R.color.white))
                .show();
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
        if(success) {
            TSnackbar.make(getActivity().getWindow().getDecorView(),R.string.login_sent_code_prompt,TSnackbar.LENGTH_LONG)
                    .setBackgroundColor(getResources().getColor(R.color.login_bg_green_color))
                    .setMessageGravity(Gravity.CENTER)
                    .setMessageTextColor(getResources().getColor(R.color.white))
                    .show();
        }else{
            TSnackbar.make(getActivity().getWindow().getDecorView(),R.string.login_sent_code_fail_prompt,TSnackbar.LENGTH_LONG)
                    .setBackgroundColor(getResources().getColor(R.color.login_bg_red_color))
                    .setMessageGravity(Gravity.CENTER)
                    .setMessageTextColor(getResources().getColor(R.color.white))
                    .show();
            countDownFinish = true;
            if(requestCountDown != null){
                requestCountDown.cancel();
            }
            if(null != btnSendSecurityCode){
                btnSendSecurityCode.setText(getString(R.string.login_resend_security_code));
                btnSendSecurityCode.setEnabled(true);
            }
        }
    }


    class RequestCountDown extends CountDownTimer {

        public RequestCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            countDownFinish = true;
            if(null != btnSendSecurityCode){
                btnSendSecurityCode.setText(getString(R.string.login_resend_security_code));
                btnSendSecurityCode.setEnabled(true);
            }



        }

        @Override
        public void onTick(long millisUntilFinished) {
            if(null != btnSendSecurityCode){
                btnSendSecurityCode.setText("" + (millisUntilFinished / 1000) + " s");
            }

        }
    }


}
