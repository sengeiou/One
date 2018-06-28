package com.ubt.en.alpha1e.action;

import android.content.Intent;
import android.os.Bundle;

import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.action.contact.ActionMainContact;
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.ubt.en.alpha1e.action.model.IEditActionUI;
import com.ubt.en.alpha1e.action.presenter.ActionMainPrenster;
import com.ubt.en.alpha1e.action.view.ActionEditsStandard;
import com.ubt.en.alpha1e.action.view.BaseActionEditLayout;
import com.ubt.globaldialog.customDialog.ConfirmDialog;
import com.vise.log.ViseLog;


public class ActionCreateActivity extends MVPBaseActivity<ActionMainContact.View, ActionMainPrenster> implements ActionMainContact.View, IEditActionUI, ActionsEditHelper.PlayCompleteListener, BaseActionEditLayout.OnSaveSucessListener {

    ActionEditsStandard mActionEdit;

    private ActionsEditHelper mHelper;

    private boolean isSaveSuccess;

    private static ActionCreateActivity mInstance;
    ConfirmDialog confirmDialog;


    @Override
    public int getContentViewId() {
        return R.layout.activity_action_create;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
        mHelper = new ActionsEditHelper(this, this);
        ((ActionsEditHelper) mHelper).setListener(this);
        mActionEdit = (ActionEditsStandard) findViewById(R.id.action_edit);
        mActionEdit.setUp(mHelper);
        mActionEdit.setOnSaveSucessListener(this);
        AppStatusUtils.setBtBussiness(true);
        AppStatusUtils.setBussiness(true);
        if (BlueClientUtil.getInstance().getConnectionState() != BluetoothState.STATE_CONNECTED && mHelper != null) {
            mHelper.showBlutoohDisconnectDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViseLog.d("onDestroy");
        if (mActionEdit != null) {
            mActionEdit.doReset();
        }
        mInstance = null;
        mHelper.unRegister();
        AppStatusUtils.setBtBussiness(false);
        AppStatusUtils.setBussiness(false);
    }

    @Override
    public void onPlaying() {
        mActionEdit.onPlaying();
    }

    @Override
    public void onPausePlay() {
        mActionEdit.onPausePlay();
    }

    @Override
    public void onFinishPlay() {
        mActionEdit.onFinishPlay();
    }

    @Override
    public void onFrameDo(int index) {
        mActionEdit.onFrameDo(index);
    }

    @Override
    public void notePlayChargingError() {

    }


    @Override
    public void onReadEng(byte[] eng_angle) {
        mActionEdit.onReadEng(eng_angle);
    }


    @Override
    public void startSave(Intent intent) {
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActionsEditHelper.SaveActionReq) {

            if (data == null) {
                return;
            }
            isSaveSuccess = (Boolean) data.getExtras().get(ActionsEditHelper.SaveActionResult);
            if (isSaveSuccess) {
                if (mHelper != null) {
                    ViseLog.d("退出动作编辑模式");
                    ((ActionsEditHelper) mHelper).doEnterOrExitActionEdit((byte) 0x04);
                }
                //NewActionInfo actionInfo = ((ActionsEditHelper)mHelper).getNewActionInfo();
                Intent intent = new Intent(this, SaveSuccessActivity.class);
                startActivity(intent);
                finish();
            }

        }

    }

    public static void finishSelf() {
        if (mInstance != null && !mInstance.isFinishing()) {
            mInstance.finish();
        }
    }


    @Override
    public void playComplete() {

    }

    @Override
    public void onDisconnect() {
        if (mHelper!=null){
            mHelper.showBlutoohDisconnectDialog();
        }

    }

    @Override
    public void tapHead() {

    }

    @Override
    public void lowerPower() {
        mActionEdit.showLowerPowerDialog();


    }


}
