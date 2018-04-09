package com.ubt.en.alpha1e.BlueTooth;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/2/5 09:57
 * @描述:
 */

public class BTDeviceFound implements Parcelable {
    private BluetoothDevice bluetoothDevice;
    private int rssi;

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.bluetoothDevice, flags);
        dest.writeInt(this.rssi);
    }

    public BTDeviceFound() {
    }

    protected BTDeviceFound(Parcel in) {
        this.bluetoothDevice = in.readParcelable(BluetoothDevice.class.getClassLoader());
        this.rssi = (short) in.readInt();
    }

    public static final Creator<BTDeviceFound> CREATOR = new Creator<BTDeviceFound>() {
        @Override
        public BTDeviceFound createFromParcel(Parcel source) {
            return new BTDeviceFound(source);
        }

        @Override
        public BTDeviceFound[] newArray(int size) {
            return new BTDeviceFound[size];
        }
    };
}
