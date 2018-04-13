package com.ubt.en.alpha1e.ble.model;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ubt.baselib.skin.SkinManager;
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

public class BluetoothDeviceListAdapter extends BaseQuickAdapter<BleDevice, BaseViewHolder> {

    public BluetoothDeviceListAdapter(@LayoutRes int layoutResId, @Nullable List<BleDevice> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BleDevice item) {
        helper.addOnClickListener(R.id.btn_buletooth_connect);
        helper.setText(R.id.tv_robot_bluetooth_name, item.getBleName());
        if (item.getStatu() == 2) {
            helper.setText(R.id.btn_buletooth_connect, "连接中");
            //helper.setBackgroundRes(R.id.btn_buletooth_connect,R.drawable.action_button_disable);
        } else {
            helper.setText(R.id.btn_buletooth_connect, SkinManager.getInstance().getTextById(R.string.ble_connect));

        }
    }
}

