package com.ubt.en.alpha1e.ble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.en.alpha1e.ble.Contact.BleGuideContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.presenter.BleGuidPrenster;
import com.ubt.en.alpha1e.ble.service.AutoConnectService;

@Route(path = ModuleUtils.Bluetooh_FirstGreetActivity)
public class FirstGreetActivity extends MVPBaseActivity<BleGuideContact.View, BleGuidPrenster> implements View.OnClickListener {

    private Button mButton;

 
    @Override
    public int getContentViewId() {
        return R.layout.ble_activity_first_greet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mButton = findViewById(R.id.ble_start);
        mButton.setOnClickListener(this);
        AutoConnectService.startAutoConnectService(this);


    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, BleGuideActivity.class));
    }
}
