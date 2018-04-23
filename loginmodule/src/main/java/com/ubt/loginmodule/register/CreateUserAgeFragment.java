package com.ubt.loginmodule.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ubt.baselib.mvp.MVPBaseFragment;
import com.ubt.loginmodule.R;
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
    @BindView(R.id.loopView_year)
    LoopView loopViewYear;
    @BindView(R.id.loopView_month)
    LoopView loopViewMonth;
    @BindView(R.id.loopView_day)
    LoopView loopViewDay;
    @BindView(R.id.btn_finish)
    Button btnFinish;

    private List<String> listMonth ;

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
        initView();
        return view;
    }

    private void initView() {
        mPresenter.getYearData();
        loopViewYear.setItemsVisibleCount(5);
        loopViewYear.setTextSize(18);

        mPresenter.getMonthData();
        loopViewMonth.setItemsVisibleCount(5);
        loopViewMonth.setTextSize(18);

        mPresenter.getDayData(31); //默认设置每月有31天，然后根据选择的月份再计算出相应月份的天数
        loopViewDay.setItemsVisibleCount(5);
        loopViewDay.setTextSize(18);

        loopViewYear.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {

            }
        });

        loopViewMonth.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
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

    }

    @OnClick(R.id.btn_finish)
    public void onClick() {
        mPresenter.register();
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
        loopViewYear.setItems(listYear);
    }

    @Override
    public void setMonthData(List<String> listMonth) {
        this.listMonth = listMonth;
        loopViewMonth.setItems(listMonth);
    }

    @Override
    public void setDayData(List<String> listDay) {
        loopViewDay.setItems(listDay);
    }

    @Override
    public void registerFinish() {
//        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}
