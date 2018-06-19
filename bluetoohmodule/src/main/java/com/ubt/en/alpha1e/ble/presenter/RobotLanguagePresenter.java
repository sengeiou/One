package com.ubt.en.alpha1e.ble.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BluetoothParamUtil;
import com.ubt.baselib.btCmd1E.ProtocolPacket;
import com.ubt.baselib.btCmd1E.cmd.BTCmdGetLanguages;
import com.ubt.baselib.btCmd1E.cmd.BTCmdGetRobotVersionMsg;
import com.ubt.baselib.btCmd1E.cmd.BTCmdGetWifiStatus;
import com.ubt.baselib.btCmd1E.cmd.BTCmdSetLanguage;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.model1E.BleNetWork;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.ble.BleHttpEntity;
import com.ubt.en.alpha1e.ble.Contact.RobotLanguageContact;
import com.ubt.en.alpha1e.ble.model.BleBaseModelInfo;
import com.ubt.en.alpha1e.ble.model.BleDownloadLanguageRsp;
import com.ubt.en.alpha1e.ble.model.BleRobotLanguageList;
import com.ubt.en.alpha1e.ble.model.BleRobotVersionInfo;
import com.ubt.en.alpha1e.ble.model.BleSetRobotLanguageRsp;
import com.ubt.en.alpha1e.ble.model.BleUpgradeProgressRsp;
import com.ubt.en.alpha1e.ble.model.RobotLanguage;
import com.ubt.en.alpha1e.ble.requestModel.GetRobotLanguageRequest;
import com.vise.log.ViseLog;
import com.vise.utils.convert.HexUtil;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class RobotLanguagePresenter extends BasePresenterImpl<RobotLanguageContact.View> implements RobotLanguageContact.Presenter {

    private BlueClientUtil mBlueClientUtil;

    @Override
    public void init(Context context) {
        mBlueClientUtil = BlueClientUtil.getInstance();
        EventBus.getDefault().register(this);
    }

    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getRobotLanguageListFromWeb() {
        GetRobotLanguageRequest robotLanguageRequest = new GetRobotLanguageRequest();
        robotLanguageRequest.setToken(SPUtils.getInstance().getString(Constant1E.SP_USER_TOKEN));
        robotLanguageRequest.setUserId(SPUtils.getInstance().getInt(Constant1E.SP_USER_ID));
        robotLanguageRequest.setEquipmentSeq("");

        ViseHttp.POST(BleHttpEntity.GET_ROBOT_LANGUAGE)
                .setJson(GsonImpl.get().toJson(robotLanguageRequest))
                .connectTimeOut(10)
                .request(new ACallback<String>() {

                    @Override
                    public void onSuccess(String data) {
                        ViseLog.d("USER_GET_INFO onSuccess:" + data);
                        if(mView == null){
                            return;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            boolean status = jsonObject.getBoolean("status");
                            if(status){
                                JSONObject modelsObject = jsonObject.getJSONObject("models");
                                ViseLog.d("modelsObject = " + modelsObject);

                                /*JSONArray modelsArray = jsonObject.getJSONArray("models");
                                ViseLog.d("modelsArray = " + modelsArray);*/

                                String modelsString = jsonObject.getString("models");
                                ViseLog.d("modelsString = " + modelsString);

                                List<RobotLanguage> robotLanguages = dealRobotLanguageData(modelsString);
                                mView.setRobotLanguageList(true, robotLanguages);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            mView.setRobotLanguageList(false, null);
                        } catch (Exception e){
                            e.printStackTrace();
                            mView.setRobotLanguageList(false, null);
                        }

                    }

                    @Override
                    public void onFail(int code, String errmsg) {
                        ViseLog.d("USER_GET_INFO onFail:" + code + "  errmsg:" + errmsg);
                        if(mView != null){
                            mView.setRobotLanguageList(false, null);
                        }
                    }
                });
    }

    private List<RobotLanguage> dealRobotLanguageData(String dataString){
        List<RobotLanguage> languages = new ArrayList<>();

        if(!TextUtils.isEmpty(dataString)){
            dataString = dataString.replace("{","").replace("}","").replace("\"","");
            ViseLog.d("dataString = " + dataString);
            if(!TextUtils.isEmpty(dataString)){
                String[] value = dataString.split(",");
                for(String val : value){
                    RobotLanguage robotLanguage = new RobotLanguage();
                    robotLanguage.setLanguageName(val.split(":")[0]);
                    robotLanguage.setLanguageSingleName(val.split(":")[1]);
                    robotLanguage.setSelect(false);
                    languages.add(robotLanguage);
                }
            }
        }
        return languages;
    }

    private List<RobotLanguage> dealRobotLanguageExistData(String[] language){
        List<RobotLanguage> languages = new ArrayList<>();

        if(language != null){
            ViseLog.d("robotLanguageList = " + language.length);
            for(String val : language){
                RobotLanguage robotLanguage = new RobotLanguage();
                robotLanguage.setLanguageName(val);
                robotLanguage.setLanguageSingleName(val);
                robotLanguage.setSelect(false);
                languages.add(robotLanguage);
            }
        }
        return languages;
    }


    @Override
    public void getRobotLanguageListFromRobot() {
        mBlueClientUtil.sendData(new BTCmdGetLanguages().toByteArray());
    }

    @Override
    public void setRobotLanguage(String language) {
        mBlueClientUtil.sendData(new BTCmdSetLanguage(language).toByteArray());
    }

    @Override
    public void getRobotWifiStatus() {
        mBlueClientUtil.sendData(new BTCmdGetWifiStatus().toByteArray());
    }

    @Override
    public void getRobotVersionMsg() {
        mBlueClientUtil.sendData(new BTCmdGetRobotVersionMsg().toByteArray());
    }

    private void onProtocolPacket(BTReadData readData) {
        ProtocolPacket packet = readData.getPack();
        switch (packet.getmCmd()) {
            case BTCmd.DV_READ_NETWORK_STATUS:
                String networkInfoJson = BluetoothParamUtil.bytesToString(packet.getmParam());
                ViseLog.d(networkInfoJson);
                BleNetWork bleNetWork = praseNetWork(networkInfoJson);
                if (mView != null) {
                    mView.setRobotNetWork(bleNetWork);
                }
                break;
            case BTCmd.DV_COMMON_CMD:
                ViseLog.d("DV_COMMON_CMD = " + BluetoothParamUtil.bytesToString(packet.getmParam()));

                String commonCmdJson = BluetoothParamUtil.bytesToString(packet.getmParam());
                BleBaseModelInfo bleBaseModel = GsonImpl.get().toObject(commonCmdJson, BleBaseModelInfo.class);

                ViseLog.d("bleBaseModel.event = " + bleBaseModel.event);
                if(bleBaseModel.event == 5){
                    BleRobotLanguageList robotLanguageList = GsonImpl.get().toObject(commonCmdJson, BleRobotLanguageList.class);
                    ViseLog.d("robotLanguageList = " + robotLanguageList.language);
                    if(mView != null){

                        mView.setRobotLanguageListExist(dealRobotLanguageExistData(robotLanguageList.language));
                    }
                }else if(bleBaseModel.event == 8){
                    BleSetRobotLanguageRsp setRobotLanguageRsp = GsonImpl.get().toObject(commonCmdJson, BleSetRobotLanguageRsp.class);
                    ViseLog.d("setRobotLanguageRsp = " + setRobotLanguageRsp.rescode);
                    if(mView != null){
                        mView.setRobotLanguageResult(setRobotLanguageRsp.rescode);
                    }
                }else if(bleBaseModel.event == 7){
                    BleDownloadLanguageRsp downloadLanguageRsp = GsonImpl.get().toObject(commonCmdJson, BleDownloadLanguageRsp.class);
                    ViseLog.d("downloadLanguageRsp = " + downloadLanguageRsp);
                    if(mView != null){
                        mView.setDownloadLanguage(downloadLanguageRsp);
                    }
                }else if(bleBaseModel.event == 9){
                    BleUpgradeProgressRsp upgradeProgressRsp = GsonImpl.get().toObject(commonCmdJson, BleUpgradeProgressRsp.class);
                    ViseLog.d("upgradeProgressRsp = " + upgradeProgressRsp);
                    if(mView != null){
                        mView.setUpgradeProgress(upgradeProgressRsp);
                    }
                }else if (bleBaseModel.event == 1) {
                    BleRobotVersionInfo robotVersionInfo = GsonImpl.get().toObject(commonCmdJson, BleRobotVersionInfo.class);
                    if (mView != null) {
                        mView.setRobotVersionInfo(robotVersionInfo);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 读取蓝牙回调数据
     *
     * @param readData
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadData(BTReadData readData) {
        ViseLog.i("data:" + HexUtil.encodeHexStr(readData.getDatas()));
//        BTCmdHelper.parseBTCmd(readData.getDatas(), this);
        onProtocolPacket(readData);
    }

    public BleNetWork praseNetWork(String netWork) {

        BleNetWork bleNetWork = null;

        try {
            JSONObject object = new JSONObject(netWork);
            boolean statu = object.optBoolean("status");
            String wifiName = object.optString("name");
            String ip = object.optString("ip");
            bleNetWork = new BleNetWork(statu, wifiName, ip);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bleNetWork;
    }
}
