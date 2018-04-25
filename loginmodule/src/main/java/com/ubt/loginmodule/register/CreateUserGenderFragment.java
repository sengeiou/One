package com.ubt.loginmodule.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ubt.baselib.mvp.MVPBaseFragment;
import com.ubt.loginmodule.R;

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


public class CreateUserGenderFragment extends MVPBaseFragment<RegisterContract.View, RegisterPresenter> implements RegisterContract.View {

    private Unbinder unbinder;
    @BindView(R.id.iv_gender_male)
    ImageView ivGenderMale;
    @BindView(R.id.iv_gender_female)
    ImageView ivGenderFemale;
    @BindView(R.id.iv_gender_robot)
    ImageView ivGenderRobot;
    @BindView(R.id.btn_next)
    Button btnNext;
    private String sex = "1";

    public static CreateUserGenderFragment newInstance() {
        return new CreateUserGenderFragment();
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
        View view = inflater.inflate(R.layout.login_fragment_user_gender, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        ivGenderMale.setSelected(true);
    }

    @OnClick({R.id.iv_gender_male, R.id.iv_gender_female,R.id.iv_gender_robot,R.id.btn_next})
    public void onClickView(View view) {
        switch (view.getId()){
            case R.id.iv_gender_male:
                sex = "1";
                ivGenderMale.setSelected(true);
                ivGenderFemale.setSelected(false);
                ivGenderRobot.setSelected(false);
                break;
            case R.id.iv_gender_female:
                sex = "2";
                ivGenderMale.setSelected(false);
                ivGenderFemale.setSelected(true);
                ivGenderRobot.setSelected(false);
                break;
            case R.id.iv_gender_robot:
                sex = "3";
                ivGenderMale.setSelected(false);
                ivGenderFemale.setSelected(false);
                ivGenderRobot.setSelected(true);
                break;
            case R.id.btn_next:
                mPresenter.updateUserInfo("", sex, "");
//                start(CreateUserAgeFragment.newInstance());
                break;
            default:
                break;
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
        if(success) {
            start(CreateUserAgeFragment.newInstance());
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
