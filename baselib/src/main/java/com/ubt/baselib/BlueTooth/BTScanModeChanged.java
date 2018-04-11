package com.ubt.baselib.BlueTooth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/2/5 09:56
 * @描述:
 */

public class BTScanModeChanged implements Parcelable {
    /**
     * Indicates that both inquiry scan and page scan are disabled on the local
     * Bluetooth adapter. Therefore this device is neither discoverable
     * nor connectable from remote Bluetooth devices.
     */
    public static final int SCAN_MODE_NONE = 20;
    /**
     * Indicates that inquiry scan is disabled, but page scan is enabled on the
     * local Bluetooth adapter. Therefore this device is not discoverable from
     * remote Bluetooth devices, but is connectable from remote devices that
     * have previously discovered this device.
     */
    public static final int SCAN_MODE_CONNECTABLE = 21;
    /**
     * Indicates that both inquiry scan and page scan are enabled on the local
     * Bluetooth adapter. Therefore this device is both discoverable and
     * connectable from remote Bluetooth devices.
     */
    public static final int SCAN_MODE_CONNECTABLE_DISCOVERABLE = 23;


    private int preScanMode;
    private int scanMode;

    public int getPreScanMode() {
        return preScanMode;
    }

    public void setPreScanMode(int preScanMode) {
        this.preScanMode = preScanMode;
    }

    public int getScanMode() {
        return scanMode;
    }

    public void setScanMode(int scanMode) {
        this.scanMode = scanMode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.preScanMode);
        dest.writeInt(this.scanMode);
    }

    public BTScanModeChanged() {
    }

    protected BTScanModeChanged(Parcel in) {
        this.preScanMode = in.readInt();
        this.scanMode = in.readInt();
    }

    public static final Creator<BTScanModeChanged> CREATOR = new Creator<BTScanModeChanged>() {
        @Override
        public BTScanModeChanged createFromParcel(Parcel source) {
            return new BTScanModeChanged(source);
        }

        @Override
        public BTScanModeChanged[] newArray(int size) {
            return new BTScanModeChanged[size];
        }
    };

    @Override
    public String toString() {
        String pre = "SCAN_MODE_NONE";
        String state = "SCAN_MODE_NONE";

        switch (preScanMode){
            case SCAN_MODE_NONE:
                pre = "SCAN_MODE_NONE";
                break;
            case SCAN_MODE_CONNECTABLE:
                pre = "SCAN_MODE_CONNECTABLE";
                break;
            case SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                pre = "SCAN_MODE_CONNECTABLE_DISCOVERABLE";
                break;
        }
        switch (scanMode){
            case SCAN_MODE_NONE:
                state = "SCAN_MODE_NONE";
                break;
            case SCAN_MODE_CONNECTABLE:
                state = "SCAN_MODE_CONNECTABLE";
                break;
            case SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                state = "SCAN_MODE_CONNECTABLE_DISCOVERABLE";
                break;
        }
        return "preScanMode:"+pre+"   scanMode:"+state;
    }
}
