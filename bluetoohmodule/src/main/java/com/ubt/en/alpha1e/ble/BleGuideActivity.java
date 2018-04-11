package com.ubt.en.alpha1e.ble;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.en.alpha1e.ble.Contact.BleGuideContact;
import com.ubt.en.alpha1e.ble.activity.BleConnectActivity;
import com.ubt.en.alpha1e.ble.presenter.BleGuidPrenster;

import butterknife.ButterKnife;
import butterknife.Unbinder;


@Route(path = ModuleUtils.Bluetooh_BleGuideActivity)
public class BleGuideActivity extends MVPBaseActivity<BleGuideContact.View, BleGuidPrenster> {
    Unbinder mUnbinder;

    Button mBleButtonNext;


    @Override
    public int getContentViewId() {
        return R.layout.ble_activity_guide;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        mBleButtonNext = findViewById(R.id.ble_button_next);
        mBleButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BleGuideActivity.this, BleConnectActivity.class));
            }
        });
    }


    public void onButtonNext() {
        ToastUtils.showShort("擦擦擦擦错");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
