package com.ubt.en.alpha1e.ble.model;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
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

public class WifiListAdapter extends BaseQuickAdapter<WifiInfoModel, BaseViewHolder> {

    private String selectedWifiName;

    public WifiListAdapter(@LayoutRes int layoutResId, @Nullable List<WifiInfoModel> data) {
        super(layoutResId, data);
    }

    public void setSelectedWifiName(String selectedWifiName) {
        this.selectedWifiName = selectedWifiName;
    }

    @Override
    protected void convert(BaseViewHolder helper, WifiInfoModel wifiInfo) {
        helper.setText(R.id.tv_wifi_name, wifiInfo.getESSID());
        ImageView ivSign = helper.getView(R.id.iv_wifi_sign);

        ImageView ivSelected = helper.getView(R.id.iv_wifi_selected);
//        //处理WIFI信号图片
//        int strength = WifiManager.calculateSignalLevel(wifiInfo.level, 4);
//        ivSign.setImageLevel(strength);
        //处理是否有密码
        if (!TextUtils.isEmpty(wifiInfo.getIE()) && (wifiInfo.getIE().contains("WPA") || wifiInfo.getIE().contains("WEP"))) {//有密码
            ivSign.setImageResource(R.drawable.img_wifi_lock_3);
        } else {
            ivSign.setImageResource(R.drawable.img_wifi_free);
        }

        if (!TextUtils.isEmpty(selectedWifiName) && selectedWifiName.equals(wifiInfo.getESSID())) {
            ivSelected.setVisibility(View.VISIBLE);
        } else {
            ivSelected.setVisibility(View.GONE);
        }
    }
}

