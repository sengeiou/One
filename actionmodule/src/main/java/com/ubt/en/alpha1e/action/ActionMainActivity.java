package com.ubt.en.alpha1e.action;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.action.contact.ActionMainContact;
import com.ubt.en.alpha1e.action.course.ActionCourseActivity;
import com.ubt.en.alpha1e.action.presenter.ActionMainPrenster;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@Route(path = ModuleUtils.Actions_ActionProgram)
public class ActionMainActivity extends MVPBaseActivity<ActionMainContact.View, ActionMainPrenster> implements ActionMainContact.View {

    Unbinder mUnbinder;
    @BindView(R2.id.action_back)
    ImageView mActionBack;
    @BindView(R2.id.rl_action_create)
    RelativeLayout mRlActionCreate;
    @BindView(R2.id.rl_action_work)
    RelativeLayout mRlActionWork;
    @BindView(R2.id.rl_action_download)
    RelativeLayout mRlActionDownload;
    @BindView(R2.id.rl_action_make)
    RelativeLayout mRlActionMake;

    private BlueClientUtil mBlueClientUtil;

    @Override
    public int getContentViewId() {
        return R.layout.activity_action_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        mBlueClientUtil = BlueClientUtil.getInstance();
    }

    @OnClick({R2.id.action_back, R2.id.rl_action_download, R2.id.rl_action_create, R2.id.rl_action_work, R2.id.rl_action_make})
    public void ClickView(View view) {
        int i = view.getId();
        if (i == R.id.action_back) {
            finish();
        } else if (i == R.id.rl_action_create) {
            if (mBlueClientUtil != null && mBlueClientUtil.getConnectionState() != 3) {
                ARouter.getInstance().build(ModuleUtils.Bluetooh_BleStatuActivity).navigation();
            } else {
                startActivity(new Intent(this, ActionCreateActivity.class));
            }
        } else if (i == R.id.rl_action_work) {
            startActivity(new Intent(this, ActionCourseActivity.class));
        } else if (i == R.id.rl_action_download) {
        } else if (i == R.id.rl_action_make) {
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
