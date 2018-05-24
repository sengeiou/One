package com.ubt.en.alpha1e.remote.presenster;

import android.content.Context;
import android.content.res.Resources;

import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BTCmdHelper;
import com.ubt.baselib.btCmd1E.IProtolPackListener;
import com.ubt.baselib.btCmd1E.ProtocolPacket;
import com.ubt.baselib.btCmd1E.cmd.BTCmdPlayAction;
import com.ubt.baselib.btCmd1E.cmd.BTCmdSwitchEditStatus;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.remote.R;
import com.ubt.en.alpha1e.remote.contract.RemoteContact;
import com.ubt.en.alpha1e.remote.model.RemoteItem;
import com.vise.log.ViseLog;
import com.vise.utils.convert.HexUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

public class RemotePrenster extends BasePresenterImpl<RemoteContact.View> implements RemoteContact.Presenter, IProtolPackListener {

    private List<RemoteItem> mRemoteItems = new ArrayList<>();
    public int[] fighterdrawableId = {R.drawable.ic_forward, R.drawable.ic_left, R.drawable.ic_forward, R.drawable.ic_left, R.drawable.ic_forward, R.drawable.ic_left,
            R.drawable.remote_arrow_fighter_1_selector, R.drawable.remote_arrow_fighter_2_selector, R.drawable.remote_arrow_fighter_3_selector,
            R.drawable.remote_arrow_fighter_4_selector, R.drawable.remote_arrow_fighter_5_selector, R.drawable.remote_arrow_fighter_6_selector};

    public int[] footballdrawableId = {R.drawable.ic_forward, R.drawable.ic_left, R.drawable.ic_forward, R.drawable.ic_left, R.drawable.ic_forward, R.drawable.ic_left,
            R.drawable.remote_arrow_football_1_selector, R.drawable.remote_arrow_football_2_selector, R.drawable.remote_arrow_football_3_selector,
            R.drawable.remote_arrow_football_4_selector, R.drawable.remote_arrow_football_5_selector, R.drawable.remote_arrow_football_6_selector};

    private BlueClientUtil mBlueClientUtil;

    private static String Robot_path = "action/controller/";

   // DBManager dbManager = null;

    @Override
    public void init(Context context, int remoteType) {
        mBlueClientUtil = BlueClientUtil.getInstance();
        initData();
        EventBus.getDefault().register(this);
        //dbManager = new DBManager(context);
        mRemoteItems = getRemoteItems(context, remoteType);
//        mRemoteItems = dbManager.getRemoteInfoByModel(DBManager.ModelType.FOOTBALL_PLAYER, false, String.valueOf(remoteType));
//
//        if (mRemoteItems != null && mRemoteItems.size() > 0) {
//            for (int i = 0; i < mRemoteItems.size(); i++) {
//                mRemoteItems.get(i).setDrawableId(remoteType == 1 ? footballdrawableId[i] : fighterdrawableId[i]);
//            }
//        }
        ViseLog.d(mRemoteItems.toString());
        startOrStopRun((byte) 0x05);
    }


    private void initData() {
        mRemoteItems.clear();

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
        switch (packet.getmCmd()) {
            case BTCmd.DV_ACTION_FINISH:
                if (mView != null) {
                    mView.playFinish();
                }
                break;
            default:
                break;
        }
    }


    /**
     * 获取列表角色
     *
     * @return
     */
    @Override
    public List<RemoteItem> getRemoteItems() {
        return mRemoteItems;
    }


    public void startOrStopRun(byte cmd) {
        ViseLog.d("startOrStopRun==" + cmd);
        if (mBlueClientUtil.getConnectionState() == 3) {
            mBlueClientUtil.sendData(new BTCmdSwitchEditStatus(cmd).toByteArray());
        }
    }

    /**
     * 播放动作名称
     *
     * @param index
     */
    public void playAction(int index) {
        //action/controller/Left slide tackle.hts
        // mBlueClientUtil.sendData(new BTCmdActionStopPlay().toByteArray());

        String actionName = "";
        if (index == -1) {
            actionName = Robot_path + "Default foot.hts";
        } else {
            actionName = Robot_path + mRemoteItems.get(index).getHts_name();
        }
        ViseLog.d("actionName===" + actionName);
        mBlueClientUtil.sendData(new BTCmdPlayAction(actionName).toByteArray());
    }


    @Override
    public void unRegister() {
        startOrStopRun((byte) 0x06);
        EventBus.getDefault().unregister(this);
    }

    private List<RemoteItem> getRemoteItems(Context context, int modeType) {
        List<RemoteItem> remoteItemList = new ArrayList<>();
        Resources resources = context.getResources();
        String[] array = null;
        if (modeType == 1) {
            array = resources.getStringArray(R.array.remote_footerball);
        }else if (modeType==2){
            array = resources.getStringArray(R.array.remote_fighter);
        }
        for (int i = 0; i < array.length; i++) {
            RemoteItem item = new RemoteItem();
            item.setHts_name(array[i]);
            item.setDrawableId(modeType == 1 ? footballdrawableId[i] : fighterdrawableId[i]);
            remoteItemList.add(item);
            ViseLog.d(item.toString());
        }
        return remoteItemList;
    }
}
