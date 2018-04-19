package com.ubt.en.alpha1e.ble.model;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ubt.en.alpha1e.ble.R;

import java.util.List;

/**
 * @author：liuhai
 * @date：2018/4/11 17:24
 * @modifier：ubt
 * @modify_date：2018/4/11 17:24
 * [A brief description]
 * version
 */

public class WifiListAdapter extends BaseQuickAdapter<ScanResult, BaseViewHolder> {

    public WifiListAdapter(@LayoutRes int layoutResId, @Nullable List<ScanResult> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScanResult wifiInfo) {
        helper.setText(R.id.tv_wifi_name, wifiInfo.SSID);
        ImageView ivSign = helper.getView(R.id.iv_wifi_sign);

        //处理WIFI信号图片
        int strength = WifiManager.calculateSignalLevel(wifiInfo.level, 4);
        //处理是否有密码
        if (wifiInfo.capabilities.contains("WPA") || wifiInfo.capabilities.contains("WEP")) {//有密码
            ivSign.setImageResource(R.drawable.ble_wifi_lock_sign);
            ivSign.setImageLevel(strength);
        } else {
            ivSign.setImageResource(R.drawable.ble_wifi_sign);
            ivSign.setImageLevel(strength);
        }
    }
}

