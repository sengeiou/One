package com.ubt.baselib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * @author：liuhai
 * @date：2018/1/25 20:18
 * @modifier：ubt
 * @modify_date：2018/1/25 20:18
 * [A brief description]
 * version
 */

public class NetUtil {
    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }


//    /**
//     * 判断网络是否连接
//     */
//    public static boolean checkNet(Context context) {
//        ConnectivityManager cm = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo info = cm.getActiveNetworkInfo();
//        return info != null;// 网络是否连接
//    }

//    /**
//     * 仅wifi联网功能是否开启
//     */
//    public static boolean checkOnlyWifi(Context context) {
//        if (PreferencesUtils.getBoolean(context, PreferenceConstants.ONLY_WIFI,
//        		false)) {
//            return isWiFi(context);
//        } else {
//            return true;
//        }
//    }

    /**
     * 判断是否为wifi联网
     */
    public static boolean isWiFi(Context cxt) {
        ConnectivityManager cm = (ConnectivityManager) cxt
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // wifi的状态：ConnectivityManager.TYPE_WIFI
        // 3G的状态：ConnectivityManager.TYPE_MOBILE
        NetworkInfo.State state = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        return NetworkInfo.State.CONNECTED == state;
    }

    /****************************************
     * 判断wifi是否已打开
     *
     * @param：mContext：上下文
     * @return：boolean 打开true，关闭false
     * @author：黄俊鑫
     ******************************************/
    @SuppressLint("MissingPermission")
    public boolean IsWifiEnabled(Context context) {
        WifiManager wifiMgr = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            return true;
        } else {
            return false;
        }
    }


    /****************************************
     * * 判断网络是否正常（wifi或者3G）
     *
     * @param：mContext：上下文
     * @return：boolean 打开true，关闭false
     * @author：黄俊鑫
     ******************************************/
    public static boolean isNetWorkConnected(Context context) {
        boolean netWorkInfoState = false;
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();




        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            netWorkInfoState = true;

        } else {
            NetworkInfo.State mobile = manager.getNetworkInfo(
                    ConnectivityManager.TYPE_MOBILE).getState();
            if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
                netWorkInfoState = true;
            }
        }
        if (netWorkInfoState) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info == null || !info.isAvailable()) {
                return false;
            }else {
                return true;
            }
        }
        return false;

    }


    public static String getWifiName(Context context){
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : null;
        return wifiId;
    }

}
