package com.ubt.en.alpha1e;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ubt.baselib.BlueTooth.BTDeviceFound;
import com.ubt.baselib.BlueTooth.BTDiscoveryStateChanged;
import com.ubt.baselib.BlueTooth.BTHeartBeatManager;
import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.BlueTooth.BTScanModeChanged;
import com.ubt.baselib.BlueTooth.BTServiceStateChanged;
import com.ubt.baselib.BlueTooth.BTStateChanged;
import com.ubt.baselib.btCmd1E.cmd.BTCmdHeartBeat;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.vise.log.ViseLog;
import com.vise.utils.convert.HexUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ModuleUtils.Main_MainActivity)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_start1)
    Button mBtnStart1;

    @BindView(R.id.btn_start2)
    Button mBtnStart12;
    @BindView(R.id.btn_htswright)
    Button btnHtswright;
    @BindView(R.id.hts_read)
    Button htsRead;
    @BindView(R.id.setting)
    Button mButton5;


    private BlueClientUtil mBlueClientUtils;
    private boolean isScanning = false;

    private BTHeartBeatManager heartBeatManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mBlueClientUtils = BlueClientUtil.getInstance();
        heartBeatManager = BTHeartBeatManager.getInstance();
        heartBeatManager.init(this);
    }

    @OnClick({R.id.btn_start1, R.id.btn_start2, R.id.btn_htswright, R.id.hts_read,R.id.setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start1:
//                ARouter.getInstance().build(ModuleUtils.Module1_Test1).navigation();
                if(mBlueClientUtils.isEnabled()){
                    if(isScanning){
                        boolean iscannel = mBlueClientUtils.cancelScan();
                        ViseLog.i("iscannel:"+iscannel);
                    }else {
                        boolean isScan =  mBlueClientUtils.startScan();
                        ViseLog.i("isScan:"+isScan);
                    }
                }else{
                    mBlueClientUtils.openBluetooth();
                }
                break;
            case R.id.btn_start2:
//                ARouter.getInstance().build(ModuleUtils.Module2_Test2).navigation();
                mBlueClientUtils.connect("A0:2C:36:89:EE:2D");
                break;
            case R.id.btn_htswright:
                heartBeatManager.startHeart(new BTCmdHeartBeat().toByteArray(),5000);
//                HtsHelper.test_write();
                break;
            case R.id.hts_read:
                heartBeatManager.stopHeart();
//                HtsHelper.test_read();
                break;
            case R.id.setting:
                //ARouter.getInstance().build(ModuleUtils.Setting_UserCenterActivity).navigation();
                ARouter.getInstance().build(ModuleUtils.Bluetooh_BleGuideActivity).navigation();
                break;
                default:
        }
    }

//    @Subscribe
//    public void onDataSynEvent(XGPushTextMessage event) {
//        ViseLog.i("event---->" + event.getContent());
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除订阅
    }

    @Subscribe
    public void onBlueDeviceFound(BTDeviceFound deviceFound){
        ViseLog.i("getAddress:"+deviceFound.getBluetoothDevice().getAddress()+"  rssi:"+deviceFound.getRssi());
    }

    @Subscribe
    public void onReadData(BTReadData readData){
        ViseLog.i("data:"+ HexUtil.encodeHexStr(readData.getDatas()));
    }

    @Subscribe
    public void onActionStateChanged(BTStateChanged stateChanged){
        ViseLog.i(stateChanged.toString());
    }

    @Subscribe
    public void onActionDiscoveryStateChanged(BTDiscoveryStateChanged stateChanged){
        ViseLog.i("getDiscoveryState:"+ stateChanged.getDiscoveryState());
        if(stateChanged.getDiscoveryState() == BTDiscoveryStateChanged.DISCOVERY_STARTED){
            isScanning = true;
        }else{
            isScanning = false;
        }
    }

    @Subscribe
    public void onActionScanModeChanged(BTScanModeChanged scanModeChanged){
        ViseLog.i(scanModeChanged.toString());
    }

    @Subscribe
    public void onBluetoothServiceStateChanged(BTServiceStateChanged serviceStateChanged){
        ViseLog.i("getState:"+ serviceStateChanged.toString());
    }
}
