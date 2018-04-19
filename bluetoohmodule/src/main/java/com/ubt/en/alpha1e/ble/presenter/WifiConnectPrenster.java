package com.ubt.en.alpha1e.ble.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.ubt.baselib.mvp.BasePresenterImpl;
import com.ubt.en.alpha1e.ble.Contact.WifiConnectContact;
import com.vise.log.ViseLog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author：liuhai
 * @date：2018/4/11 11:11
 * @modifier：ubt
 * @modify_date：2018/4/11 11:11
 * [A brief description]
 * version
 */

public class WifiConnectPrenster extends BasePresenterImpl<WifiConnectContact.View> implements WifiConnectContact.Presenter {

    private Context mContext;

    private WifiManager mWifiManager;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList = new ArrayList<>();
    private List<ScanResult> mWifiListItem = new ArrayList<>();
    private Map<String, Integer> mWifiLevelMap = new LinkedHashMap();
    private Map<String, ScanResult> mWifiItemMap = new LinkedHashMap();

    @Override
    public void init(Context context) {
        this.mContext = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        registerWifiReceiver();
        startScanWifi();
    }

    @Override
    public void startScanWifi() {
        //判断wifi是否打开，不打开的话，自动打开WIIF开关
        if (mWifiManager.isWifiEnabled()) {
            startScan();
            getAllNetWorkList();
        } else {
            //打开wifi
            mWifiManager.setWifiEnabled(true);
        }
    }


    /**
     * 开始搜索Wifi
     */
    public void startScan() {
        mWifiManager.startScan();

        // 得到扫描结果
        refreshWIFIList();

        ViseLog.d("mWifiList.size = " + mWifiList.size());
    }


    /**
     * 注册WIFI相关监听ACTION接收器
     */
    public void registerWifiReceiver() {
        //WIFI Receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);

        mContext.registerReceiver(mWifiReceiver, filter);
    }

    /**
     * WIFI 广播接收器
     */
    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            ViseLog.d("intentAction = " + intentAction);
            if (intentAction.equals(WifiManager.RSSI_CHANGED_ACTION)) {
                //  int strength = getStrength(context);
                //wifiStateImage.setImageLevel(strength);
            } else if (intentAction.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                //Log.d(TAG, "NETWORK_STATE_CHANGED_ACTION");
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {//如果断开连接
                    //wifiStateImage.setImageLevel(0);
                }
                refreshWifiSignal();
            } else if (intentAction.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {//WIFI开关
                int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
                ViseLog.d("wifistate = " + wifistate);
                if (wifistate == WifiManager.WIFI_STATE_DISABLED) {//如果关闭
                    //wifiStateImage.setImageLevel(0);
                    //connectstate.setText(R.string.str_net_openwlan);
                } else if (wifistate == WifiManager.WIFI_STATE_ENABLING) {
                    //connectstate.setText(R.string.str_net_openwlaning);
                } else if (wifistate == WifiManager.WIFI_STATE_DISABLING) {
                    //connectstate.setText(R.string.str_net_closewlaning);
                } else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
                    startScan();
                    getAllNetWorkList();
                }
            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intentAction)
                    || WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intentAction)) {
                refreshWifiSignal();
            }
        }
    };

    /**
     * 刷新Wifi信号
     */
    private void refreshWifiSignal() {
        // save current selection
        refreshWIFIList();
    }

    /**
     * 获取WIFI列表，过滤无知WIFI
     *
     * @return
     */
    private void refreshWIFIList() {
        // 得到扫描结果
        mWifiList = mWifiManager.getScanResults();
        ViseLog.d("mWifiList.size = " + mWifiList.size());

        mWifiListItem.clear();
        mWifiLevelMap.clear();
        mWifiItemMap.clear();

        for (int i = 0; i < mWifiList.size(); i++) {
            ScanResult sresult = mWifiList.get(i);
            String ssidStr = sresult.SSID;
            if (TextUtils.isEmpty(ssidStr) || sresult.capabilities.contains("[IBSS]")) {
                continue;
            } else {
                if (!mWifiLevelMap.containsKey(ssidStr)) {
                    mWifiLevelMap.put(ssidStr, (Integer) sresult.level);
                    mWifiItemMap.put(ssidStr, sresult);
                } else {
                    if (sresult.level > mWifiLevelMap.get(ssidStr)) {
                        mWifiLevelMap.remove(ssidStr);
                        mWifiItemMap.remove(ssidStr);

                        mWifiLevelMap.put(ssidStr, (Integer) sresult.level);
                        mWifiItemMap.put(ssidStr, sresult);
                    }
                }
            }
        }
        Set<String> wifiKeySet = mWifiItemMap.keySet();
        for (String wifiKey : wifiKeySet) {
            mWifiListItem.add(mWifiItemMap.get(wifiKey));
        }

        if (mView != null) {
            mView.getWifiList(mWifiListItem);
        }

    }

    /**
     * 获取所有Wifi列表
     */
    public void getAllNetWorkList() {

        // 开始扫描网络
        startScan();

        ScanResult mScanResult = null;
        if (mWifiListItem != null) {
            for (int i = 0; i < mWifiListItem.size(); i++) {
                // 得到扫描结果
                mScanResult = mWifiListItem.get(i);
                ViseLog.d("mScanResult->" + i + "----"
                        + mScanResult.BSSID + "  "
                        + mScanResult.SSID + "  "
                        + mScanResult.frequency + "   "
                        + mScanResult.capabilities + "   "
                        + mScanResult.level);

            }
        }
    }

    /**
     * 获取WIFI信号强度
     *
     * @param context
     * @return
     */
    public int getStrength(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getBSSID() != null) {
            int strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
            // 链接速度
            // int speed = info.getLinkSpeed();
            // // 链接速度单位
            // String units = WifiInfo.LINK_SPEED_UNITS;
            // // Wifi源名称
            // String ssid = info.getSSID();
            return strength;

        }
        return 0;
    }

    /**
     * 销毁wifi注册广播
     */
    @Override
    public void unRegister() {
        mContext.unregisterReceiver(mWifiReceiver);

    }

}
