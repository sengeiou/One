package com.ubt.loginmodule.register;


import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;

import java.util.List;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class RegisterContract {
    interface View extends BaseView {
        void sendSuccess();
        void sendFailed();
        void signUpSuccess();
        void signUpFailed();
        void setYearData(List<String> listYear);
        void setMonthData(List<String> listMonth);
        void setDayData(List<String> listDay);
        void registerFinish();
    }

    interface  Presenter extends BasePresenter<View> {
        void sendSecurityCode(String account);
        void signUp(String account, String password, String code);
        void getYearData();
        void getMonthData();
        void getDayData(int dayCount);
        void register();
        
    }
}
