package com.ubt.en.alpha1e.ble.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.ubt.baselib.customView.BaseDialog;
import com.ubt.baselib.model1E.BleNetWork;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.en.alpha1e.ble.Contact.RobotLanguageContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.R2;
import com.ubt.en.alpha1e.ble.dialog.SwitchIngLanguageDialog;
import com.ubt.en.alpha1e.ble.model.BleDownloadLanguageRsp;
import com.ubt.en.alpha1e.ble.model.BleSwitchLanguageRsp;
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
    private static final int SHOW_SET_LANGUAGE_RESULT = 2;
    private static final int SHOW_SET_LANGUAGE_PROGRESS = 3;
    private static final int UPDATE_DOWNLOAD_LANGUAGE = 4;

    @BindView(R2.id.iv_back)
    ImageView mIvBack;

    Unbinder mUnbinder;
    @BindView(R2.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R2.id.rv_robot_language)
    RecyclerView rvRobotLanguage;

    private RobotLanguageAdapter mAdapter = null;
    private List<RobotLanguage> mRobotLanguages = new ArrayList<>();
    private String mCurrentRobotLanguage;

    private List<RobotLanguage> mRobotLanguageExists = new ArrayList<>();

    private SwitchIngLanguageDialog switchProgressDialog = null;

    private boolean hasConnectWifi = true;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH_DATA:
                    RobotLanguage selectLanguage = getSelectLanguage();
                    if(selectLanguage != null && selectLanguage.getLanguageSingleName().equals(mCurrentRobotLanguage)){
                        tvTitleRight.setAlpha(0.3f);
                        tvTitleRight.setEnabled(false);
                    }else {
                        tvTitleRight.setAlpha(1.0f);
                        tvTitleRight.setEnabled(true);
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                case SHOW_SET_LANGUAGE_RESULT:
                    int status = msg.arg1;
                    ViseLog.d("SHOW_SET_LANGUAGE_RESULT = " + status);
                    if(status == 0){
                        showSetLanguageDialog(true);
                    }else if(status == 1){
                        showSetLanguageDialog(false);
                    }else if(status == 2){
                        showLowBatteryDialog();
                    }
                    break;
                case SHOW_SET_LANGUAGE_PROGRESS:

                    BleSwitchLanguageRsp switchLanguageRsp = (BleSwitchLanguageRsp)msg.obj;
                    ViseLog.d("switchLanguageRsp = " + switchLanguageRsp);
                    if(switchLanguageRsp.result == 0 ){

                        if(switchLanguageRsp.progess == 100 && switchProgressDialog != null){
                            switchProgressDialog.dismiss();

                            Message msg1 = new Message();
                            msg1.what = SHOW_SET_LANGUAGE_RESULT;
                            msg1.arg1 = 0;
                            mHandler.sendMessage(msg1);
                        }else{
                            showSwitchLanguageDialog(switchLanguageRsp.progess);
                        }

                    }else if(switchLanguageRsp.result == 1 || switchLanguageRsp.result == 2 ){

                        switchProgressDialog.dismiss();

                        Message msg1 = new Message();
                        msg1.what = SHOW_SET_LANGUAGE_RESULT;
                        msg1.arg1 = 1;
                        mHandler.sendMessage(msg1);
                    }
                    break;
                case UPDATE_DOWNLOAD_LANGUAGE:
                    BleDownloadLanguageRsp downloadLanguageRsp = (BleDownloadLanguageRsp) msg.obj;
                    ViseLog.d("downloadLanguageRsp = " + downloadLanguageRsp);

                    if(downloadLanguageRsp.result == 1){
                        ToastUtils.showShort(SkinManager.getInstance().getTextById(R.string.about_robot_language_package_download_fail));
                    }else if(downloadLanguageRsp.result == 0){
                        for(RobotLanguage robotLanguage : mRobotLanguages){
                            if(robotLanguage.getLanguageSingleName().equals(downloadLanguageRsp.language)){
                                robotLanguage.setResult(downloadLanguageRsp.result);
                                robotLanguage.setProgess(downloadLanguageRsp.progess);
                                mAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }

                    break;
                default:
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
        mPresenter.init(this);

        mCurrentRobotLanguage = getIntent().getStringExtra("CURRENT_ROBOT_LANGUAGE");

        ViseLog.d("mCurrentRobotLanguage = " + mCurrentRobotLanguage);

        mAdapter = new RobotLanguageAdapter(R.layout.ble_robot_language_item, mRobotLanguages);
        rvRobotLanguage.setLayoutManager(new LinearLayoutManager(this));
        rvRobotLanguage.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        AppStatusUtils.setBtBussiness(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getRobotLanguageListFromWeb();

        mPresenter.getRobotLanguageListFromRobot();

        mPresenter.getRobotWifiStatus();

    }

    @Override
    public void setRobotLanguageList(boolean status, List<RobotLanguage> list) {
        ViseLog.d("status = " + status);
        if(status){
            mRobotLanguages.clear();
            mRobotLanguages.addAll(list);

            for(RobotLanguage robotLanguage : mRobotLanguages){
                if(robotLanguage.getLanguageSingleName().equals(mCurrentRobotLanguage)){
                    robotLanguage.setSelect(true);
                    break;
                }
            }
            mHandler.sendEmptyMessage(REFRESH_DATA);
        }else {
            ToastUtils.showShort("network exception");
        }
    }

    @Override
    public void setRobotLanguageListExist(List<RobotLanguage> list) {
        if(list != null ){
            mRobotLanguageExists.clear();
            mRobotLanguageExists.addAll(list);
            ViseLog.d("mRobotLanguageExists = " + mRobotLanguageExists.size());
        }
    }

    @Override
    public void setRobotLanguageResult(int status) {

    }

    @Override
    public void setDownloadLanguage(BleDownloadLanguageRsp downloadLanguageRsp) {
        Message msg = new Message();
        msg.what = UPDATE_DOWNLOAD_LANGUAGE;
        msg.obj = downloadLanguageRsp;
        mHandler.sendMessage(msg);
    }

    @Override
    public void setSwitchLanguageResult(BleSwitchLanguageRsp switchLanguageRsp) {
        Message msg = new Message();
        msg.what = SHOW_SET_LANGUAGE_PROGRESS;
        msg.obj = switchLanguageRsp;
        mHandler.sendMessage(msg);
    }

    @Override
    public void setRobotNetWork(BleNetWork bleNetWork) {
        if(bleNetWork.isStatu()){
            hasConnectWifi = true;
        }else {
            hasConnectWifi = false;
        }
        ViseLog.d("bleNetWork = " + bleNetWork.isStatu() + " hasConnectWifi = " + hasConnectWifi);
    }

    @OnClick({R2.id.iv_back,R2.id.tv_title_right})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            finishActivity();

        } else if (i == R.id.tv_title_right) {
            final RobotLanguage selectLanguage = getSelectLanguage();
            if (selectLanguage == null) {
                return;
            }

            if(selectLanguage.getLanguageSingleName().equals(mCurrentRobotLanguage)){
                ViseLog.d("same");
                return;
            }

            if (!hasConnectWifi) {
                new BaseDialog.Builder(this)
                        .setMessage(SkinManager.getInstance().getTextById(R.string.about_robot_language_package_dialogue).replace("#", selectLanguage.getLanguageName()))
                        .setConfirmButtonId(R.string.base_cancel)
                        .setConfirmButtonColor(R.color.base_blue)
                        .setCancleButtonID(R.string.base_connect)
                        .setCancleButtonColor(R.color.base_blue)
                        .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                if (view.getId() == R.id.button_confirm) {
                                    dialog.dismiss();

                                } else if (view.getId() == R.id.button_cancle) {
                                    dialog.dismiss();

                                    BleSearchWifiActivity.launch(BleRobotLanguageActivity.this, false, "");
                                }
                            }
                        }).create().show();
                return;
            }

            new BaseDialog.Builder(this)
                    .setMessage(SkinManager.getInstance().getTextById(R.string.about_robot_language_dialogue).replace("#", selectLanguage.getLanguageName()))
                    .setConfirmButtonId(R.string.base_cancel)
                    .setConfirmButtonColor(R.color.base_blue)
                    .setCancleButtonID(R.string.base_confirm)
                    .setCancleButtonColor(R.color.base_blue)
                    .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                        @Override
                        public void onClick(DialogPlus dialog, View view) {
                            if (view.getId() == R.id.button_confirm) {
                                dialog.dismiss();

                            } else if (view.getId() == R.id.button_cancle) {

                                dialog.dismiss();
                                ViseLog.d("selectLanguage.getLanguageSingleName() = " + selectLanguage.getLanguageSingleName());
                                mPresenter.setRobotLanguage(selectLanguage.getLanguageSingleName());
                            }
                        }
                    }).create().show();


        } else {
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
        mPresenter.unRegister();
        mUnbinder.unbind();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        for(RobotLanguage robotLanguage : mRobotLanguages){
            robotLanguage.setSelect(false);
        }
        mRobotLanguages.get(position).setSelect(true);
        mHandler.sendEmptyMessage(REFRESH_DATA);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示设置语言对话框
     */
    public void showSetLanguageDialog(boolean isSuccess) {

        String message ;
        int imgId ;
        if(isSuccess){
            message = SkinManager.getInstance().getTextById(R.string.about_robot_language_changing_success);
            imgId = R.drawable.img_language_ok;
        }else {
            message = SkinManager.getInstance().getTextById(R.string.about_robot_language_changing_fail);
            imgId = R.drawable.img_language_failed;
        }

        View contentView = LayoutInflater.from(this).inflate(R.layout.ble_dialog_set_language_result, null);
        TextView tvResult = contentView.findViewById(R.id.tv_result);
        tvResult.setText(message);
        ((ImageView) contentView.findViewById(R.id.iv_result)).setImageResource(imgId);
        ViewHolder viewHolder = new ViewHolder(contentView);
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = (int) ((display.getWidth()) * 0.55); //设置宽度

        DialogPlus.newDialog(this)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.CENTER)
                .setContentWidth(width)
                .setContentBackgroundResource(android.R.color.transparent)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.btn_ok) {//点击确定以后刷新列表并解锁下一关

                            dialog.dismiss();
                        }
                    }
                })
                .setCancelable(false)
                .create().show();

    }

    /**
     * 显示低电量
     */
    public void showLowBatteryDialog() {

        String titleMsg = SkinManager.getInstance().getTextById(R.string.about_robot_language_low_battery_tips_1);
        String detailMsg = SkinManager.getInstance().getTextById(R.string.about_robot_language_low_battery_tips_2);

        View contentView = LayoutInflater.from(this).inflate(R.layout.ble_dialog_low_battery, null);
        TextView tvTitle = contentView.findViewById(R.id.tv_title);
        TextView tvMessage = contentView.findViewById(R.id.tv_message);
        tvTitle.setText(titleMsg);
        tvMessage.setText(detailMsg);

        ViewHolder viewHolder = new ViewHolder(contentView);
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = (int) ((display.getWidth()) * 0.55); //设置宽度

        DialogPlus.newDialog(this)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.CENTER)
                .setContentWidth(width)
                .setContentBackgroundResource(android.R.color.transparent)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.btn_ok) {//点击确定以后刷新列表并解锁下一关

                            dialog.dismiss();
                        }
                    }
                })
                .setCancelable(false)
                .create().show();

    }


    /**
     * 切换语言对话框
     */
    public void showSwitchLanguageDialog(int progress) {

        ViseLog.d("-switchLanguageDialog->");
        if(switchProgressDialog == null){
            switchProgressDialog = new SwitchIngLanguageDialog(this)
                    .setCancel(false);

            switchProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    switchProgressDialog = null;
                }
            });
        }

        switchProgressDialog.setProgress(progress);

        if(!switchProgressDialog.isShowing()){
            switchProgressDialog.doShow();
        }
    }

    private void finishActivity() {

        finish();
    }


}
