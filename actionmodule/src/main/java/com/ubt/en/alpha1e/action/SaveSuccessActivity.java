package com.ubt.en.alpha1e.action;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.en.alpha1e.action.contact.ActionMainContact;
import com.ubt.en.alpha1e.action.presenter.ActionMainPrenster;
import com.vise.log.ViseLog;

public class SaveSuccessActivity extends MVPBaseActivity<ActionMainContact.View, ActionMainPrenster> implements ActionMainContact.View {

    @Override
    public int getContentViewId() {
        return R.layout.activity_save_success;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.img_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SaveSuccessActivity.this, ActionCreateActivity.class));
                finish();
            }
        });
    }

    //监听手机屏幕上的按键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ViseLog.d("返回键");
            startActivity(new Intent(SaveSuccessActivity.this, ActionCreateActivity.class));
            finish();
        }
        return false;
        //return super.onKeyDown(keyCode, event);
    }
}
