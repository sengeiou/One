package com.ubt.loginmodule.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.baselib.mvp.MVPBaseFragment;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.loginmodule.R;
import com.ubt.loginmodule.R2;
import com.ubt.loginmodule.login.LoginActivity;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

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


public class CreateUserAgeFragment extends MVPBaseFragment<RegisterContract.View, RegisterPresenter> implements RegisterContract.View {

    private Unbinder unbinder;
    @BindView(R2.id.loopView_year)
    LoopView loopViewYear;
    @BindView(R2.id.loopView_month)
    LoopView loopViewMonth;
    @BindView(R2.id.loopView_day)
    LoopView loopViewDay;
    @BindView(R2.id.btn_finish)
    Button btnFinish;
    private List<String> listYear;
    private List<String> listMonth ;
    private List<String> listDay;
    private String year;
    private String month;
    private String day;

    public static CreateUserAgeFragment newInstance() {
        return new CreateUserAgeFragment();
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
        View view = inflater.inflate(R.layout.login_fragment_user_age, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((RegisterActivity)getActivity()).setTvSkipVisible(false);
        initView();
        return view;
    }

    private void initView() {
        mPresenter.getYearData();
        loopViewYear.setItemsVisibleCount(5);
        loopViewYear.setTextSize(18);
        loopViewYear.setCurrentPosition(90);

        mPresenter.getMonthData();
        loopViewMonth.setItemsVisibleCount(5);
        loopViewMonth.setTextSize(18);
        loopViewMonth.setCurrentPosition(0);

        mPresenter.getDayData(31); //默认设置每月有31天，然后根据选择的月份再计算出相应月份的天数
        loopViewDay.setItemsVisibleCount(5);
        loopViewDay.setTextSize(18);
        loopViewDay.setCurrentPosition(0);

        loopViewYear.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                year = listYear.get(index);
            }
        });

        loopViewMonth.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                month = listMonth.get(index);
                int month = Integer.valueOf(listMonth.get(index));
                switch (month){
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        mPresenter.getDayData(31);
                        break;
                    case 2:
                        mPresenter.getDayData(28);
                        break;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        mPresenter.getDayData(30);
                        break;
                        default:break;
                }

            }
        });

        loopViewDay.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                day = listDay.get(index);
            }
        });

    }

    @OnClick(R2.id.btn_finish)
    public void onClick() {
        String birthday = year + "-" + month + "-" + day;
        mPresenter.updateUserInfo("","",birthday);
        BaseLoadingDialog.show(getActivity());
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
        this.listYear = listYear;
        loopViewYear.setItems(listYear);
    }

    @Override
    public void setMonthData(List<String> listMonth) {
        this.listMonth = listMonth;
        loopViewMonth.setItems(listMonth);
    }

    @Override
    public void setDayData(List<String> listDay) {
        this.listDay = listDay;
        loopViewDay.setItems(listDay);
    }

    @Override
    public void registerFinish() {

    }

    @Override
    public void updateUserInfoSuccess(boolean success) {
        BaseLoadingDialog.dismiss(getActivity());
        if(success) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }else{
            ToastUtils.showShort("提交后台失败");
        }
    }

    @Override
    public void sendSecurityCodeSuccess(boolean success) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        BaseLoadingDialog.dismiss(getActivity());
    }


}
