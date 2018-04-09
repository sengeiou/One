package com.ubt.factorytest.test;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import android.util.Log;

import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientListenerAdapter;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.factorytest.bluetooth.ubtbtprotocol.ProtocolPacket;
import com.ubt.factorytest.test.data.DataServer;
import com.ubt.factorytest.test.data.IFactoryListener;
import com.ubt.factorytest.test.data.btcmd.BaseBTReq;
import com.ubt.factorytest.test.data.btcmd.FactoryTool;
import com.ubt.factorytest.test.data.btcmd.GetWifiStatus;
import com.ubt.factorytest.test.data.btcmd.GsensirTest;
import com.ubt.factorytest.test.data.btcmd.HeartBeat;
import com.ubt.factorytest.test.data.btcmd.IntoFactoryTest;
import com.ubt.factorytest.test.data.btcmd.MicTestReq;
import com.ubt.factorytest.test.data.btcmd.PirTest;
import com.ubt.factorytest.test.data.btcmd.PlayAction;
import com.ubt.factorytest.test.data.btcmd.ReadDevStatus;
import com.ubt.factorytest.test.data.btcmd.VolumeAdjust;
import com.ubt.factorytest.test.recycleview.TestClickEntity;
import com.ubt.factorytest.utils.ByteHexHelper;
import com.ubt.factorytest.utils.ContextUtils;
import com.ubt.factorytest.utils.EspressoIdlingResource;
import com.ubt.factorytest.utils.FileUtils;
import com.ubt.factorytest.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/11/16 17:56
 * @描述:
 */

public class TestPresenter implements TestContract.Presenter,IFactoryListener {
    private static final String TAG = "BTTestPresenter";
    private TestContract.View mView;
    private DataServer mDataServer;
    private BlueClientUtil mBlueClient;
    private int btState = BluetoothState.STATE_CONNECTED;
    private String mBTMac;
    private byte[] mHeartBeat;
    private Timer mHeartTime;
    private boolean isPIRTesting = false;
    private boolean isGSensirTesting = false;


    public TestPresenter(TestContract.View view, DataServer dataServer) {
        FactoryTool.getInstance().init();
        mView = view;
        mDataServer = dataServer;
        initBT();
        mView.setPresenter(this);
    }

    private void initBT(){
//        mBluetoothController = BluetoothController.getInstance();
        mBlueClient = BlueClientUtil.getInstance();
        initBTListener();
        //与机器人握手
//        byte[] hw = {(byte) 0xFB, (byte) 0xBF, 0x06, 0x01, 0x00, 0x07, (byte) 0xED};
//        mBluetoothController.write(hw);
        mHeartTime = new Timer("heartBeat");
        mHeartBeat = new HeartBeat().toByteArray();
        startHeart();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mBlueClient.sendData(new IntoFactoryTest(IntoFactoryTest.START_TEST).toByteArray());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mBlueClient.sendData(new ReadDevStatus().toByteArray());
    }

    private BlueClientListenerAdapter mBTListener = new BlueClientListenerAdapter() {

        @Override
        public void onReadData(BluetoothDevice device, byte[] data) {
            Log.d(TAG, "zz TestPresenter onReadData data:" + ByteHexHelper.bytesToHexString(data));
            FactoryTool.getInstance().parseBTCmd(data, TestPresenter.this);
        }

        @Override
        public void onActionDiscoveryStateChanged(String discoveryState) {
            Log.d(TAG, "zz TestPresenter onActionDiscoveryStateChanged:" + discoveryState);
            if (discoveryState.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {

            } else if (discoveryState.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {

            }
        }

        @Override
        public void onBluetoothServiceStateChanged(int state) {
            Log.i(TAG,"zz TestPresenter onBluetoothServiceStateChanged state="+state);
            switch (state){
                case BluetoothState.STATE_CONNECTED:
                    break;
                case BluetoothState.STATE_CONNECTING:
                    break;
                case BluetoothState.STATE_DISCONNECTED:
                    if(btState == BluetoothState.STATE_CONNECTED) {
                        stopHeart();
                        mView.btDisconnected();
                    }
                    break;
                default:
                    break;
            }

            btState = state;
        }

    };

    @Override
    public void start() {

    }


    @Override
    public List<TestClickEntity> getTestInitData() {
        return mDataServer.getTestInitData();
    }

    @Override
    public List<TestClickEntity> getDataCache() {
        return mDataServer.getDataCache();
    }

    @Override
    public void setDataResult(int poisition, String result) {
        mDataServer.setDataResult(poisition, result);
    }

    @Override
    public void setDataisPass(int poisition, boolean isPass) {
        mDataServer.setDataisPass(poisition, isPass);
    }

    @Override
    public void initBTListener() {
        mBlueClient.setBlueListener(mBTListener);
    }

    @Override
    public void reInitBT() {
        if (mBlueClient != null){
            mBlueClient.setBlueListener(mBTListener);
        }
    }

    @Override
    public void disconnectBT() {
        stopHeart();
        mBlueClient.disconnect();
    }

    @Override
    public void startTest(TestClickEntity item) {
        stopPIRorGsensir(item);
        String factoryParam = SpUtils.getInstance().getFactoryConf();
        if(TextUtils.isEmpty(factoryParam)){
            mView.showParamConfDialog();
            return;
        }
        int itemID = item.getTestID();
        if(itemID == TestClickEntity.TEST_ITEM_SAVETESTPROFILE){
            if(mDataServer.isTestComplete()) {
                File filePath = FileUtils.getDiskCacheDir(ContextUtils.getContext());
                boolean isSaveOk = FileUtils.writeStringToFile(mDataServer.getDataCache().toString(),
                        filePath.getPath(), mBTMac + ".txt", false);
                if (isSaveOk) {
                    mView.showToast("保存测试文件" + mBTMac + ".txt 成功");
                    mDataServer.setDataResult(FactoryTool.getInstance().getPosition(mDataServer, itemID),
                            filePath.getPath()+"/"+mBTMac + ".txt");
                } else {
                    mView.showToast("保存测试文件失败!!!!");
                    mDataServer.setDataResult(FactoryTool.getInstance().getPosition(mDataServer, itemID),
                            "保存测试文件失败!!!!");
                }
            }else{
                mView.showToast("测试未完成，保存测试文件失败!!!!");
                mDataServer.setDataResult(FactoryTool.getInstance().getPosition(mDataServer, itemID),
                        "测试未完成，保存测试文件失败!!!!");
            }

        }else {
            byte[] cmd = FactoryTool.getInstance().getReqBytes(item);
            if (cmd != null) {
                EspressoIdlingResource.increment();
                mBlueClient.sendData(cmd);
            }
        }
    }

    @Override
    public void saveBTMac(String mac) {
        mBTMac = mac;
    }

    @Override
    public void setBTRSSI(String rssi) {
        List<TestClickEntity> data = getDataCache();
        for(TestClickEntity entity:data){
            if(entity.getTestID() == TestClickEntity.TEST_ITEM_BTSENSITIVITY){
                entity.setTestResult(mBTMac+"     "+rssi);
                if(Integer.valueOf(rssi) >= -65){
                    entity.setPass(true);
                }else{
                    entity.setPass(false);
                }
                entity.setTested(true);
            }
        }
    }

    @Override
    public void stopRobotRecord() {
        mBlueClient.sendData(new MicTestReq(MicTestReq.STOP_RECORDER).toByteArray());
    }

    @Override
    public void adjustVolume(int type) {
        int curVol = FactoryTool.getInstance().getCurrentVol();
        if(type == TestContract.ADJUST_SUB){
            if(curVol <= 10){
                curVol = 0;
            }else{
                curVol -= 10;
            }
        }else {
            if(curVol >= 245){
                curVol = 255;
            }else{
                curVol += 10;
            }
        }
        FactoryTool.getInstance().setCurrentVol(curVol);
        mBlueClient.sendData(new VolumeAdjust((byte)curVol).toByteArray());
    }

    @Override
    public void startAgeing() {
        mBlueClient.sendData(new PlayAction("action/testmode/Action-老化测试动作简版.hts").toByteArray());
    }

    @Override
    public void getWifiStatus() {
        mBlueClient.sendData(new GetWifiStatus().toByteArray());
    }

    @Override
    public void stopFactoryTest() {
        mBlueClient.sendData(new IntoFactoryTest(IntoFactoryTest.STOP_TEST).toByteArray());
    }

    private void startHeart(){
        mHeartTime.schedule(new TimerTask() {
            @Override
            public void run() {
                if(mBlueClient.getConnectionState() == BluetoothState.STATE_CONNECTED) {
                    Log.i(TAG, "startHeart");
                    mBlueClient.sendData(mHeartBeat);
                }else{
                    Log.e(TAG, "蓝牙已断开，停止心跳!!!");
                    mHeartTime.cancel();

                }
            }
        }, 8000, 3000);
    }

    private void stopHeart(){
        if(mHeartTime != null){
            mHeartTime.cancel();
        }
    }

    /**
     *  切换命令后停止红外 和陀螺仪测试
     * @param item
     */
    private synchronized void stopPIRorGsensir(TestClickEntity item){
        if(item.getTestID() == TestClickEntity.TEST_ITEM_PIRTEST){
            if(!isPIRTesting){
                isPIRTesting = true;
            }
        }else if(item.getTestID() == TestClickEntity.TEST_ITEM_GSENSIRTEST){
            if(!isGSensirTesting){
                isGSensirTesting = true;
            }
        }else{
            if(isPIRTesting){
                isPIRTesting = false;
                mBlueClient.sendData(new PirTest(PirTest.STOP_PIR).toByteArray());
                Log.d(TAG,"退出红外上报");
            }else if(isGSensirTesting){
                isGSensirTesting = false;
                mBlueClient.sendData(new GsensirTest(GsensirTest.STOP_GSENSIR).toByteArray());
                Log.d(TAG,"退出陀螺仪上报");
            }
        }

    }

    @Override
    public void onProtocolPacket(ProtocolPacket packet) {
        byte cmd = packet.getmCmd();
        switch(cmd){
            case BaseBTReq.HEART_CMD:
                break;
            case BaseBTReq.WIFI_STATUS:
                try {
                    JSONObject jsonObject = new JSONObject(new String(packet.getmParam()));
                    String name = jsonObject.getString("name");
                    String ip = jsonObject.getString("ip");
                    mView.setWifiStatus(name, ip);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case BaseBTReq.READ_DEV_STATUS:
                FactoryTool.getInstance().parseDevStatus(packet.getmParam());
                break;
            case BaseBTReq.INTO_FACTORY_TEST:
                Log.i(TAG,"进入工厂测试模式成功!!!");
                break;
            default:
                int pos = FactoryTool.getInstance().getItemPositon(mDataServer,packet);
                if(pos >= 0){
                    mView.notifyItemChanged(pos);
                }
                break;
        }

        if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
            EspressoIdlingResource.decrement();
        }
    }
}
