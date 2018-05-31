package com.ubt.playaction.play;

import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.BlueTooth.BTServiceStateChanged;
import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BTCmdHelper;
import com.ubt.baselib.btCmd1E.BluetoothParamUtil;
import com.ubt.baselib.btCmd1E.IProtolPackListener;
import com.ubt.baselib.btCmd1E.ProtocolPacket;
import com.ubt.baselib.btCmd1E.cmd.BTCmdActionStopPlay;
import com.ubt.baselib.btCmd1E.cmd.BTCmdGetActionList;
import com.ubt.baselib.btCmd1E.cmd.BTCmdPause;
import com.ubt.baselib.btCmd1E.cmd.BTCmdPlayAction;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.playaction.R;
import com.ubt.playaction.model.ActionData;
import com.vise.log.ViseLog;
import com.vise.utils.convert.HexUtil;

import org.greenrobot.eventbus.EventBus;
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


public class PlayActionManger implements IProtolPackListener {

    private static PlayActionManger instance;
    private BlueClientUtil mBlueClient;
    private int mConnectState ;
    public static final int STOP = 0;
    public static final int PLAYING = 1;
    public static final int PAUSE = 2;
    private int playState = STOP;
    private String currentPlayActionName= "";
    private List<ActionData> actionDataList = new ArrayList<ActionData>();
    private IPlayActionMangerListener listener;

    private List<ActionData> actionCycleList = new ArrayList<ActionData>();
    private boolean cycle = false;
    private int currentCyclePos = 0;

    public static PlayActionManger getInstance() {
        if (instance == null) {
            synchronized (PlayActionManger.class) {
                if (instance == null) {
                    instance = new PlayActionManger();
                }
            }
        }
        return  instance;
    }

    public void init(IPlayActionMangerListener listener) {
        this.listener = listener;
        mBlueClient = BlueClientUtil.getInstance();
        if(!EventBus.getDefault().isRegistered(this)){//加上判断
            EventBus.getDefault().register(this);
        }
    }

    public void unRegister() {
        if(EventBus.getDefault().isRegistered(this)){//加上判断
            EventBus.getDefault().unregister(this);
        }
    }

    public void getActionList() {
        if(mBlueClient.getConnectionState() == 3){
            ViseLog.d("getActionList");
            actionDataList.clear();
            mBlueClient.sendData(new BTCmdGetActionList("action").toByteArray());
        }
    }

    public void playAction(String actionName) {
        if(mBlueClient.getConnectionState() == 3){
            mBlueClient.sendData(new BTCmdPlayAction(actionName).toByteArray());
            playState = PLAYING;
            currentPlayActionName =actionName;
            if(listener != null){
                listener.notePlayStart(actionName);
            }
        }
    }


    public void stopAction() {
        if(mBlueClient.getConnectionState() == 3){
            mBlueClient.sendData(new BTCmdActionStopPlay().toByteArray());
            currentPlayActionName= "";
            if(listener != null) {
                listener.notePlayStop();
            }
        }
    }

    public void playPauseAction() {
        if(mBlueClient.getConnectionState() == 3){
            if(playState == PLAYING){
                ViseLog.d("playPauseAction PAUSE");
                mBlueClient.sendData(new BTCmdPause(BTCmdPause.PAUSE).toByteArray());
                playState = PAUSE;
            }else if(playState == PAUSE){
                ViseLog.d("playPauseAction CONTINUE");
                mBlueClient.sendData(new BTCmdPause(BTCmdPause.CONTINUE).toByteArray());
                playState = PLAYING;
            }

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
        BTCmdHelper.parseBTCmd(readData.getDatas(), this);
    }

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
                if(listener != null) {
                    listener.setActionList(actionDataList);
                }
                break;
            case BTCmd.DV_ACTION_FINISH:
                String name = BluetoothParamUtil.bytesToString(packet.getmParam());
                playState = STOP;
                currentPlayActionName="";
                if(listener != null) {
                    listener.notePlayFinish(name);
                }

                ViseLog.d("DV_ACTION_FINISH:" + cycle + "--actionCycleList:" + actionCycleList.size());

                if(cycle){
                    currentCyclePos++;
                    playAction(actionCycleList.get(currentCyclePos).getActionName());
                }


                break;
            case BTCmd.DV_STOPPLAY:  //停止播放
                ViseLog.d("DV_STOPPLAY");
                playState = STOP;
                currentPlayActionName = "";
                if(listener != null) {
                    listener.notePlayStop();
                }
                break;
            case BTCmd.DV_PAUSE: //暂停或者继续
                ViseLog.d("DV_PAUSE:" + packet.getmParam()[0]);
                if(listener != null) {
                    listener.notePlayOrPause();
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

    public String getCurrentPlayActionName(){
        return currentPlayActionName;
    }

    public int getPlayState() {
        return playState;
    }

    public boolean isCycle() {
        return cycle;
    }

    public void setCycle(boolean cycle) {
        this.cycle = cycle;
    }

    public List<ActionData> getActionCycleList() {
        return actionCycleList;
    }

    public void setActionCycleList(List<ActionData> actionCycleList) {
        this.actionCycleList.clear();
        this.actionCycleList.addAll(actionCycleList);
//        this.actionCycleList = actionCycleList;
    }

    public int getCurrentCyclePos() {
        return currentCyclePos;
    }

    public void setCurrentCyclePos(int currentCyclePos) {
        this.currentCyclePos = currentCyclePos;
    }

    public List<ActionData> getActionDataList() {
        return actionDataList;
    }

    public void setActionDataList(List<ActionData> actionDataList) {
        this.actionDataList = actionDataList;
    }

    public interface IPlayActionMangerListener{
        void setActionList( List<ActionData> actionDataList);
        void notePlayStart(String actionName);
        void notePlayStop();
//        void refreshPlayState();
        void notePlayFinish(String name);
        void notePlayOrPause();
    }



}
