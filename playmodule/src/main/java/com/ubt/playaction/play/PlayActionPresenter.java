package com.ubt.playaction.play;

import android.content.Context;

import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.BlueTooth.BTServiceStateChanged;
import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BTCmdHelper;
import com.ubt.baselib.btCmd1E.BluetoothParamUtil;
import com.ubt.baselib.btCmd1E.IProtolPackListener;
import com.ubt.baselib.btCmd1E.ProtocolPacket;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.playaction.R;
import com.ubt.playaction.model.ActionData;
import com.vise.log.ViseLog;
import com.vise.utils.convert.HexUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class PlayActionPresenter extends BasePresenterImpl<PlayActionContract.View> implements PlayActionContract.Presenter, IProtolPackListener, PlayActionManger.IPlayActionMangerListener {

    public static final int STOP = 0;
    public static final int PLAYING = 1;
    public static final int PAUSE = 2;
    private int playState = STOP;

    private Context context;
    private BlueClientUtil mBlueClient;
    private int mConnectState ;
    private List<ActionData> actionDataList = new ArrayList<ActionData>();

    private PlayActionManger playActionManger;

    public int getPlayState() {
        return playState;
    }

    @Override
    public void getActionList() {
        playActionManger.getActionList();
    }

    @Override
    public void register(Context context) {
        this.context = context;
        playActionManger = PlayActionManger.getInstance();
        playActionManger.init(this);
    }

    @Override
    public void unRegister() {
        playActionManger.unRegister();
    }

    @Override
    public void playAction(String actionName) {

        playActionManger.playAction(actionName);
    }

    @Override
    public void stopAction() {
        playActionManger.stopAction();
    }

    @Override
    public void playPauseAction() {

        playActionManger.playPauseAction();
    }


    /**
     * 读取蓝牙回调数据
     *
     * @param readData
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadData(BTReadData readData) {
        ViseLog.i("data:" + HexUtil.encodeHexStr(readData.getDatas()));
        BTCmdHelper.parseBTCmd(readData.getDatas(), this);
    }

    /**
     * 蓝牙数据解析回调
     *
     * @param packet
     */
    @Override
    public void onProtocolPacket(ProtocolPacket packet) {
        ViseLog.d("onProtocolPacket:" + packet.getmCmd() + "---getmParam:" +  new String(packet.getmParam()));
        switch (packet.getmCmd()) {
            case BTCmd.UV_GETACTIONFILE:
                ViseLog.d("UV_GETACTIONFILE:" +  BluetoothParamUtil.bytesToString(packet.getmParam()));
                String actionName =  BluetoothParamUtil.bytesToString(packet.getmParam());
                handleData(actionName);
                break;
            case BTCmd.UV_STOPACTIONFILE:
                ViseLog.d("UV_STOPACTIONFILE" );
                if(mView != null) {
                    mView.getActionList(actionDataList);
                }
                break;
            case BTCmd.DV_ACTION_FINISH:
                String name = BluetoothParamUtil.bytesToString(packet.getmParam());
                playState = STOP;
                if(mView != null) {
                    mView.notePlayFinish(name);
                }
                break;
            case BTCmd.DV_STOPPLAY:  //停止播放
                ViseLog.d("DV_STOPPLAY");
                playState = STOP;
                if(mView != null) {
                    mView.notePlayOrPause();
                }
                break;
            case BTCmd.DV_PAUSE: //暂停或者继续
                ViseLog.d("DV_PAUSE:" + packet.getmParam()[0]);
                if(mView != null) {
                    mView.notePlayOrPause();
                }
                break;
            case BTCmd.DV_PLAYACTION:
                ViseLog.d("DV_PLAYACTION");
                break;
            default:
                break;
        }
    }

    private void handleData(String actionName) {
        ActionData actionData = new ActionData();
        actionData.setActionName(actionName);
        actionData.setActionIcon(R.drawable.img_dance);
        actionData.setActionTime("03:25");
        actionDataList.add(actionData);
    }

    /**
     * 蓝牙连接断开状态
     *
     * @param serviceStateChanged
     */
    @org.greenrobot.eventbus.Subscribe
    public void onBluetoothServiceStateChanged(BTServiceStateChanged serviceStateChanged) {
        ViseLog.i("getState:" + serviceStateChanged.toString());
        mConnectState = serviceStateChanged.getState();
        switch (serviceStateChanged.getState()) {
            case BluetoothState.STATE_CONNECTED://蓝牙配对成功
                ViseLog.d("蓝牙配对成功");
                // stopConnectBleTask();
                break;
            case BluetoothState.STATE_CONNECTING://正在连接
                ViseLog.e("正在连接");
                break;
            case BluetoothState.STATE_DISCONNECTED:
                ViseLog.e("蓝牙连接断开");
                unRegister();
                break;
            default:

                break;
        }
    }

    public int  getConnectState(){
        return mConnectState;
    }


    @Override
    public void setActionList(List<ActionData> actionDataList) {
        if(mView != null) {
            mView.getActionList(actionDataList);
        }
    }

    @Override
    public void notePlayStart(String actionName) {
        if(mView != null){
            mView.notePlayStart(actionName);
        }
    }

    @Override
    public void notePlayStop() {
        if(mView != null) {
            mView.notePlayStop();
        }
    }



    @Override
    public void notePlayFinish(String name) {
        if(mView != null) {
            mView.notePlayFinish(name);
        }
    }

    @Override
    public void notePlayOrPause() {
        if(mView != null) {
            mView.notePlayOrPause();
        }
    }
}
