package com.ubt.en.alpha1e.action;

import android.os.Bundle;
import android.view.View;

import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.en.alpha1e.action.contact.ActionMainContact;
import com.ubt.en.alpha1e.action.presenter.ActionMainPrenster;

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
                finish();
            }
        });
    }
}
