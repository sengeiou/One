package com.ubt.en.alpha1e.ble.presenter;

import android.content.Context;

import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.BlueTooth.BTServiceStateChanged;
import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.ProtocolPacket;
import com.ubt.baselib.btCmd1E.cmd.BTCmdConnectWifi;
import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.ble.Contact.WifiInputContact;
import com.ubt.en.alpha1e.ble.R;
import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class WifiInputPrenster extends BasePresenterImpl<WifiInputContact.View> implements WifiInputContact.Presenter {

    BlueClientUtil mBlueClientUtil;
    private int MESSAGE_TIMEOUt = 60 * 1000;
    private int MESSAGE_WHAT = 0x11;
    private Context mContext;
    Disposable disposable;
    private boolean isConnected;

    @Override
    public void init(Context context) {
        this.mContext = context;
        EventBus.getDefault().register(this);
        mBlueClientUtil = BlueClientUtil.getInstance();
    }


    @Override
    public void sendPasswd(String wifiName, String passwd) {
        isConnected = false;
        BaseLoadingDialog.show(mContext, SkinManager.getInstance().getTextById(R.string.wifi_connecting));
        mBlueClientUtil.sendData(new BTCmdConnectWifi(wifiName, passwd).toByteArray());
        disposable = Observable.timer(60, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                ViseLog.d("发送密码超时了");
                if (mView != null) {
                    mView.connectWifiResult(3);
                }
                if (disposable != null) {
                    disposable.dispose();
                }
            }
        });
    }

    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
        if (disposable != null) {
            disposable.dispose();
        }
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
            case BluetoothState.STATE_CONNECTED://蓝牙配对成功

                break;
            case BluetoothState.STATE_CONNECTING://正在连接
                ViseLog.e("正在连接");
                break;
            case BluetoothState.STATE_DISCONNECTED:
                if (mView != null) {
                    // mView.connectWifiResult(3);
                    mView.blutoohDisconnect();
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
//        ViseLog.i("data:" + HexUtil.encodeHexStr(readData.getDatas()));
//        BTCmdHelper.parseBTCmd(readData.getDatas(), this);
        onProtocolPacket(readData);
    }

    /**
     * 蓝牙数据解析回调
     *
     * @param readData
     */
    private void onProtocolPacket(BTReadData readData) {
        ProtocolPacket packet = readData.getPack();
        switch (packet.getmCmd()) {
            case BTCmd.DV_DO_NETWORK_CONNECT:
                ViseLog.d("param====" + packet.getmParam()[0]+"  isConnected=="+isConnected);
                if ((packet.getmParam()[0] == 2 || packet.getmParam()[0] == 3 )&& !isConnected) {
                    ViseLog.d("对话框消失");
                    isConnected = true;
                    BaseLoadingDialog.dismiss(mContext);
                    if (mView != null) {
                        mView.connectWifiResult(packet.getmParam()[0]);
                    }
                    if (disposable != null) {
                        disposable.dispose();
                    }
                }

                break;
            default:
                break;
        }
    }

}
