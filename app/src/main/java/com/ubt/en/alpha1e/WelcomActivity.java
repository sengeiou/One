package com.ubt.en.alpha1e;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.model1E.UserInfoModel;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.ContextUtils;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.baselib.utils.ULog;
import com.ubt.en.alpha1e.presenter.WelcomContact;
import com.ubt.en.alpha1e.presenter.WelcomPrenster;
import com.vise.log.ViseLog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import pl.droidsonroids.gif.GifDrawable;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/15 15:14
 * @描述: 开机流程：1，刷新TOKEN--》2，获取用户信息--》3，初始化语言包
 */

public class WelcomActivity extends MVPBaseActivity<WelcomContact.View, WelcomPrenster> implements WelcomContact.View {
    private static final int REQUEST_CODE_PERMISSION_MULTI = 1111;

    @BindView(R.id.gif_start_welcome)
    ImageView gifWelcome;
    GifDrawable gifDrawable;
    private NavigationCallback navigationCallback;

    /**
     * 下载语言包是否结束
     */
    private boolean isDownLanguageCompleted;
    /**
     * 下载动画是否结束
     */
    private boolean isPermissionCompleted;

    private boolean isTimeOut = false;
    Disposable disposable;

    @Override
    public int getContentViewId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        startParamInit(); //同步后台参数
        //initView();
        applyPermission();
        //gifDrawable.start();
        initNavigationListener();
        getSHA();
        startTimer();
    }

    /**
     * 开启两秒超时，达到两秒才进入首页
     */
    private void startTimer() {
        disposable = Observable.timer(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        ViseLog.d("到达两秒超时");
                        isTimeOut = true;
                        compareLanguageAndPermissResult();
                    }
                });

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
                ViseLog.i("postcard=" + postcard.toString());
                WelcomActivity.this.finish();
            }

            @Override
            public void onInterrupt(Postcard postcard) {

            }
        };
    }

    /**
     * 初始化数据
     */
    private void initView() {
//        try {
//            gifDrawable = new GifDrawable(getResources(), R.drawable.gif_welcome);
//            gifDrawable.addAnimationListener(new AnimationListener() {
//                @Override
//                public void onAnimationCompleted() {
//                    applyPermission();
//                }
//            });
//            gifWelcome.setImageDrawable(gifDrawable);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }

    }

    private void applyPermission() {
        ViseLog.d("申请权限");
        // 申请多个权限。
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_PERMISSION_MULTI)
                .permission(Permission.LOCATION, Permission.STORAGE, Permission.MICROPHONE)
                .callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        rationale.resume();
                    }
                })
                .start();
    }

    /**
     * 回调监听。
     */
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {

                case REQUEST_CODE_PERMISSION_MULTI:
                    ViseLog.d("申请权限 onSucceed requestCode=" + requestCode +
                            "grantPermissions:" + grantPermissions);
                    ViseLog.d("完成权限申请");
                    isPermissionCompleted = true;
                    compareLanguageAndPermissResult();
                    ULog.init(ContextUtils.getContext(), true);
                    break;
                default:
                    break;

            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION_MULTI:
                    ViseLog.d("申请权限 onFailed requestCode=" + requestCode +
                            "deniedPermissions=" + deniedPermissions.toString());
                    ToastUtils.showShort("申请权限失败");
                    WelcomActivity.this.finish();
                    break;
                default:
                    break;

            }

        }

    };

    /**
     * 跳转到其它模块
     */
    private void startMainActivity() {
        final String startModule = ModuleUtils.Login_Module;
        final UserInfoModel userInfoModel = (UserInfoModel) SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
        ViseLog.d("userInfoModel:" + userInfoModel);
        if (null == userInfoModel) {
            ARouter.getInstance().build(startModule).navigation(WelcomActivity.this, navigationCallback);
        } else {
            if (userInfoModel.getNickName() == null) {
                ARouter.getInstance().build(ModuleUtils.Login_Register)
                        .withBoolean(Constant1E.EMPTY_NICK_NAME, true)
                        .navigation(WelcomActivity.this, navigationCallback);
            } else if (userInfoModel.getSex() == null) {
                ARouter.getInstance().build(ModuleUtils.Login_Register)
                        .withBoolean(Constant1E.EMPTY_SEX, true)
                        .navigation(WelcomActivity.this, navigationCallback);
            } else if (userInfoModel.getBirthDate() == null) {
                ARouter.getInstance().build(ModuleUtils.Login_Register)
                        .withBoolean(Constant1E.EMPTY_BIRTHDAY, true)
                        .navigation(WelcomActivity.this, navigationCallback);
            } else {
                ARouter.getInstance().build(ModuleUtils.Main_MainActivity)
                        .navigation(WelcomActivity.this, navigationCallback);
            }

        }
    }


    private void getSHA() {
        try {
            int i = 0;
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                i++;
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String KeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                //KeyHash 就是你要的，不用改任何代码  复制粘贴 ;
                ViseLog.e("KeyHash:" + KeyHash);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    /**
     * 完成语言包更新
     */
    @Override
    public void updateLanguageCompleted() {
        isDownLanguageCompleted = true;
        compareLanguageAndPermissResult();
    }

    /**
     * 获取用户信息完成
     */
    @Override
    public void getUserInfoCompleted() {
        mPresenter.initLanugage(this);
    }


    /**
     * 比较动画及权限结束和更新语言包结束
     */
    private void compareLanguageAndPermissResult() {
        ViseLog.d("完成语言包更新 isDownLanguageCompleted:" + isDownLanguageCompleted + "  isPermissionCompleted:" + isPermissionCompleted);
        if (isDownLanguageCompleted && isPermissionCompleted && isTimeOut) {
            startMainActivity();
        }
    }

    /**
     * 开始后台参数同步
     */
    private void startParamInit() {
        if (SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO) != null) {
            mPresenter.refreshToken();  //同步用户信息参数
        } else {
            mPresenter.initLanugage(this);
        }
    }

}
