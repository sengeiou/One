package com.ubt.loginmodule.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.loginmodule.LoginUtil;
import com.ubt.loginmodule.R;
import com.ubt.loginmodule.TextWatcherUtil;

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
    @BindView(R.id.edt_email) EditText edtEmail;
    @BindView(R.id.edt_password) EditText edtPassword;
    @BindView(R.id.edt_security_code) EditText edtSecurityCode;
    @BindView(R.id.iv_clear_account) ImageView ivClearAccount;
    @BindView(R.id.iv_clear_password) ImageView ivClearPassword;
    @BindView(R.id.iv_show_password) ImageView ivShowPassword;
    @BindView(R.id.btn_send_security) Button btnSendSecurityCode;
    @BindView(R.id.btn_sign_up) Button btnSignUp;
    @BindView(R.id.iv_privacy) ImageView ivPrivacy;
    @BindView(R.id.tv_service_privacy) TextView tvServicePrivacy;
    @BindView(R.id.view_div) View viewDiv;

    private boolean showPassword = false;
    private boolean select = true;

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
        initView();
        return view;
    }

    private void initView(){
        edtEmail.addTextChangedListener(new TextWatcherUtil(edtEmail, new TextWatcherUtil.TextWatcherListener() {

            @Override
            public void hasText() {
                ivClearAccount.setVisibility(View.VISIBLE);
                btnSendSecurityCode.setEnabled(true);
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

    @OnClick({R.id.iv_clear_account, R.id.iv_clear_password, R.id.iv_show_password, R.id.btn_send_security,R.id.btn_sign_up,R.id.iv_privacy,R.id.tv_service_privacy})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.iv_clear_account:
                edtEmail.setText("");
                break;
            case R.id.iv_clear_password:
                edtPassword.setText("");
                break;
            case R.id.iv_show_password:
                if(showPassword){
                    //if show hidden
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edtPassword.setSelection(edtPassword.getText().length());
                    ivShowPassword.setImageResource(R.drawable.ic_password_disshow);
                    showPassword = false;
                }else{
                    // if hidden show
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edtPassword.setSelection(edtPassword.getText().length());
                    ivShowPassword.setImageResource(R.drawable.ic_password_show);
                    showPassword = true;
                }
                break;
            case R.id.btn_send_security:
                //TODO send code to service
                if(LoginUtil.isEmail(edtEmail.getText().toString())){
                    String email = edtEmail.getText().toString();
                    mPresenter.sendSecurityCode(email);
                }else{
                    TSnackbar.make(getActivity().getWindow().getDecorView(),R.string.login_input_wrong_email_warning,TSnackbar.LENGTH_LONG)
                            .setBackgroundColor(getResources().getColor(R.color.login_bg_red_color))
                            .setMessageGravity(Gravity.CENTER)
                            .setMessageTextColor(getResources().getColor(R.color.white))
                            .show();
                }
                break;
            case R.id.btn_sign_up:
                //TODO sign up
                if(!select){
                    TSnackbar.make(getActivity().getWindow().getDecorView(),R.string.login_select_privacy,TSnackbar.LENGTH_LONG)
                            .setBackgroundColor(getResources().getColor(R.color.login_bg_red_color))
                            .setMessageGravity(Gravity.CENTER)
                            .setMessageTextColor(getResources().getColor(R.color.white))
                            .show();
                    return;
                }


                if(LoginUtil.isEmail(edtEmail.getText().toString())){
                    String email = edtEmail.getText().toString();
                    String password = edtPassword.getText().toString();
                    String code = edtSecurityCode.getText().toString();
                    mPresenter.signUp(email, password, code);
                }else{
                    TSnackbar.make(getActivity().getWindow().getDecorView(),R.string.login_input_wrong_email_warning,TSnackbar.LENGTH_LONG)
                            .setBackgroundColor(getResources().getColor(R.color.login_bg_red_color))
                            .setMessageGravity(Gravity.CENTER)
                            .setMessageTextColor(getResources().getColor(R.color.white))
                            .show();
                }
                break;
            case R.id.iv_privacy:
                if(select){
                    select = false;
                    ivPrivacy.setImageResource(R.drawable.ic_policy_row_disselect);
                }else{
                    select = true;
                    ivPrivacy.setImageResource(R.drawable.ic_policy_row_selected);
                }
                break;
            case R.id.tv_service_privacy:
                //TODO url to h5
                ToastUtils.showShort("Privacy Policy!");
                break;
            default:
                break;

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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
        TSnackbar.make(getActivity().getWindow().getDecorView(),R.string.login_sent_code_fail_prompt,TSnackbar.LENGTH_LONG)
                .setBackgroundColor(getResources().getColor(R.color.login_bg_red_color))
                .setMessageGravity(Gravity.CENTER)
                .setMessageTextColor(getResources().getColor(R.color.white))
                .show();
    }

    @Override
    public void signUpSuccess() {
        start(CreateAccountSuccessFragment.newInstance());
    }

    @Override
    public void signUpFailed() {
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
}
