package com.ubt.baselib.BlueTooth;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

import com.ubt.baselib.btCmd1E.ProtocolPacket;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/2/5 09:52
 * @描述:
 */

public class BTReadData implements Parcelable {
    private BluetoothDevice bluetoothDevice;
    private byte[] datas;
    private ProtocolPacket pack;

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public byte[] getDatas() {
        return datas;
    }

    public void setDatas(byte[] bytes) {
        this.datas = bytes;
    }

    public ProtocolPacket getPack() {
        return pack;
    }

    public void setPack(ProtocolPacket pack) {
        this.pack = pack;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.bluetoothDevice, flags);
        dest.writeByteArray(this.datas);
    }

    public BTReadData() {
    }

    protected BTReadData(Parcel in) {
        this.bluetoothDevice = in.readParcelable(BluetoothDevice.class.getClassLoader());
        this.datas = in.createByteArray();
    }

    public static final Creator<BTReadData> CREATOR = new Creator<BTReadData>() {
        @Override
        public BTReadData createFromParcel(Parcel source) {
            return new BTReadData(source);
        }

        @Override
        public BTReadData[] newArray(int size) {
            return new BTReadData[size];
        }
    };
}
