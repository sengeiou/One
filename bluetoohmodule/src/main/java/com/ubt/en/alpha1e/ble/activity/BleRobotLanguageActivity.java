package com.ubt.en.alpha1e.ble.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.ubt.baselib.customView.BaseDialog;
import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.en.alpha1e.ble.Contact.RobotLanguageContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.R2;
import com.ubt.en.alpha1e.ble.model.RobotLanguage;
import com.ubt.en.alpha1e.ble.model.RobotLanguageAdapter;
import com.ubt.en.alpha1e.ble.presenter.RobotLanguagePresenter;
import com.vise.log.ViseLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BleRobotLanguageActivity extends MVPBaseActivity<RobotLanguageContact.View, RobotLanguagePresenter> implements RobotLanguageContact.View, BaseQuickAdapter.OnItemClickListener {

    private static final int REFRESH_DATA = 1;

    @BindView(R2.id.iv_back)
    ImageView mIvBack;

    Unbinder mUnbinder;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.rv_robot_language)
    RecyclerView rvRobotLanguage;

    private RobotLanguageAdapter mAdapter = null;
    private List<RobotLanguage> mRobotLanguages = new ArrayList<>();
    private String mCurrentRobotLanguage;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH_DATA:
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public static void launch(Context context, String currentRobotLanguage) {
        Intent intent = new Intent(context, BleRobotLanguageActivity.class);
        intent.putExtra("CURRENT_ROBOT_LANGUAGE", currentRobotLanguage);
        context.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.ble_activity_robot_language;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        mCurrentRobotLanguage = getIntent().getStringExtra("CURRENT_ROBOT_LANGUAGE");

        mAdapter = new RobotLanguageAdapter(R.layout.ble_robot_language_item, mRobotLanguages);
        rvRobotLanguage.setLayoutManager(new LinearLayoutManager(this));
        rvRobotLanguage.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getRobotLanguageList();
    }

    @Override
    public void setRobotLanguageList(boolean status, List<RobotLanguage> list) {
        ViseLog.d("status = " + status);
        if(status){
            mRobotLanguages.clear();
            mRobotLanguages.addAll(list);

            for(RobotLanguage robotLanguage : mRobotLanguages){
                if(robotLanguage.getLanguageName().equals(mCurrentRobotLanguage)){
                    robotLanguage.setSelect(true);
                    break;
                }
            }
            mHandler.sendEmptyMessage(REFRESH_DATA);
        }else {
            ToastUtils.showShort("network exception");
        }
    }

    @OnClick({R.id.iv_back,R.id.tv_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finishActivity();
                break;
            case R.id.tv_title_right:

                RobotLanguage selectLanguage = getSelectLanguage();
                if(selectLanguage == null){
                    return;
                }

                new BaseDialog.Builder(this)
                        .setMessage(SkinManager.getInstance().getTextById(R.string.about_robot_language_dialogue).replace("#",selectLanguage.getLanguageName()) )
                        .setConfirmButtonId(R.string.base_confirm)
                        .setConfirmButtonColor(R.color.base_blue)
                        .setCancleButtonID(R.string.base_cancel)
                        .setCancleButtonColor(R.color.base_blue)
                        .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                if (view.getId() == R.id.button_confirm) {
                                    dialog.dismiss();

                                    BaseLoadingDialog.show(BleRobotLanguageActivity.this, R.string.about_robot_language_changing);

                                } else if (view.getId() == R.id.button_cancle) {
                                    dialog.dismiss();
                                }
                            }
                        }).create().show();

                break;
            default:
                break;
        }
    }


    private RobotLanguage getSelectLanguage(){
        RobotLanguage selectLanguage = null;
        for(RobotLanguage robotLanguage : mRobotLanguages){
            if(robotLanguage.isSelect()){
                selectLanguage = robotLanguage;
                break;
            }
        }
        return selectLanguage;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        for(RobotLanguage robotLanguage : mRobotLanguages){
            robotLanguage.setSelect(false);
        }
        mRobotLanguages.get(position).setSelect(true);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishActivity() {

        finish();
    }


}
