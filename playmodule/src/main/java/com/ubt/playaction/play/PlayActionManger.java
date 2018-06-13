package com.ubt.playaction.play;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.BlueTooth.BTServiceStateChanged;
import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BluetoothParamUtil;
import com.ubt.baselib.btCmd1E.ProtocolPacket;
import com.ubt.baselib.btCmd1E.cmd.BTCmdActionStopPlay;
import com.ubt.baselib.btCmd1E.cmd.BTCmdGetActionList;
import com.ubt.baselib.btCmd1E.cmd.BTCmdPause;
import com.ubt.baselib.btCmd1E.cmd.BTCmdPlayAction;
import com.ubt.baselib.customView.BaseLowBattaryDialog;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.playaction.R;
import com.ubt.playaction.model.ActionData;
import com.ubt.playaction.model.ActionIconAndTime;
import com.ubt.playaction.model.PlayConstant;
import com.vise.log.ViseLog;

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


public class PlayActionManger {

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

    private int MSG_STOP = 0;
    private long DELAY_TIME = 1000*60*20;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_STOP){
                stopAction();
                cycle = false;
            }
        }
    };

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
            ViseLog.d("IPlayActionMangerListenerthis:" + this.getClass());
            EventBus.getDefault().register(this);
        }
    }

    public void unRegister() {
        if(EventBus.getDefault().isRegistered(this)){//加上判断
            EventBus.getDefault().unregister(this);
        }

        //蓝牙断开的时候恢复初始状态
        playState = STOP;
        cycle = false;
        actionCycleList.clear();
        currentCyclePos= 0;
        currentPlayActionName="";
        if(mHandler!=null){
            mHandler.removeMessages(MSG_STOP);
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
            if(AppStatusUtils.isLowPower()){
                BaseLowBattaryDialog.getInstance().showLow5ActionDialog(null);
                return;
            }
            mBlueClient.sendData(new BTCmdPlayAction(actionName).toByteArray());
            getCurrentPlayPos(actionName);
            playState = PLAYING;
            ViseLog.d("playAction currentPlayActionName:" + actionName);
            currentPlayActionName =actionName;
            if(listener != null){
                listener.notePlayStart(actionName);
            }

            int mode = SPUtils.getInstance().getInt(PlayConstant.SP_PLAY_MODE, PlayConstant.SP_PLAY_MODE_ORDER);
            if( mode== PlayConstant.SP_PLAY_MODE_LSIT|| mode == PlayConstant.SP_PLAY_MODE_SINGLE){
                setCycle(true);
            }
        }
    }

    private void getCurrentPlayPos(String actionName){
        for(int  i=0; i<actionCycleList.size(); i++){
            if (actionName.equals(actionCycleList.get(i).getActionName())){
                currentCyclePos = i;
                return;
            }
        }

        ViseLog.d("getCurrentPlayPos currentCyclePos:" + currentCyclePos);
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

        onProtocolPacket(readData);
    }

    private void onProtocolPacket(BTReadData readData) {
        ProtocolPacket packet = readData.getPack();
        switch (packet.getmCmd()) {
            case BTCmd.UV_GETACTIONFILE:
                ViseLog.d("UV_GETACTIONFILE:" +  new String(packet.getmParam()));
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

                int mode = SPUtils.getInstance().getInt(PlayConstant.SP_PLAY_MODE,PlayConstant.SP_PLAY_MODE_ORDER);
                ViseLog.d("DV_ACTION_FINISH mode:" + mode + "--actionCycleList:" + actionCycleList.size() + "--currentCyclePos:" + currentCyclePos);
                if(mode == PlayConstant.SP_PLAY_MODE_LSIT){
                    currentCyclePos++;
                    playAction(actionCycleList.get(currentCyclePos%(actionCycleList.size())).getActionName());
                }else if(mode == PlayConstant.SP_PLAY_MODE_SINGLE){
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
            case BTCmd.DV_TAP_HEAD:
                ViseLog.d("DV_TAP_HEAD");
                playState = STOP;
                currentPlayActionName = "";
                cycle = false;
                if(mHandler != null){
                    mHandler.removeMessages(MSG_STOP);
                }
                if(listener != null) {
                    listener.notePlayStop();
                    listener.noteTapHead();
                }
                break;
            default:
                break;
        }
    }

    private void handleData(String actionName) {
        ViseLog.d("double 1:" + actionName);
        ActionData actionData = new ActionData();
        actionData.setActionName(actionName);
        if(!TextUtils.isEmpty(ActionIconAndTime.getImageIcon(actionName))){
            if(ActionIconAndTime.getImageIcon(actionName).equals("action")){
                actionData.setActionIcon(R.drawable.img_workout);
            }else if(ActionIconAndTime.getImageIcon(actionName).equals("dance")){
                actionData.setActionIcon(R.drawable.img_dance);
            }else if(ActionIconAndTime.getImageIcon(actionName).equals("taiji")){
                actionData.setActionIcon(R.drawable.img_taichi);
            }else if(ActionIconAndTime.getImageIcon(actionName).equals("yoga")){
                actionData.setActionIcon(R.drawable.img_yoga);
            }else if(ActionIconAndTime.getImageIcon(actionName).equals("story")){
                actionData.setActionIcon(R.drawable.img_story);
            }else{
                actionData.setActionIcon(R.drawable.img_dance);
            }
        }else{
            actionData.setActionIcon(R.drawable.img_dance);
        }

        if(!TextUtils.isEmpty(ActionIconAndTime.getTime(actionName))){
            actionData.setActionTime(ActionIconAndTime.getTime(actionName));
        }else{
            actionData.setActionTime("03:25");
        }
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
        if(cycle){
            if(mHandler != null){
                mHandler.sendEmptyMessageDelayed(MSG_STOP, DELAY_TIME);
            }
        }else{
            if(mHandler != null){
                mHandler.removeMessages(MSG_STOP);
            }

        }
    }

    public List<ActionData> getActionCycleList() {
        return actionCycleList;
    }

    public void setActionCycleList(List<ActionData> actionCycleList) {
        this.actionCycleList.clear();
        this.actionCycleList.addAll(actionCycleList);
    }

    public void addActionCycleList(ActionData actionData){

        for(int i=0; i<actionCycleList.size(); i++){
            if(actionCycleList.get(i).getActionName().equals(actionData.getActionName())){
                ViseLog.d("addActionCycleList:" + actionData.getActionName());
                return;
            }
        }

        ViseLog.d("addActionCycleList actionData:" + actionData);

        actionCycleList.add(0, actionData);

//        actionCycleList.add(actionData);
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
        void notePlayFinish(String name);
        void notePlayOrPause();
        void noteTapHead();
    }



}
