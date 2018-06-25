package com.ubt.loginmodule.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.mvp.MVPBaseFragment;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.loginmodule.R;
import com.ubt.loginmodule.R2;
import com.ubt.loginmodule.login.LoginActivity;
import com.vise.log.ViseLog;
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
    private String year = "1990";
    private String month = "1";
    private String day = "1";

    private NavigationCallback navigationCallback;

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
        initNavigationListener();
        return view;
    }

    private void initNavigationListener() {
        navigationCallback = new NavigationCallback() {
            @Override
            public void onFound(Postcard postcard) {

            }

            @Override
            public void onLost(Postcard postcard) {

            }

            @Override
            public void onArrival(Postcard postcard) {
                ViseLog.i("postcard="+postcard.toString());
                getActivity().finish();
                LoginActivity.finishSelf();
            }

            @Override
            public void onInterrupt(Postcard postcard) {

            }
        };
    }


    private void initView() {
        mPresenter.getYearData();
        loopViewYear.setItemsVisibleCount(5);
        loopViewYear.setTextSize(18);
        loopViewYear.setInitPosition(90);

        mPresenter.getMonthData();
        loopViewMonth.setItemsVisibleCount(5);
        loopViewMonth.setTextSize(18);
        loopViewMonth.setInitPosition(0);

        mPresenter.getDayData(31); //默认设置每月有31天，然后根据选择的月份再计算出相应月份的天数
        loopViewDay.setItemsVisibleCount(5);
        loopViewDay.setTextSize(18);
        loopViewDay.setInitPosition(0);

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
    public void onResume() {
        super.onResume();
        ViseLog.d("year:" + loopViewYear.getSelectedItem() + "month:" + loopViewMonth.getSelectedItem() + "day:" + loopViewDay.getSelectedItem());
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
            if(SPUtils.getInstance().getBoolean(Constant1E.SP_LOGIN)){

                boolean noFirst = SPUtils.getInstance().getBoolean(Constant1E.IS_FIRST_ENTER_GREET);
                if(noFirst){
                    ARouter.getInstance().build(ModuleUtils.Main_MainActivity).navigation(getActivity(),navigationCallback);
                }else{
                    ARouter.getInstance().build(ModuleUtils.Bluetooh_FirstGreetActivity).navigation(getActivity(),navigationCallback);
                }

            }else{
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//                getActivity().finish();
                boolean noFirst = SPUtils.getInstance().getBoolean(Constant1E.IS_FIRST_ENTER_GREET);
                if(noFirst){
                    ARouter.getInstance().build(ModuleUtils.Main_MainActivity).navigation(getActivity(),navigationCallback);
                }else{
                    ARouter.getInstance().build(ModuleUtils.Bluetooh_FirstGreetActivity).navigation(getActivity(),navigationCallback);
                }
            }

        }else{
            ToastUtils.showShort("update failed");
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
