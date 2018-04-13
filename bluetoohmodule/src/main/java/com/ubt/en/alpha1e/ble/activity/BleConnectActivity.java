package com.ubt.en.alpha1e.ble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.en.alpha1e.ble.Contact.BleConnectContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.R2;
import com.ubt.en.alpha1e.ble.model.BleDevice;
import com.ubt.en.alpha1e.ble.model.BluetoothDeviceListAdapter;
import com.ubt.en.alpha1e.ble.presenter.BleConnectPrenster;
import com.ubt.globaldialog.customDialog.loading.LoadingDialog;
import com.vise.log.ViseLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BleConnectActivity extends MVPBaseActivity<BleConnectContact.View, BleConnectPrenster> implements BleConnectContact.View, BaseQuickAdapter.OnItemChildClickListener {

    Unbinder mUnbinder;
    @BindView(R2.id.ble_buletooth_device_list)
    RecyclerView mBuletoothDeviceList;
    @BindView(R2.id.ble_connect_loading)
    ProgressBar mBleConnectLoading;
    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.iv_help)
    ImageView mIvHelp;
    @BindView(R.id.rl_sucessed)
    RelativeLayout mRlSucessed;


    private BluetoothDeviceListAdapter mDeviceListAdapter;


    @Override
    public int getContentViewId() {
        return R.layout.ble_activity_connect;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        initUi();
        mPresenter.register(this);

    }

    private void initUi() {
        mDeviceListAdapter = new BluetoothDeviceListAdapter(R.layout.ble_item_bledevice_layout, mPresenter.getBleDevices());
        mBuletoothDeviceList.setLayoutManager(new LinearLayoutManager(this));
        mBuletoothDeviceList.setAdapter(mDeviceListAdapter);
        mDeviceListAdapter.setOnItemChildClickListener(this);
    }


    @OnClick({R2.id.iv_back, R2.id.iv_help})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_help:

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unRegister();
    }


    @Override
    public void notifyDataSetChanged() {
        ViseLog.d("搜到蓝牙数量======" + mPresenter.getBleDevices().size());
        mDeviceListAdapter.notifyDataSetChanged();
    }

    /**
     * 搜索蓝牙成功
     */
    @Override
    public void searchSuccess() {
        mBleConnectLoading.setVisibility(View.GONE);
    }


    /**
     * 蓝牙搜索失败
     */
    @Override
    public void searchBleFiled() {

    }


    /**
     * 与机器人连接蓝牙成功，根据从什么页面过来进行跳转还是直接finish
     */
    @Override
    public void connectSuccess() {
        mRlSucessed.setVisibility(View.VISIBLE);
        mRlSucessed.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(BleConnectActivity.this, BleConnectWifiActivity.class));
                mRlSucessed.setVisibility(View.GONE);
            }
        }, 1000);
    }

    /**
     * 连接机器人失败
     */
    @Override
    public void connectFailed() {

    }

    @Override
    public void connecting() {
        ViseLog.d("开始连接蓝牙");
        LoadingDialog.show(this);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        BleDevice device = (BleDevice) adapter.getItem(position);
        if (device != null) {
            mPresenter.connect(device.getMac());
        }
    }
}
