package com.ubt.loginmodule.register;


import com.ubt.baselib.mvp.BasePresenterImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class RegisterPresenter extends BasePresenterImpl<RegisterContract.View> implements RegisterContract.Presenter{

    private static final int START_YEAR = 1900;
    private static final int END_YEAR = 2014;

    @Override
    public void sendSecurityCode(String account) {

    }

    @Override
    public void signUp(String account, String password, String code) {
        mView.signUpSuccess();
    }

    @Override
    public void getYearData() {
        List<String> listYear = new ArrayList<String>();
        for(int i=START_YEAR; i<=END_YEAR; i++){
            listYear.add(String.valueOf(i));
        }

        if(mView != null){
            mView.setYearData(listYear);
        }

    }

    @Override
    public void getMonthData() {
        List<String> listMonth = new ArrayList<String>();
        for(int i=1; i<=12; i++){
            listMonth.add(String.valueOf(i));
        }

        if(mView != null){
            mView.setMonthData(listMonth);
        }
    }

    @Override
    public void getDayData(int dayCount) {
        List<String> listDay = new ArrayList<String>();
        for(int i=1; i<=dayCount; i++){
            listDay.add(String.valueOf(i));
        }

        if(mView != null){
            mView.setDayData(listDay);
        }
    }

    @Override
    public void register() {

        if(mView != null){
            mView.registerFinish();
        }
    }
}
