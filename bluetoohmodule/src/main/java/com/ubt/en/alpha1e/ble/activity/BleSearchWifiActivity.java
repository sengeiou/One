package com.ubt.en.alpha1e.ble.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.en.alpha1e.ble.Contact.WifiConnectContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.R2;
import com.ubt.en.alpha1e.ble.model.WifiListAdapter;
import com.ubt.en.alpha1e.ble.presenter.WifiConnectPrenster;
import com.vise.log.ViseLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BleSearchWifiActivity extends MVPBaseActivity<WifiConnectContact.View, WifiConnectPrenster> implements WifiConnectContact.View, BaseQuickAdapter.OnItemClickListener {

    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.wifiTextview)
    TextView mWifiTextview;
    @BindView(R2.id.bleTextview2)
    TextView mBleTextview2;
    @BindView(R2.id.ble_connect_loading)
    ProgressBar mBleConnectLoading;
    @BindView(R2.id.ble_wifi_list)
    RecyclerView mBleWifiList;

    WifiListAdapter mWifiListAdapter;
    @BindView(R2.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R2.id.ble_input)
    Button mBleInput;
    @BindView(R2.id.ll_not_wifi_data)
    LinearLayout mLlNotWifiData;

    private List<ScanResult> mScanResults = new ArrayList<>();
    Unbinder mUnbinder;

    private boolean isFirstEnter;
    private String wifiName;
    //用于记录进入SaveActivity的时间
    private long inSaveActTime;

    public static void launch(Context context, boolean isFrom, String wifiName) {
        Intent intent = new Intent(context, BleSearchWifiActivity.class);
        intent.putExtra("first_enter", isFrom);
        intent.putExtra("WI_FI_NAME", wifiName);
        context.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.ble_activity_connect_wifi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        isFirstEnter = getIntent().getBooleanExtra("first_enter", false);
        ViseLog.d("isFirseEnter===" + isFirstEnter);
        wifiName = getIntent().getStringExtra("WI_FI_NAME");
        mWifiListAdapter = new WifiListAdapter(R.layout.ble_item_wifi_layout, mScanResults);
        mBleWifiList.setLayoutManager(new LinearLayoutManager(this));
        mBleWifiList.setAdapter(mWifiListAdapter);
        mWifiListAdapter.setSelectedWifiName(wifiName);
        mWifiListAdapter.setOnItemClickListener(this);
        ViseLog.d("isFirstEnter==" + isFirstEnter);
        mPresenter.init(this);
        AppStatusUtils.setBtBussiness(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppStatusUtils.setBtBussiness(true);
    }

    @Override
    public void getWifiList(List<ScanResult> scanResultList) {
        ViseLog.d(Thread.currentThread().getName() + "-----------------");
        if (scanResultList.size() == 0) {
            mLlNotWifiData.setVisibility(View.VISIBLE);
            mLlContent.setVisibility(View.GONE);
        } else {
            mLlNotWifiData.setVisibility(View.GONE);
            mLlContent.setVisibility(View.VISIBLE);
            mBleConnectLoading.setVisibility(View.GONE);
            mScanResults.clear();
            mScanResults.addAll(scanResultList);
            mWifiListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 蓝牙断开
     */
    @Override
    public void blutoohDisconnect() {
        finishActivity();
    }

    @OnClick({R2.id.iv_back, R2.id.ble_input})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            finishActivity();
        } else if (i == R.id.ble_input) {
            BleWifiInputActivity.launch(this, "", isFirstEnter);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresenter.unRegister();
        AppStatusUtils.setBtBussiness(false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //当当前的时间-进入SaveActivity的时间<1秒时，直接返回
        if ((System.currentTimeMillis() - inSaveActTime) < 1000) {
            return;
        }
        inSaveActTime = System.currentTimeMillis();
        ScanResult scanResult = (ScanResult) adapter.getData().get(position);
        BleWifiInputActivity.launch(this, scanResult.SSID, isFirstEnter);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishActivity() {
        boolean isFirstSearchWifi = SPUtils.getInstance().getBoolean(Constant1E.IS_FIRST_ENTER_WIFI_LIST, false);
        if (!isFirstSearchWifi) {
            ARouter.getInstance().build(ModuleUtils.Bluetooh_BleStatuActivity).withInt(Constant1E.ENTER_BLESTATU_ACTIVITY, 1).navigation();
            SPUtils.getInstance().put(Constant1E.IS_FIRST_ENTER_WIFI_LIST, true);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == 1) {
                finish();
            }
        }
    }
}
