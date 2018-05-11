package com.ubt.en.alpha1e.ble.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.en.alpha1e.ble.Contact.BleGuideContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.presenter.BleGuidPrenster;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BleGuideActivity extends MVPBaseActivity<BleGuideContact.View, BleGuidPrenster> implements BleGuideContact.View {
    Unbinder mUnbinder;

    Button mBleButtonNext;
    CheckBox mCheckBox;


    @Override
    public int getContentViewId() {
        return R.layout.ble_activity_guide;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        mPresenter.init(this);
        mBleButtonNext = findViewById(R.id.ble_connect);
        mBleButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ARouter.getInstance().build(ModuleUtils.Bluetooh_BleConnectActivity).withBoolean("first_enter", true).navigation();
                // startActivity(new Intent(BleGuideActivity.this, BleConnectActivity.class));
                mPresenter.checkBlestatu();
            }
        });

        mCheckBox = findViewById(R.id.ble_checkbox);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBleButtonNext.setEnabled(isChecked);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        mPresenter.unRegister();
    }

    @Override
    public void goBleSraechActivity() {
        if (mCheckBox.isChecked()) {
            BleConnectActivity.launch(BleGuideActivity.this, true);
        }
    }
}
