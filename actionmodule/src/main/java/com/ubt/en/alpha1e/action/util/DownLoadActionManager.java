package com.ubt.en.alpha1e.action.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.BlueTooth.BTServiceStateChanged;
import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BluetoothParamUtil;
import com.ubt.baselib.btCmd1E.ProtocolPacket;
import com.ubt.baselib.btCmd1E.cmd.BTCmdDownLoadAction;
import com.ubt.baselib.btCmd1E.cmd.BTCmdGetActionList;
import com.ubt.baselib.btCmd1E.cmd.BTCmdGetWifiStatus;
import com.ubt.baselib.btCmd1E.cmd.BTCmdPlayAction;
import com.ubt.baselib.model1E.BleNetWork;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.action.model.DownloadProgressInfo;
import com.ubt.en.alpha1e.action.model.DynamicActionModel;
import com.ubt.htslib.base.ActionInfo;
import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author：liuhai
 * @date：2018/5/15 11:17
 * @modifier：ubt
 * @modify_date：2018/5/15 11:17
 * [A brief description]
 * version
 */

public class DownLoadActionManager  {
    private static final String TAG = DownLoadActionManager.class.getSimpleName();

    private static DownLoadActionManager singleton;
    private static Context mContext;

    // 所有正在下载的任务
    private List<DynamicActionModel> mRobotDownList;
    private List<DynamicActionModel> downLoadCompleteList;
    private DynamicActionModel playingInfo;
    PlayState mPlayState = PlayState.action_init;

    private BlockingQueue<ActionInfo> mQueue;

    private List<DownLoadActionListener> mDownLoadActionListeners;

    private List<String> robotActionList = new ArrayList<>();
    Handler mhandler;
    public static int STATU_INIT = 1;//初始化
    public static int STATU_DOWNLOADING = 2;//正在下载
    public static int STATU_PLAYING = 3;//正在播放
    public static int STATU_PLAY_FINISH = 4;//播放结束

    private BlueClientUtil mBlueClientUtil;

    public static enum PlayState {
        action_init, action_playing, action_pause, action_finish
    }

    public enum State {
        busy, success, fail, connect_fail
    }


    public enum Action_type {
        Story_type, Dance_type, Base_type, Custom_type, My_download, My_new, My_download_local, My_new_local, My_gamepad, All, Unkown, MY_WALK, MY_COURSE
    }

    public static Action_type getDataType = Action_type.Unkown;
    // 所有监听者

    private DownLoadActionManager() {
        mRobotDownList = new ArrayList<>();
        downLoadCompleteList = new ArrayList<>();
        mDownLoadActionListeners = new ArrayList<>();
        mQueue = new LinkedBlockingDeque<ActionInfo>();
        mhandler = new Handler(Looper.getMainLooper());
        EventBus.getDefault().register(this);
        mBlueClientUtil = BlueClientUtil.getInstance();
    }


    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }

    public static DownLoadActionManager getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (singleton == null) {
            synchronized (DownLoadActionManager.class) {
                if (singleton == null) {
                    singleton = new DownLoadActionManager();
                }
            }
        }
        return singleton;
    }

    /**
     * 读取蓝牙回调数据
     *
     * @param readData
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadData(BTReadData readData) {
//        ViseLog.i("data:" + HexUtil.encodeHexStr(readData.getDatas()));
//        BTCmdHelper.parseBTCmd(readData.getDatas(), this);
        onProtocolPacket(readData);
    }

    /**
     * 蓝牙数据解析回调
     *
     * @param readData
     */
    public void onProtocolPacket(BTReadData readData) {
        ProtocolPacket packet = readData.getPack();
        byte[] param = packet.getmParam();
        byte cmd = packet.getmCmd();
        if (cmd == BTCmd.DV_DO_DOWNLOAD_ACTION) {
            String downloadProgressJson = BluetoothParamUtil.bytesToString(param);
            ViseLog.d( "downloadProgressJson : " + downloadProgressJson);
            DownloadProgressInfo downloadProgressInfo = GsonImpl.get().toObject(downloadProgressJson, DownloadProgressInfo.class);
            DynamicActionModel actionInfo = getRobotDownloadActionById(downloadProgressInfo.actionId);
            if (actionInfo == null) {
                ViseLog.d( "actionInfo : null ");
                return;
            }
            for (DownLoadActionListener mActionListener : mDownLoadActionListeners) {
                if (mActionListener != null) {
                    mActionListener.getDownLoadProgress(actionInfo, downloadProgressInfo);
                }
            }
            if (downloadProgressInfo.status == 1) {
                //下载中
                ViseLog.d( "机器人下载进度, actionName : " + actionInfo.getActionName() + " " + downloadProgressInfo.progress);
            } else {
                //2 下载成功 3 未联网 0 下载失败
                //UbtLog.d("actionInfo : " + actionInfo );
                State state;
                if (downloadProgressInfo.status == 3) {
                    state = State.connect_fail;
                } else if (downloadProgressInfo.status == 2) {
                    state = State.success;
                    ViseLog.d( "机器人下载成功：hts_file_name = " + actionInfo.getActionName());
                    //机器人下载成功，加入缓存
                    if (null != downLoadCompleteList && !TextUtils.isEmpty(actionInfo.getActionName())) {
                        ViseLog.d( "机器人下载成功：hts_file_name = " + actionInfo.getActionName());

                        ViseLog.d( "机器人下载成功：sendFileName = " + actionInfo.getActionOriginalId());
                        downLoadCompleteList.add(actionInfo);
//                            if (!MyActionsHelper.mCacheActionsNames.isEmpty() && !TextUtils.isEmpty(actionInfo.getActionOriginalId())) {
//                                // String sendFileName = actionInfo.hts_file_name.split("\\.")[0];
//                                String sendFileName = actionInfo.getActionOriginalId();
//
//                                //UbtLog.d("机器人下载成功：hts_file_name = " + sendFileName);
////                                MyActionsHelper.mCacheActionsNames.add((MyActionsHelper.localSize + MyActionsHelper.myDownloadSize), sendFileName);
////                                MyActionsHelper.myDownloadSize++;
//                            }

                    }
                } else {
                    state = State.fail;
                }
                ViseLog.d( "机器人下载结束, actionName : " + actionInfo.getActionName() + " state : " + state + "  ");
                mRobotDownList.remove(actionInfo);
            }


        } else if ((cmd & 0xff) == (BTCmd.DV_DO_CHECK_ACTION_FILE_EXIST & 0xff)) {
            ViseLog.d( "播放文件是否存在：" + param[0]);
            downRobotAction(playingInfo);
            ViseLog.d( "播放文件：" + FileTools.actions_download_robot_path + "/" + playingInfo.getActionName() + ".hts");
        } else if ((cmd & 0xff) == (BTCmd.UV_GETACTIONFILE & 0xff)) {
            if (getDataType == Action_type.MY_WALK) {
                return;
            }
            String name = BluetoothParamUtil.bytesToString(param);
            ViseLog.d( "获取文件：" + name);
            robotActionList.add(name);
        } else if ((cmd & 0xff) == (BTCmd.UV_STOPACTIONFILE & 0xff)) {
            ViseLog.d( "获取文件结束");
            for (DownLoadActionListener mActionListener : mDownLoadActionListeners) {
                if (null != mActionListener) {
                    mActionListener.getRobotActionLists(robotActionList);
                }
            }
        } else if (cmd == BTCmd.DV_ACTION_FINISH) {
            String finishPlayActionName = BluetoothParamUtil.bytesToString(param);
            ViseLog.d( "finishPlayActionName = " + finishPlayActionName);
            if (null != playingInfo && finishPlayActionName.contains(playingInfo.getActionOriginalId())) {
                playingInfo = null;
                for (DownLoadActionListener mActionListener : mDownLoadActionListeners) {
                    if (null != mActionListener) {
                        mActionListener.playActionFinish(finishPlayActionName);
                    }
                }
            }
//                        if (!TextUtils.isEmpty(finishPlayActionName) && finishPlayActionName.contains("初始化")) {
//                        } else {
//                            for (DownLoadActionListener mActionListener : mDownLoadActionListeners) {
//                                if (null != mActionListener) {
//                                    mActionListener.playActionFinish(finishPlayActionName);
//                                }
//                            }
//                        }
        } else if (cmd == BTCmd.DV_TAP_HEAD) {
            ViseLog.d("EditHelper", "TAP_HEAD = " + cmd);
            for (DownLoadActionListener mActionListener : mDownLoadActionListeners) {
                if (null != mActionListener) {
                    mActionListener.doTapHead();
                }
            }
            if (playingInfo != null) {
                playingInfo = null;
            }
        } else if (cmd == BTCmd.DV_READ_NETWORK_STATUS) {
            String networkInfoJson = BluetoothParamUtil.bytesToString(param);
            ViseLog.d( "base cmd = " + cmd + "    networkInfoJson = " + networkInfoJson);

            BleNetWork bleNetWork = praseNetWork(networkInfoJson);
            if (bleNetWork.isStatu()) {
                //hasConnectNetwork = true;
                ViseLog.d( "base 网络已经连接");
            } else {
                //hasConnectNetwork = false;
                ViseLog.d( "base 网络没有连接");
            }
            for (DownLoadActionListener mActionListener : mDownLoadActionListeners) {
                if (null != mActionListener) {
                    mActionListener.isAlpha1EConnectNet(bleNetWork.isStatu());
                }
            }
        }
    }


    //                "status":	"true",
//                    "name":	"UBT-alpha2-bigbox",
//                    "ip":	"192.168.79.60"
//        }
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

    /**
     * 蓝牙连接断开状态
     *
     * @param serviceStateChanged
     */
    @org.greenrobot.eventbus.Subscribe
    public void onBluetoothServiceStateChanged(BTServiceStateChanged serviceStateChanged) {
        ViseLog.i("getState:" + serviceStateChanged.toString());

        switch (serviceStateChanged.getState()) {

            case BluetoothState.STATE_CONNECTING://正在连接
                ViseLog.e("正在连接");
                break;
            case BluetoothState.STATE_DISCONNECTED:
                ViseLog.e("蓝牙连接断开");
                mRobotDownList.clear();
                downLoadCompleteList.clear();
                playingInfo = null;
                for (DownLoadActionListener mActionListener : mDownLoadActionListeners) {
                    if (null != mActionListener) {
                        mActionListener.onBlutheDisconnected();
                    }
                }
                break;
            default:

                break;
        }
    }


    // 添加监听者
    public void addDownLoadActionListener(DownLoadActionListener listener) {
        if (!mDownLoadActionListeners.contains(listener)) {
            mDownLoadActionListeners.add(listener);
        }
    }


    // 移除监听者
    public void removeDownLoadActionListener(DownLoadActionListener listener) {
        if (mDownLoadActionListeners.contains(listener)) {
            mDownLoadActionListeners.remove(listener);
        }
    }
//
//    // 添加监听者
//    public void addListener(ActionsDownLoadManagerListener listener) {
//        if (!mDownListenerLists.contains(listener)) {
//            mDownListenerLists.add(listener);
//        }
//    }
//
//
//    // 移除监听者
//    public void removeListener(ActionsDownLoadManagerListener listener) {
//        if (mDownListenerLists.contains(listener)) {
//            mDownListenerLists.remove(listener);
//        }
//    }


    /**
     * 获取机器人动作列表
     */
    public void getRobotAction() {
        robotActionList.clear();

        if (mBlueClientUtil.getConnectionState() == BluetoothState.STATE_CONNECTED) {
            mBlueClientUtil.sendData(new BTCmdGetActionList(new String(FileTools.actions_download_robot_path)).toByteArray());
        }

//        try {
//            doSendComm(BTCmd.DV_GETACTIONFILE, (new String(FileTools.actions_download_robot_path)).getBytes("GBK"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }


    /**
     * 播放动作文件
     *
     * @param isFromDetail 是否从详情页播放
     * @param actionInfo
     */
    public void playAction(boolean isFromDetail, DynamicActionModel actionInfo) {
        ViseLog.d( "actionInfo======" + actionInfo.toString());
        String path = FileTools.actions_download_robot_path + "/" + actionInfo.getActionOriginalId() + ".hts";
        ViseLog.d( "path======" + path);

        if (mBlueClientUtil.getConnectionState() == BluetoothState.STATE_CONNECTED) {
            mBlueClientUtil.sendData(new BTCmdPlayAction(path).toByteArray());
        }
        if (isFromDetail) {
            for (DownLoadActionListener mActionListener : mDownLoadActionListeners) {
                if (null != mActionListener) {
                    mActionListener.doActionPlay(actionInfo.getActionId(), 1);
                }
            }
        }
        playingInfo = actionInfo;
    }

    /**
     * 读取 1E 联网状态
     */
    public void readNetworkStatus() {
        ViseLog.d( "--readNetworkStatus-- ");
        if (mBlueClientUtil.getConnectionState() == BluetoothState.STATE_CONNECTED) {
            mBlueClientUtil.sendData(new BTCmdGetWifiStatus().toByteArray());
        }
    }

    /**
     * 发送机器人下载
     *
     * @param dynamicActionModel 实体类
     */
    public void downRobotAction(DynamicActionModel dynamicActionModel) {
        // mQueue.add(actionInfo);

        DynamicActionModel c_info = getRobotDownloadActionById(dynamicActionModel.getActionId());
        if (c_info == null) {
            c_info = dynamicActionModel;
            //倒序
            mRobotDownList.add(0, c_info);
        }
//        String params = BluetoothParamUtil.paramsToJsonString(new String[]{dynamicActionModel.getActionId() + "",
//                dynamicActionModel.getActionOriginalId(), dynamicActionModel.getActionUrl()}, BTCmd.DV_DO_DOWNLOAD_ACTION);
//
//        /*String params = BluetoothParamUtil.paramsToJsonString(new String[]{ actionInfo.actionId + "",
//                actionInfo.actionName,"https://services.ubtrobot.com/action/16/3/蚂蚁与鸽子.zip" }, ConstValue.DV_DO_DOWNLOAD_ACTION);*/
//        doSendComm(BTCmd.DV_DO_DOWNLOAD_ACTION, BluetoothParamUtil.stringToBytes(params));
        ViseLog.d( "params =========== " + "dynamicActionModel.getActionOriginalId()==" + dynamicActionModel.getActionOriginalId() + " url==" +
                dynamicActionModel.getActionUrl());
        if (mBlueClientUtil.getConnectionState() == BluetoothState.STATE_CONNECTED) {
            mBlueClientUtil.sendData(new BTCmdDownLoadAction(dynamicActionModel.getActionId() + "",
                    dynamicActionModel.getActionOriginalId(), dynamicActionModel.getActionUrl()).toByteArray());
        }
    }

//    private void doSendComm(byte cmd, byte[] param) {
//        if (null != ((AlphaApplication) mContext
//                .getApplicationContext()).getCurrentBluetooth()) {
//            ((AlphaApplication) mContext.getApplicationContext())
//                    .getBlueToothManager()
//                    .sendCommand(((AlphaApplication) mContext
//                                    .getApplicationContext()).getCurrentBluetooth()
//                                    .getAddress(), cmd,
//                            param, param == null ? 0 : param.length,
//                            false);
//        }
//    }

    public void resetData() {
        if (singleton != null) {
            singleton.mRobotDownList.clear();
        }
    }

    /**
     * 获取正在播放的动作
     *
     * @return
     */
    public DynamicActionModel getPlayingInfo() {
        return playingInfo;
    }

    /**
     * 结束动作
     *
     * @param isFromDetail 是否从详情页结束
     */
    public void stopAction(boolean isFromDetail) {
        if (mBlueClientUtil.getConnectionState() == BluetoothState.STATE_CONNECTED) {
            if (isFromDetail) {
                for (DownLoadActionListener mActionListener : mDownLoadActionListeners) {
                    if (null != mActionListener) {
                        if (null != playingInfo) {
                            mActionListener.doActionPlay(playingInfo.getActionId(), 0);
                        }
                    }
                }
            }

            this.playingInfo = null;
        }
    }

    /**
     * 根据id获取ActionInfo
     *
     * @param id
     * @return
     */

    public DynamicActionModel getRobotDownloadActionById(long id) {
        DynamicActionModel info = null;
        for (int i = 0; i < mRobotDownList.size(); i++) {
            if (mRobotDownList.get(i).getActionId() == id) {
                info = mRobotDownList.get(i);
            }
        }
        return info;
    }

    /**
     * 判断是否在下载中
     *
     * @param action_id
     * @return
     */
    public boolean isRobotDownloading(long action_id) {
        boolean result = false;
        for (int i = 0; i < mRobotDownList.size(); i++) {
            if (mRobotDownList.get(i).getActionId() == action_id) {
                result = true;
            }
        }
        return result;
    }


    /**
     * 获取本地下载列表
     *
     * @return
     */
    public List<DynamicActionModel> getRobotDownList() {
        return mRobotDownList;
    }

    /**
     * 根据id获取ActionInfo
     *
     * @param id
     * @return
     */
    public boolean isCompletedActionById(long id) {
        boolean result = false;
        for (int i = 0; i < downLoadCompleteList.size(); i++) {
            if (downLoadCompleteList.get(i).getActionId() == id) {
                result = true;
                break;
            }
        }
        return result;
    }

    public interface DownLoadActionListener {
        void getRobotActionLists(List<String> list);

        void getDownLoadProgress(DynamicActionModel info, DownloadProgressInfo progressInfo);

        void playActionFinish(String actionName);

        void onBlutheDisconnected();

        void doActionPlay(long actionId, int statu);

        void doTapHead();

        void isAlpha1EConnectNet(boolean statu);
    }

}
