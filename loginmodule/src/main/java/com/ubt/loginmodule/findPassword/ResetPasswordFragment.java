package com.ubt.loginmodule.findPassword;

import android.content.Context;
import android.content.Intent;
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

import com.app.abby.tsnackbar.TSnackbar;
import com.ubt.baselib.mvp.MVPBaseFragment;
import com.ubt.loginmodule.LoginConstant.LoginSP;
import com.ubt.loginmodule.R;
import com.ubt.loginmodule.TextWatcherUtil;
import com.ubt.loginmodule.login.LoginActivity;

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


public class ResetPasswordFragment extends MVPBaseFragment<FindPasswordContract.View, FindPasswordPresenter> implements FindPasswordContract.View {

    Unbinder unbinder;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.iv_clear_password)
    ImageView ivClearPassword;
    @BindView(R.id.iv_show_password)
    ImageView ivShowPassword;
    @BindView(R.id.edt_password_again)
    EditText edtPasswordAgain;
    @BindView(R.id.iv_clear_password_again)
    ImageView ivClearPasswordAgain;
    @BindView(R.id.iv_show_password_again)
    ImageView ivShowPasswordAgain;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.view_div)
    View viewDiv;
    @BindView(R.id.view_div_again)
    View viewDivAgain;
    private boolean showPassword = false;
    private boolean showPasswordAgain = false;
    private String email;

    public static ResetPasswordFragment newInstance() {
        return  new ResetPasswordFragment();
    }

    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);
        email = args.getString(LoginSP.SP_EMAIL);
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
        View view = inflater.inflate(R.layout.login_fragment_reset_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        edtPassword.addTextChangedListener(new TextWatcherUtil(edtPassword, new TextWatcherUtil.TextWatcherListener() {
            @Override
            public void hasText() {
                ivClearPassword.setVisibility(View.VISIBLE);
                viewDiv.setVisibility(View.VISIBLE);
            }

            @Override
            public void noText() {
                ivClearPassword.setVisibility(View.INVISIBLE);
                viewDiv.setVisibility(View.INVISIBLE);
            }
        }));

        edtPasswordAgain.addTextChangedListener(new TextWatcherUtil(edtPasswordAgain, new TextWatcherUtil.TextWatcherListener() {
            @Override
            public void hasText() {
                ivClearPasswordAgain.setVisibility(View.VISIBLE);
                viewDivAgain.setVisibility(View.VISIBLE);
            }

            @Override
            public void noText() {
                ivClearPasswordAgain.setVisibility(View.INVISIBLE);
                viewDivAgain.setVisibility(View.INVISIBLE);
            }
        }));
    }

    @OnClick({R.id.iv_clear_password, R.id.iv_clear_password_again, R.id.iv_show_password, R.id.iv_show_password_again, R.id.btn_confirm,R.id.iv_back})
    public void onClickView(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                pop();
                break;
            case R.id.iv_clear_password:
                edtPassword.setText("");
                break;
            case R.id.iv_clear_password_again:
                edtPasswordAgain.setText("");
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
            case R.id.iv_show_password_again:
                if(showPasswordAgain){
                    //if show hidden
                    edtPasswordAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edtPasswordAgain.setSelection(edtPasswordAgain.getText().length());
                    ivShowPasswordAgain.setImageResource(R.drawable.ic_password_disshow);
                    showPasswordAgain = false;
                }else{
                    // if hidden show
                    edtPasswordAgain.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edtPasswordAgain.setSelection(edtPasswordAgain.getText().length());
                    ivShowPasswordAgain.setImageResource(R.drawable.ic_password_show);
                    showPasswordAgain = true;
                }
                break;
            case R.id.btn_confirm:
                String password = edtPassword.getText().toString();
                String passwordAgain = edtPasswordAgain.getText().toString();
                if(password.equals(passwordAgain)){
                    mPresenter.resetPassword(email,edtPassword.getText().toString());
                }else{
                    TSnackbar.make(getActivity().getWindow().getDecorView(),R.string.login_password_confirm_not_match,TSnackbar.LENGTH_LONG)
                            .setBackgroundColor(getResources().getColor(R.color.login_bg_red_color))
                            .setMessageGravity(Gravity.CENTER)
                            .setMessageTextColor(getResources().getColor(R.color.white))
                            .show();
                }

                break;
            default:break;
        }
    }

    @Override
    public void onDestroy() {
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
        //TODO reset password succes goto app
        getActivity().finish();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void resetPasswordFailed() {

    }
}
