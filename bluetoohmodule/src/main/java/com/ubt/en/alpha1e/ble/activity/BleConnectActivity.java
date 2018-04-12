package com.ubt.en.alpha1e.ble.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
import butterknife.Unbinder;

public class BleConnectActivity extends MVPBaseActivity<BleConnectContact.View, BleConnectPrenster> implements BleConnectContact.View, BaseQuickAdapter.OnItemChildClickListener {

    Unbinder mUnbinder;
    @BindView(R2.id.ble_buletooth_device_list)
    RecyclerView mBuletoothDeviceList;


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
        LoadingDialog.show(this);
        mDeviceListAdapter = new BluetoothDeviceListAdapter(R.layout.ble_item_bledevice_layout, mPresenter.getBleDevices());
        mBuletoothDeviceList.setLayoutManager(new LinearLayoutManager(this));
        mBuletoothDeviceList.setAdapter(mDeviceListAdapter);
        mDeviceListAdapter.setOnItemChildClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unRegister();
    }


    @Override
    public void notifyDataSetChanged() {
        mDeviceListAdapter.notifyDataSetChanged();
    }

    /**
     * 搜索蓝牙成功
     */
    @Override
    public void searchSuccess() {
        LoadingDialog.dismiss(this);
    }


    /**
     * 蓝牙搜索失败
     */
    @Override
    public void searchBleFiled() {

    }


    /**
     * 与机器人连接蓝牙成功
     */
    @Override
    public void connectSuccess() {
        LoadingDialog.dismiss(this);
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
