package com.ubt.en.alpha1e.ble.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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

    private static final int MESSAGE_TIMEOUT = 60 * 1000;

    private static final int MESSAGE_DISSMISSLOADING = 2000;

    /**
     * 连接网络超时Message what
     */
    private static final int MESSAGE_WHAT_CONNECT = 0x11;

    /**
     * 连接成功后消失Message what
     */
    private static final int MESSAGE_WHAT_DISSMISS_SUCCESS = 0x12;

    /**
     * 连接成功后消失Message what
     */
    private static final int MESSAGE_WHAT_DISSMISS_FAILED = 0x13;

    private boolean isFirstEnter;

    public static void launch(Context context, String wifiName, boolean isFromFirst) {
        Intent intent = new Intent(context, BleWifiInputActivity.class);
        intent.putExtra("WIFI_NAME", wifiName);
        intent.putExtra("first_enter", isFromFirst);
        context.startActivity(intent);
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
                    BaseLoadingDialog.show(this, SkinManager.getInstance().getTextById(R.string.ble_wifi_connecting));
                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT_CONNECT, MESSAGE_TIMEOUT);
                } else {
                    new BaseDialog.Builder(this)
                            .setMessage(R.string.ble_wifi_connect_without_password)
                            .setConfirmButtonId(R.string.base_confirm)
                            .setConfirmButtonColor(R.color.base_color_red)
                            .setCancleButtonID(R.string.base_cancel)
                            .setCancleButtonColor(R.color.black)
                            .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                                @Override
                                public void onClick(DialogPlus dialog, View view) {
                                    if (view.getId() == R.id.button_confirm) {
                                        mPresenter.sendPasswd(wifiName, wifiPasswd);
                                        BaseLoadingDialog.show(BleWifiInputActivity.this, SkinManager.getInstance().getTextById(R.string.ble_wifi_connecting));
                                        mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT_CONNECT, MESSAGE_TIMEOUT);
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
        mHandler.removeMessages(MESSAGE_WHAT_CONNECT);
        mHandler.removeMessages(MESSAGE_WHAT_DISSMISS_SUCCESS);
        mHandler.removeMessages(MESSAGE_WHAT_DISSMISS_FAILED);
        mPresenter.unRegister();
        AppStatusUtils.setBtBussiness(false);
    }

    /**
     * 联网结果
     *
     * @param type 0 未连接 1 连接中 2 连接成功 3 连接失败
     */
    @Override
    public void connectWifiResult(int type) {
        ViseLog.d("type====" + type);
        if (type == 2) {
            mHandler.removeMessages(MESSAGE_WHAT_CONNECT);
            BaseLoadingDialog.dismiss(this);
            BaseLoadingDialog.show(this, SkinManager.getInstance().getTextById(R.string.ble_wifi_connect_succeed),
                    R.drawable.img_connect_wifi_succeed);
            mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT_DISSMISS_SUCCESS, MESSAGE_DISSMISSLOADING);
        } else if (type == 3) {
            mHandler.removeMessages(MESSAGE_WHAT_CONNECT);
            BaseLoadingDialog.dismiss(this);
            BaseLoadingDialog.show(this, SkinManager.getInstance().getTextById(R.string.ble_wifi_connect_fail),
                    R.drawable.img_overtime);
            mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT_DISSMISS_FAILED, MESSAGE_DISSMISSLOADING);
        }
    }

    @Override
    public void blutoohDisconnect() {
        finishActivity();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_WHAT_CONNECT) {
                connectWifiResult(3);
            } else if (msg.what == MESSAGE_WHAT_DISSMISS_SUCCESS) {//连接成功对话框消失
                BaseLoadingDialog.dismiss(BleWifiInputActivity.this);
                ARouter.getInstance().build(ModuleUtils.Main_MainActivity).navigation();
                finish();
            } else if (msg.what == MESSAGE_WHAT_DISSMISS_FAILED) {//连接失败对话框消失
                BaseLoadingDialog.dismiss(BleWifiInputActivity.this);
            }
        }
    };

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
        boolean isFirstSearchWifi = SPUtils.getInstance().getBoolean(Constant1E.IS_FIRST_ENTER_WIFI_LIST, false);
        if (!isFirstSearchWifi) {
            ARouter.getInstance().build(ModuleUtils.Bluetooh_BleStatuActivity).withInt(Constant1E.ENTER_BLESTATU_ACTIVITY, 1).navigation();
            SPUtils.getInstance().put(Constant1E.IS_FIRST_ENTER_WIFI_LIST, true);
        }
        finish();
    }
}
