package com.ubt.en.alpha1e.ble.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.en.alpha1e.ble.Contact.BleConnectContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.R2;
import com.ubt.en.alpha1e.ble.model.BleDevice;
import com.ubt.en.alpha1e.ble.model.BluetoothDeviceListAdapter;
import com.ubt.en.alpha1e.ble.presenter.BleConnectPrenster;
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
    private DialogPlus mDialog = null;


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
     * 开始搜索蓝牙
     */
    @Override
    public void startSerchBle() {
        mBleConnectLoading.setVisibility(View.VISIBLE);
        //有可能在弹出蓝牙权限框的时候，用户长时间没有点击确定，这时已经弹出超时的对话框，所以在此取消对话框
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
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
        showLoadingDialog(false, "");
    }


    /**
     * 与机器人连接蓝牙成功，根据从什么页面过来进行跳转还是直接finish
     */
    @Override
    public void connectSuccess() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
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
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showLoadingDialog(false, "");

            }
        },1000);
    }

    /**
     * 开始连接蓝牙
     */
    @Override
    public void connecting(String mac) {
        ViseLog.d("开始连接蓝牙");
        showLoadingDialog(true, mac);

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        BleDevice device = (BleDevice) adapter.getItem(position);
        if (device != null) {
            mPresenter.connect(device);
        }
    }

    /**
     * 显示对话框
     *
     * @param isConnecting true表示正在连接蓝牙的状态  false表示超时对话框
     * @param mac          连接的蓝牙地址
     */
    public void showLoadingDialog(final boolean isConnecting, String mac) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.ble_dialog_result, null);
        ViewHolder viewHolder = new ViewHolder(contentView);

        ProgressBar progressBar = contentView.findViewById(R.id.ble_progress_connect_loading);
        int drawableId = isConnecting ? R.drawable.ble_search_loading : R.drawable.img_overtime;

        progressBar.setIndeterminateDrawable(this.getResources().getDrawable(drawableId));
        TextView textContent = contentView.findViewById(R.id.tv_content);

        String content = isConnecting ? SkinManager.getInstance().getTextById(R.string.ble_connecting) + " : " + mac :
                SkinManager.getInstance().getTextById(R.string.ble_connect_bluetooh_outtime);

        textContent.setText(content);

        Button btn = contentView.findViewById(R.id.btn_confirm);

        String btnMsg = isConnecting ? SkinManager.getInstance().getTextById(R.string.ble_cancle) :
                SkinManager.getInstance().getTextById(R.string.ble_try_again);

        btn.setText(btnMsg);

        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = (int) ((display.getWidth()) * 0.5); //设置宽度
        mDialog = DialogPlus.newDialog(this)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.CENTER)
                .setContentBackgroundResource(R.drawable.base_rect_background)
                .setContentWidth(width)
                .setCancelable(false)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.btn_confirm) {
                            if (isConnecting) {
                                mPresenter.disconnect();
                            } else {
                                mPresenter.getBleDevices().clear();
                                notifyDataSetChanged();
                                mPresenter.startScanBle();
                            }
                            dialog.dismiss();
                        }
                    }
                })
                .create();
        mDialog.show();
    }


}
