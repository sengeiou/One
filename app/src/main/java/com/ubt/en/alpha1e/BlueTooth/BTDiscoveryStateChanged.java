package com.ubt.en.alpha1e.BlueTooth;

import android.bluetooth.BluetoothAdapter;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/2/5 10:03
 * @描述:
 */

public class BTDiscoveryStateChanged implements Parcelable {
    public static final String DISCOVERY_STARTED = BluetoothAdapter.ACTION_DISCOVERY_STARTED; //扫描开始
    public static final String DISCOVERY_FINISHED = BluetoothAdapter.ACTION_DISCOVERY_FINISHED; //扫描结束

    private String discoveryState;

    public String getDiscoveryState() {
        return discoveryState;
    }

    public void setDiscoveryState(String discoveryState) {
        this.discoveryState = discoveryState;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.discoveryState);
    }

    public BTDiscoveryStateChanged() {
    }

    protected BTDiscoveryStateChanged(Parcel in) {
        this.discoveryState = in.readString();
    }

    public static final Creator<BTDiscoveryStateChanged> CREATOR = new Creator<BTDiscoveryStateChanged>() {
        @Override
        public BTDiscoveryStateChanged createFromParcel(Parcel source) {
            return new BTDiscoveryStateChanged(source);
        }

        @Override
        public BTDiscoveryStateChanged[] newArray(int size) {
            return new BTDiscoveryStateChanged[size];
        }
    };
}
