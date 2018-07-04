package com.ubt.loginmodule.register;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.mvp.MVPBaseFragment;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.loginmodule.R;
import com.ubt.loginmodule.R2;
import com.ubt.loginmodule.login.LoginActivity;
import com.vise.log.ViseLog;

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


public class CreateAccountSuccessFragment extends MVPBaseFragment<RegisterContract.View, RegisterPresenter> implements RegisterContract.View {

    private Unbinder unbinder;
    @BindView(R2.id.tv_account)
    TextView tvAccount;
    @BindView(R2.id.btn_next)
    Button btnNext;

    private static int MSG_CODE = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_CODE){
//                start(CreateUserNameFragment.newInstance());
                enterMain();
            }
        }
    };

    private NavigationCallback navigationCallback;

    public static CreateAccountSuccessFragment newInstance(){
        return new CreateAccountSuccessFragment();
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
        View view = inflater.inflate(R.layout.login_fragment_sign_up_success, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((RegisterActivity)getActivity()).setTvSkipVisible(false);
//        UserInfoModel userInfoModel = (UserInfoModel) SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
        tvAccount.setText(SPUtils.getInstance().getString(Constant1E.SP_USER_EMAIL));
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

    @Override
    public void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(MSG_CODE, 2000);
    }

    private void enterMain() {
        boolean noFirst = SPUtils.getInstance().getBoolean(Constant1E.IS_FIRST_ENTER_GREET);
        if(noFirst){
            ARouter.getInstance().build(ModuleUtils.Main_MainActivity).navigation(getActivity(),navigationCallback);
        }else{
            ARouter.getInstance().build(ModuleUtils.Bluetooh_FirstGreetActivity).navigation(getActivity(),navigationCallback);
        }
    }

    @OnClick(R2.id.btn_next)
    public void onClickView(){
        mHandler.removeMessages(MSG_CODE);
//        start(CreateUserNameFragment.newInstance());
        enterMain();
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

    }

    @Override
    public void sendSecurityCodeSuccess(boolean success) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if(mHandler != null){
            mHandler.removeMessages(MSG_CODE);
        }
    }
}
