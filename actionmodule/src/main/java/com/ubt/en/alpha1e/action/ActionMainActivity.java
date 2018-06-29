package com.ubt.en.alpha1e.action;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseLowBattaryDialog;
import com.ubt.baselib.model1E.PlayEvent;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.action.contact.ActionMainContact;
import com.ubt.en.alpha1e.action.course.ActionCourseActivity;
import com.ubt.en.alpha1e.action.presenter.ActionMainPrenster;

import org.greenrobot.eventbus.EventBus;

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
        AppStatusUtils.setBtBussiness(true);
        EventBus.getDefault().post(new PlayEvent(PlayEvent.Event.ACTION_STOP));
    }

    @OnClick({R2.id.action_back, R2.id.rl_action_download, R2.id.rl_action_create, R2.id.rl_action_work, R2.id.rl_action_make})
    public void ClickView(View view) {
        int i = view.getId();
        if (i == R.id.action_back) {
            finish();
        } else if (i == R.id.rl_action_create) {
            if (AppStatusUtils.isLowPower()){
                BaseLowBattaryDialog.getInstance().showLow5Dialog(new BaseLowBattaryDialog.IDialog5Click() {
                    @Override
                    public void onOK() {

                    }
                });
                return;
            }
            startActivity(new Intent(this, ActionCreateActivity.class));
        } else if (i == R.id.rl_action_work) {
            startActivity(new Intent(this, DynamicActionActivity.class));
        } else if (i == R.id.rl_action_download) {
        } else if (i == R.id.rl_action_make) {
            startActivity(new Intent(this, ActionCourseActivity.class));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        AppStatusUtils.setBtBussiness(false);
    }
}
