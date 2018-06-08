package com.ubt.en.alpha1e.ble.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.orhanobut.dialogplus.DialogPlus;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseDialog;
import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.en.alpha1e.ble.Contact.WifiInputContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.R2;
import com.ubt.en.alpha1e.ble.presenter.WifiInputPrenster;
import com.vise.log.ViseLog;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

@Route(path = ModuleUtils.Bluetooh_BleWifiInputActivity)
public class BleWifiInputActivity extends MVPBaseActivity<WifiInputContact.View, WifiInputPrenster> implements WifiInputContact.View {

    @BindView(R2.id.ble_iv_back)
    ImageView mBleImageview3;
    @BindView(R2.id.ble_iv_close)
    ImageView mBleImageview4;
    @BindView(R2.id.bleTextview)
    TextView mBleTextview;
    @BindView(R2.id.bleTextview4)
    TextView mBleTextview4;
    @BindView(R2.id.ble_edit_name)
    EditText mBleEditName;
    @BindView(R2.id.ble_edit_passwd)
    EditText mBleEditPasswd;
    @BindView(R2.id.ble_show_passwd)
    ImageView mBleShowPasswd;
    @BindView(R2.id.ble_choose_wifi)
    TextView mBleChooseWifi;
    @BindView(R2.id.ble_connect_wifi)
    Button mBleConnectWifi;
    private Unbinder mUnbinder;

    private String wifiName;
    private String wifiPasswd;

    private boolean isShowPassWord = false;//是否显示密码


    private boolean isFirstEnter;

    private Disposable mDisposable;

    public static void launch(Activity context, String wifiName, boolean isFromFirst) {
        Intent intent = new Intent(context, BleWifiInputActivity.class);
        intent.putExtra("WIFI_NAME", wifiName);
        intent.putExtra("first_enter", isFromFirst);
        //context.startActivity(intent);
        context.startActivityForResult(intent, 1001);
    }

    @Override
    public int getContentViewId() {
        return R.layout.ble_activity_wifi_input;
    }


    @OnClick({R2.id.ble_iv_back, R2.id.ble_iv_close, R2.id.ble_show_passwd, R2.id.ble_connect_wifi, R2.id.ble_choose_wifi})
    public void clickView(View view) {
        int i = view.getId();
        if (i == R.id.ble_iv_back) {
            finish();

        } else if (i == R.id.ble_iv_close) {
            ARouter.getInstance().build(ModuleUtils.Main_MainActivity).navigation();
            finish();

        } else if (i == R.id.ble_choose_wifi) {
            finish();
        } else if (i == R.id.ble_show_passwd) {
            isShowPassWord = !isShowPassWord;
            mBleShowPasswd.setImageResource(isShowPassWord ? R.drawable.ic_password_show : R.drawable.ic_password_disshow);

            mBleEditPasswd.setInputType(isShowPassWord ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mBleEditPasswd.setSelection(mBleEditPasswd.getText().length());

        } else if (i == R.id.ble_connect_wifi) {
            wifiName = mBleEditName.getText().toString().trim();
            wifiPasswd = mBleEditPasswd.getText().toString().trim();
            if (!TextUtils.isEmpty(wifiName)) {
                if (!TextUtils.isEmpty(wifiPasswd)) {
                    mPresenter.sendPasswd(wifiName, wifiPasswd);
                } else {
                    new BaseDialog.Builder(this)
                            .setMessage(R.string.wifi_connect_without_password)
                            .setConfirmButtonId(R.string.base_confirm)
                            .setConfirmButtonColor(R.color.base_color_red)
                            .setCancleButtonID(R.string.base_cancel)
                            .setCancleButtonColor(R.color.black)
                            .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                                @Override
                                public void onClick(DialogPlus dialog, View view) {
                                    if (view.getId() == R.id.button_confirm) {
                                        mPresenter.sendPasswd(wifiName, wifiPasswd);
                                        dialog.dismiss();
                                    } else if (view.getId() == R.id.button_cancle) {
                                        dialog.dismiss();
                                    }

                                }
                            }).create().show();
                }

            }

        } else {

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        wifiName = getIntent().getStringExtra("WIFI_NAME");
        isFirstEnter = getIntent().getBooleanExtra("first_enter", false);
        ViseLog.d("isFirseEnter===" + isFirstEnter);
        mBleEditName.setText(wifiName);
        mBleEditName.setSelection(wifiName.length());//将光标移至文字末尾
        if (!TextUtils.isEmpty(wifiName)) {
            mBleEditPasswd.setFocusable(true);
        }
        mBleEditName.addTextChangedListener(mTextWatcher);
        mPresenter.init(this);
        AppStatusUtils.setBtBussiness(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresenter.unRegister();
        mDisposable.dispose();
        AppStatusUtils.setBtBussiness(false);
        BaseLoadingDialog.dismiss(this);
    }

    /**
     * 联网结果
     *
     * @param type 0 未连接 1 连接中 2 连接成功 3 连接失败
     */
    @Override
    public void connectWifiResult(final int type) {
        String content = "";
        int imageId = 0;
        if (type == 2 || type == 3) {
            content = type == 2 ? SkinManager.getInstance().getTextById(R.string.wifi_connect_succeed) :
                    SkinManager.getInstance().getTextById(R.string.wifi_connect_fail);
            imageId = type == 2 ? R.drawable.img_connect_wifi_succeed : R.drawable.img_overtime;
            BaseLoadingDialog.show(this, content,
                    imageId);
            mDisposable = Observable.timer(2, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    ViseLog.d("obser===" + type);
                    BaseLoadingDialog.dismiss(BleWifiInputActivity.this);
                    if (type == 2) {
                        if (isFirstEnter) {
                            ARouter.getInstance().build(ModuleUtils.Main_MainActivity).navigation();
                        } else {
                            setResult(1);
                        }
                        finish();
                    }
                }
            });
        }
    }

    @Override
    public void blutoohDisconnect() {
        finishActivity();
    }


    /**
     * 文本内容改变监听器
     */
    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable editable) {
            ViseLog.d("afterTextChanged 改变");

            setNetworkButtonState();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }
    };

    /**
     * 文本内容改变，按钮处理逻辑
     * 内容为空时，不可点击，非空时，可以点击
     */
    private void setNetworkButtonState() {
        if (mBleEditName == null) {
            return;
        }
        if (mBleEditName.getText().toString().trim().length() > 0) {
            ViseLog.d("afterTextChanged ENBLE");
            mBleConnectWifi.setEnabled(true);
        } else {
            ViseLog.d("afterTextChanged DISABLE");
            mBleConnectWifi.setEnabled(false);
        }
    }

    private void finishActivity() {
        BaseLoadingDialog.dismiss(this);
        boolean isFirstSearchWifi = SPUtils.getInstance().getBoolean(Constant1E.IS_FIRST_ENTER_WIFI_LIST, false);
        if (!isFirstSearchWifi) {
            ARouter.getInstance().build(ModuleUtils.Bluetooh_BleStatuActivity).withInt(Constant1E.ENTER_BLESTATU_ACTIVITY, 1).navigation();
            SPUtils.getInstance().put(Constant1E.IS_FIRST_ENTER_WIFI_LIST, true);
        }
        finish();
    }
}
