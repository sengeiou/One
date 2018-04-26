package com.ubt.en.alpha1e.ble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.model1E.UserInfoModel;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.en.alpha1e.ble.Contact.BleGuideContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.presenter.BleGuidPrenster;

@Route(path = ModuleUtils.Bluetooh_FirstGreetActivity)
public class FirstGreetActivity extends MVPBaseActivity<BleGuideContact.View, BleGuidPrenster> implements View.OnClickListener {

    private Button mButton;

    private TextView tvName;

    @Override
    public int getContentViewId() {
        return R.layout.ble_activity_first_greet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mButton = findViewById(R.id.ble_start);
        mButton.setOnClickListener(this);
        tvName = findViewById(R.id.ble_tvname);
        UserInfoModel model = (UserInfoModel) SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
        if (model != null) {
            tvName.setText(SkinManager.getInstance().getTextById(R.string.ble_hello_cristinal) + " " + model.getNickName() + "!");
        }
        SPUtils.getInstance().put(Constant1E.IS_FIRST_ENTER_GREET, true);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, BleGuideActivity.class));
    }
}
