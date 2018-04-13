package com.ubt.baselib.BlueTooth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/2/5 09:55
 * @描述:
 */

public class BTStateChanged implements Parcelable {
    /**
     * Indicates the local Bluetooth adapter is off.
     */
    public static final int STATE_OFF = 10;
    /**
     * Indicates the local Bluetooth adapter is turning on. However local
     * clients should wait for {@link #STATE_ON} before attempting to
     * use the adapter.
     */
    public static final int STATE_TURNING_ON = 11;
    /**
     * Indicates the local Bluetooth adapter is on, and ready for use.
     */
    public static final int STATE_ON = 12;
    /**
     * Indicates the local Bluetooth adapter is turning off. Local clients
     * should immediately attempt graceful disconnection of any remote links.
     */
    public static final int STATE_TURNING_OFF = 13;

    private int preState;
    private int state;


    public int getPreState() {
        return preState;
    }

    public void setPreState(int preState) {
        this.preState = preState;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.preState);
        dest.writeInt(this.state);
    }

    public BTStateChanged() {
    }

    protected BTStateChanged(Parcel in) {
        this.preState = in.readInt();
        this.state = in.readInt();
    }

    public static final Creator<BTStateChanged> CREATOR = new Creator<BTStateChanged>() {
        @Override
        public BTStateChanged createFromParcel(Parcel source) {
            return new BTStateChanged(source);
        }

        @Override
        public BTStateChanged[] newArray(int size) {
            return new BTStateChanged[size];
        }
    };


    @Override
    public String toString() {
        String pre = "STATE_OFF";
        String cur = "STATE_OFF";

        switch (preState){
            case STATE_OFF:
                pre = "STATE_OFF";
                break;
            case STATE_TURNING_ON:
                pre = "STATE_TURNING_ON";
                break;
            case STATE_ON:
                pre = "STATE_ON";
                break;
            case STATE_TURNING_OFF:
                pre = "STATE_TURNING_OFF";
                break;
        }

        switch (state){
            case STATE_OFF:
                cur = "STATE_OFF";
                break;
            case STATE_TURNING_ON:
                cur = "STATE_TURNING_ON";
                break;
            case STATE_ON:
                cur = "STATE_ON";
                break;
            case STATE_TURNING_OFF:
                cur = "STATE_TURNING_OFF";
                break;
        }


        return "preState:"+pre+"  state:"+cur;
    }
}
