package com.ubt.en.alpha1e.ble.activity;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ubt.baselib.mvp.MVPBaseActivity;
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

public class BleConnectWifiActivity extends MVPBaseActivity<WifiConnectContact.View, WifiConnectPrenster> implements WifiConnectContact.View, BaseQuickAdapter.OnItemClickListener {

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

    @Override
    public int getContentViewId() {
        return R.layout.ble_activity_connect_wifi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        mWifiListAdapter = new WifiListAdapter(R.layout.ble_item_wifi_layout, mScanResults);
        mBleWifiList.setLayoutManager(new LinearLayoutManager(this));
        mBleWifiList.setAdapter(mWifiListAdapter);
        mWifiListAdapter.setOnItemClickListener(this);
        mPresenter.init(this);

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

    @OnClick({R2.id.iv_back, R2.id.ble_input})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ble_input:

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
