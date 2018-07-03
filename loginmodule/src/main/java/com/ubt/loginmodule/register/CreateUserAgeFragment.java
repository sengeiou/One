package com.ubt.loginmodule.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
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


public class CreateUserAgeFragment extends MVPBaseFragment<RegisterContract.View, RegisterPresenter> implements RegisterContract.View {

    private Unbinder unbinder;
    @BindView(R2.id.iv_age_less)
    ImageView ivAgeLess16;
    @BindView(R2.id.tv_age_less)
    TextView tvAgeLess16;
    @BindView(R2.id.iv_age_16_24)
    ImageView ivAge16to24;
    @BindView(R2.id.tv_age_16_24)
    TextView tvAge16to24;
    @BindView(R2.id.iv_age_25_35)
    ImageView ivAge25to35;
    @BindView(R2.id.tv_age_25_35)
    TextView tvAge25to35;
    @BindView(R2.id.iv_age_36_45)
    ImageView ivAge36to45;
    @BindView(R2.id.tv_age_36_45)
    TextView tvAge36to45;
    @BindView(R2.id.iv_age_more_54)
    ImageView ivAgeMore45;
    @BindView(R2.id.tv_age_more_45)
    TextView tvAgeMore45;
    @BindView(R2.id.btn_next)
    Button btnNext;


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
        View view = inflater.inflate(R.layout.login_fragment_user_age_new, container, false);
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
        ivAge16to24.setSelected(true);

    }

    @OnClick({R2.id.iv_age_less, R2.id.iv_age_16_24, R2.id.iv_age_25_35, R2.id.iv_age_36_45, R2.id.iv_age_more_54,R2.id.btn_next})
    public void onClick(View view) {
        int id = view.getId();
        String birthday = tvAge16to24.getText().toString();
        if(id == R.id.iv_age_less){
//            ToastUtils.showShort("less 16");
            showTip(getActivity());
        }else if(id == R.id.iv_age_16_24){
           birthday = tvAge16to24.getText().toString();
           ivAge16to24.setSelected(true);
           ivAge25to35.setSelected(false);
           ivAge36to45.setSelected(false);
           ivAgeMore45.setSelected(false);
           ivAgeLess16.setSelected(false);
        }else if(id == R.id.iv_age_25_35){
            birthday = tvAge25to35.getText().toString();
            ivAge16to24.setSelected(false);
            ivAge25to35.setSelected(true);
            ivAge36to45.setSelected(false);
            ivAgeMore45.setSelected(false);
            ivAgeLess16.setSelected(false);

        }else if(id == R.id.iv_age_36_45){
            birthday = tvAge36to45.getText().toString();
            ivAge16to24.setSelected(false);
            ivAge25to35.setSelected(false);
            ivAge36to45.setSelected(true);
            ivAgeMore45.setSelected(false);
            ivAgeLess16.setSelected(false);
        }else if(id == R.id.iv_age_more_54){
            birthday =tvAgeMore45.getText().toString();
            ivAge16to24.setSelected(false);
            ivAge25to35.setSelected(false);
            ivAge36to45.setSelected(false);
            ivAgeMore45.setSelected(true);
            ivAgeLess16.setSelected(false);
        }else if(id == R.id.btn_next){
            SPUtils.getInstance().put(Constant1E.SP_USER_AGE, birthday);
            start(CreateAccountFragment.newInstance());
        }

    }


    public void showTip(Context context) {
        if (mDialogPlus != null) {
            mDialogPlus.dismiss();
            mDialogPlus = null;
        }



        View contentView = LayoutInflater.from(context).inflate(R.layout.base_dialog_age_less_16, null);

        ViewHolder viewHolder = new ViewHolder(contentView);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = (int) ((display.getWidth()) * 0.55); //设置宽度
        int height = (int) ((display.getHeight()) * 0.6);
        mDialogPlus = DialogPlus.newDialog(context)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.CENTER)
                .setContentWidth(width)
                .setContentHeight(height)
                .setContentBackgroundResource(android.R.color.transparent)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.btn_ok) {//点击确定以后刷新列表并解锁下一关
                            dialog.dismiss();
                            mDialogPlus = null;
                        }
                    }
                })
                .setCancelable(false)
                .create();
        mDialogPlus.show();


    }

    DialogPlus mDialogPlus = null;


    @Override
    public void onResume() {
        super.onResume();
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
  /*      BaseLoadingDialog.dismiss(getActivity());
        if(success) {
            if(SPUtils.getInstance().getBoolean(Constant1E.SP_LOGIN)){

                boolean noFirst = SPUtils.getInstance().getBoolean(Constant1E.IS_FIRST_ENTER_GREET);
                if(noFirst){
                    ARouter.getInstance().build(ModuleUtils.Main_MainActivity).navigation(getActivity(),navigationCallback);
                }else{
                    ARouter.getInstance().build(ModuleUtils.Bluetooh_FirstGreetActivity).navigation(getActivity(),navigationCallback);
                }

            }else{

                boolean noFirst = SPUtils.getInstance().getBoolean(Constant1E.IS_FIRST_ENTER_GREET);
                if(noFirst){
                    ARouter.getInstance().build(ModuleUtils.Main_MainActivity).navigation(getActivity(),navigationCallback);
                }else{
                    ARouter.getInstance().build(ModuleUtils.Bluetooh_FirstGreetActivity).navigation(getActivity(),navigationCallback);
                }
            }

        }else{
            ToastUtils.showShort("update failed");
        }*/
    }

    @Override
    public void sendSecurityCodeSuccess(boolean success) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
//        BaseLoadingDialog.dismiss(getActivity());
    }


}
