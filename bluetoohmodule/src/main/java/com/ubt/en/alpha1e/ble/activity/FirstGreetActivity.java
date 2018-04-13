package com.ubt.en.alpha1e.ble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.en.alpha1e.ble.Contact.BleGuideContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.presenter.BleGuidPrenster;

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
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, BleGuideActivity.class));
    }
}
