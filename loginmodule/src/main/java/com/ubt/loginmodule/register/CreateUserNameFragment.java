package com.ubt.loginmodule.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ubt.baselib.mvp.MVPBaseFragment;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.loginmodule.R;
import com.ubt.loginmodule.R2;

import java.util.List;

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


public class CreateUserNameFragment extends MVPBaseFragment<RegisterContract.View, RegisterPresenter> implements RegisterContract.View {

    private Unbinder unbinder;
    @BindView(R2.id.edt_first_name)
    EditText edtFirstName;
    @BindView(R2.id.edt_last_name)
    EditText edtLastName;
    @BindView(R2.id.view_first_name)
    View viewFistName;
    @BindView(R2.id.view_last_name)
    View viewLastName;
    @BindView(R2.id.btn_next)
    Button btnNext;
    @BindView(R2.id.tv_hint)
    TextView tvHint;

    public static CreateUserNameFragment newInstance(){
        return new CreateUserNameFragment();
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
        View view = inflater.inflate(R.layout.login_fragment_user_name, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R2.id.btn_next)
    public void onClick() {
        String firstName = edtFirstName.getText().toString();
        String lastName = edtLastName.getText().toString();
        if(TextUtils.isEmpty(firstName)){
            viewFistName.setBackgroundColor(getResources().getColor(R.color.login_bg_red_color));
        }
        if(TextUtils.isEmpty(lastName)){
            viewLastName.setBackgroundColor(getResources().getColor(R.color.login_bg_red_color));
        }
        if(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)){
            tvHint.setTextColor(getResources().getColor(R.color.login_bg_red_color));
            return;
        }
        String userName = firstName + lastName;
        mPresenter.updateUserInfo(userName, null,null);

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
        if(success){
            start(CreateUserGenderFragment.newInstance());
        }else{
            ToastUtils.showShort("update user name failed");
        }
    }

    @Override
    public void sendSecurityCodeSuccess(boolean success) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
