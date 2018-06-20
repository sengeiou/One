package com.ubt.en.alpha1e.ble.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.baselib.model1E.BleNetWork;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.en.alpha1e.ble.Contact.RobotLanguageContact;
import com.ubt.en.alpha1e.ble.R;
import com.ubt.en.alpha1e.ble.R2;
import com.ubt.en.alpha1e.ble.model.BleDownloadLanguageRsp;
import com.ubt.en.alpha1e.ble.model.BleRobotVersionInfo;
import com.ubt.en.alpha1e.ble.model.BleUpgradeProgressRsp;
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
    private static final int UPDATE_DOWNLOAD_LANGUAGE = 2;
    private static final int DOWNLOAD_FAIL_RESET = 3;
    private static final int DOWNLOAD_SUCCESS_RESET = 4;
    private static final int DEAL_CHANGE_LANGUAGE_RESULT = 5;
    private static final int UPDATE_CURRENT_LANGUAGE = 6;

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
                case UPDATE_DOWNLOAD_LANGUAGE:
                    BleDownloadLanguageRsp downloadLanguageRsp = (BleDownloadLanguageRsp) msg.obj;
                    ViseLog.d("downloadLanguageRsp = " + downloadLanguageRsp);

                    for(RobotLanguage robotLanguage : mRobotLanguages){
                        if(robotLanguage.getLanguageSingleName().equals(downloadLanguageRsp.language)){
                            robotLanguage.setResult(downloadLanguageRsp.result);
                            robotLanguage.setProgess(downloadLanguageRsp.progress);
                            mAdapter.notifyDataSetChanged();

                            if(downloadLanguageRsp.result > 0){
                                ToastUtils.showShort(SkinManager.getInstance().getTextById(R.string.about_robot_language_package_download_fail));

                                //下载失败后，2S后进度条消失
                                Message resetMsg = new Message();
                                resetMsg.what = DOWNLOAD_FAIL_RESET;
                                resetMsg.obj = robotLanguage;
                                mHandler.sendMessageDelayed(resetMsg,2000);
                            }else if(downloadLanguageRsp.progress == 100){

                                //下载成功后，进度条消失
                                Message resetMsg = new Message();
                                resetMsg.what = DOWNLOAD_SUCCESS_RESET;
                                resetMsg.obj = robotLanguage;
                                mHandler.sendMessage(resetMsg);
                            }
                            break;
                        }
                    }
                    BaseLoadingDialog.dismiss(BleRobotLanguageActivity.this);

                    break;
                case DOWNLOAD_FAIL_RESET:
                case DOWNLOAD_SUCCESS_RESET:
                    RobotLanguage resetRobotLanguage = (RobotLanguage)msg.obj;
                    if(resetRobotLanguage != null && mAdapter != null){
                        ViseLog.d("resetRobotLanguage = " + resetRobotLanguage);
                        resetRobotLanguage.setResult(-1);
                        resetRobotLanguage.setProgess(0);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case DEAL_CHANGE_LANGUAGE_RESULT:
                    int result = msg.arg1;
                    if(result != 0){
                        BaseLoadingDialog.dismiss(BleRobotLanguageActivity.this);
                        if(result == 2){//低电量
                            showLowBatteryDialog(BleRobotLanguageActivity.this);
                        }else if(result == 3){//未联网
                            showConnectWifiDialog();
                        }
                    }
                    break;
                case UPDATE_CURRENT_LANGUAGE:
                    String currentLanguage = (String) msg.obj;
                    ViseLog.d("currentLanguage = " + currentLanguage + "  mCurrentRobotLanguage = " + mCurrentRobotLanguage);
                    if(!TextUtils.isEmpty(currentLanguage) && !currentLanguage.equals(mCurrentRobotLanguage)){
                        mCurrentRobotLanguage = currentLanguage;
                        mHandler.sendEmptyMessage(REFRESH_DATA);
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

        AppStatusUtils.setBtBussiness(false);
        initUI();
    }

    private void initUI(){
        mAdapter = new RobotLanguageAdapter(R.layout.ble_robot_language_item, mRobotLanguages);
        rvRobotLanguage.setLayoutManager(new LinearLayoutManager(this));
        rvRobotLanguage.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mPresenter.getRobotLanguageListFromWeb();

        //mPresenter.getRobotLanguageListFromRobot();

    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.getRobotWifiStatus();

        mPresenter.getRobotVersionMsg();

        /*BleDownloadLanguageRsp downloadLanguageRsp = new BleDownloadLanguageRsp();
        downloadLanguageRsp.result = 1;
        downloadLanguageRsp.language = "zh";
        downloadLanguageRsp.progress = 90;

        Message msg = new Message();
        msg.what = UPDATE_DOWNLOAD_LANGUAGE;
        msg.obj = downloadLanguageRsp;
        mHandler.sendMessageDelayed(msg,5000);*/

        /*Message msg = new Message();
        msg.what = DEAL_CHANGE_LANGUAGE_RESULT;
        msg.arg1 = 3;
        mHandler.sendMessageDelayed(msg,5000);*/

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
            ToastUtils.showShort(SkinManager.getInstance().getTextById(R.string.common_btn_no_network));
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
        ViseLog.d("status = " + status);
        Message msg = new Message();
        msg.what = DEAL_CHANGE_LANGUAGE_RESULT;
        msg.arg1 = status;
        mHandler.sendMessage(msg);

    }

    @Override
    public void setDownloadLanguage(BleDownloadLanguageRsp downloadLanguageRsp) {
        Message msg = new Message();
        msg.what = UPDATE_DOWNLOAD_LANGUAGE;
        msg.obj = downloadLanguageRsp;
        mHandler.sendMessage(msg);
    }

    @Override
    public void setUpgradeProgress(BleUpgradeProgressRsp upgradeProgressRsp) {

        //这里只是出来加载框消失，具体的结果处理，在全局服务里面处理
        BaseLoadingDialog.dismiss(this);
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

    @Override
    public void setRobotVersionInfo(BleRobotVersionInfo robotVersionInfo) {
        if(robotVersionInfo != null){
            Message msg = new Message();
            msg.what = UPDATE_CURRENT_LANGUAGE;
            msg.obj = robotVersionInfo.lang;
            mHandler.sendMessage(msg);
        }
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
            ViseLog.d("hasConnectWifi = " + hasConnectWifi + "  selectLanguage = " + selectLanguage.getLanguageName());

            if (!hasConnectWifi) {
                showConnectWifiDialog();
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

                                BaseLoadingDialog.show(15,BleRobotLanguageActivity.this);
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
        if(mHandler.hasMessages(DOWNLOAD_FAIL_RESET)){
            mHandler.removeMessages(DOWNLOAD_FAIL_RESET);
        }
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

    private void finishActivity() {

        finish();
    }

    /**
     * 显示连接WIFI对话框
     */
    private void showConnectWifiDialog() {
        final RobotLanguage selectLanguage = getSelectLanguage();
        if (selectLanguage == null) {
            return;
        }

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
    }


    /**
     * 显示低电量
     */
    private void showLowBatteryDialog(Context context) {

        String titleMsg = SkinManager.getInstance().getTextById(R.string.about_robot_language_low_battery_tips_1);
        String detailMsg = SkinManager.getInstance().getTextById(R.string.about_robot_language_low_battery_tips_2);

        View contentView = LayoutInflater.from(context).inflate(R.layout.ble_dialog_low_battery, null);
        TextView tvTitle = contentView.findViewById(R.id.tv_title);
        TextView tvMessage = contentView.findViewById(R.id.tv_message);
        tvTitle.setText(titleMsg);
        tvMessage.setText(detailMsg);

        ViewHolder viewHolder = new ViewHolder(contentView);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = (int) ((display.getWidth()) * 0.55); //设置宽度

        DialogPlus.newDialog(context)
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

}
