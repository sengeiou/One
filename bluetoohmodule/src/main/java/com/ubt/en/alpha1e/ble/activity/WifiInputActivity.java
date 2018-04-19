package com.ubt.en.alpha1e.ble.activity;

import android.os.Bundle;

import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.en.alpha1e.ble.Contact.WifiInputContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.presenter.WifiInputPrenster;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WifiInputActivity extends MVPBaseActivity<WifiInputContact.View, WifiInputPrenster> implements WifiInputContact.View {

    private Unbinder mUnbinder;

    @Override
    public int getContentViewId() {
        return R.id.ble_activity_wifi_input;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
