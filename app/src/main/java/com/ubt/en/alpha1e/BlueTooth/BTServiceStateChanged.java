package com.ubt.en.alpha1e.BlueTooth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/2/5 10:07
 * @描述:
 */

public class BTServiceStateChanged implements Parcelable {
    /** we're doing nothing*/
    public static final int STATE_NONE = 0;

    /** now listening for incoming connections*/
    public static final int STATE_LISTEN = 1;

    /** now initiating an outgoing connection*/
    public static final int STATE_CONNECTING = 2;

    /** now connected to a remote device*/
    public static final int STATE_CONNECTED = 3;

    /** lost the connection*/
    public static final int STATE_DISCONNECTED = 4;

    /** unknown state*/
    public static final int STATE_UNKNOWN = 5;

    /** got all characteristics*/
    public static final int STATE_GOT_CHARACTERISTICS = 6;

    private int state;

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
        dest.writeInt(this.state);
    }

    public BTServiceStateChanged() {
    }

    protected BTServiceStateChanged(Parcel in) {
        this.state = in.readInt();
    }

    public static final Creator<BTServiceStateChanged> CREATOR = new Creator<BTServiceStateChanged>() {
        @Override
        public BTServiceStateChanged createFromParcel(Parcel source) {
            return new BTServiceStateChanged(source);
        }

        @Override
        public BTServiceStateChanged[] newArray(int size) {
            return new BTServiceStateChanged[size];
        }
    };

    @Override
    public String toString() {
        String sState = "STATE_NONE";

        switch (state){
            case STATE_NONE:
                sState = "STATE_NONE";
                break;
            case STATE_LISTEN:
                sState = "STATE_LISTEN";
                break;
            case STATE_CONNECTING:
                sState = "STATE_CONNECTING";
                break;
            case STATE_CONNECTED:
                sState = "STATE_CONNECTED";
                break;
            case STATE_DISCONNECTED:
                sState = "STATE_DISCONNECTED";
                break;
            case STATE_UNKNOWN:
                sState = "STATE_UNKNOWN";
                break;
            case STATE_GOT_CHARACTERISTICS:
                sState = "STATE_GOT_CHARACTERISTICS";
                break;
        }
        return sState;
    }
}
